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
import studio.ignitionigloogames.twistedtrek.import1.generic.GenericDungeonObject;

public class ItemShop extends GenericDungeonObject {
    // Constructors
    public ItemShop() {
	super(false);
    }

    @Override
    public void postMoveActionHook() {
	Fantastle5.getApplication().getItems().showShop();
    }

    @Override
    public String getName() {
	return "Item Shop";
    }

    @Override
    public String getPluralName() {
	return "Item Shops";
    }

    @Override
    public String getDescription() {
	return "Item Shops sell items used in battle.";
    }

    @Override
    public byte getObjectID() {
	return (byte) 0;
    }
}
