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

import studio.ignitionigloogames.twistedtrek.import1.Import1;
import studio.ignitionigloogames.twistedtrek.import1.PreferencesManager;
import studio.ignitionigloogames.twistedtrek.import1.game.ObjectInventory;
import studio.ignitionigloogames.twistedtrek.import1.generic.GenericWall;

public class DamageableWall extends GenericWall {
    // Constructors
    public DamageableWall() {
	super();
    }

    @Override
    public boolean arrowHitAction(final int locX, final int locY, final int locZ, final int locW, final int dirX,
	    final int dirY, final int arrowType, final ObjectInventory inv) {
	this.moveFailedAction(true, locX, locY, inv);
	return false;
    }

    @Override
    public void moveFailedAction(final boolean ie, final int dirX, final int dirY, final ObjectInventory inv) {
	// Crack the wall
	final int pz = Import1.getApplication().getGameManager().getPlayerManager().getPlayerLocationZ();
	final int pw = Import1.getApplication().getGameManager().getPlayerManager().getPlayerLocationW();
	Import1.getApplication().getGameManager().morph(new CrackedWall(), dirX, dirY, pz, pw);
	// Play move failed sound, if it's enabled
	if (Import1.getApplication().getPrefsManager().getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
	    this.playMoveFailedSound();
	}
    }

    @Override
    public String getName() {
	return "Damageable Wall";
    }

    @Override
    public String getPluralName() {
	return "Damageable Walls";
    }

    @Override
    public String getGameName() {
	return "Wall";
    }

    @Override
    public String getMoveFailedSoundName() {
	return "crack";
    }

    @Override
    public byte getObjectID() {
	return (byte) 0;
    }

    @Override
    public String getDescription() {
	return "Damageable Walls turn into Cracked Walls when hit.";
    }
}