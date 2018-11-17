/*  TallerTower: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: mazer5d@worldwizard.net
 */
package studio.ignitionigloogames.twistedtrek.import2.maze.effects;

import studio.ignitionigloogames.twistedtrek.import2.creatures.party.PartyManager;

public class PowerWither extends MazeEffect {
    // Constants
    private static final int MP_LOST = -3;

    // Constructor
    public PowerWither(final int newRounds) {
	super("Power Wither", newRounds);
    }

    @Override
    public int modifyMove1(final int arg) {
	PartyManager.getParty().getLeader().offsetCurrentMP(PowerWither.MP_LOST);
	return arg;
    }
}