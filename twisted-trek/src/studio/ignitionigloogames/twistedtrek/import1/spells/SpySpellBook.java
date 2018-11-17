package studio.ignitionigloogames.twistedtrek.import1.spells;

import studio.ignitionigloogames.twistedtrek.import1.creatures.StatConstants;
import studio.ignitionigloogames.twistedtrek.import1.effects.DamageEffect;
import studio.ignitionigloogames.twistedtrek.import1.effects.DrainEffect;
import studio.ignitionigloogames.twistedtrek.import1.effects.Effect;
import studio.ignitionigloogames.twistedtrek.import1.effects.HealingEffect;
import studio.ignitionigloogames.twistedtrek.import1.effects.RegeneratingEffect;

public class SpySpellBook extends SpellBook {
    // Constructor
    public SpySpellBook() {
	super(6);
    }

    @Override
    protected void defineSpells() {
	final RegeneratingEffect spell0Effect = new RegeneratingEffect("Focus", 1, 5, 0.2,
		StatConstants.STAT_MAXIMUM_MP, Effect.DEFAULT_DECAY_RATE);
	spell0Effect.setMessage(Effect.MESSAGE_INITIAL, "You focus your thoughts on the battle at hand!");
	spell0Effect.setMessage(Effect.MESSAGE_SUBSEQUENT, "You regain some MP!");
	spell0Effect.setMessage(Effect.MESSAGE_WEAR_OFF, "Your focus is broken!");
	final Spell spell0 = new Spell(spell0Effect, 1, 'P', "focus");
	this.spells[0] = spell0;
	final DamageEffect spell1Effect = new DamageEffect("Wind Sword", 1, 1, 0.4, StatConstants.STAT_MAXIMUM_HP,
		Effect.DEFAULT_DECAY_RATE);
	spell1Effect.setMessage(Effect.MESSAGE_INITIAL, "You conjure a sword of wind, then throw it at the enemy!");
	spell1Effect.setMessage(Effect.MESSAGE_SUBSEQUENT, "The enemy loses some health from being cut!");
	final Spell spell1 = new Spell(spell1Effect, 3, 'E', "windswrd");
	this.spells[1] = spell1;
	final HealingEffect spell2Effect = new HealingEffect("Full Heal", 1, 1, 1, StatConstants.STAT_MAXIMUM_HP,
		Effect.DEFAULT_DECAY_RATE);
	spell2Effect.setMessage(Effect.MESSAGE_INITIAL, "You conjure a full body wrap, and apply it to your wounds!");
	spell2Effect.setMessage(Effect.MESSAGE_SUBSEQUENT, "You are healed completely!");
	final Spell spell2 = new Spell(spell2Effect, 6, 'P', "heal");
	this.spells[2] = spell2;
	final DrainEffect spell3Effect = new DrainEffect("Dust Drain", 1, 1, 1, StatConstants.STAT_MAXIMUM_MP,
		Effect.DEFAULT_DECAY_RATE);
	spell3Effect.setMessage(Effect.MESSAGE_INITIAL, "You conjure a dust devil, and throw it at the enemy!");
	spell3Effect.setMessage(Effect.MESSAGE_SUBSEQUENT,
		"The enemy loses the ability to cast spells in the confusion!");
	final Spell spell3 = new Spell(spell3Effect, 10, 'E', "drain");
	this.spells[3] = spell3;
	final Effect spell4Effect = new Effect("Fortified", 10);
	spell4Effect.setAffectedStat(StatConstants.STAT_MAXIMUM_HP);
	spell4Effect.setEffect(Effect.EFFECT_MULTIPLY, 5, Effect.DEFAULT_SCALE_FACTOR, StatConstants.STAT_NONE);
	spell4Effect.setMessage(Effect.MESSAGE_INITIAL, "You conjure up a potion of fortification, and drink it!");
	spell4Effect.setMessage(Effect.MESSAGE_SUBSEQUENT, "Your maximum health is increased!");
	spell4Effect.setMessage(Effect.MESSAGE_WEAR_OFF, "The potion wears off!");
	final Spell spell4 = new Spell(spell4Effect, 25, 'P', "potion");
	this.spells[4] = spell4;
	final DamageEffect spell5Effect = new DamageEffect("Tornado", 1, 1, 0.99, StatConstants.STAT_CURRENT_HP,
		Effect.DEFAULT_DECAY_RATE);
	spell5Effect.setMessage(Effect.MESSAGE_INITIAL, "You summon a tornado, then throw it at the enemy!");
	spell5Effect.setMessage(Effect.MESSAGE_SUBSEQUENT, "The enemy gets sucked in, then comes out MUCH weaker!");
	final Spell spell5 = new Spell(spell5Effect, 50, 'E', "tornado");
	this.spells[5] = spell5;
    }

    @Override
    public int getID() {
	return 2;
    }
}
