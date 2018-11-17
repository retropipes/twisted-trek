/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package studio.ignitionigloogames.twistedtrek.import2.items.combat;

import studio.ignitionigloogames.twistedtrek.import2.items.combat.predefined.Bolt;
import studio.ignitionigloogames.twistedtrek.import2.items.combat.predefined.Bomb;
import studio.ignitionigloogames.twistedtrek.import2.items.combat.predefined.Fireball;
import studio.ignitionigloogames.twistedtrek.import2.items.combat.predefined.Potion;
import studio.ignitionigloogames.twistedtrek.import2.items.combat.predefined.Rope;

public class CombatItemList {
    // Fields
    private final CombatItem[] allItems;

    // Constructor
    public CombatItemList() {
	this.allItems = new CombatItem[] { new Bomb(), new Rope(), new Bolt(), new Potion(), new Fireball() };
    }

    // Methods
    public CombatItem[] getAllItems() {
	return this.allItems;
    }

    public String[] getAllNames() {
	final String[] allNames = new String[this.allItems.length];
	for (int x = 0; x < this.allItems.length; x++) {
	    allNames[x] = this.allItems[x].getName();
	}
	return allNames;
    }

    CombatItem getItemByName(final String name) {
	for (final CombatItem allItem : this.allItems) {
	    if (name.equals(allItem.getName())) {
		return allItem;
	    }
	}
	return null;
    }
}
