/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package studio.ignitionigloogames.twistedtrek.import2.battle.window.time;

import studio.ignitionigloogames.twistedtrek.import2.prefs.PreferencesManager;

class WindowTimeBattleSpeed {
    // Constants
    private static int SPEED_FACTOR = 10;

    // Constructor
    private WindowTimeBattleSpeed() {
	// Do nothing
    }

    // Method
    static int getSpeed() {
	return PreferencesManager.getBattleSpeed() / WindowTimeBattleSpeed.SPEED_FACTOR;
    }
}