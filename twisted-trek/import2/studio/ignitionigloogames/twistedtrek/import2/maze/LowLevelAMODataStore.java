/* Import2: An RPG */
package studio.ignitionigloogames.twistedtrek.import2.maze;

import org.retropipes.diane.storage.ObjectStorage;

import studio.ignitionigloogames.twistedtrek.import2.maze.abc.AbstractMazeObject;

class LowLevelAMODataStore extends ObjectStorage<AbstractMazeObject> {
    // Constructor
    public LowLevelAMODataStore(final int... shape) {
	super(shape);
    }

    // Copy constructor
    public LowLevelAMODataStore(final LowLevelAMODataStore source) {
	super(source);
    }
}
