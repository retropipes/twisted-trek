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
package studio.ignitionigloogames.twistedtrek.import1.objects;

import studio.ignitionigloogames.randomrange.RandomRange;
import studio.ignitionigloogames.twistedtrek.import1.Import1;
import studio.ignitionigloogames.twistedtrek.import1.PreferencesManager;
import studio.ignitionigloogames.twistedtrek.import1.creatures.PCManager;
import studio.ignitionigloogames.twistedtrek.import1.game.ObjectInventory;
import studio.ignitionigloogames.twistedtrek.import1.generic.GenericTrap;
import studio.ignitionigloogames.twistedtrek.import1.resourcemanagers.SoundManager;

public class DrainTrap extends GenericTrap {
    // Fields
    private RandomRange amountDrained;
    private static final int MIN_DRAIN = -1;
    private int maxDrain;

    // Constructors
    public DrainTrap() {
	super();
    }

    @Override
    public String getName() {
	return "Drain Trap";
    }

    @Override
    public String getPluralName() {
	return "Drain Traps";
    }

    @Override
    public byte getObjectID() {
	return (byte) 11;
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY, final ObjectInventory inv) {
	this.maxDrain = PCManager.getPlayer().getMaximumMP() / 10;
	this.amountDrained = new RandomRange(this.maxDrain, DrainTrap.MIN_DRAIN);
	PCManager.getPlayer().regenerate(this.amountDrained.generate());
	if (Import1.getApplication().getPrefsManager().getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
	    SoundManager.play("barrier");
	}
	Import1.getApplication().getGameManager().decay();
    }

    @Override
    public String getDescription() {
	return "Drain Traps drain your magic when stepped on, then disappear.";
    }
}