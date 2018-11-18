package studio.ignitionigloogames.twistedtrek.import1.creatures.faiths;

import java.awt.Color;

import studio.ignitionigloogames.twistedtrek.import1.resourcemanagers.datamanagers.FaithDataManager;

public final class Faith {
    private final int faithID;
    private final double[] multipliers;

    Faith(final int fid) {
	this.multipliers = FaithDataManager.getFaithData(fid);
	this.faithID = fid;
    }

    public double getMultiplierForOtherFaith(final int fid) {
	return this.multipliers[fid];
    }

    public int getFaithID() {
	return this.faithID;
    }

    public String getName() {
	return FaithConstants.FAITH_NAMES[this.faithID];
    }

    public String getDamageType() {
	return FaithConstants.FAITH_DAMAGE_TYPES[this.faithID];
    }

    public Color getColor() {
	return FaithConstants.FAITH_COLORS[this.faithID];
    }
}
