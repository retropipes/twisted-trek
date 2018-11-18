package studio.ignitionigloogames.twistedtrek.import2.ai.window;

import studio.ignitionigloogames.randomrange.RandomRange;
import studio.ignitionigloogames.twistedtrek.import2.creatures.AbstractCreature;

public class HardWindowAIRoutine extends AbstractWindowAIRoutine {
    // Fields
    private int[] roundsRemaining;
    private static final int CAST_SPELL_CHANCE = 40;
    private static final int STEAL_CHANCE = 8;
    private static final int DRAIN_CHANCE = 40;
    private static final int HEAL_THRESHOLD = 40;
    private static final int FLEE_CHANCE = 5;

    // Constructors
    public HardWindowAIRoutine() {
	// Do nothing
    }

    @Override
    public int getNextAction(final AbstractCreature c) {
	if (this.roundsRemaining == null) {
	    this.roundsRemaining = new int[c.getSpellBook().getSpellCount()];
	}
	if (this.spellCheck(c)) {
	    // Cast a spell
	    return AbstractWindowAIRoutine.ACTION_CAST_SPELL;
	} else if (CommonWindowAIRoutines.check(HardWindowAIRoutine.STEAL_CHANCE)) {
	    // Steal
	    return AbstractWindowAIRoutine.ACTION_STEAL;
	} else if (CommonWindowAIRoutines.check(HardWindowAIRoutine.DRAIN_CHANCE)) {
	    // Drain MP
	    return AbstractWindowAIRoutine.ACTION_DRAIN;
	} else if (CommonWindowAIRoutines.check(HardWindowAIRoutine.FLEE_CHANCE)) {
	    // Flee
	    return AbstractWindowAIRoutine.ACTION_FLEE;
	} else {
	    // Something hostile is nearby, so attack it
	    return AbstractWindowAIRoutine.ACTION_ATTACK;
	}
    }

    private boolean spellCheck(final AbstractCreature c) {
	final RandomRange random = new RandomRange(1, 100);
	final int chance = random.generate();
	if (chance <= HardWindowAIRoutine.CAST_SPELL_CHANCE) {
	    final int maxIndex = CommonWindowAIRoutines.getMaxCastIndex(c);
	    if (maxIndex > -1) {
		// Select a random spell to cast
		final RandomRange randomSpell = new RandomRange(0, maxIndex);
		final int randomSpellID = randomSpell.generate();
		if (randomSpellID == CommonWindowAIRoutines.SPELL_INDEX_HEAL) {
		    // Healing spell was selected - is healing needed?
		    if (c.getCurrentHP() > c.getMaximumHP() * HardWindowAIRoutine.HEAL_THRESHOLD / 100) {
			// Do not need healing
			return false;
		    }
		}
		if (this.roundsRemaining[randomSpellID] == 0) {
		    this.spell = c.getSpellBook().getSpellByID(randomSpellID);
		    this.roundsRemaining[randomSpellID] = this.spell.getEffect().getInitialRounds();
		    return true;
		} else {
		    // Spell selected already active
		    return false;
		}
	    } else {
		// Not enough MP to cast anything
		return false;
	    }
	} else {
	    // Not casting a spell
	    return false;
	}
    }

    @Override
    public void newRoundHook() {
	// Decrement effect counters
	for (int z = 0; z < this.roundsRemaining.length; z++) {
	    if (this.roundsRemaining[z] > 0) {
		this.roundsRemaining[z]--;
	    }
	}
    }
}
