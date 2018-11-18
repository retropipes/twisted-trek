/* Import2: An RPG */
package studio.ignitionigloogames.twistedtrek.import2.items;

import studio.ignitionigloogames.commondialogs.CommonDialogs;
import studio.ignitionigloogames.twistedtrek.import2.creatures.AbstractCreature;

public final class Socks extends Equipment {
    // Fields
    private final int actionType;
    private final int actionAmount;
    private static final int STEP_ACTION_HEAL = 1;
    private static final int STEP_ACTION_REGENERATE = 2;
    private static final int STEP_ACTION_XP = 3;
    private static final int STEP_ACTION_MONEY = 4;

    // Constructors
    Socks(final String itemName, final int price, final int action, final int amount) {
	super(itemName, price);
	this.actionType = action;
	this.actionAmount = amount;
    }

    // Methods
    void stepAction(final AbstractCreature wearer) {
	switch (this.actionType) {
	case Socks.STEP_ACTION_HEAL:
	    wearer.heal(this.actionAmount);
	    break;
	case Socks.STEP_ACTION_REGENERATE:
	    wearer.regenerate(this.actionAmount);
	    break;
	case Socks.STEP_ACTION_XP:
	    wearer.offsetExperience(this.actionAmount);
	    if (wearer.checkLevelUp()) {
		wearer.levelUp();
		CommonDialogs.showDialog(wearer.getName() + " reached level " + wearer.getLevel() + ".");
	    }
	    break;
	case Socks.STEP_ACTION_MONEY:
	    wearer.offsetGold(this.actionAmount);
	    break;
	default:
	    break;
	}
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + this.actionAmount;
	return prime * result + this.actionType;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!super.equals(obj)) {
	    return false;
	}
	if (!(obj instanceof Socks)) {
	    return false;
	}
	final Socks other = (Socks) obj;
	if (this.actionAmount != other.actionAmount) {
	    return false;
	}
	if (this.actionType != other.actionType) {
	    return false;
	}
	return true;
    }
}
