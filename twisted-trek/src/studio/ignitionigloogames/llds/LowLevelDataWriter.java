/*  LLDS: Arbitrary dimension arrays for Java programs
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-low-level-data-storage
 */
package studio.ignitionigloogames.llds;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LowLevelDataWriter implements AutoCloseable {
    // Fields
    private BufferedWriter bw;
    private static final String END_OF_LINE = "\r\n";

    // Constructors
    public LowLevelDataWriter(String filename) throws IOException {
        this.bw = new BufferedWriter(new FileWriter(filename));
        this.writeHeader();
        this.writeOpeningDocTag();
    }

    // Methods
    @Override
    public void close() throws IOException {
        this.writeClosingDocTag();
        this.bw.close();
    }

    public void writeDouble(double d) throws IOException {
        this.bw.write("<" + LowLevelDataIOConstants.DOUBLE_TAG + ">"
                + Double.toString(d) + "</" + LowLevelDataIOConstants.DOUBLE_TAG
                + ">" + LowLevelDataWriter.END_OF_LINE);
    }

    public void writeInt(int i) throws IOException {
        this.bw.write("<" + LowLevelDataIOConstants.INT_TAG + ">"
                + Integer.toString(i) + "</" + LowLevelDataIOConstants.INT_TAG
                + ">" + LowLevelDataWriter.END_OF_LINE);
    }

    public void writeLong(long l) throws IOException {
        this.bw.write("<" + LowLevelDataIOConstants.LONG_TAG + ">"
                + Long.toString(l) + "</" + LowLevelDataIOConstants.LONG_TAG
                + ">" + LowLevelDataWriter.END_OF_LINE);
    }

    public void writeByte(byte b) throws IOException {
        this.bw.write("<" + LowLevelDataIOConstants.BYTE_TAG + ">"
                + Byte.toString(b) + "</" + LowLevelDataIOConstants.BYTE_TAG
                + ">" + LowLevelDataWriter.END_OF_LINE);
    }

    public void writeBoolean(boolean b) throws IOException {
        this.bw.write("<" + LowLevelDataIOConstants.BOOLEAN_TAG + ">"
                + Boolean.toString(b) + "</"
                + LowLevelDataIOConstants.BOOLEAN_TAG + ">"
                + LowLevelDataWriter.END_OF_LINE);
    }

    public void writeString(String s) throws IOException {
        this.bw.write("<" + LowLevelDataIOConstants.STRING_TAG + ">"
                + LowLevelDataWriter.replaceSpecialCharacters(s) + "</"
                + LowLevelDataIOConstants.STRING_TAG + ">"
                + LowLevelDataWriter.END_OF_LINE);
    }

    public void writeOpeningGroup(String groupName) throws IOException {
        this.bw.write(
                "<" + LowLevelDataWriter.replaceSpecialCharacters(groupName)
                        + ">" + LowLevelDataWriter.END_OF_LINE);
    }

    public void writeClosingGroup(String groupName) throws IOException {
        this.bw.write(
                "</" + LowLevelDataWriter.replaceSpecialCharacters(groupName)
                        + ">" + LowLevelDataWriter.END_OF_LINE);
    }

    private void writeHeader() throws IOException {
        this.bw.write(LowLevelDataIOConstants.HEADER
                + LowLevelDataWriter.END_OF_LINE);
    }

    private void writeOpeningDocTag() throws IOException {
        this.bw.write("<" + LowLevelDataIOConstants.DOC_TAG + ">"
                + LowLevelDataWriter.END_OF_LINE);
    }

    private void writeClosingDocTag() throws IOException {
        this.bw.write("</" + LowLevelDataIOConstants.DOC_TAG + ">");
    }

    private static String replaceSpecialCharacters(String s) {
        String r = s;
        r = r.replace("&", "&amp;");
        r = r.replace("<", "&lt;");
        r = r.replace(">", "&gt;");
        r = r.replace("\"", "&quot;");
        r = r.replace("\'", "&apos;");
        r = r.replace("\r", "");
        return r.replace("\n", "&#xA;");
    }
}
