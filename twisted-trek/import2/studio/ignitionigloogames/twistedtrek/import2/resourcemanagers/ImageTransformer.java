/* Import2: An RPG */
package studio.ignitionigloogames.twistedtrek.import2.resourcemanagers;

import java.awt.Color;

import studio.ignitionigloogames.images.BufferedImageIcon;
import studio.ignitionigloogames.twistedtrek.import2.maze.utilities.ImageColorConstants;

public class ImageTransformer {
    public static final int MAX_WINDOW_SIZE = 700;
    private static final Color TRANSPARENT = new Color(200, 100, 100);
    private static Color REPLACE = new Color(200, 100, 100, 0);

    static BufferedImageIcon templateTransformImage(final BufferedImageIcon input, final int transformColor,
	    final int imageSize) {
	if (transformColor == ImageColorConstants.COLOR_NONE) {
	    return input;
	} else {
	    try {
		final BufferedImageIcon result = new BufferedImageIcon(input);
		for (int x = 0; x < imageSize; x++) {
		    for (int y = 0; y < imageSize; y++) {
			final int pixel = input.getRGB(x, y);
			final Color c = new Color(pixel);
			final int r = c.getRed();
			final int g = c.getGreen();
			final int b = c.getBlue();
			if (r == g && r == b && g == b) {
			    final Color tc = new Color(transformColor);
			    final double tr = (tc.getRed() + 1) / 256.0;
			    final double tg = (tc.getGreen() + 1) / 256.0;
			    final double tb = (tc.getBlue() + 1) / 256.0;
			    final int newR = (int) (r * tr);
			    final int newG = (int) (g * tg);
			    final int newB = (int) (b * tb);
			    final Color nc = new Color(newR, newG, newB);
			    result.setRGB(x, y, nc.getRGB());
			}
		    }
		}
		return result;
	    } catch (final NullPointerException np) {
		return input;
	    }
	}
    }

    public static BufferedImageIcon getTransformedImage(final BufferedImageIcon icon, final int imageSize) {
	try {
	    final BufferedImageIcon result = new BufferedImageIcon(icon);
	    if (icon != null) {
		for (int x = 0; x < imageSize; x++) {
		    for (int y = 0; y < imageSize; y++) {
			final int pixel = icon.getRGB(x, y);
			final Color c = new Color(pixel);
			if (c.equals(ImageTransformer.TRANSPARENT)) {
			    result.setRGB(x, y, ImageTransformer.REPLACE.getRGB());
			}
		    }
		}
		return result;
	    } else {
		return null;
	    }
	} catch (final NullPointerException np) {
	    return null;
	} catch (final IllegalArgumentException ia) {
	    return null;
	}
    }

    public static BufferedImageIcon getCompositeImage(final BufferedImageIcon icon1, final BufferedImageIcon icon2,
	    final int imageSize) {
	try {
	    final BufferedImageIcon result = new BufferedImageIcon(icon2);
	    if (icon1 != null && icon2 != null) {
		for (int x = 0; x < imageSize; x++) {
		    for (int y = 0; y < imageSize; y++) {
			final int pixel = icon2.getRGB(x, y);
			final Color c = new Color(pixel);
			if (c.equals(ImageTransformer.TRANSPARENT)) {
			    result.setRGB(x, y, icon1.getRGB(x, y));
			}
		    }
		}
		return result;
	    } else {
		return null;
	    }
	} catch (final NullPointerException np) {
	    return null;
	} catch (final IllegalArgumentException ia) {
	    return null;
	}
    }

    public static BufferedImageIcon getVirtualCompositeImage(final BufferedImageIcon icon1,
	    final BufferedImageIcon icon2, final BufferedImageIcon icon3, final int imageSize) {
	try {
	    final BufferedImageIcon icon4 = ImageTransformer.getCompositeImage(icon1, icon2, imageSize);
	    final BufferedImageIcon result = new BufferedImageIcon(icon3);
	    if (icon3 != null && icon4 != null) {
		for (int x = 0; x < imageSize; x++) {
		    for (int y = 0; y < imageSize; y++) {
			final int pixel = icon3.getRGB(x, y);
			final Color c = new Color(pixel);
			if (c.equals(ImageTransformer.TRANSPARENT)) {
			    result.setRGB(x, y, icon4.getRGB(x, y));
			}
		    }
		}
		return result;
	    } else {
		return null;
	    }
	} catch (final NullPointerException np) {
	    return null;
	} catch (final IllegalArgumentException ia) {
	    return null;
	}
    }

    public static String normalizeName(final String name) {
	final StringBuilder sb = new StringBuilder(name);
	for (int x = 0; x < sb.length(); x++) {
	    if (!Character.isLetter(sb.charAt(x)) && !Character.isDigit(sb.charAt(x))) {
		sb.setCharAt(x, '_');
	    }
	}
	return sb.toString().toLowerCase();
    }

    public static int getGraphicSize() {
	return 64;
    }
}
