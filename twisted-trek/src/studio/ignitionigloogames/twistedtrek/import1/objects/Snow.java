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

import studio.ignitionigloogames.twistedtrek.import1.generic.GenericGround;

public class Snow extends GenericGround {
    // Constructors
    public Snow() {
	super();
    }

    @Override
    public String getName() {
	return "Snow";
    }

    @Override
    public String getPluralName() {
	return "Squares of Snow";
    }

    @Override
    public byte getObjectID() {
	return (byte) 8;
    }

    @Override
    public String getDescription() {
	return "Snow is one of the many types of ground.";
    }
}