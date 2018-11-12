package studio.ignitionigloogames.twistedtrek.screens;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import studio.ignitionigloogames.twistedtrek.Creature;
import studio.ignitionigloogames.twistedtrek.Item;
import studio.ignitionigloogames.twistedtrek.Spell;
import studio.ignitionigloogames.twistedtrek.panels.GuiPanel;
import studio.ignitionigloogames.twistedtrek.panels.MessagePanel;

public class ReadSpellScreen implements Screen {
    protected Creature player;
    private final String letters;
    private final Item item;
    private final int sx;
    private final int sy;

    public ReadSpellScreen(final Creature newPlayer, final int nsx, final int nsy, final Item newItem) {
	this.player = newPlayer;
	this.letters = "abcdefghijklmnopqrstuvwxyz";
	this.item = newItem;
	this.sx = nsx;
	this.sy = nsy;
    }

    @Override
    public void displayOutput(final GuiPanel terminal, final MessagePanel messages) {
	final ArrayList<String> lines = this.getList();
	for (final String line : lines) {
	    messages.write(line);
	}
	messages.write("What would you like to read?");
    }

    private ArrayList<String> getList() {
	final ArrayList<String> lines = new ArrayList<>();
	for (int i = 0; i < this.item.writtenSpells().size(); i++) {
	    final Spell spell = this.item.writtenSpells().get(i);
	    final String line = this.letters.charAt(i) + " - " + spell.name() + " (" + spell.manaCost() + " mana)";
	    lines.add(line);
	}
	return lines;
    }

    @Override
    public Screen respondToUserInput(final KeyEvent key, final MouseEvent mouse) {
	if (key != null) {
	    final char c = key.getKeyChar();
	    final Item[] items = this.player.inventory().getItems();
	    if (this.letters.indexOf(c) > -1 && items.length > this.letters.indexOf(c)
		    && items[this.letters.indexOf(c)] != null) {
		return this.use(this.item.writtenSpells().get(this.letters.indexOf(c)));
	    } else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
		return null;
	    } else {
		return this;
	    }
	} else {
	    return this;
	}
    }

    protected Screen use(final Spell spell) {
	if (spell.requiresTarget()) {
	    return new CastSpellScreen(this.player, "", this.sx, this.sy, spell);
	}
	this.player.castSpell(spell, this.player.x, this.player.y);
	return null;
    }
}
