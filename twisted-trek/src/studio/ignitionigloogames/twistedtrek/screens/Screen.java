package studio.ignitionigloogames.twistedtrek.screens;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import studio.ignitionigloogames.twistedtrek.panels.GuiPanel;
import studio.ignitionigloogames.twistedtrek.panels.MessagePanel;

public interface Screen {
    public void displayOutput(GuiPanel terminal, MessagePanel messages);

    public Screen respondToUserInput(KeyEvent key, MouseEvent mouse);
}
