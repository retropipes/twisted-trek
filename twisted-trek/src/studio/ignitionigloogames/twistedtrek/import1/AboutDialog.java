/*  Fantastle: A Maze-Solving Game
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import studio.ignitionigloogames.twistedtrek.import1.resourcemanagers.GraphicsManager;

public class AboutDialog {
    // Fields
    private JFrame aboutFrame;
    private Container aboutPane, textPane, buttonPane, logoPane;
    private JButton aboutOK;
    private EventHandler handler;

    // Constructors
    public AboutDialog(final String ver) {
	this.setUpGUI(ver);
    }

    // Methods
    public void showAboutDialog() {
	this.aboutFrame.setVisible(true);
    }

    void hideAboutDialog() {
	this.aboutFrame.setVisible(false);
    }

    private void setUpGUI(final String ver) {
	this.handler = new EventHandler();
	this.aboutFrame = new JFrame("About Fantastle");
	this.aboutPane = new Container();
	this.textPane = new Container();
	this.buttonPane = new Container();
	this.logoPane = new Container();
	this.aboutOK = new JButton("OK");
	this.aboutOK.setDefaultCapable(true);
	this.aboutFrame.getRootPane().setDefaultButton(this.aboutOK);
	this.aboutFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	this.aboutPane.setLayout(new BorderLayout());
	this.logoPane.setLayout(new FlowLayout());
	this.logoPane.add(new JLabel("", GraphicsManager.getMiniatureLogo(), SwingConstants.LEFT));
	this.textPane.setLayout(new GridLayout(4, 1));
	this.textPane.add(new JLabel("Fantastle Version: " + ver));
	this.textPane.add(new JLabel("Author: Eric Ahnell"));
	this.textPane.add(new JLabel("Web Site: http://fantastle.worldwizard.net/"));
	this.textPane.add(new JLabel("E-mail bug reports to: fantastle@worldwizard.net  "));
	this.buttonPane.setLayout(new FlowLayout());
	this.buttonPane.add(this.aboutOK);
	this.aboutPane.add(this.logoPane, BorderLayout.WEST);
	this.aboutPane.add(this.textPane, BorderLayout.CENTER);
	this.aboutPane.add(this.buttonPane, BorderLayout.SOUTH);
	this.aboutFrame.setResizable(false);
	this.aboutOK.addActionListener(this.handler);
	this.aboutFrame.setContentPane(this.aboutPane);
	this.aboutFrame.pack();
    }

    private class EventHandler implements ActionListener {
	public EventHandler() {
	    // TODO Auto-generated constructor stub
	}

	// Handle buttons
	@Override
	public void actionPerformed(final ActionEvent e) {
	    try {
		final AboutDialog ad = AboutDialog.this;
		final String cmd = e.getActionCommand();
		if (cmd.equals("OK")) {
		    ad.hideAboutDialog();
		}
	    } catch (final Exception ex) {
		Fantastle5.debug(ex);
	    }
	}
    }
}