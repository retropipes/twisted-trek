/* Import2: An RPG */
package studio.ignitionigloogames.twistedtrek.import2.descriptionmanagers;

import java.io.IOException;

import org.retropipes.diane.fileio.utility.ResourceStreamReader;

import studio.ignitionigloogames.twistedtrek.import2.Import2;
import studio.ignitionigloogames.twistedtrek.import2.creatures.faiths.FaithConstants;
import studio.ignitionigloogames.twistedtrek.import2.maze.Extension;

public class FaithDescriptionManager {
    public static String getFaithDescription(final int f) {
	final String name = FaithConstants.getFaithName(f).toLowerCase();
	try (final ResourceStreamReader rsr = new ResourceStreamReader(FaithDescriptionManager.class
		.getResourceAsStream("/com/puttysoftware/tallertower/resources/descriptions/faith/" + name
			+ Extension.getInternalDataExtensionWithPeriod()))) {
	    // Fetch description
	    final String desc = rsr.readString();
	    return desc;
	} catch (final IOException e) {
	    Import2.logError(e);
	    return null;
	}
    }
}
