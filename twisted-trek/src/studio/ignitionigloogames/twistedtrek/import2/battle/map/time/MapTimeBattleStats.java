/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell


Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package studio.ignitionigloogames.twistedtrek.import2.battle.map.time;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import studio.ignitionigloogames.images.BufferedImageIcon;
import studio.ignitionigloogames.twistedtrek.import2.maze.objects.BattleCharacter;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.StatImageConstants;
import studio.ignitionigloogames.twistedtrek.import2.resourcemanagers.StatImageManager;

public class MapTimeBattleStats {
    // Fields
    private Container statsPane;
    private JLabel nameLabel;
    private JLabel teamLabel;
    private JLabel hpLabel;
    private JLabel mpLabel;
    private JLabel attLabel;
    private JLabel defLabel;

    // Constructors
    public MapTimeBattleStats() {
	this.setUpGUI();
	this.updateIcons();
    }

    // Methods
    public Container getStatsPane() {
	return this.statsPane;
    }

    public void updateStats(final BattleCharacter bc) {
	this.nameLabel.setText(bc.getName());
	this.teamLabel.setText(bc.getTeamString());
	this.hpLabel.setText(bc.getTemplate().getHPString());
	this.mpLabel.setText(bc.getTemplate().getMPString());
	this.attLabel.setText(Integer.toString(bc.getTemplate().getAttack()));
	this.defLabel.setText(Integer.toString(bc.getTemplate().getDefense()));
    }

    private void setUpGUI() {
	this.statsPane = new Container();
	this.statsPane.setLayout(new GridLayout(6, 1));
	this.nameLabel = new JLabel("", null, SwingConstants.LEFT);
	this.teamLabel = new JLabel("", null, SwingConstants.LEFT);
	this.hpLabel = new JLabel("", null, SwingConstants.LEFT);
	this.mpLabel = new JLabel("", null, SwingConstants.LEFT);
	this.attLabel = new JLabel("", null, SwingConstants.LEFT);
	this.defLabel = new JLabel("", null, SwingConstants.LEFT);
	this.statsPane.add(this.nameLabel);
	this.statsPane.add(this.teamLabel);
	this.statsPane.add(this.hpLabel);
	this.statsPane.add(this.mpLabel);
	this.statsPane.add(this.attLabel);
	this.statsPane.add(this.defLabel);
    }

    private void updateIcons() {
	final BufferedImageIcon nameImage = StatImageManager.getImage(StatImageConstants.STAT_IMAGE_NAME);
	this.nameLabel.setIcon(nameImage);
	final BufferedImageIcon teamImage = StatImageManager.getImage(StatImageConstants.STAT_IMAGE_TEAM);
	this.teamLabel.setIcon(teamImage);
	final BufferedImageIcon hpImage = StatImageManager.getImage(StatImageConstants.STAT_IMAGE_HEALTH);
	this.hpLabel.setIcon(hpImage);
	final BufferedImageIcon mpImage = StatImageManager.getImage(StatImageConstants.STAT_IMAGE_MAGIC);
	this.mpLabel.setIcon(mpImage);
	final BufferedImageIcon attImage = StatImageManager.getImage(StatImageConstants.STAT_IMAGE_ATTACK);
	this.attLabel.setIcon(attImage);
	final BufferedImageIcon defImage = StatImageManager.getImage(StatImageConstants.STAT_IMAGE_DEFENSE);
	this.defLabel.setIcon(defImage);
    }
}
