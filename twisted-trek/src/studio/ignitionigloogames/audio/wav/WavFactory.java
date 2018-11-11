package studio.ignitionigloogames.audio.wav;

import java.net.URL;

public abstract class WavFactory extends Thread {
    // Constants
    protected static final int EXTERNAL_BUFFER_SIZE = 4096; // 4Kb
    private static int ACTIVE_MEDIA_COUNT = 0;
    private static int MAX_MEDIA_ACTIVE = 5;
    private static WavFactory[] ACTIVE_MEDIA = new WavFactory[WavFactory.MAX_MEDIA_ACTIVE];
    private static ThreadGroup MEDIA_GROUP = new ThreadGroup("WAV Media Players");
    private static WavExceptionHandler meh = new WavExceptionHandler();

    // Constructor
    protected WavFactory(final ThreadGroup group) {
        super(group, "WAV Media Player " + WavFactory.ACTIVE_MEDIA_COUNT);
    }

    // Methods
    public abstract void stopLoop();

    protected abstract void updateNumber(int newNumber);

    abstract int getNumber();

    // Factories
    public static WavFactory loadFile(final String file) {
        return WavFactory.provisionMedia(new WavFile(WavFactory.MEDIA_GROUP,
                file, WavFactory.ACTIVE_MEDIA_COUNT));
    }

    public static WavFactory loadResource(final URL resource) {
        return WavFactory.provisionMedia(new WavResource(WavFactory.MEDIA_GROUP,
                resource, WavFactory.ACTIVE_MEDIA_COUNT));
    }

    private static WavFactory provisionMedia(final WavFactory src) {
        if (WavFactory.ACTIVE_MEDIA_COUNT >= WavFactory.MAX_MEDIA_ACTIVE) {
            WavFactory.killAllMediaPlayers();
        }
        try {
            if (src != null) {
                src.setUncaughtExceptionHandler(WavFactory.meh);
                WavFactory.ACTIVE_MEDIA[WavFactory.ACTIVE_MEDIA_COUNT] = src;
                WavFactory.ACTIVE_MEDIA_COUNT++;
            }
        } catch (final ArrayIndexOutOfBoundsException aioob) {
            // Do nothing
        }
        return src;
    }

    private static void killAllMediaPlayers() {
        WavFactory.MEDIA_GROUP.interrupt();
    }

    static synchronized void taskCompleted(final int taskNum) {
        WavFactory.ACTIVE_MEDIA[taskNum] = null;
        for (int z = taskNum + 1; z < WavFactory.ACTIVE_MEDIA.length; z++) {
            if (WavFactory.ACTIVE_MEDIA[z] != null) {
                WavFactory.ACTIVE_MEDIA[z - 1] = WavFactory.ACTIVE_MEDIA[z];
                if (WavFactory.ACTIVE_MEDIA[z - 1].isAlive()) {
                    WavFactory.ACTIVE_MEDIA[z - 1].updateNumber(z - 1);
                }
            }
        }
        WavFactory.ACTIVE_MEDIA_COUNT--;
    }
}
