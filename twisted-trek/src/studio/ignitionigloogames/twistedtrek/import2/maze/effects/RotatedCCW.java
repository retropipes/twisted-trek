/*  Import2: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: mazer5d@worldwizard.net
 */
package studio.ignitionigloogames.twistedtrek.import2.maze.effects;

import studio.ignitionigloogames.twistedtrek.import2.maze.utilities.DirectionConstants;

public class RotatedCCW extends MazeEffect {
    // Constructor
    public RotatedCCW(final int newRounds) {
	super("Rotated CCW", newRounds);
    }

    @Override
    public int modifyMove1(final int arg) {
	switch (arg) {
	case DirectionConstants.DIRECTION_NORTH:
	    return DirectionConstants.DIRECTION_WEST;
	case DirectionConstants.DIRECTION_SOUTH:
	    return DirectionConstants.DIRECTION_EAST;
	case DirectionConstants.DIRECTION_WEST:
	    return DirectionConstants.DIRECTION_SOUTH;
	case DirectionConstants.DIRECTION_EAST:
	    return DirectionConstants.DIRECTION_NORTH;
	case DirectionConstants.DIRECTION_NORTHWEST:
	    return DirectionConstants.DIRECTION_SOUTHWEST;
	case DirectionConstants.DIRECTION_NORTHEAST:
	    return DirectionConstants.DIRECTION_NORTHWEST;
	case DirectionConstants.DIRECTION_SOUTHWEST:
	    return DirectionConstants.DIRECTION_SOUTHEAST;
	case DirectionConstants.DIRECTION_SOUTHEAST:
	    return DirectionConstants.DIRECTION_NORTHEAST;
	default:
	    break;
	}
	return 0;
    }
}