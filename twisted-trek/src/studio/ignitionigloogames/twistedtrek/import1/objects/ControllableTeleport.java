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

import studio.ignitionigloogames.twistedtrek.import1.Application;
import studio.ignitionigloogames.twistedtrek.import1.Import1;
import studio.ignitionigloogames.twistedtrek.import1.Messager;
import studio.ignitionigloogames.twistedtrek.import1.PreferencesManager;
import studio.ignitionigloogames.twistedtrek.import1.game.ObjectInventory;
import studio.ignitionigloogames.twistedtrek.import1.generic.GenericTeleport;
import studio.ignitionigloogames.twistedtrek.import1.generic.MazeObject;

public class ControllableTeleport extends GenericTeleport {
    // Constructors
    public ControllableTeleport() {
	super(0, 0, 0, 0);
    }

    // Scriptability
    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY, final ObjectInventory inv) {
	final Application app = Import1.getApplication();
	if (app.getPrefsManager().getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
	    this.playMoveSuccessSound();
	}
	app.getGameManager().controllableTeleport();
    }

    @Override
    public String getName() {
	return "Controllable Teleport";
    }

    @Override
    public String getPluralName() {
	return "Controllable Teleports";
    }

    @Override
    public void editorProbeHook() {
	Messager.showMessage(this.getName());
    }

    @Override
    public MazeObject editorPropertiesHook() {
	return null;
    }

    @Override
    public byte getObjectID() {
	return (byte) 0;
    }

    @Override
    public String getMoveSuccessSoundName() {
	return "walk";
    }

    @Override
    public String getDescription() {
	return "Controllable Teleports let you choose the place you teleport to.";
    }

    @Override
    public int getCustomFormat() {
	return 0;
    }
}