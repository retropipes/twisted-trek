package studio.ignitionigloogames.twistedtrek.import1.objects;

import studio.ignitionigloogames.twistedtrek.import1.Fantastle5;
import studio.ignitionigloogames.twistedtrek.import1.generic.GenericDungeonObject;

public class WeaponsShop extends GenericDungeonObject {
    // Constructors
    public WeaponsShop() {
	super(false);
    }

    @Override
    public void postMoveActionHook() {
	Fantastle5.getApplication().getWeapons().showShop();
    }

    @Override
    public String getName() {
	return "Weapons Shop";
    }

    @Override
    public String getPluralName() {
	return "Weapons Shops";
    }

    @Override
    public String getDescription() {
	return "Weapons Shops sell weapons used to fight monsters.";
    }

    @Override
    public byte getObjectID() {
	return (byte) 5;
    }
}
