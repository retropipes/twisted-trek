/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell


Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package studio.ignitionigloogames.twistedtrek.import2.datamanagers;

import java.io.IOException;
import java.util.ArrayList;

import studio.ignitionigloogames.ioutils.ResourceStreamReader;
import studio.ignitionigloogames.twistedtrek.import2.TallerTower;

public class SoundDataManager {
    public static String[] getSoundData() {
	try (final ResourceStreamReader rsr = new ResourceStreamReader(SoundDataManager.class
		.getResourceAsStream("/com/puttysoftware/tallertower/resources/data/sound/sounds.txt"))) {
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
	    TallerTower.getErrorLogger().logError(e);
	    return null;
	}
    }
}
