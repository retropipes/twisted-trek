/* Import1: A Maze-Solving Game */
package studio.ignitionigloogames.twistedtrek.import1.generic;

public abstract class GenericTrappedWall extends GenericWall {
    // Fields
    private int number;
    protected static final int NUMBER_MASTER = -1;

    // Constructors
    protected GenericTrappedWall(final int newNumber) {
	super();
	this.number = newNumber;
    }

    public int getNumber() {
	return this.number;
    }

    @Override
    public GenericTrappedWall clone() {
	final GenericTrappedWall copy = (GenericTrappedWall) super.clone();
	copy.number = this.number;
	return copy;
    }

    @Override
    public String getName() {
	if (this.number == GenericTrappedWall.NUMBER_MASTER) {
	    return "Master Trapped Wall";
	} else {
	    return "Trapped Wall " + this.number;
	}
    }

    @Override
    public String getGameName() {
	return "Wall";
    }

    @Override
    public String getPluralName() {
	if (this.number == GenericTrappedWall.NUMBER_MASTER) {
	    return "Master Trapped Walls";
	} else {
	    return "Trapped Walls " + this.number;
	}
    }

    @Override
    public byte getGroupID() {
	return (byte) 28;
    }

    @Override
    public byte getObjectID() {
	return (byte) this.number;
    }

    @Override
    protected void setTypes() {
	this.type.set(TypeConstants.TYPE_TRAPPED_WALL);
	this.type.set(TypeConstants.TYPE_WALL);
    }
}