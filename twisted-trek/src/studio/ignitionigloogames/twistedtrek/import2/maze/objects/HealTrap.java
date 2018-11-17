/*  Import2: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: Import2@worldwizard.net
 */
package studio.ignitionigloogames.twistedtrek.import2.maze.objects;

import studio.ignitionigloogames.twistedtrek.import2.creatures.party.PartyManager;
import studio.ignitionigloogames.twistedtrek.import2.maze.abc.AbstractTrap;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.ObjectImageConstants;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.SoundConstants;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.SoundManager;

public class HealTrap extends AbstractTrap {
    // Constructors
    public HealTrap() {
	super(ObjectImageConstants.OBJECT_IMAGE_HEAL_TRAP);
    }

    @Override
    public String getName() {
	return "Heal Trap";
    }

    @Override
    public String getPluralName() {
	return "Heal Traps";
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY) {
	int healing = PartyManager.getParty().getLeader().getMaximumHP() / 10;
	if (healing < 1) {
	    healing = 1;
	}
	PartyManager.getParty().getLeader().heal(healing);
	SoundManager.playSound(SoundConstants.SOUND_HEAL);
    }

    @Override
    public String getDescription() {
	return "Heal Traps heal you when stepped on.";
    }
}