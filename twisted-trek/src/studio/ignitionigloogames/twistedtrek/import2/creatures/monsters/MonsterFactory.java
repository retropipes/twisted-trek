/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package studio.ignitionigloogames.twistedtrek.import2.creatures.monsters;

import studio.ignitionigloogames.twistedtrek.import2.creatures.AbstractCreature;
import studio.ignitionigloogames.twistedtrek.import2.creatures.party.PartyManager;
import studio.ignitionigloogames.twistedtrek.import2.maze.Maze;

public class MonsterFactory {
    private MonsterFactory() {
	// Do nothing
    }

    public static AbstractCreature getNewMonsterInstance() {
	if (PartyManager.getParty().getTowerLevel() == Maze.getMaxLevels() - 1) {
	    return new BossMonster();
	} else {
	    return new BothRandomScalingStaticMonster();
	}
    }
}
