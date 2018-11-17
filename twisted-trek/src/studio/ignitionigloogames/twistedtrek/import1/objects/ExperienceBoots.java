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
import studio.ignitionigloogames.twistedtrek.import1.Messager;
import studio.ignitionigloogames.twistedtrek.import1.creatures.PCManager;
import studio.ignitionigloogames.twistedtrek.import1.creatures.PlayerCharacter;
import studio.ignitionigloogames.twistedtrek.import1.generic.GenericBoots;

public class ExperienceBoots extends GenericBoots {
    // Constants
    private static final int EXP_AMOUNT = 1;

    // Constructors
    public ExperienceBoots() {
	super();
    }

    @Override
    public String getName() {
	return "Experience Boots";
    }

    @Override
    public String getPluralName() {
	return "Pairs of Experience Boots";
    }

    @Override
    public byte getObjectID() {
	return (byte) 9;
    }

    @Override
    public String getDescription() {
	return "Experience Boots give you experience as you walk. Note that you can only wear one pair of boots at once.";
    }

    @Override
    public void stepAction() {
	final PlayerCharacter playerCharacter = PCManager.getPlayer();
	playerCharacter.offsetExperience(ExperienceBoots.EXP_AMOUNT);
	Import1.getApplication().getGameManager().getScoreTracker().incrementScore();
	// Level Up Check
	if (playerCharacter.checkLevelUp()) {
	    playerCharacter.levelUp();
	    Import1.getApplication().getGameManager().keepNextMessage();
	    Messager.showMessage("You reached level " + playerCharacter.getLevel() + ".");
	}
    }
}
