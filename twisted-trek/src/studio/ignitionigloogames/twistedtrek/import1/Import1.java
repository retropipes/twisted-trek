/*  Import1: A Maze-Solving Game
Copyright (C) 2008-2010 Eric Ahnell

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Any questions should be directed to the author via email at: fantastle@worldwizard.net
 */
package studio.ignitionigloogames.twistedtrek.import1;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import studio.ignitionigloogames.errorlogger.ErrorLogger;
import studio.ignitionigloogames.twistedtrek.import1.resourcemanagers.GraphicsManager;
import studio.ignitionigloogames.twistedtrek.import1.resourcemanagers.ImageCache;
import studio.ignitionigloogames.twistedtrek.import1.resourcemanagers.MonsterImageCache;

public class Import1 {
    // Constants
    private static Application application;
    private static final String PROGRAM_NAME = "Import1";
    private static final ErrorLogger debug = new ErrorLogger(Import1.PROGRAM_NAME);
    private static boolean IN_FANTASTLE_5 = true;

    // Methods
    public static Application getApplication() {
	return Import1.application;
    }

    public static void debug(final Throwable t) {
	Import1.debug.logError(t);
    }

    public static boolean inImport1() {
	return Import1.IN_FANTASTLE_5;
    }

    public static void leaveImport1() {
	Import1.IN_FANTASTLE_5 = false;
    }

    public static void main_disabled(final String[] args) {
	if (System.getProperty("os.name").startsWith("Mac OS X")) {
	    // Mac OS X-specific stuff
	    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Import1");
	    System.setProperty("apple.laf.useScreenMenuBar", "true");
	} else {
	    try {
		// Tell the UIManager to use the platform native look and feel
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (final Exception e) {
		// Do nothing
	    }
	}
	try {
	    Import1.application = new Application();
	    Import1.application.postConstruct();
	    // Load stuff
	    Import1.showLoadingScreen();
	    // Done loading
	    Import1.application.playLogoSound();
	    Import1.application.getGUIManager().showGUI();
	} catch (final Throwable t) {
	    Import1.debug(t);
	}
    }

    private static void showLoadingScreen() {
	// Set up wait frame
	final JFrame waitFrame = new JFrame("Loading...");
	final Container logoContainer = new Container();
	final Container textContainer = new Container();
	final JLabel waitLogo = new JLabel("", GraphicsManager.getLoadingLogo(), SwingConstants.CENTER);
	final JLabel waitLabel = new JLabel("Creating Caches...");
	final JProgressBar waitProgress = new JProgressBar();
	waitProgress.setMinimum(0);
	waitProgress.setMaximum(100);
	waitProgress.setValue(0);
	waitFrame.getContentPane().setLayout(new BorderLayout());
	logoContainer.setLayout(new FlowLayout());
	textContainer.setLayout(new GridLayout(2, 1));
	logoContainer.add(waitLogo);
	textContainer.add(waitLabel);
	textContainer.add(waitProgress);
	waitFrame.getContentPane().add(logoContainer, BorderLayout.CENTER);
	waitFrame.getContentPane().add(textContainer, BorderLayout.SOUTH);
	waitFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	waitFrame.setResizable(false);
	waitFrame.pack();
	// Do the loading
	waitFrame.setVisible(true);
	// Create logo cache
	Import1.getApplication().getGUIManager().updateLogo();
	waitProgress.setValue(20);
	// Create image cache
	ImageCache.recreateCache();
	waitProgress.setValue(40);
	// Create monster image cache
	MonsterImageCache.recreateMonsterCache();
	waitProgress.setValue(60);
	// Create sound cache
	waitProgress.setValue(80);
	// Create stat image cache
	Import1.getApplication().getGameManager().getStatGUI().updateGUI();
	waitProgress.setValue(100);
	waitFrame.setVisible(false);
    }
}
