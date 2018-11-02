module studio.ignitionigloogames.twistedtrek {
    requires java.desktop;

    provides javax.sound.sampled.spi.AudioFileReader
	    with com.github.trilarion.sound.vorbis.sampled.spi.VorbisAudioFileReader;
    provides javax.sound.sampled.spi.FormatConversionProvider
	    with com.github.trilarion.sound.vorbis.sampled.spi.VorbisFormatConversionProvider;
}