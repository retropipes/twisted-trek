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
package studio.ignitionigloogames.twistedtrek.import1.generic;

import studio.ignitionigloogames.twistedtrek.import1.Application;
import studio.ignitionigloogames.twistedtrek.import1.Fantastle5;
import studio.ignitionigloogames.twistedtrek.import1.PreferencesManager;
import studio.ignitionigloogames.twistedtrek.import1.game.ObjectInventory;
import studio.ignitionigloogames.twistedtrek.import1.maze.Maze;

public abstract class GenericLock extends MazeObject {
    // Field declarations
    private GenericKey key;

    // Constructors
    protected GenericLock(final GenericKey mgk) {
	super(true);
	this.key = mgk;
    }

    protected GenericLock(final GenericKey mgk, final boolean doesAcceptPushInto) {
	super(true, false, doesAcceptPushInto, false, false, false, false, true, false, 0);
	this.key = mgk;
    }

    protected GenericLock(final boolean isSolid, final GenericKey mgk) {
	super(isSolid);
	this.key = mgk;
    }

    @Override
    public boolean equals(final Object obj) {
	if (obj == null) {
	    return false;
	}
	if (this.getClass() != obj.getClass()) {
	    return false;
	}
	final GenericLock other = (GenericLock) obj;
	if (this.key != other.key && (this.key == null || !this.key.equals(other.key))) {
	    return false;
	}
	return true;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 71 * hash + (this.key != null ? this.key.hashCode() : 0);
	return hash;
    }

    @Override
    public GenericLock clone() {
	final GenericLock copy = (GenericLock) super.clone();
	copy.key = this.key.clone();
	return copy;
    }

    // Accessor methods
    public GenericKey getKey() {
	return this.key;
    }

    // Scriptability
    @Override
    public abstract void moveFailedAction(final boolean ie, final int dirX, final int dirY, final ObjectInventory inv);

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY, final ObjectInventory inv) {
	if (!this.key.isInfinite()) {
	    inv.removeItem(this.key);
	}
	final Application app = Fantastle5.getApplication();
	app.getGameManager().decay();
	// Play unlock sound, if it's enabled
	if (Fantastle5.getApplication().getPrefsManager().getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
	    this.playMoveSuccessSound();
	}
    }

    @Override
    public boolean isConditionallySolid(final ObjectInventory inv) {
	return !inv.isItemThere(this.key);
    }

    @Override
    public boolean isConditionallyDirectionallySolid(final boolean ie, final int dirX, final int dirY,
	    final ObjectInventory inv) {
	return !inv.isItemThere(this.key);
    }

    @Override
    public abstract String getName();

    @Override
    public byte getGroupID() {
	return (byte) 5;
    }

    @Override
    protected void setTypes() {
	this.type.set(TypeConstants.TYPE_LOCK);
    }

    @Override
    public int getLayer() {
	return Maze.LAYER_OBJECT;
    }

    @Override
    public String getMoveSuccessSoundName() {
	return "unlock";
    }

    @Override
    public int getCustomProperty(final int propID) {
	return MazeObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }
}