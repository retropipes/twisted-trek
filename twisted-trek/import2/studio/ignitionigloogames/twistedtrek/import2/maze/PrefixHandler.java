/* Import2: An RPG */
package studio.ignitionigloogames.twistedtrek.import2.maze;

import java.io.IOException;

import studio.ignitionigloogames.xio.XDataReader;
import studio.ignitionigloogames.xio.XDataWriter;

public class PrefixHandler implements PrefixIO {
    private static final byte FORMAT_VERSION = (byte) FormatConstants.MAZE_FORMAT_LATEST;

    @Override
    public int readPrefix(final XDataReader reader) throws IOException {
	final byte formatVer = PrefixHandler.readFormatVersion(reader);
	final boolean res = PrefixHandler.checkFormatVersion(formatVer);
	if (!res) {
	    throw new IOException("Unsupported maze format version: " + formatVer);
	}
	return formatVer;
    }

    @Override
    public void writePrefix(final XDataWriter writer) throws IOException {
	PrefixHandler.writeFormatVersion(writer);
    }

    private static byte readFormatVersion(final XDataReader reader) throws IOException {
	return reader.readByte();
    }

    private static boolean checkFormatVersion(final byte version) {
	return version <= PrefixHandler.FORMAT_VERSION;
    }

    private static void writeFormatVersion(final XDataWriter writer) throws IOException {
	writer.writeByte(PrefixHandler.FORMAT_VERSION);
    }
}
