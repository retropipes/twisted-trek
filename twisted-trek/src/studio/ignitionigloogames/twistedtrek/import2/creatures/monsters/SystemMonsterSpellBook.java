/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package studio.ignitionigloogames.twistedtrek.import2.creatures.monsters;

import studio.ignitionigloogames.twistedtrek.import2.battle.BattleTarget;
import studio.ignitionigloogames.twistedtrek.import2.creatures.StatConstants;
import studio.ignitionigloogames.twistedtrek.import2.effects.Effect;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.SoundConstants;
import studio.ignitionigloogames.twistedtrek.import2.spells.Spell;
import studio.ignitionigloogames.twistedtrek.import2.spells.SpellBook;

class SystemMonsterSpellBook extends SpellBook {
    // Constructor
    public SystemMonsterSpellBook() {
	super(6, false);
	this.setName("System Monster Spell Book");
    }

    @Override
    protected void defineSpells() {
	final Effect spell0Effect = new Effect("Poisoned", 3);
	spell0Effect.setEffect(Effect.EFFECT_MULTIPLY, StatConstants.STAT_CURRENT_HP, 0.8);
	spell0Effect.setDecayRate(Effect.EFFECT_MULTIPLY, StatConstants.STAT_CURRENT_HP, 0.875);
	spell0Effect.setMessage(Effect.MESSAGE_INITIAL, "The enemy breathes poisonous breath at you!");
	spell0Effect.setMessage(Effect.MESSAGE_SUBSEQUENT, "You lose some health from being poisoned!");
	spell0Effect.setMessage(Effect.MESSAGE_WEAR_OFF, "You are no longer poisoned!");
	final Spell spell0 = new Spell(spell0Effect, 1, BattleTarget.ENEMY, SoundConstants.SOUND_SLIME);
	this.spells[0] = spell0;
	final Effect spell1Effect = new Effect("Recover", 1);
	spell1Effect.setEffect(Effect.EFFECT_ADD, StatConstants.STAT_CURRENT_HP, 15);
	spell1Effect.setMessage(Effect.MESSAGE_INITIAL, "The enemy applies a bandage to its wounds!");
	spell1Effect.setMessage(Effect.MESSAGE_SUBSEQUENT, "The enemy regains some health!");
	final Spell spell1 = new Spell(spell1Effect, 2, BattleTarget.SELF, SoundConstants.SOUND_HEAL);
	this.spells[1] = spell1;
	final Effect spell2Effect = new Effect("Weapon Drain", 5);
	spell2Effect.setEffect(Effect.EFFECT_MULTIPLY, StatConstants.STAT_ATTACK, 0.8, Effect.DEFAULT_SCALE_FACTOR,
		StatConstants.STAT_NONE);
	spell2Effect.setMessage(Effect.MESSAGE_INITIAL, "The enemy drains your weapon of some of its power!");
	spell2Effect.setMessage(Effect.MESSAGE_SUBSEQUENT, "Your attack is decreased!");
	spell2Effect.setMessage(Effect.MESSAGE_WEAR_OFF, "Your weapon's power has returned!");
	final Spell spell2 = new Spell(spell2Effect, 3, BattleTarget.ENEMY, SoundConstants.SOUND_DRAIN);
	this.spells[2] = spell2;
	final Effect spell3Effect = new Effect("Armor Drain", 5);
	spell3Effect.setEffect(Effect.EFFECT_MULTIPLY, StatConstants.STAT_DEFENSE, 0.8, Effect.DEFAULT_SCALE_FACTOR,
		StatConstants.STAT_NONE);
	spell3Effect.setMessage(Effect.MESSAGE_INITIAL, "The enemy drains your armor of some of its power!");
	spell3Effect.setMessage(Effect.MESSAGE_SUBSEQUENT, "Your defense is decreased!");
	spell3Effect.setMessage(Effect.MESSAGE_WEAR_OFF, "Your armor's power has returned!");
	final Spell spell3 = new Spell(spell3Effect, 5, BattleTarget.ENEMY, SoundConstants.SOUND_DRAIN);
	this.spells[3] = spell3;
	final Effect spell4Effect = new Effect("Weapon Charge", 5);
	spell4Effect.setEffect(Effect.EFFECT_MULTIPLY, StatConstants.STAT_ATTACK, 1.25, Effect.DEFAULT_SCALE_FACTOR,
		StatConstants.STAT_NONE);
	spell4Effect.setMessage(Effect.MESSAGE_INITIAL, "The enemy charges its weapon with power!");
	spell4Effect.setMessage(Effect.MESSAGE_SUBSEQUENT, "The enemy's attack is increased!");
	spell4Effect.setMessage(Effect.MESSAGE_WEAR_OFF, "The enemy's weapon returns to normal!");
	final Spell spell4 = new Spell(spell4Effect, 7, BattleTarget.SELF, SoundConstants.SOUND_DEFENSE);
	this.spells[4] = spell4;
	final Effect spell5Effect = new Effect("Armor Charge", 5);
	spell5Effect.setEffect(Effect.EFFECT_MULTIPLY, StatConstants.STAT_DEFENSE, 1.25, Effect.DEFAULT_SCALE_FACTOR,
		StatConstants.STAT_NONE);
	spell5Effect.setMessage(Effect.MESSAGE_INITIAL, "The enemy charges its armor with power!");
	spell5Effect.setMessage(Effect.MESSAGE_SUBSEQUENT, "The enemy's defense is increased!");
	spell5Effect.setMessage(Effect.MESSAGE_WEAR_OFF, "The enemy's armor returns to normal!");
	final Spell spell5 = new Spell(spell5Effect, 11, BattleTarget.SELF, SoundConstants.SOUND_ATTACK);
	this.spells[5] = spell5;
    }
}
