package studio.ignitionigloogames.twistedtrek.import1.creatures.genders;

import studio.ignitionigloogames.twistedtrek.import1.Messager;

public class GenderManager implements GenderConstants {
    private static boolean CACHE_CREATED = false;
    private static Gender[] CACHE;

    public static Gender selectGender() {
	final String[] names = GenderConstants.GENDER_NAMES;
	String dialogResult = null;
	dialogResult = Messager.showInputDialog("Select a Gender", "Select Gender", names, names[0]);
	if (dialogResult != null) {
	    int index;
	    for (index = 0; index < names.length; index++) {
		if (dialogResult.equals(names[index])) {
		    break;
		}
	    }
	    return GenderManager.getGender(index);
	} else {
	    return null;
	}
    }

    public static Gender getGender(final int genderID) {
	if (!GenderManager.CACHE_CREATED) {
	    // Create cache
	    GenderManager.CACHE = new Gender[GenderConstants.GENDERS_COUNT];
	    for (int x = 0; x < GenderConstants.GENDERS_COUNT; x++) {
		GenderManager.CACHE[x] = new Gender(x);
	    }
	    GenderManager.CACHE_CREATED = true;
	}
	return GenderManager.CACHE[genderID];
    }
}
