/* Import2: An RPG */
package studio.ignitionigloogames.twistedtrek.import2.maze;

import java.io.IOException;

import org.retropipes.diane.fileio.XDataReader;
import org.retropipes.diane.fileio.XDataWriter;

public class MazeNote {
    // Fields
    private String contents;

    // Constructor
    public MazeNote() {
	this.contents = "Empty Note";
    }

    // Copy constructor
    public MazeNote(final MazeNote source) {
	this.contents = source.contents;
    }

    // Methods
    public String getContents() {
	return this.contents;
    }

    public void setContents(final String newContents) {
	this.contents = newContents;
    }

    static MazeNote readNote(final XDataReader reader) throws IOException {
	final MazeNote mn = new MazeNote();
	mn.contents = reader.readString();
	return mn;
    }

    void writeNote(final XDataWriter writer) throws IOException {
	writer.writeString(this.contents);
    }
}
