/* Import1: A Maze-Solving Game */
package studio.ignitionigloogames.twistedtrek.import1.effects;

import studio.ignitionigloogames.twistedtrek.import1.Import1;
import studio.ignitionigloogames.twistedtrek.import1.Messager;
import studio.ignitionigloogames.twistedtrek.import1.generic.MazeObject;

public class EffectManager implements EffectConstants {
    // Fields
    private final Effect[] activeEffects;
    private static final int NUM_EFFECTS = 6;

    // Constructors
    public EffectManager() {
	this.activeEffects = new Effect[EffectManager.NUM_EFFECTS];
	this.activeEffects[EffectConstants.EFFECT_ROTATED_CLOCKWISE] = new RotatedCW(0);
	this.activeEffects[EffectConstants.EFFECT_ROTATED_COUNTERCLOCKWISE] = new RotatedCCW(0);
	this.activeEffects[EffectConstants.EFFECT_U_TURNED] = new UTurned(0);
	this.activeEffects[EffectConstants.EFFECT_CONFUSED] = new Confused(0);
	this.activeEffects[EffectConstants.EFFECT_DIZZY] = new Dizzy(0);
	this.activeEffects[EffectConstants.EFFECT_DRUNK] = new Drunk(0);
    }

    // Methods
    public void decayEffects() {
	for (int x = 0; x < EffectManager.NUM_EFFECTS; x++) {
	    if (this.activeEffects[x].isActive()) {
		this.activeEffects[x].useEffect();
		if (!this.activeEffects[x].isActive()) {
		    Import1.getApplication().getGameManager().keepNextMessage();
		    Messager.showMessage("You feel normal again.");
		}
	    }
	}
    }

    public void activateEffect(final int effectID, final int duration) {
	this.activeEffects[effectID].extendEffect(duration);
	this.handleMutualExclusiveEffects(effectID);
    }

    private void deactivateEffect(final int effectID) {
	this.activeEffects[effectID].deactivateEffect();
    }

    public void deactivateAllEffects() {
	for (int x = 0; x < EffectManager.NUM_EFFECTS; x++) {
	    this.activeEffects[x].deactivateEffect();
	}
    }

    private void handleMutualExclusiveEffects(final int effectID) {
	if (effectID == EffectConstants.EFFECT_ROTATED_CLOCKWISE) {
	    this.deactivateEffect(EffectConstants.EFFECT_ROTATED_COUNTERCLOCKWISE);
	    this.deactivateEffect(EffectConstants.EFFECT_U_TURNED);
	} else if (effectID == EffectConstants.EFFECT_ROTATED_COUNTERCLOCKWISE) {
	    this.deactivateEffect(EffectConstants.EFFECT_ROTATED_CLOCKWISE);
	    this.deactivateEffect(EffectConstants.EFFECT_U_TURNED);
	} else if (effectID == EffectConstants.EFFECT_U_TURNED) {
	    this.deactivateEffect(EffectConstants.EFFECT_ROTATED_CLOCKWISE);
	    this.deactivateEffect(EffectConstants.EFFECT_ROTATED_COUNTERCLOCKWISE);
	} else if (effectID == EffectConstants.EFFECT_CONFUSED) {
	    this.deactivateEffect(EffectConstants.EFFECT_DIZZY);
	    this.deactivateEffect(EffectConstants.EFFECT_DRUNK);
	} else if (effectID == EffectConstants.EFFECT_DIZZY) {
	    this.deactivateEffect(EffectConstants.EFFECT_CONFUSED);
	    this.deactivateEffect(EffectConstants.EFFECT_DRUNK);
	} else if (effectID == EffectConstants.EFFECT_DRUNK) {
	    this.deactivateEffect(EffectConstants.EFFECT_CONFUSED);
	    this.deactivateEffect(EffectConstants.EFFECT_DIZZY);
	}
    }

    public int[] doEffects(final int x, final int y) {
	int[] res = new int[] { x, y };
	int dir = MazeObject.resolveRelativeDirection(x, y);
	for (int z = 0; z < EffectManager.NUM_EFFECTS; z++) {
	    if (this.activeEffects[z].isActive()) {
		dir = this.activeEffects[z].modifyMove1(dir);
		res = MazeObject.unresolveRelativeDirection(dir);
		res = this.activeEffects[z].modifyMove2(res);
	    }
	}
	return res;
    }
}