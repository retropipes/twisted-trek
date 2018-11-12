package studio.ignitionigloogames.twistedtrek;

import java.io.IOException;

import studio.ignitionigloogames.twistedtrek.fileio.XMLFileReader;
import studio.ignitionigloogames.twistedtrek.fileio.XMLFileWriter;

public class Spell {
    private String name;

    public String name() {
	return this.name;
    }

    private int manaCost;

    public int manaCost() {
	return this.manaCost;
    }

    private Effect effect;

    public Effect effect() {
	return new Effect(this.effect);
    }

    @SuppressWarnings("static-method")
    public boolean requiresTarget() {
	return true;
    }

    public Spell() {
	// Create an empty spell to be populated later
    }

    public Spell(final String newName, final int newManaCost, final Effect newEffect) {
	this.name = newName;
	this.manaCost = newManaCost;
	this.effect = newEffect;
    }

    public void loadSpell(final XMLFileReader reader) throws IOException {
	reader.readOpeningGroup("spell");
	this.name = reader.readCustomString("name");
	this.manaCost = reader.readCustomInt("manaCost");
	final Effect ef = new Effect((Item) null);
	ef.loadEffect(reader);
	this.effect = ef;
	reader.readClosingGroup("spell");
    }

    public void saveSpell(final XMLFileWriter writer) throws IOException {
	writer.writeOpeningGroup("spell");
	writer.writeCustomString(this.name, "name");
	writer.writeCustomInt(this.manaCost, "manaCost");
	this.effect.saveEffect(writer);
	writer.writeClosingGroup("spell");
    }
}
