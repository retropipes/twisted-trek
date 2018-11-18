/*  Import2: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package studio.ignitionigloogames.twistedtrek.import2.creatures.races;

import studio.ignitionigloogames.twistedtrek.import2.descriptionmanagers.RaceDescriptionManager;

public class Race {
    private final int[] data;
    private final int raceID;
    private final String desc;

    Race(final int rid, final int... rdata) {
	if (rdata == null) {
	    throw new IllegalArgumentException("Exactly " + RaceConstants.RACE_ATTRIBUTE_COUNT
		    + " attributes must be specified; got 0 attributes!");
	}
	if (rdata.length != RaceConstants.RACE_ATTRIBUTE_COUNT) {
	    throw new IllegalArgumentException("Exactly " + RaceConstants.RACE_ATTRIBUTE_COUNT
		    + " attributes must be specified; got " + rdata.length + " attributes!");
	}
	this.raceID = rid;
	this.data = rdata;
	this.desc = RaceDescriptionManager.getRaceDescription(rid);
    }

    public int getAttribute(final int aid) {
	return this.data[aid];
    }

    public String getDescription() {
	return this.desc;
    }

    public int getRaceID() {
	return this.raceID;
    }
}
