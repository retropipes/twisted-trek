/* Import2: An RPG */
package studio.ignitionigloogames.twistedtrek.import2.datamanagers;

import java.io.IOException;
import java.util.ArrayList;

import org.retropipes.diane.fileio.utility.ResourceStreamReader;

import studio.ignitionigloogames.twistedtrek.import2.Import2;

public class MusicDataManager {
    public static String[] getMusicData() {
	try (final ResourceStreamReader rsr = new ResourceStreamReader(MusicDataManager.class
		.getResourceAsStream("/com/puttysoftware/tallertower/resources/data/music/music.txt"))) {
	    // Fetch data
	    final ArrayList<String> rawData = new ArrayList<>();
	    String line = "";
	    while (line != null) {
		line = rsr.readString();
		if (line != null) {
		    rawData.add(line);
		}
	    }
	    return rawData.toArray(new String[rawData.size()]);
	} catch (final IOException e) {
	    Import2.logError(e);
	    return null;
	}
    }
}
