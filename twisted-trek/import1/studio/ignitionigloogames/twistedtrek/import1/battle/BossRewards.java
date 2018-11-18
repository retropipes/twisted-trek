/* Import1: A Maze-Solving Game */
package studio.ignitionigloogames.twistedtrek.import1.battle;

import studio.ignitionigloogames.twistedtrek.import1.Messager;
import studio.ignitionigloogames.twistedtrek.import1.creatures.PCManager;
import studio.ignitionigloogames.twistedtrek.import1.creatures.PlayerCharacter;

public class BossRewards {
    // Methods
    public void doRewards() {
	final PlayerCharacter playerCharacter = PCManager.getPlayer();
	final String[] rewardOptions = { "Attack", "Defense", "HP", "MP" };
	String dialogResult = null;
	while (dialogResult == null) {
	    dialogResult = Messager.showInputDialog("You get to increase a stat permanently.\nWhich Stat?",
		    "Boss Rewards", rewardOptions, rewardOptions[0]);
	}
	if (dialogResult.equals(rewardOptions[0])) {
	    // Attack
	    playerCharacter.spendPointOnAttack();
	} else if (dialogResult.equals(rewardOptions[1])) {
	    // Defense
	    playerCharacter.spendPointOnDefense();
	} else if (dialogResult.equals(rewardOptions[2])) {
	    // HP
	    playerCharacter.spendPointOnHP();
	} else {
	    // MP
	    playerCharacter.spendPointOnMP();
	}
	final int pAtk = playerCharacter.getPermanentAttackPoints();
	final int pDef = playerCharacter.getPermanentDefensePoints();
	final int pHP = playerCharacter.getPermanentHPPoints();
	final int pMP = playerCharacter.getPermanentMPPoints();
	final int k = playerCharacter.getKills();
	PCManager.createNewPCPostKill(pAtk, pDef, pHP, pMP, k);
    }
}
