/* Import2: An RPG */
package studio.ignitionigloogames.twistedtrek.import2.items;

import studio.ignitionigloogames.twistedtrek.import2.creatures.castes.CasteConstants;
import studio.ignitionigloogames.twistedtrek.import2.names.NamesConstants;
import studio.ignitionigloogames.twistedtrek.import2.names.NamesManager;

public class WeaponConstants {
    // Constants
    private static String[] WEAPON_1H = null;
    private static String[] WEAPON_2H = null;
    private static final String[] WEAPON_CHOICES = { "One-Handed Weapons", "Two-Handed Weapons" };
    private static String[] HAND_CHOICES = null;

    // Private Constructor
    private WeaponConstants() {
	// Do nothing
    }

    // Methods
    public static String[] getWeaponChoices() {
	return WeaponConstants.WEAPON_CHOICES;
    }

    public static synchronized String[] getHandChoices() {
	if (WeaponConstants.HAND_CHOICES == null) {
	    final String[] temp = EquipmentSlotConstants.getSlotNames();
	    final String[] temp2 = new String[2];
	    temp2[0] = temp[EquipmentSlotConstants.SLOT_MAINHAND];
	    temp2[1] = temp[EquipmentSlotConstants.SLOT_OFFHAND];
	    WeaponConstants.HAND_CHOICES = temp2;
	}
	return WeaponConstants.HAND_CHOICES;
    }

    public static synchronized String[] get1HWeapons() {
	if (WeaponConstants.WEAPON_1H == null) {
	    final String[] temp = new String[CasteConstants.CASTES_COUNT];
	    for (int x = 0; x < temp.length; x++) {
		temp[x] = NamesManager.getName(NamesConstants.SECTION_EQUIP_WEAPONS_1H,
			NamesConstants.SECTION_ARRAY_WEAPONS_1H[x]);
	    }
	    WeaponConstants.WEAPON_1H = temp;
	}
	return WeaponConstants.WEAPON_1H;
    }

    public static synchronized String[] get2HWeapons() {
	if (WeaponConstants.WEAPON_2H == null) {
	    final String[] temp = new String[CasteConstants.CASTES_COUNT];
	    for (int x = 0; x < temp.length; x++) {
		temp[x] = NamesManager.getName(NamesConstants.SECTION_EQUIP_WEAPONS_2H,
			NamesConstants.SECTION_ARRAY_WEAPONS_2H[x]);
	    }
	    WeaponConstants.WEAPON_2H = temp;
	}
	return WeaponConstants.WEAPON_2H;
    }
}
