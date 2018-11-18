/* Import2: An RPG */
package studio.ignitionigloogames.twistedtrek.import2.battle.map;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JLabel;

import studio.ignitionigloogames.twistedtrek.import2.maze.objects.BattleCharacter;

public class MapBattleEffects {
    // Fields
    private Container effectsPane;
    private JLabel[] effectLabels;

    // Constructors
    public MapBattleEffects() {
	// Do nothing
    }

    // Methods
    public Container getEffectsPane() {
	if (this.effectsPane == null) {
	    this.effectsPane = new Container();
	}
	return this.effectsPane;
    }

    public void updateEffects(final BattleCharacter bc) {
	final int count = bc.getTemplate().getActiveEffectCount();
	if (count > 0) {
	    this.setUpGUI(count);
	    final String[] es = bc.getTemplate().getCompleteEffectStringArray();
	    for (int x = 0; x < count; x++) {
		this.effectLabels[x].setText(es[x]);
	    }
	}
    }

    private void setUpGUI(final int count) {
	this.effectsPane = this.getEffectsPane();
	this.effectsPane.removeAll();
	this.effectsPane.setLayout(new GridLayout(count, 1));
	this.effectLabels = new JLabel[count];
	for (int x = 0; x < count; x++) {
	    this.effectLabels[x] = new JLabel(" ");
	}
    }
}
