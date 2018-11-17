package studio.ignitionigloogames.twistedtrek.import2.battle;

import javax.swing.JOptionPane;

import studio.ignitionigloogames.twistedtrek.import2.creatures.party.PartyManager;
import studio.ignitionigloogames.twistedtrek.import2.creatures.party.PartyMember;

public class BossRewards {
    // Fields
    static final String[] rewardOptions = { "Attack", "Defense", "HP", "MP" };

    // Constructor
    private BossRewards() {
	// Do nothing
    }

    // Methods
    public static void doRewards() {
	final PartyMember player = PartyManager.getParty().getLeader();
	String dialogResult = null;
	while (dialogResult == null) {
	    dialogResult = (String) JOptionPane.showInputDialog(null,
		    "You get to increase a stat permanently.\nWhich Stat?", "Boss Rewards",
		    JOptionPane.QUESTION_MESSAGE, null, BossRewards.rewardOptions, BossRewards.rewardOptions[0]);
	}
	if (dialogResult.equals(BossRewards.rewardOptions[0])) {
	    // Attack
	    player.spendPointOnAttack();
	} else if (dialogResult.equals(BossRewards.rewardOptions[1])) {
	    // Defense
	    player.spendPointOnDefense();
	} else if (dialogResult.equals(BossRewards.rewardOptions[2])) {
	    // HP
	    player.spendPointOnHP();
	} else if (dialogResult.equals(BossRewards.rewardOptions[3])) {
	    // MP
	    player.spendPointOnMP();
	}
	PartyManager.updatePostKill();
    }
}
