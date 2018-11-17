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
import studio.ignitionigloogames.twistedtrek.import1.Messager;
import studio.ignitionigloogames.twistedtrek.import1.PreferencesManager;
import studio.ignitionigloogames.twistedtrek.import1.effects.EffectConstants;
import studio.ignitionigloogames.twistedtrek.import1.game.ObjectInventory;
import studio.ignitionigloogames.twistedtrek.import1.generic.GenericTrap;
import studio.ignitionigloogames.twistedtrek.import1.generic.MazeObject;

public class ConfusionTrap extends GenericTrap {
    // Fields
    private static final int EFFECT_DURATION = 10;

    // Constructors
    public ConfusionTrap() {
	super();
    }

    @Override
    public String getName() {
	return "Confusion Trap";
    }

    @Override
    public String getPluralName() {
	return "Confusion Traps";
    }

    @Override
    public byte getObjectID() {
	return (byte) 4;
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY, final ObjectInventory inv) {
	Messager.showMessage("You are confused!");
	Fantastle5.getApplication().getGameManager().activateEffect(EffectConstants.EFFECT_CONFUSED,
		ConfusionTrap.EFFECT_DURATION);
	if (Fantastle5.getApplication().getPrefsManager().getSoundEnabled(PreferencesManager.SOUNDS_GAME)) {
	    MazeObject.playConfusedSound();
	}
    }

    @Override
    public String getMoveSuccessSoundName() {
	return "confused";
    }

    @Override
    public String getDescription() {
	return "Confusion Traps randomly alter your controls for 10 steps when stepped on.";
    }
}