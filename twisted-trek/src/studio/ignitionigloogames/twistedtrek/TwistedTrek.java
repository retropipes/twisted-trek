/* Twisted Trek: A Dual-World Action RPG */
package studio.ignitionigloogames.twistedtrek;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import studio.ignitionigloogames.twistedtrek.panels.GuiPanel;
import studio.ignitionigloogames.twistedtrek.panels.MessagePanel;
import studio.ignitionigloogames.twistedtrek.screens.Screen;
import studio.ignitionigloogames.twistedtrek.screens.StartScreen;

public class TwistedTrek extends JFrame implements KeyListener, MouseListener {
    private static final long serialVersionUID = 1060623638149583738L;
    private final GuiPanel terminal;
    private final MessagePanel messages;
    private Screen screen;

    public TwistedTrek() {
	super("Twisted Trek");
	this.terminal = new GuiPanel();
	this.messages = new MessagePanel();
	this.setLayout(new BorderLayout());
	this.add(this.terminal, BorderLayout.CENTER);
	this.add(this.messages, BorderLayout.SOUTH);
	this.add(this.messages.getStatsLabel(), BorderLayout.NORTH);
	this.pack();
	this.screen = new StartScreen();
	this.addKeyListener(this);
	this.terminal.addMouseListener(this);
	this.repaint();
    }

    @Override
    public void repaint() {
	this.terminal.clear();
	this.screen.displayOutput(this.terminal, this.messages);
	this.pack();
	super.repaint();
    }

    @Override
    public void keyPressed(final KeyEvent e) {
	if (!e.isMetaDown()) {
	    this.screen = this.screen.respondToUserInput(e, null);
	    this.repaint();
	}
    }

    @Override
    public void keyReleased(final KeyEvent e) {
    }

    @Override
    public void keyTyped(final KeyEvent e) {
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
	this.screen = this.screen.respondToUserInput(null, e);
	this.pack();
	this.repaint();
    }

    @Override
    public void mousePressed(final MouseEvent e) {
	// Do nothing
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
	// Do nothing
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
	// Do nothing
    }

    @Override
    public void mouseExited(final MouseEvent e) {
	// Do nothing
    }

    public static void main(final String[] args) {
	TwistedTrek.hookLAF();
	final TwistedTrek app = new TwistedTrek();
	app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	app.setVisible(true);
    }

    private static void hookLAF() {
	if (System.getProperty("os.name").startsWith("Mac")) {
	    // macOS-specific stuff
	    try {
		// Tell the UIManager to use the platform native look and
		// feel
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// Hint to the UI that the L&F is decorated
		JFrame.setDefaultLookAndFeelDecorated(true);
	    } catch (final Exception e) {
		// Do nothing
	    }
	    System.setProperty("apple.laf.useScreenMenuBar", "true");
	} else if (System.getProperty("os.name").startsWith("Windows")) {
	    // Windows-specific stuff
	    try {
		// Tell the UIManager to use the platform native look and
		// feel
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// Hint to the UI that the L&F is decorated
		JFrame.setDefaultLookAndFeelDecorated(true);
	    } catch (final Exception e) {
		// Do nothing
	    }
	} else {
	    // All other platforms
	    try {
		// Tell the UIManager to use the Nimbus look and feel
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		// Hint to the UI that the L&F is decorated
		JFrame.setDefaultLookAndFeelDecorated(true);
	    } catch (final Exception e) {
		// Do nothing
	    }
	}
    }
}
