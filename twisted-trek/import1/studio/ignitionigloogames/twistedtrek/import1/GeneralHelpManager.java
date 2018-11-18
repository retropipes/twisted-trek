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

import java.awt.FlowLayout;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import studio.ignitionigloogames.help.HTMLHelpViewer;
import studio.ignitionigloogames.twistedtrek.import1.resourcemanagers.GraphicsManager;
import studio.ignitionigloogames.twistedtrek.import1.resourcemanagers.HelpManager;

public class GeneralHelpManager {
    // Fields
    private final JFrame helpFrame;
    private final HTMLHelpViewer hv;

    // Constructors
    public GeneralHelpManager() {
	final URL helpURL = HelpManager.getHelpURL();
	this.hv = new HTMLHelpViewer(helpURL);
	this.helpFrame = new JFrame("Import1 Help");
	this.helpFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	this.helpFrame.setLayout(new FlowLayout());
	this.helpFrame.add(this.hv.getHelp());
	if (Import1.getApplication().getPrefsManager().isMobileModeEnabled()) {
	    this.hv.setHelpSize(GraphicsManager.MAX_MOBILE_WINDOW_SIZE, GraphicsManager.MAX_MOBILE_WINDOW_SIZE);
	} else {
	    this.hv.setHelpSize(GraphicsManager.MAX_DESKTOP_WINDOW_SIZE, GraphicsManager.MAX_DESKTOP_WINDOW_SIZE);
	}
	this.helpFrame.pack();
	this.helpFrame.setResizable(false);
    }

    // Methods
    public void showHelp() {
	this.helpFrame.setVisible(true);
    }

    public void updateHelpSize() {
	if (Import1.getApplication().getPrefsManager().isMobileModeEnabled()) {
	    this.hv.setHelpSize(GraphicsManager.MAX_MOBILE_WINDOW_SIZE, GraphicsManager.MAX_MOBILE_WINDOW_SIZE);
	} else {
	    this.hv.setHelpSize(GraphicsManager.MAX_DESKTOP_WINDOW_SIZE, GraphicsManager.MAX_DESKTOP_WINDOW_SIZE);
	}
	this.helpFrame.pack();
    }
}