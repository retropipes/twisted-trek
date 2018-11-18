/* Import2: An RPG */
package studio.ignitionigloogames.twistedtrek.import2.resourcemanagers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import studio.ignitionigloogames.images.BufferedImageIcon;

public class LogoManager {
    private static final String DEFAULT_LOAD_PATH = "/com/puttysoftware/tallertower/resources/graphics/logo/";
    private static String LOAD_PATH = LogoManager.DEFAULT_LOAD_PATH;
    private static Class<?> LOAD_CLASS = LogoManager.class;

    static BufferedImageIcon getUncachedLogo(final String name) {
	try {
	    final URL url = LogoManager.LOAD_CLASS.getResource(LogoManager.LOAD_PATH + name + ".png");
	    final BufferedImage image = ImageIO.read(url);
	    return new BufferedImageIcon(image);
	} catch (final IOException ie) {
	    return null;
	} catch (final NullPointerException np) {
	    return null;
	} catch (final IllegalArgumentException ia) {
	    return null;
	}
    }

    public static BufferedImageIcon getLogo() {
	return LogoCache.getCachedLogo("logo");
    }

    public static BufferedImageIcon getMiniatureLogo() {
	return LogoCache.getCachedLogo("minilogo");
    }

    public static BufferedImageIcon getMicroLogo() {
	return LogoCache.getCachedLogo("micrologo");
    }

    public static BufferedImageIcon getIconLogo() {
	return LogoCache.getCachedLogo("logo");
    }
}
