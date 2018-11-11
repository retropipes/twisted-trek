/*  LLDS: Arbitrary dimension arrays for Java programs
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-low-level-data-storage
 */
package studio.ignitionigloogames.llds;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LowLevelDataReader implements AutoCloseable {
    // Fields
    private BufferedReader br;

    // Constructors
    public LowLevelDataReader(String filename) throws IOException {
        this.br = new BufferedReader(new FileReader(filename));
        this.readHeader();
        this.readOpeningDocTag();
    }

    public LowLevelDataReader(InputStream stream) throws IOException {
        this.br = new BufferedReader(new InputStreamReader(stream));
        this.readHeader();
        this.readOpeningDocTag();
    }

    // Methods
    @Override
    public void close() throws IOException {
        this.readClosingDocTag();
        this.br.close();
    }

    public double readDouble() throws IOException {
        String line = this.br.readLine();
        if (line != null) {
            String[] split = LowLevelDataReader.splitLine(line);
            LowLevelDataReader.validateOpeningTag(split[0],
                    LowLevelDataIOConstants.DOUBLE_TAG);
            LowLevelDataReader.validateClosingTag(split[2],
                    LowLevelDataIOConstants.DOUBLE_TAG);
            return Double.parseDouble(split[1]);
        } else {
            throw new IOException("End of file!");
        }
    }

    public int readInt() throws IOException {
        String line = this.br.readLine();
        if (line != null) {
            String[] split = LowLevelDataReader.splitLine(line);
            LowLevelDataReader.validateOpeningTag(split[0],
                    LowLevelDataIOConstants.INT_TAG);
            LowLevelDataReader.validateClosingTag(split[2],
                    LowLevelDataIOConstants.INT_TAG);
            return Integer.parseInt(split[1]);
        } else {
            throw new IOException("End of file!");
        }
    }

    public long readLong() throws IOException {
        String line = this.br.readLine();
        if (line != null) {
            String[] split = LowLevelDataReader.splitLine(line);
            LowLevelDataReader.validateOpeningTag(split[0],
                    LowLevelDataIOConstants.LONG_TAG);
            LowLevelDataReader.validateClosingTag(split[2],
                    LowLevelDataIOConstants.LONG_TAG);
            return Long.parseLong(split[1]);
        } else {
            throw new IOException("End of file!");
        }
    }

    public byte readByte() throws IOException {
        String line = this.br.readLine();
        if (line != null) {
            String[] split = LowLevelDataReader.splitLine(line);
            LowLevelDataReader.validateOpeningTag(split[0],
                    LowLevelDataIOConstants.BYTE_TAG);
            LowLevelDataReader.validateClosingTag(split[2],
                    LowLevelDataIOConstants.BYTE_TAG);
            return Byte.parseByte(split[1]);
        } else {
            throw new IOException("End of file!");
        }
    }

    public boolean readBoolean() throws IOException {
        String line = this.br.readLine();
        if (line != null) {
            String[] split = LowLevelDataReader.splitLine(line);
            LowLevelDataReader.validateOpeningTag(split[0],
                    LowLevelDataIOConstants.BOOLEAN_TAG);
            LowLevelDataReader.validateClosingTag(split[2],
                    LowLevelDataIOConstants.BOOLEAN_TAG);
            return Boolean.parseBoolean(split[1]);
        } else {
            throw new IOException("End of file!");
        }
    }

    public String readString() throws IOException {
        String line = this.br.readLine();
        if (line != null) {
            String[] split = LowLevelDataReader.splitLine(line);
            LowLevelDataReader.validateOpeningTag(split[0],
                    LowLevelDataIOConstants.STRING_TAG);
            LowLevelDataReader.validateClosingTag(split[2],
                    LowLevelDataIOConstants.STRING_TAG);
            return LowLevelDataReader.replaceSpecialCharacters(split[1]);
        } else {
            throw new IOException("End of file!");
        }
    }

    public void readOpeningGroup(String groupName) throws IOException {
        String line = this.br.readLine();
        if (line != null) {
            LowLevelDataReader.validateOpeningTag(
                    LowLevelDataReader.replaceSpecialCharacters(line),
                    groupName);
        } else {
            throw new IOException("End of file!");
        }
    }

    public void readClosingGroup(String groupName) throws IOException {
        String line = this.br.readLine();
        if (line != null) {
            LowLevelDataReader.validateClosingTag(
                    LowLevelDataReader.replaceSpecialCharacters(line),
                    groupName);
        } else {
            throw new IOException("End of file!");
        }
    }

    private static void validateOpeningTag(String tag, String tagType)
            throws IOException {
        if (!tag.equals("<" + tagType + ">")) {
            throw new LowLevelDataIOException("Expected opening tag of <"
                    + tagType + ">, found " + tag + "!");
        }
    }

    private static void validateClosingTag(String tag, String tagType)
            throws IOException {
        if (!tag.equals("</" + tagType + ">")) {
            throw new LowLevelDataIOException("Expected closing tag of </"
                    + tagType + ">, found " + tag + "!");
        }
    }

    private static String[] splitLine(String line) {
        String[] split = new String[3];
        int loc0 = line.indexOf('>') + 1;
        int loc2 = line.indexOf('<', loc0);
        split[0] = line.substring(0, loc0);
        split[1] = line.substring(loc0, loc2);
        split[2] = line.substring(loc2);
        return split;
    }

    private void readHeader() throws IOException {
        String header = this.br.readLine();
        if (header == null) {
            throw new LowLevelDataIOException("Corrupt or invalid header!");
        }
        if (!header.equals(LowLevelDataIOConstants.HEADER)) {
            throw new LowLevelDataIOException("Corrupt or invalid header!");
        }
    }

    private void readOpeningDocTag() throws IOException {
        String line = this.br.readLine();
        if (line != null
                && !line.equals("<" + LowLevelDataIOConstants.DOC_TAG + ">")) {
            throw new LowLevelDataIOException(
                    "Opening doc tag does not match: expected <"
                            + LowLevelDataIOConstants.DOC_TAG + ">, found "
                            + line + "!");
        }
    }

    private void readClosingDocTag() throws IOException {
        String line = this.br.readLine();
        if (line != null
                && !line.equals("</" + LowLevelDataIOConstants.DOC_TAG + ">")) {
            throw new LowLevelDataIOException(
                    "Closing doc tag does not match: expected </"
                            + LowLevelDataIOConstants.DOC_TAG + ">, found "
                            + line + "!");
        }
    }

    private static String replaceSpecialCharacters(String s) {
        String r = s;
        r = r.replace("&amp;", "&");
        r = r.replace("&lt;", "<");
        r = r.replace("&gt;", ">");
        r = r.replace("&quot;", "\"");
        r = r.replace("&apos;", "\'");
        return r.replace("&#xA;", "\n");
    }
}
