/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package studio.ignitionigloogames.twistedtrek.import2.maze.objects;

import studio.ignitionigloogames.randomrange.RandomRange;
import studio.ignitionigloogames.twistedtrek.import2.Application;
import studio.ignitionigloogames.twistedtrek.import2.TallerTower;
import studio.ignitionigloogames.twistedtrek.import2.maze.Maze;
import studio.ignitionigloogames.twistedtrek.import2.maze.abc.AbstractMazeObject;
import studio.ignitionigloogames.twistedtrek.import2.maze.abc.AbstractTeleport;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.ObjectImageConstants;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.SoundConstants;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.SoundManager;

public class StairsDown extends AbstractTeleport {
    // Constructors
    public StairsDown() {
	super(0, 0, 0);
    }

    @Override
    public int getBaseID() {
	return ObjectImageConstants.OBJECT_IMAGE_STAIRS_DOWN;
    }

    @Override
    public String getName() {
	return "Stairs Down";
    }

    @Override
    public String getPluralName() {
	return "Sets of Stairs Down";
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY) {
	final Application app = TallerTower.getApplication();
	app.getGameManager().goToLevelOffset(-1);
	SoundManager.playSound(SoundConstants.SOUND_DOWN);
    }

    @Override
    public String getDescription() {
	return "Stairs Down lead to the level below.";
    }

    @Override
    public int getCustomFormat() {
	return 0;
    }

    @Override
    public int getCustomProperty(final int propID) {
	return AbstractMazeObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }

    @Override
    public boolean shouldGenerateObject(final Maze maze, final int row, final int col, final int floor, final int level,
	    final int layer) {
	if (level > Maze.getMinLevels() - 1) {
	    // Generate Stairs Down at 30% rate
	    final RandomRange reject = new RandomRange(1, 100);
	    return reject.generate() < 30;
	} else {
	    // Do not generate Stairs Down on the bottom
	    return false;
	}
    }
}
