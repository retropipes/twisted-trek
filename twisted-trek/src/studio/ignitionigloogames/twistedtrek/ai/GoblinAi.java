/* Twisted Trek: A Dual-World Action RPG */
package studio.ignitionigloogames.twistedtrek.ai;

import studio.ignitionigloogames.twistedtrek.Creature;

public class GoblinAi extends CreatureAi {
    private final Creature player;

    public GoblinAi(final Creature newCreature, final Creature newPlayer) {
	super(newCreature);
	this.player = newPlayer;
    }

    @Override
    public void onUpdate() {
	if (this.canUseBetterEquipment()) {
	    this.useBetterEquipment();
	} else if (this.canRangedWeaponAttack(this.player)) {
	    this.creature.rangedWeaponAttack(this.player);
	} else if (this.canThrowAt(this.player)) {
	    this.creature.throwItem(this.getWeaponToThrow(), this.player.x, this.player.y, this.player.z);
	} else if (this.creature.canSee(this.player.x, this.player.y, this.player.z)) {
	    this.hunt(this.player);
	} else if (this.canPickup()) {
	    this.creature.pickup();
	} else {
	    this.wander();
	}
    }
}
