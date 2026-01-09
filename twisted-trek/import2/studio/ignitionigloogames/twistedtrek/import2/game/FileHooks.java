/* Import2: An RPG */
package studio.ignitionigloogames.twistedtrek.import2.game;

import java.io.IOException;

import org.retropipes.diane.fileio.XDataReader;
import org.retropipes.diane.fileio.XDataWriter;

import studio.ignitionigloogames.twistedtrek.import2.creatures.party.PartyManager;

public class FileHooks {
    private FileHooks() {
	// Do nothing
    }

    public static void loadGameHook(final XDataReader mapFile) throws IOException {
	PartyManager.loadGameHook(mapFile);
    }

    public static void saveGameHook(final XDataWriter mapFile) throws IOException {
	PartyManager.saveGameHook(mapFile);
    }
}
