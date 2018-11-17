/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package studio.ignitionigloogames.twistedtrek.import2.maze.objects;

import studio.ignitionigloogames.twistedtrek.import2.maze.abc.AbstractPassThroughObject;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.ObjectImageConstants;

public class OpenDoor extends AbstractPassThroughObject {
    // Constructors
    public OpenDoor() {
	super();
    }

    @Override
    public int getBaseID() {
	return ObjectImageConstants.OBJECT_IMAGE_OPEN_DOOR;
    }

    // Scriptability
    @Override
    public String getName() {
	return "Open Door";
    }

    @Override
    public String getPluralName() {
	return "Open Doors";
    }

    @Override
    public String getDescription() {
	return "Open Doors are purely decorative.";
    }
}
