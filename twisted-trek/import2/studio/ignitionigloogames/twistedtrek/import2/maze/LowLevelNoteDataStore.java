/* Import2: An RPG */
package studio.ignitionigloogames.twistedtrek.import2.maze;

import org.retropipes.diane.storage.ObjectStorage;

class LowLevelNoteDataStore extends ObjectStorage<MazeNote> {
    // Constructor
    LowLevelNoteDataStore(final int... shape) {
	super(shape);
    }

    // Methods
    public MazeNote getNote(final int... loc) {
	return this.getCell(loc);
    }

    public void setNote(final MazeNote obj, final int... loc) {
	this.setCell(obj, loc);
    }
}
