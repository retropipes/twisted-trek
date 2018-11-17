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
import studio.ignitionigloogames.twistedtrek.import1.PreferencesManager;
import studio.ignitionigloogames.twistedtrek.import1.game.ObjectInventory;
import studio.ignitionigloogames.twistedtrek.import1.generic.GenericMovableObject;
import studio.ignitionigloogames.twistedtrek.import1.generic.MazeObject;
import studio.ignitionigloogames.twistedtrek.import1.maze.Maze;

public class PullableBlockOnce extends GenericMovableObject {
    // Constructors
    public PullableBlockOnce() {
	super(false, true);
    }

    @Override
    public String getName() {
	return "Pullable Block Once";
    }

    @Override
    public String getPluralName() {
	return "Pullable Blocks Once";
    }

    @Override
    public void pullAction(final ObjectInventory inv, final MazeObject mo, final int x, final int y, final int pushX,
	    final int pushY) {
	final Application app = Import1.getApplication();
	app.getGameManager().updatePulledPosition(x, y, pushX, pushY, this);
	if (app.getPrefsManager().getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
	    MazeObject.playPullSuccessSound();
	}
	app.getGameManager().morphOther(new Wall(), pushX, pushY, Maze.LAYER_OBJECT);
    }

    @Override
    public byte getObjectID() {
	return (byte) 7;
    }

    @Override
    public String getDescription() {
	return "Pullable Blocks Once can only be pulled once, before turning into a wall.";
    }
}