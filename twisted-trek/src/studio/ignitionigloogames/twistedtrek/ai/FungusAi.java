package studio.ignitionigloogames.twistedtrek.ai;

import studio.ignitionigloogames.twistedtrek.Creature;
import studio.ignitionigloogames.twistedtrek.StuffFactory;

public class FungusAi extends CreatureAi {
    private final StuffFactory factory;
    private int spreadcount;

    public FungusAi(final Creature newCreature, final StuffFactory newFactory) {
	super(newCreature);
	this.factory = newFactory;
    }

    @Override
    public void onUpdate() {
	if (this.spreadcount < 5 && Math.random() < 0.01) {
	    this.spread();
	}
    }

    private void spread() {
	final int x = this.creature.x + (int) (Math.random() * 11) - 5;
	final int y = this.creature.y + (int) (Math.random() * 11) - 5;
	if (!this.creature.canEnter(x, y, this.creature.z)) {
	    return;
	}
	this.creature.doAction("spawn a child");
	final Creature child = this.factory.newFungus(this.creature.z);
	child.x = x;
	child.y = y;
	child.z = this.creature.z;
	this.spreadcount++;
    }
}
