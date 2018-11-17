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
package studio.ignitionigloogames.twistedtrek.import1.objects;

import studio.ignitionigloogames.twistedtrek.import1.Fantastle5;
import studio.ignitionigloogames.twistedtrek.import1.PreferencesManager;
import studio.ignitionigloogames.twistedtrek.import1.generic.GenericGem;
import studio.ignitionigloogames.twistedtrek.import1.generic.MazeObject;

public class LightnessGem extends GenericGem {
    // Constructors
    public LightnessGem() {
	super();
    }

    @Override
    public String getName() {
	return "Lightness Gem";
    }

    @Override
    public String getPluralName() {
	return "Lightness Gems";
    }

    @Override
    public byte getObjectID() {
	return (byte) 3;
    }

    @Override
    public void postMoveActionHook() {
	final int currLevel = Fantastle5.getApplication().getGameManager().getPlayerManager().getPlayerLocationW();
	Fantastle5.getApplication().getMazeManager().getMaze().incrementVisionRadius(currLevel);
	if (Fantastle5.getApplication().getPrefsManager().getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
	    MazeObject.playLightSound();
	}
    }

    @Override
    public String getMoveSuccessSoundName() {
	return "light";
    }

    @Override
    public String getDescription() {
	return "Lightness Gems increase the visible area by 1.";
    }
}
