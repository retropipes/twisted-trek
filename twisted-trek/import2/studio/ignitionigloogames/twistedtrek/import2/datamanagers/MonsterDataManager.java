/* Import2: An RPG */
package studio.ignitionigloogames.twistedtrek.import2.datamanagers;

import java.io.IOException;
import java.util.ArrayList;

import org.retropipes.diane.fileio.utility.ResourceStreamReader;

import studio.ignitionigloogames.twistedtrek.import2.Import2;

public class MonsterDataManager {
    public static String[] getMonsterData() {
	try (final ResourceStreamReader rsr = new ResourceStreamReader(MonsterDataManager.class
		.getResourceAsStream("/com/puttysoftware/tallertower/resources/data/monsters/monsters.txt"))) {
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
