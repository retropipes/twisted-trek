/* Import2: An RPG */
package studio.ignitionigloogames.twistedtrek.import2.battle.window.turn;

import javax.swing.JFrame;

import studio.ignitionigloogames.randomrange.RandomRange;
import studio.ignitionigloogames.twistedtrek.import2.Application;
import studio.ignitionigloogames.twistedtrek.import2.Import2;
import studio.ignitionigloogames.twistedtrek.import2.ai.window.AbstractWindowAIRoutine;
import studio.ignitionigloogames.twistedtrek.import2.battle.AbstractBattle;
import studio.ignitionigloogames.twistedtrek.import2.battle.BattleResults;
import studio.ignitionigloogames.twistedtrek.import2.battle.damageengines.AbstractDamageEngine;
import studio.ignitionigloogames.twistedtrek.import2.creatures.AbstractCreature;
import studio.ignitionigloogames.twistedtrek.import2.creatures.StatConstants;
import studio.ignitionigloogames.twistedtrek.import2.creatures.monsters.AbstractMonster;
import studio.ignitionigloogames.twistedtrek.import2.creatures.monsters.BossMonster;
import studio.ignitionigloogames.twistedtrek.import2.creatures.monsters.MonsterFactory;
import studio.ignitionigloogames.twistedtrek.import2.creatures.party.PartyManager;
import studio.ignitionigloogames.twistedtrek.import2.creatures.party.PartyMember;
import studio.ignitionigloogames.twistedtrek.import2.effects.Effect;
import studio.ignitionigloogames.twistedtrek.import2.game.GameLogicManager;
import studio.ignitionigloogames.twistedtrek.import2.items.combat.CombatItemChucker;
import studio.ignitionigloogames.twistedtrek.import2.maze.abc.AbstractMazeObject;
import studio.ignitionigloogames.twistedtrek.import2.maze.objects.BattleCharacter;
import studio.ignitionigloogames.twistedtrek.import2.prefs.PreferencesManager;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.MusicConstants;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.MusicManager;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.SoundConstants;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.SoundManager;
import studio.ignitionigloogames.twistedtrek.import2.spells.SpellCaster;

public class WindowTurnBattleLogic extends AbstractBattle {
    // Fields
    private int stealAmount;
    private int damage;
    private boolean enemyDidDamage;
    private boolean playerDidDamage;
    private AbstractCreature enemy;
    private int result;
    private final AbstractDamageEngine pde;
    private final AbstractDamageEngine ede;
    private WindowTurnBattleGUI battleGUI;
    private static final int BASE_RUN_CHANCE = 80;
    private static final int RUN_CHANCE_DIFF_FACTOR = 5;
    private static final int ENEMY_BASE_RUN_CHANCE = 60;
    private static final int ENEMY_RUN_CHANCE_DIFF_FACTOR = 10;

    // Constructor
    public WindowTurnBattleLogic() {
	// Initialize Battle Parameters
	this.pde = AbstractDamageEngine.getPlayerInstance();
	this.ede = AbstractDamageEngine.getEnemyInstance();
	this.damage = 0;
	this.stealAmount = 0;
	this.enemyDidDamage = false;
	this.playerDidDamage = false;
	// Initialize GUI
	this.battleGUI = new WindowTurnBattleGUI();
    }

    @Override
    public JFrame getOutputFrame() {
	return this.battleGUI.getOutputFrame();
    }

    @Override
    public final boolean doPlayerActions(final int actionToPerform) {
	boolean success = true;
	final PartyMember playerCharacter = PartyManager.getParty().getLeader();
	if (actionToPerform == AbstractWindowAIRoutine.ACTION_ATTACK) {
	    final int actions = playerCharacter.getWindowBattleActionsPerRound();
	    for (int x = 0; x < actions; x++) {
		this.computePlayerDamage();
		this.displayPlayerRoundResults();
	    }
	} else if (actionToPerform == AbstractWindowAIRoutine.ACTION_CAST_SPELL) {
	    success = this.castSpell();
	} else if (actionToPerform == AbstractWindowAIRoutine.ACTION_FLEE) {
	    final RandomRange rf = new RandomRange(0, 100);
	    final int runChance = rf.generate();
	    if (runChance <= this.computeRunChance()) {
		// Success
		this.setResult(BattleResults.FLED);
	    } else {
		// Failure
		success = false;
		this.updateMessageAreaFleeFailed();
	    }
	} else if (actionToPerform == AbstractWindowAIRoutine.ACTION_STEAL) {
	    success = this.steal();
	    if (success) {
		SoundManager.playSound(SoundConstants.SOUND_DRAIN);
		this.updateMessageAreaPostSteal();
	    } else {
		SoundManager.playSound(SoundConstants.SOUND_ACTION_FAILED);
		this.updateMessageAreaStealFailed();
	    }
	} else if (actionToPerform == AbstractWindowAIRoutine.ACTION_DRAIN) {
	    success = this.drain();
	    if (success) {
		SoundManager.playSound(SoundConstants.SOUND_DRAIN);
		this.updateMessageAreaPostDrain();
	    } else {
		SoundManager.playSound(SoundConstants.SOUND_ACTION_FAILED);
		this.updateMessageAreaDrainFailed();
	    }
	} else if (actionToPerform == AbstractWindowAIRoutine.ACTION_USE_ITEM) {
	    success = this.useItem();
	}
	return success;
    }

    @Override
    public final void executeNextAIAction() {
	final int actionToPerform = this.enemy.getWindowAI().getNextAction(this.enemy);
	if (actionToPerform == AbstractWindowAIRoutine.ACTION_ATTACK) {
	    final int actions = this.enemy.getWindowBattleActionsPerRound();
	    for (int x = 0; x < actions; x++) {
		this.computeEnemyDamage();
		this.displayEnemyRoundResults();
	    }
	} else if (actionToPerform == AbstractWindowAIRoutine.ACTION_CAST_SPELL) {
	    SpellCaster.castSpell(this.enemy.getWindowAI().getSpellToCast(), this.enemy);
	} else if (actionToPerform == AbstractWindowAIRoutine.ACTION_FLEE) {
	    final RandomRange rf = new RandomRange(0, 100);
	    final int runChance = rf.generate();
	    if (runChance <= this.computeEnemyRunChance()) {
		// Success
		this.setResult(BattleResults.ENEMY_FLED);
	    } else {
		// Failure
		this.updateMessageAreaEnemyFleeFailed();
	    }
	}
    }

    private void computeDamage(final AbstractCreature theEnemy, final AbstractCreature acting,
	    final AbstractDamageEngine activeDE) {
	// Compute Damage
	this.damage = 0;
	final int actual = activeDE.computeDamage(theEnemy, acting);
	// Hit or Missed
	this.damage = actual;
	if (activeDE.weaponFumble()) {
	    acting.doDamage(this.damage);
	} else {
	    if (this.damage < 0) {
		acting.doDamage(-this.damage);
	    } else {
		theEnemy.doDamage(this.damage);
	    }
	}
	// Check damage
	if (acting instanceof PartyMember) {
	    if (this.damage > 0) {
		this.playerDidDamage = true;
	    } else if (this.damage < 0) {
		this.enemyDidDamage = true;
	    }
	} else if (acting instanceof AbstractMonster || acting instanceof BossMonster) {
	    if (this.damage > 0) {
		this.enemyDidDamage = true;
	    } else if (this.damage < 0) {
		this.playerDidDamage = true;
	    }
	}
    }

    final void computePlayerDamage() {
	// Compute Player Damage
	this.computeDamage(this.enemy, PartyManager.getParty().getLeader(), this.pde);
    }

    final void computeEnemyDamage() {
	// Compute Enemy Damage
	this.computeDamage(PartyManager.getParty().getLeader(), this.enemy, this.ede);
    }

    final int computeRunChance() {
	return WindowTurnBattleLogic.BASE_RUN_CHANCE
		- this.enemy.getLevelDifference() * WindowTurnBattleLogic.RUN_CHANCE_DIFF_FACTOR;
    }

    final int computeEnemyRunChance() {
	return WindowTurnBattleLogic.ENEMY_BASE_RUN_CHANCE
		+ this.enemy.getLevelDifference() * WindowTurnBattleLogic.ENEMY_RUN_CHANCE_DIFF_FACTOR;
    }

    @Override
    public final void displayBattleStats() {
	final PartyMember playerCharacter = PartyManager.getParty().getLeader();
	final String enemyName = this.enemy.getName();
	final String fightingWhat = this.enemy.getFightingWhatString();
	final String monsterLevelString = enemyName + "'s Level: " + Integer.toString(this.enemy.getLevel());
	final String monsterHPString = this.enemy.getHPString();
	final String monsterMPString = this.enemy.getMPString();
	final String playerHPString = playerCharacter.getHPString();
	final String playerMPString = playerCharacter.getMPString();
	final String displayMonsterHPString = enemyName + "'s HP: " + monsterHPString;
	final String displayMonsterMPString = enemyName + "'s MP: " + monsterMPString;
	final String displayPlayerHPString = "Your HP: " + playerHPString;
	final String displayPlayerMPString = "Your MP: " + playerMPString;
	final String displayString = fightingWhat + "\n" + monsterLevelString + "\n" + displayMonsterHPString + "\n"
		+ displayMonsterMPString + "\n" + displayPlayerHPString + "\n" + displayPlayerMPString;
	this.setStatusMessage(displayString);
    }

    final void displayPlayerRoundResults() {
	// Display player round results
	if (this.result != BattleResults.ENEMY_FLED) {
	    final String enemyName = this.enemy.getName();
	    final String playerDamageString = Integer.toString(this.damage);
	    final String playerFumbleDamageString = Integer.toString(this.damage);
	    String displayPlayerDamageString = null;
	    String playerWhackString = "";
	    if (this.pde.weaponFumble()) {
		displayPlayerDamageString = "FUMBLE! You drop your weapon, doing " + playerFumbleDamageString
			+ " damage to yourself!";
		SoundManager.playSound(SoundConstants.SOUND_FUMBLE);
	    } else {
		if (this.damage == 0) {
		    displayPlayerDamageString = "You try to hit the " + enemyName + ", but MISS!";
		    SoundManager.playSound(SoundConstants.SOUND_MISSED);
		} else if (this.damage < 0) {
		    displayPlayerDamageString = "You try to hit the " + enemyName + ", but are RIPOSTED for "
			    + -this.damage + " damage!";
		    SoundManager.playSound(SoundConstants.SOUND_COUNTER);
		} else {
		    displayPlayerDamageString = "You hit the " + enemyName + " for " + playerDamageString + " damage!";
		    SoundManager.playSound(SoundConstants.SOUND_HIT);
		}
		if (this.pde.weaponCrit()) {
		    playerWhackString += "CRITICAL HIT!\n";
		    SoundManager.playSound(SoundConstants.SOUND_CRITICAL);
		}
		if (this.pde.weaponPierce()) {
		    playerWhackString += "Your attack pierces the " + enemyName + "'s armor!\n";
		}
	    }
	    final String displayString = playerWhackString + displayPlayerDamageString;
	    this.setStatusMessage(displayString);
	}
    }

    final void displayEnemyRoundResults() {
	// Display enemy round results
	if (this.result != BattleResults.ENEMY_FLED) {
	    final String enemyName = this.enemy.getName();
	    final String enemyDamageString = Integer.toString(this.damage);
	    final String enemyFumbleDamageString = Integer.toString(this.damage);
	    String displayEnemyDamageString = null;
	    String enemyWhackString = "";
	    if (this.ede.weaponFumble()) {
		displayEnemyDamageString = "FUMBLE! The " + enemyName + " drops its weapon, doing "
			+ enemyFumbleDamageString + " damage to itself!";
		SoundManager.playSound(SoundConstants.SOUND_FUMBLE);
		enemyWhackString = "";
	    } else {
		if (this.damage == 0) {
		    displayEnemyDamageString = "The " + enemyName + " tries to hit you, but MISSES!";
		    SoundManager.playSound(SoundConstants.SOUND_MISSED);
		} else if (this.damage < 0) {
		    displayEnemyDamageString = "The " + enemyName + " tries to hit you, but you RIPOSTE for "
			    + -this.damage + " damage!";
		    SoundManager.playSound(SoundConstants.SOUND_COUNTER);
		} else {
		    displayEnemyDamageString = "The " + enemyName + " hits you for " + enemyDamageString + " damage!";
		    SoundManager.playSound(SoundConstants.SOUND_HIT);
		}
		if (this.ede.weaponCrit()) {
		    enemyWhackString += "CRITICAL HIT!\n";
		    SoundManager.playSound(SoundConstants.SOUND_CRITICAL);
		}
		if (this.ede.weaponPierce()) {
		    enemyWhackString += "The " + enemyName + "'s attack pierces YOUR armor!\n";
		}
	    }
	    final String displayString = enemyWhackString + displayEnemyDamageString;
	    this.setStatusMessage(displayString);
	}
    }

    // Methods
    @Override
    public void doBattle() {
	try {
	    final Application app = Import2.getApplication();
	    final GameLogicManager gm = app.getGameManager();
	    if (app.getMode() != Application.STATUS_BATTLE) {
		SoundManager.playSound(SoundConstants.SOUND_BATTLE);
	    }
	    app.setMode(Application.STATUS_BATTLE);
	    gm.hideOutput();
	    gm.stopMovement();
	    this.enemy = MonsterFactory.getNewMonsterInstance();
	    this.enemy.loadCreature();
	    if (PreferencesManager.getMusicEnabled(PreferencesManager.MUSIC_BATTLE)) {
		MusicManager.playMusic(MusicConstants.MUSIC_BATTLE);
	    }
	    this.enemyDidDamage = false;
	    this.playerDidDamage = false;
	    this.setResult(BattleResults.IN_PROGRESS);
	    this.battleGUI.initBattle(this.enemy.getImage());
	    this.firstUpdateMessageArea();
	} catch (final Throwable t) {
	    Import2.getErrorLogger().logError(t);
	}
    }

    @Override
    public void doBattleByProxy() {
	this.enemy = MonsterFactory.getNewMonsterInstance();
	this.enemy.loadCreature();
	final PartyMember playerCharacter = PartyManager.getParty().getLeader();
	final AbstractCreature m = this.enemy;
	playerCharacter.offsetExperience(m.getExperience());
	playerCharacter.offsetGold(m.getGold());
	// Level Up Check
	if (playerCharacter.checkLevelUp()) {
	    playerCharacter.levelUp();
	    Import2.getApplication().getGameManager().keepNextMessage();
	    Import2.getApplication().showMessage("You reached level " + playerCharacter.getLevel() + ".");
	}
    }

    @Override
    public final int getResult() {
	final PartyMember playerCharacter = PartyManager.getParty().getLeader();
	int currResult;
	if (this.result != BattleResults.IN_PROGRESS) {
	    return this.result;
	}
	if (this.enemy.isAlive() && !playerCharacter.isAlive()) {
	    if (!this.playerDidDamage) {
		currResult = BattleResults.ANNIHILATED;
	    } else {
		currResult = BattleResults.LOST;
	    }
	} else if (!this.enemy.isAlive() && playerCharacter.isAlive()) {
	    if (!this.enemyDidDamage) {
		currResult = BattleResults.PERFECT;
	    } else {
		currResult = BattleResults.WON;
	    }
	} else if (!this.enemy.isAlive() && !playerCharacter.isAlive()) {
	    currResult = BattleResults.DRAW;
	} else {
	    currResult = BattleResults.IN_PROGRESS;
	}
	return currResult;
    }

    @Override
    public final void maintainEffects(final boolean player) {
	if (player) {
	    final PartyMember playerCharacter = PartyManager.getParty().getLeader();
	    playerCharacter.useEffects();
	    playerCharacter.cullInactiveEffects();
	} else {
	    this.enemy.useEffects();
	    this.enemy.cullInactiveEffects();
	}
    }

    @Override
    public final void displayActiveEffects() {
	boolean flag1 = false, flag2 = false, flag3 = false;
	final PartyMember playerCharacter = PartyManager.getParty().getLeader();
	final String effectString = playerCharacter.getCompleteEffectString();
	final String effectMessages = playerCharacter.getAllCurrentEffectMessages();
	final String enemyEffectMessages = this.enemy.getAllCurrentEffectMessages();
	final String nMsg = Effect.getNullMessage();
	if (!effectString.equals(nMsg)) {
	    flag1 = true;
	}
	if (!effectMessages.equals(nMsg)) {
	    flag2 = true;
	}
	if (!enemyEffectMessages.equals(nMsg)) {
	    flag3 = true;
	}
	if (flag1) {
	    this.setStatusMessage(effectString);
	}
	if (flag2) {
	    this.setStatusMessage(effectMessages);
	}
	if (flag3) {
	    this.setStatusMessage(enemyEffectMessages);
	}
    }

    final void clearMessageArea() {
	this.battleGUI.clearMessageArea();
    }

    @Override
    public final void setStatusMessage(final String s) {
	this.battleGUI.setStatusMessage(s);
    }

    final void stripExtraNewLine() {
	this.battleGUI.stripExtraNewLine();
    }

    final void firstUpdateMessageArea() {
	this.clearMessageArea();
	this.setStatusMessage("*** Beginning of Round ***");
	this.displayBattleStats();
	this.setStatusMessage("*** Beginning of Round ***\n");
	// Determine initiative
	boolean enemyGotJump = false;
	if (this.enemy.getSpeed() > PartyManager.getParty().getLeader().getSpeed()) {
	    // Enemy acts first!
	    enemyGotJump = true;
	} else if (this.enemy.getSpeed() < PartyManager.getParty().getLeader().getSpeed()) {
	    // You act first!
	    enemyGotJump = false;
	} else {
	    // Equal, decide randomly
	    final RandomRange jump = new RandomRange(0, 1);
	    final int whoFirst = jump.generate();
	    if (whoFirst == 1) {
		// Enemy acts first!
		enemyGotJump = true;
	    } else {
		// You act first!
		enemyGotJump = false;
	    }
	}
	if (enemyGotJump) {
	    this.setStatusMessage("The enemy acts first!");
	    this.executeNextAIAction();
	    // Display Active Effects
	    this.displayActiveEffects();
	    // Maintain Effects
	    this.maintainEffects(true);
	    this.maintainEffects(false);
	    // Check result
	    this.setResult(this.getResult());
	    if (this.result != BattleResults.IN_PROGRESS) {
		this.doResult();
		return;
	    }
	} else {
	    this.setStatusMessage("You act first!");
	}
	this.setStatusMessage("\n*** End of Round ***");
	this.displayBattleStats();
	this.setStatusMessage("*** End of Round ***");
	this.stripExtraNewLine();
	this.battleGUI.getOutputFrame().pack();
    }

    final void updateMessageAreaEnemyFleeFailed() {
	this.setStatusMessage("The enemy tries to run away, but doesn't quite make it!");
    }

    final void updateMessageAreaPostSteal() {
	this.setStatusMessage("You try to steal money, and successfully steal " + this.stealAmount + " Gold!");
    }

    final void updateMessageAreaPostDrain() {
	this.setStatusMessage("You try to drain the enemy, and succeed!");
    }

    final void updateMessageAreaFleeFailed() {
	this.setStatusMessage("You try to run away, but don't quite make it!");
    }

    @Override
    public final boolean steal() {
	final PartyMember playerCharacter = PartyManager.getParty().getLeader();
	final int stealChance = StatConstants.CHANCE_STEAL;
	final RandomRange chance = new RandomRange(0, 100);
	final int randomChance = chance.generate();
	if (randomChance <= stealChance) {
	    // Succeeded
	    final RandomRange stole = new RandomRange(0, this.enemy.getGold());
	    this.stealAmount = stole.generate();
	    playerCharacter.offsetGold(this.stealAmount);
	    return true;
	} else {
	    // Failed
	    this.stealAmount = 0;
	    return false;
	}
    }

    @Override
    public final boolean drain() {
	final PartyMember playerCharacter = PartyManager.getParty().getLeader();
	final int drainChance = StatConstants.CHANCE_DRAIN;
	final RandomRange chance = new RandomRange(0, 100);
	final int randomChance = chance.generate();
	if (randomChance <= drainChance) {
	    // Succeeded
	    final RandomRange drained = new RandomRange(0, this.enemy.getCurrentMP());
	    final int drainAmount = drained.generate();
	    this.enemy.offsetCurrentMP(-drainAmount);
	    playerCharacter.offsetCurrentMP(drainAmount);
	    return true;
	} else {
	    // Failed
	    return false;
	}
    }

    final void updateMessageAreaStealFailed() {
	this.setStatusMessage("You try to steal money from the enemy, but the attempt fails!");
    }

    final void updateMessageAreaDrainFailed() {
	this.setStatusMessage("You try to drain the enemy's MP, but the attempt fails!");
    }

    @Override
    public void resetGUI() {
	// Destroy old GUI
	this.battleGUI.getOutputFrame().dispose();
	// Create new GUI
	this.battleGUI = new WindowTurnBattleGUI();
    }

    @Override
    public void doResult() {
	if (PreferencesManager.getMusicEnabled(PreferencesManager.MUSIC_BATTLE)) {
	    MusicManager.stopMusic();
	}
	final PartyMember playerCharacter = PartyManager.getParty().getLeader();
	final AbstractCreature m = this.enemy;
	boolean rewardsFlag = false;
	if (m instanceof BossMonster) {
	    if (this.result == BattleResults.WON || this.result == BattleResults.PERFECT) {
		this.setStatusMessage("You defeated the Boss!");
		SoundManager.playSound(SoundConstants.SOUND_VICTORY);
		rewardsFlag = true;
	    } else if (this.result == BattleResults.LOST) {
		this.setStatusMessage("The Boss defeated you...");
		SoundManager.playSound(SoundConstants.SOUND_GAME_OVER);
		PartyManager.getParty().getLeader().onDeath(-10);
	    } else if (this.result == BattleResults.ANNIHILATED) {
		this.setStatusMessage("The Boss defeated you without suffering damage... you were annihilated!");
		SoundManager.playSound(SoundConstants.SOUND_GAME_OVER);
		PartyManager.getParty().getLeader().onDeath(-20);
	    } else if (this.result == BattleResults.DRAW) {
		this.setStatusMessage("The Boss battle was a draw. You are fully healed!");
		playerCharacter.healPercentage(AbstractCreature.FULL_HEAL_PERCENTAGE);
		playerCharacter.regeneratePercentage(AbstractCreature.FULL_HEAL_PERCENTAGE);
	    } else if (this.result == BattleResults.FLED) {
		this.setStatusMessage("You ran away successfully!");
	    } else if (this.result == BattleResults.ENEMY_FLED) {
		this.setStatusMessage("The Boss ran away!");
	    }
	} else {
	    if (this.result == BattleResults.WON) {
		this.setStatusMessage("You gain " + m.getExperience() + " experience and " + m.getGold() + " Gold.");
		playerCharacter.offsetExperience(m.getExperience());
		playerCharacter.offsetGold(m.getGold());
		SoundManager.playSound(SoundConstants.SOUND_VICTORY);
	    } else if (this.result == BattleResults.PERFECT) {
		this.setStatusMessage("You gain " + m.getExperience() + " experience and " + m.getGold()
			+ " Gold,\nplus " + m.getPerfectBonusGold() + " extra gold for a perfect fight!");
		playerCharacter.offsetExperience(m.getExperience());
		playerCharacter.offsetGold(m.getGold() + m.getPerfectBonusGold());
		SoundManager.playSound(SoundConstants.SOUND_VICTORY);
	    } else if (this.result == BattleResults.LOST) {
		this.setStatusMessage("You lost...");
		SoundManager.playSound(SoundConstants.SOUND_GAME_OVER);
		PartyManager.getParty().getLeader().onDeath(-10);
	    } else if (this.result == BattleResults.ANNIHILATED) {
		this.setStatusMessage("You lost without hurting your foe... you were annihilated!");
		SoundManager.playSound(SoundConstants.SOUND_GAME_OVER);
		PartyManager.getParty().getLeader().onDeath(-20);
	    } else if (this.result == BattleResults.DRAW) {
		this.setStatusMessage("The battle was a draw. You are fully healed!");
		playerCharacter.healPercentage(AbstractCreature.FULL_HEAL_PERCENTAGE);
		playerCharacter.regeneratePercentage(AbstractCreature.FULL_HEAL_PERCENTAGE);
	    } else if (this.result == BattleResults.FLED) {
		this.setStatusMessage("You ran away successfully!");
	    } else if (this.result == BattleResults.ENEMY_FLED) {
		this.setStatusMessage("The enemy runs away!");
		this.setStatusMessage("Since the enemy ran away, you gain nothing for this battle.");
	    }
	}
	// Cleanup
	this.battleGUI.doResultCleanup();
	playerCharacter.stripAllEffects();
	this.enemy.stripAllEffects();
	// Level Up Check
	if (playerCharacter.checkLevelUp()) {
	    playerCharacter.levelUp();
	    if (PreferencesManager.getSoundsEnabled()) {
		SoundManager.playSound(SoundConstants.SOUND_LEVEL_UP);
	    }
	    this.setStatusMessage("You reached level " + playerCharacter.getLevel() + ".");
	}
	// Final Cleanup
	this.battleGUI.doResultFinalCleanup(rewardsFlag);
    }

    @Override
    public final void setResult(final int newResult) {
	this.result = newResult;
    }

    @Override
    public final void battleDone() {
	this.battleGUI.getOutputFrame().setVisible(false);
	final GameLogicManager gm = Import2.getApplication().getGameManager();
	gm.showOutput();
	gm.redrawMaze();
    }

    @Override
    public boolean getLastAIActionResult() {
	return true;
    }

    @Override
    public boolean castSpell() {
	final PartyMember playerCharacter = PartyManager.getParty().getLeader();
	return SpellCaster.selectAndCastSpell(playerCharacter);
    }

    @Override
    public boolean useItem() {
	final PartyMember playerCharacter = PartyManager.getParty().getLeader();
	return CombatItemChucker.selectAndUseItem(playerCharacter);
    }

    @Override
    public void endTurn() {
	// Do nothing
    }

    @Override
    public AbstractCreature getEnemy() {
	return this.enemy;
    }

    @Override
    public boolean updatePosition(final int x, final int y) {
	return false;
    }

    @Override
    public void fireArrow(final int x, final int y) {
	// Do nothing
    }

    @Override
    public void arrowDone(final BattleCharacter hit) {
	// Do nothing
    }

    @Override
    public void redrawOneBattleSquare(final int x, final int y, final AbstractMazeObject obj3) {
	// Do nothing
    }

    @Override
    public boolean isWaitingForAI() {
	return false;
    }
}
