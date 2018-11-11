package studio.ignitionigloogames.audio.wav;

import java.lang.Thread.UncaughtExceptionHandler;

public class WavExceptionHandler implements UncaughtExceptionHandler {
    @Override
    public void uncaughtException(final Thread thr, final Throwable exc) {
        try {
            if (thr instanceof WavFactory) {
                final WavFactory media = (WavFactory) thr;
                WavFactory.taskCompleted(media.getNumber());
            }
        } catch (Throwable t) {
            // Ignore
        }
    }
}
