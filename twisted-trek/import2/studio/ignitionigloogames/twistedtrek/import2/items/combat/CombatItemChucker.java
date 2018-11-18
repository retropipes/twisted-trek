/*  Import2: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package studio.ignitionigloogames.twistedtrek.import2.items.combat;

import studio.ignitionigloogames.commondialogs.CommonDialogs;
import studio.ignitionigloogames.twistedtrek.import2.Import2;
import studio.ignitionigloogames.twistedtrek.import2.battle.BattleTarget;
import studio.ignitionigloogames.twistedtrek.import2.creatures.AbstractCreature;
import studio.ignitionigloogames.twistedtrek.import2.creatures.party.PartyManager;
import studio.ignitionigloogames.twistedtrek.import2.effects.Effect;
import studio.ignitionigloogames.twistedtrek.import2.items.ItemInventory;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.SoundConstants;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.SoundManager;

public class CombatItemChucker {
    // Fields
    private static final CombatItemList COMBAT_ITEMS = new CombatItemList();

    // Private Constructor
    private CombatItemChucker() {
	// Do nothing
    }

    public static boolean selectAndUseItem(final AbstractCreature user) {
	boolean result = false;
	final CombatItem i = CombatItemChucker.selectItem(user);
	if (i != null) {
	    result = CombatItemChucker.useItem(i, user);
	}
	return result;
    }

    public static boolean useItem(final CombatItem used, final AbstractCreature user) {
	if (used != null) {
	    final Effect e = used.getEffect();
	    // Play item's associated sound effect, if it has one
	    final int snd = used.getSound();
	    SoundManager.playSound(snd);
	    e.resetEffect();
	    final AbstractCreature target = CombatItemChucker.resolveTarget(used, user.getTeamID());
	    used.use();
	    if (target.isEffectActive(e)) {
		target.extendEffect(e, e.getInitialRounds());
	    } else {
		e.restoreEffect();
		target.applyEffect(e);
	    }
	    return true;
	} else {
	    return false;
	}
    }

    private static AbstractCreature resolveTarget(final CombatItem cast, final int teamID) {
	final BattleTarget target = cast.getTarget();
	switch (target) {
	case SELF:
	    if (teamID == AbstractCreature.TEAM_PARTY) {
		return PartyManager.getParty().getLeader();
	    } else {
		return Import2.getApplication().getBattle().getEnemy();
	    }
	case ENEMY:
	    if (teamID == AbstractCreature.TEAM_PARTY) {
		return Import2.getApplication().getBattle().getEnemy();
	    } else {
		return PartyManager.getParty().getLeader();
	    }
	default:
	    return null;
	}
    }

    private static CombatItem selectItem(final AbstractCreature user) {
	final ItemInventory ii = user.getItems();
	if (ii != null) {
	    final String[] names = ii.generateCombatUsableStringArray();
	    final String[] displayNames = ii.generateCombatUsableDisplayStringArray();
	    if (names != null && displayNames != null) {
		// Play using item sound
		SoundManager.playSound(SoundConstants.SOUND_SPELL);
		String dialogResult = null;
		dialogResult = CommonDialogs.showInputDialog("Select an Item to Use", "Select Item", displayNames,
			displayNames[0]);
		if (dialogResult != null) {
		    int index;
		    for (index = 0; index < displayNames.length; index++) {
			if (dialogResult.equals(displayNames[index])) {
			    break;
			}
		    }
		    final CombatItem i = CombatItemChucker.COMBAT_ITEMS.getItemByName(names[index]);
		    if (ii.getUses(i) > 0) {
			return i;
		    } else {
			CommonDialogs.showErrorDialog("You try to use an item, but realize you've run out!",
				"Select Item");
			return null;
		    }
		} else {
		    return null;
		}
	    } else {
		CommonDialogs.showErrorDialog("You try to use an item, but realize you don't have any!", "Select Item");
		return null;
	    }
	} else {
	    CommonDialogs.showErrorDialog("You try to use an item, but realize you don't have any!", "Select Item");
	    return null;
	}
    }
}
