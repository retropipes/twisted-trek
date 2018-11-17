package studio.ignitionigloogames.twistedtrek.import2.maze;

import java.io.IOException;

import studio.ignitionigloogames.twistedtrek.import2.TallerTower;
import studio.ignitionigloogames.twistedtrek.import2.game.FileHooks;
import studio.ignitionigloogames.xio.XDataReader;
import studio.ignitionigloogames.xio.XDataWriter;

public class SuffixHandler implements SuffixIO {
    @Override
    public void readSuffix(final XDataReader reader, final int formatVersion) throws IOException {
	TallerTower.getApplication().getGameManager();
	FileHooks.loadGameHook(reader);
    }

    @Override
    public void writeSuffix(final XDataWriter writer) throws IOException {
	TallerTower.getApplication().getGameManager();
	FileHooks.saveGameHook(writer);
    }
}
