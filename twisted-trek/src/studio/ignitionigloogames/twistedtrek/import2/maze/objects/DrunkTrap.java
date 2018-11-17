/*  TallerTower: An RPG
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: TallerTower@worldwizard.net
 */
package studio.ignitionigloogames.twistedtrek.import2.maze.objects;

import studio.ignitionigloogames.twistedtrek.import2.TallerTower;
import studio.ignitionigloogames.twistedtrek.import2.maze.abc.AbstractTrap;
import studio.ignitionigloogames.twistedtrek.import2.maze.effects.MazeEffectConstants;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.ObjectImageConstants;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.SoundConstants;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.SoundManager;

public class DrunkTrap extends AbstractTrap {
    // Constructors
    public DrunkTrap() {
	super(ObjectImageConstants.OBJECT_IMAGE_DRUNK_TRAP);
    }

    @Override
    public String getName() {
	return "Drunk Trap";
    }

    @Override
    public String getPluralName() {
	return "Drunk Traps";
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY) {
	TallerTower.getApplication().showMessage("You stumble around drunkenly!");
	TallerTower.getApplication().getGameManager().activateEffect(MazeEffectConstants.EFFECT_DRUNK);
	SoundManager.playSound(SoundConstants.SOUND_DRUNK);
    }

    @Override
    public String getDescription() {
	return "Drunk Traps alter your movement in a way that resembles being intoxicated for 9 steps when stepped on.";
    }
}