/*  Fantastle: A Maze-Solving Game
Copyright (C) 2008-2010 Eric Ahnell

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Any questions should be directed to the author via email at: fantastle@worldwizard.net
 */
package studio.ignitionigloogames.twistedtrek.import1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import studio.ignitionigloogames.twistedtrek.import1.editor.MazeEditor;
import studio.ignitionigloogames.twistedtrek.import1.resourcemanagers.ImageCache;
import studio.ignitionigloogames.twistedtrek.import1.resourcemanagers.MonsterImageCache;

public class MenuManager {
    // Fields
    private JMenuBar mainMenuBar;
    private JMenu fileMenu, editMenu, playMenu, gameMenu, debugMenu, helpMenu;
    private JMenuItem fileNew, fileOpen, fileClose, fileSave, fileSaveAs, fileExit;
    private JMenuItem editUndo, editRedo, editCutLevel, editCopyLevel, editPasteLevel, editInsertLevelFromClipboard,
	    editPreferences, editClearHistory, editGoTo, editUpOneFloor, editDownOneFloor, editUpOneLevel,
	    editDownOneLevel, editAddLevel, editRemoveLevel, editResizeLevel, editToggleLayer, editMazePreferences;
    private JMenuItem playPlay, playEdit;
    private JMenuItem gameEquipment, gameInventory, gameUse, gameReset, gameShowScore, gameShowTable;
    private JMenuItem debugViewImageCache, debugViewMonsterCache, debugResetPreferences;
    private JMenuItem helpAbout, helpGeneralHelp, helpObjectHelp;
    private KeyStroke fileNewAccel, fileOpenAccel, fileCloseAccel, fileSaveAccel, fileSaveAsAccel;
    private KeyStroke editUndoAccel, editRedoAccel, editCutLevelAccel, editCopyLevelAccel, editPasteLevelAccel,
	    editInsertLevelFromClipboardAccel, editPreferencesAccel, editClearHistoryAccel, editGoToAccel,
	    editUpOneFloorAccel, editDownOneFloorAccel, editUpOneLevelAccel, editDownOneLevelAccel,
	    editToggleLayerAccel;
    private KeyStroke playPlayMazeAccel, playEditMazeAccel;
    private KeyStroke gameInventoryAccel, gameUseAccel, gameResetAccel, gameShowScoreAccel, gameShowTableAccel;
    private final EventHandler handler;

    // Constructors
    public MenuManager() {
	this.handler = new EventHandler();
	this.createAccelerators();
	this.createMenus();
	this.setInitialMenuState();
    }

    // Methods
    public JMenuBar getMainMenuBar() {
	return this.mainMenuBar;
    }

    public void setGameMenus() {
	this.fileNew.setEnabled(false);
	this.fileOpen.setEnabled(false);
	this.fileExit.setEnabled(true);
	this.editUndo.setEnabled(false);
	this.editRedo.setEnabled(false);
	this.editCutLevel.setEnabled(false);
	this.editCopyLevel.setEnabled(false);
	this.editPasteLevel.setEnabled(false);
	this.editInsertLevelFromClipboard.setEnabled(false);
	this.editPreferences.setEnabled(true);
	this.editClearHistory.setEnabled(false);
	this.editGoTo.setEnabled(false);
	this.editUpOneFloor.setEnabled(false);
	this.editDownOneFloor.setEnabled(false);
	this.editUpOneLevel.setEnabled(false);
	this.editDownOneLevel.setEnabled(false);
	this.editAddLevel.setEnabled(false);
	this.editRemoveLevel.setEnabled(false);
	this.editResizeLevel.setEnabled(false);
	this.editToggleLayer.setEnabled(false);
	this.editMazePreferences.setEnabled(false);
	this.gameEquipment.setEnabled(true);
	this.gameInventory.setEnabled(true);
	this.gameUse.setEnabled(true);
	this.gameReset.setEnabled(true);
	this.gameShowScore.setEnabled(true);
	this.gameShowTable.setEnabled(true);
	this.checkFlags();
    }

    public void setEditorMenus() {
	this.fileNew.setEnabled(false);
	this.fileOpen.setEnabled(false);
	this.fileExit.setEnabled(true);
	this.editCutLevel.setEnabled(true);
	this.editCopyLevel.setEnabled(true);
	this.editPasteLevel.setEnabled(true);
	this.editInsertLevelFromClipboard.setEnabled(true);
	this.editPreferences.setEnabled(true);
	this.editGoTo.setEnabled(true);
	this.editResizeLevel.setEnabled(true);
	this.editToggleLayer.setEnabled(true);
	this.editMazePreferences.setEnabled(true);
	this.gameEquipment.setEnabled(false);
	this.gameInventory.setEnabled(false);
	this.gameUse.setEnabled(false);
	this.gameReset.setEnabled(false);
	this.gameShowScore.setEnabled(false);
	this.gameShowTable.setEnabled(false);
	this.checkFlags();
    }

    public void setPrefMenus() {
	this.fileNew.setEnabled(false);
	this.fileOpen.setEnabled(false);
	this.fileClose.setEnabled(false);
	this.fileSave.setEnabled(false);
	this.fileSaveAs.setEnabled(false);
	this.fileExit.setEnabled(true);
	this.editUndo.setEnabled(false);
	this.editRedo.setEnabled(false);
	this.editCutLevel.setEnabled(false);
	this.editCopyLevel.setEnabled(false);
	this.editPasteLevel.setEnabled(false);
	this.editInsertLevelFromClipboard.setEnabled(false);
	this.editPreferences.setEnabled(false);
	this.editClearHistory.setEnabled(false);
	this.editGoTo.setEnabled(false);
	this.editUpOneFloor.setEnabled(false);
	this.editDownOneFloor.setEnabled(false);
	this.editUpOneLevel.setEnabled(false);
	this.editDownOneLevel.setEnabled(false);
	this.editAddLevel.setEnabled(false);
	this.editRemoveLevel.setEnabled(false);
	this.editResizeLevel.setEnabled(false);
	this.editToggleLayer.setEnabled(false);
	this.editMazePreferences.setEnabled(false);
	this.gameEquipment.setEnabled(false);
	this.gameInventory.setEnabled(false);
	this.gameUse.setEnabled(false);
	this.gameReset.setEnabled(false);
	this.gameShowScore.setEnabled(false);
	this.gameShowTable.setEnabled(false);
    }

    public void setMainMenus() {
	this.fileNew.setEnabled(true);
	this.fileOpen.setEnabled(true);
	this.fileExit.setEnabled(true);
	this.editUndo.setEnabled(false);
	this.editRedo.setEnabled(false);
	this.editCutLevel.setEnabled(false);
	this.editCopyLevel.setEnabled(false);
	this.editPasteLevel.setEnabled(false);
	this.editInsertLevelFromClipboard.setEnabled(false);
	this.editPreferences.setEnabled(true);
	this.editClearHistory.setEnabled(false);
	this.editGoTo.setEnabled(false);
	this.editUpOneFloor.setEnabled(false);
	this.editDownOneFloor.setEnabled(false);
	this.editUpOneLevel.setEnabled(false);
	this.editDownOneLevel.setEnabled(false);
	this.editAddLevel.setEnabled(false);
	this.editRemoveLevel.setEnabled(false);
	this.editResizeLevel.setEnabled(false);
	this.editToggleLayer.setEnabled(false);
	this.editMazePreferences.setEnabled(false);
	this.gameEquipment.setEnabled(false);
	this.gameInventory.setEnabled(false);
	this.gameUse.setEnabled(false);
	this.gameReset.setEnabled(false);
	this.gameShowScore.setEnabled(false);
	this.gameShowTable.setEnabled(false);
	this.checkFlags();
    }

    public void enableUpOneFloor() {
	this.editUpOneFloor.setEnabled(true);
    }

    public void disableUpOneFloor() {
	this.editUpOneFloor.setEnabled(false);
    }

    public void enableDownOneFloor() {
	this.editDownOneFloor.setEnabled(true);
    }

    public void disableDownOneFloor() {
	this.editDownOneFloor.setEnabled(false);
    }

    public void enableUpOneLevel() {
	this.editUpOneLevel.setEnabled(true);
    }

    public void disableUpOneLevel() {
	this.editUpOneLevel.setEnabled(false);
    }

    public void enableDownOneLevel() {
	this.editDownOneLevel.setEnabled(true);
    }

    public void disableDownOneLevel() {
	this.editDownOneLevel.setEnabled(false);
    }

    public void enableAddLevel() {
	this.editAddLevel.setEnabled(true);
    }

    public void disableAddLevel() {
	this.editAddLevel.setEnabled(false);
    }

    public void enableRemoveLevel() {
	this.editRemoveLevel.setEnabled(true);
    }

    public void disableRemoveLevel() {
	this.editRemoveLevel.setEnabled(false);
    }

    public void enableUndo() {
	this.editUndo.setEnabled(true);
    }

    public void disableUndo() {
	this.editUndo.setEnabled(false);
    }

    public void enableRedo() {
	this.editRedo.setEnabled(true);
    }

    public void disableRedo() {
	this.editRedo.setEnabled(false);
    }

    public void enableClearHistory() {
	this.editClearHistory.setEnabled(true);
    }

    public void disableClearHistory() {
	this.editClearHistory.setEnabled(false);
    }

    public void enableCutLevel() {
	this.editCutLevel.setEnabled(true);
    }

    public void disableCutLevel() {
	this.editCutLevel.setEnabled(false);
    }

    public void enablePasteLevel() {
	this.editPasteLevel.setEnabled(true);
    }

    public void disablePasteLevel() {
	this.editPasteLevel.setEnabled(false);
    }

    public void enableInsertLevelFromClipboard() {
	this.editInsertLevelFromClipboard.setEnabled(true);
    }

    public void disableInsertLevelFromClipboard() {
	this.editInsertLevelFromClipboard.setEnabled(false);
    }

    public void checkFlags() {
	final Application app = Fantastle5.getApplication();
	if (app.getMazeManager().getDirty()) {
	    this.setMenusDirtyOn();
	} else {
	    this.setMenusDirtyOff();
	}
	if (app.getMazeManager().getLoaded()) {
	    this.setMenusLoadedOn();
	} else {
	    this.setMenusLoadedOff();
	}
	if (app.getMode() == Application.STATUS_EDITOR) {
	    if (app.getMazeManager().getMaze().isPasteBlocked()) {
		this.disablePasteLevel();
		this.disableInsertLevelFromClipboard();
	    } else {
		this.enablePasteLevel();
		this.enableInsertLevelFromClipboard();
	    }
	    if (app.getMazeManager().getMaze().isCutBlocked()) {
		this.disableCutLevel();
	    } else {
		this.enableCutLevel();
	    }
	}
    }

    private void setMenusDirtyOn() {
	this.fileSave.setEnabled(true);
    }

    private void setMenusDirtyOff() {
	this.fileSave.setEnabled(false);
    }

    private void setMenusLoadedOn() {
	final Application app = Fantastle5.getApplication();
	if (app.getMode() == Application.STATUS_GUI) {
	    this.fileClose.setEnabled(false);
	    this.fileSaveAs.setEnabled(false);
	    if (app.getMazeManager().getMaze().findPlayerOnLevel(0)) {
		this.playPlay.setEnabled(true);
	    } else {
		this.playPlay.setEnabled(false);
	    }
	    this.playEdit.setEnabled(true);
	} else {
	    this.fileClose.setEnabled(true);
	    this.fileSaveAs.setEnabled(true);
	    this.playPlay.setEnabled(false);
	    this.playEdit.setEnabled(false);
	}
    }

    private void setMenusLoadedOff() {
	this.fileClose.setEnabled(false);
	this.fileSaveAs.setEnabled(false);
	this.playPlay.setEnabled(false);
	this.playEdit.setEnabled(false);
    }

    private void createAccelerators() {
	int modKey;
	if (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
	    modKey = InputEvent.META_DOWN_MASK;
	} else {
	    modKey = InputEvent.CTRL_DOWN_MASK;
	}
	this.fileNewAccel = KeyStroke.getKeyStroke(KeyEvent.VK_N, modKey);
	this.fileOpenAccel = KeyStroke.getKeyStroke(KeyEvent.VK_O, modKey);
	this.fileCloseAccel = KeyStroke.getKeyStroke(KeyEvent.VK_W, modKey);
	this.fileSaveAccel = KeyStroke.getKeyStroke(KeyEvent.VK_S, modKey);
	this.fileSaveAsAccel = KeyStroke.getKeyStroke(KeyEvent.VK_S, modKey | InputEvent.SHIFT_DOWN_MASK);
	this.editUndoAccel = KeyStroke.getKeyStroke(KeyEvent.VK_Z, modKey);
	this.editRedoAccel = KeyStroke.getKeyStroke(KeyEvent.VK_Z, modKey | InputEvent.SHIFT_DOWN_MASK);
	this.editCutLevelAccel = KeyStroke.getKeyStroke(KeyEvent.VK_X, modKey);
	this.editCopyLevelAccel = KeyStroke.getKeyStroke(KeyEvent.VK_C, modKey);
	this.editPasteLevelAccel = KeyStroke.getKeyStroke(KeyEvent.VK_V, modKey);
	this.editInsertLevelFromClipboardAccel = KeyStroke.getKeyStroke(KeyEvent.VK_F, modKey);
	this.editPreferencesAccel = KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, modKey);
	this.editClearHistoryAccel = KeyStroke.getKeyStroke(KeyEvent.VK_Y, modKey);
	this.editGoToAccel = KeyStroke.getKeyStroke(KeyEvent.VK_G, modKey | InputEvent.SHIFT_DOWN_MASK);
	this.playPlayMazeAccel = KeyStroke.getKeyStroke(KeyEvent.VK_P, modKey);
	this.playEditMazeAccel = KeyStroke.getKeyStroke(KeyEvent.VK_E, modKey);
	this.gameInventoryAccel = KeyStroke.getKeyStroke(KeyEvent.VK_I, modKey);
	this.gameUseAccel = KeyStroke.getKeyStroke(KeyEvent.VK_U, modKey);
	this.gameResetAccel = KeyStroke.getKeyStroke(KeyEvent.VK_R, modKey);
	this.gameShowScoreAccel = KeyStroke.getKeyStroke(KeyEvent.VK_G, modKey);
	this.gameShowTableAccel = KeyStroke.getKeyStroke(KeyEvent.VK_T, modKey);
	this.editUpOneFloorAccel = KeyStroke.getKeyStroke(KeyEvent.VK_UP, modKey);
	this.editDownOneFloorAccel = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, modKey);
	this.editUpOneLevelAccel = KeyStroke.getKeyStroke(KeyEvent.VK_UP, modKey | InputEvent.SHIFT_DOWN_MASK);
	this.editDownOneLevelAccel = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, modKey | InputEvent.SHIFT_DOWN_MASK);
	this.editToggleLayerAccel = KeyStroke.getKeyStroke(KeyEvent.VK_L, modKey);
    }

    private void createMenus() {
	this.mainMenuBar = new JMenuBar();
	this.fileMenu = new JMenu("File");
	this.editMenu = new JMenu("Edit");
	this.playMenu = new JMenu("Play");
	this.gameMenu = new JMenu("Game");
	this.debugMenu = new JMenu("Debug");
	this.helpMenu = new JMenu("Help");
	this.fileNew = new JMenuItem("New...");
	this.fileNew.setAccelerator(this.fileNewAccel);
	this.fileOpen = new JMenuItem("Open...");
	this.fileOpen.setAccelerator(this.fileOpenAccel);
	this.fileClose = new JMenuItem("Close");
	this.fileClose.setAccelerator(this.fileCloseAccel);
	this.fileSave = new JMenuItem("Save");
	this.fileSave.setAccelerator(this.fileSaveAccel);
	this.fileSaveAs = new JMenuItem("Save As...");
	this.fileSaveAs.setAccelerator(this.fileSaveAsAccel);
	this.fileExit = new JMenuItem("Exit");
	this.editUndo = new JMenuItem("Undo");
	this.editUndo.setAccelerator(this.editUndoAccel);
	this.editRedo = new JMenuItem("Redo");
	this.editRedo.setAccelerator(this.editRedoAccel);
	this.editCutLevel = new JMenuItem("Cut Level");
	this.editCutLevel.setAccelerator(this.editCutLevelAccel);
	this.editCopyLevel = new JMenuItem("Copy Level");
	this.editCopyLevel.setAccelerator(this.editCopyLevelAccel);
	this.editPasteLevel = new JMenuItem("Paste Level");
	this.editPasteLevel.setAccelerator(this.editPasteLevelAccel);
	this.editInsertLevelFromClipboard = new JMenuItem("Insert Level From Clipboard");
	this.editInsertLevelFromClipboard.setAccelerator(this.editInsertLevelFromClipboardAccel);
	this.editPreferences = new JMenuItem("Preferences...");
	this.editPreferences.setAccelerator(this.editPreferencesAccel);
	this.editClearHistory = new JMenuItem("Clear History");
	this.editClearHistory.setAccelerator(this.editClearHistoryAccel);
	this.editGoTo = new JMenuItem("Go To...");
	this.editGoTo.setAccelerator(this.editGoToAccel);
	this.editUpOneFloor = new JMenuItem("Up One Floor");
	this.editUpOneFloor.setAccelerator(this.editUpOneFloorAccel);
	this.editDownOneFloor = new JMenuItem("Down One Floor");
	this.editDownOneFloor.setAccelerator(this.editDownOneFloorAccel);
	this.editUpOneLevel = new JMenuItem("Up One Level");
	this.editUpOneLevel.setAccelerator(this.editUpOneLevelAccel);
	this.editDownOneLevel = new JMenuItem("Down One Level");
	this.editDownOneLevel.setAccelerator(this.editDownOneLevelAccel);
	this.editAddLevel = new JMenuItem("Add a Level...");
	this.editRemoveLevel = new JMenuItem("Remove a Level...");
	this.editResizeLevel = new JMenuItem("Resize Current Level...");
	this.editToggleLayer = new JMenuItem("Toggle Layer");
	this.editToggleLayer.setAccelerator(this.editToggleLayerAccel);
	this.editMazePreferences = new JMenuItem("Maze Preferences...");
	this.playPlay = new JMenuItem("Play");
	this.playPlay.setAccelerator(this.playPlayMazeAccel);
	this.playEdit = new JMenuItem("Edit");
	this.playEdit.setAccelerator(this.playEditMazeAccel);
	this.gameEquipment = new JMenuItem("Show Equipment...");
	this.gameInventory = new JMenuItem("Show Inventory...");
	this.gameInventory.setAccelerator(this.gameInventoryAccel);
	this.gameUse = new JMenuItem("Use an Item...");
	this.gameUse.setAccelerator(this.gameUseAccel);
	this.gameReset = new JMenuItem("Reset Current Level");
	this.gameReset.setAccelerator(this.gameResetAccel);
	this.gameShowScore = new JMenuItem("Show Current Score");
	this.gameShowScore.setAccelerator(this.gameShowScoreAccel);
	this.gameShowTable = new JMenuItem("Show Score Table");
	this.gameShowTable.setAccelerator(this.gameShowTableAccel);
	this.debugViewImageCache = new JMenuItem("View Image Cache");
	this.debugViewMonsterCache = new JMenuItem("View Monster Cache");
	this.debugResetPreferences = new JMenuItem("Reset Preferences");
	this.helpAbout = new JMenuItem("About Fantastle...");
	this.helpGeneralHelp = new JMenuItem("Fantastle Help");
	this.helpObjectHelp = new JMenuItem("Fantastle Object Help");
	this.fileNew.addActionListener(this.handler);
	this.fileOpen.addActionListener(this.handler);
	this.fileClose.addActionListener(this.handler);
	this.fileSave.addActionListener(this.handler);
	this.fileSaveAs.addActionListener(this.handler);
	this.fileExit.addActionListener(this.handler);
	this.editUndo.addActionListener(this.handler);
	this.editRedo.addActionListener(this.handler);
	this.editCutLevel.addActionListener(this.handler);
	this.editCopyLevel.addActionListener(this.handler);
	this.editPasteLevel.addActionListener(this.handler);
	this.editInsertLevelFromClipboard.addActionListener(this.handler);
	this.editPreferences.addActionListener(this.handler);
	this.editClearHistory.addActionListener(this.handler);
	this.editGoTo.addActionListener(this.handler);
	this.editUpOneFloor.addActionListener(this.handler);
	this.editDownOneFloor.addActionListener(this.handler);
	this.editUpOneLevel.addActionListener(this.handler);
	this.editDownOneLevel.addActionListener(this.handler);
	this.editAddLevel.addActionListener(this.handler);
	this.editRemoveLevel.addActionListener(this.handler);
	this.editResizeLevel.addActionListener(this.handler);
	this.editToggleLayer.addActionListener(this.handler);
	this.editMazePreferences.addActionListener(this.handler);
	this.playPlay.addActionListener(this.handler);
	this.playEdit.addActionListener(this.handler);
	this.gameEquipment.addActionListener(this.handler);
	this.gameInventory.addActionListener(this.handler);
	this.gameUse.addActionListener(this.handler);
	this.gameReset.addActionListener(this.handler);
	this.gameShowScore.addActionListener(this.handler);
	this.gameShowTable.addActionListener(this.handler);
	this.debugViewImageCache.addActionListener(this.handler);
	this.debugViewMonsterCache.addActionListener(this.handler);
	this.debugResetPreferences.addActionListener(this.handler);
	this.helpAbout.addActionListener(this.handler);
	this.helpGeneralHelp.addActionListener(this.handler);
	this.helpObjectHelp.addActionListener(this.handler);
	this.fileMenu.add(this.fileNew);
	this.fileMenu.add(this.fileOpen);
	this.fileMenu.add(this.fileClose);
	this.fileMenu.add(this.fileSave);
	this.fileMenu.add(this.fileSaveAs);
	if (!System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
	    this.fileMenu.add(this.fileExit);
	}
	this.editMenu.add(this.editUndo);
	this.editMenu.add(this.editRedo);
	this.editMenu.add(this.editCutLevel);
	this.editMenu.add(this.editCopyLevel);
	this.editMenu.add(this.editPasteLevel);
	this.editMenu.add(this.editInsertLevelFromClipboard);
	if (!System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
	    this.editMenu.add(this.editPreferences);
	}
	this.editMenu.add(this.editClearHistory);
	this.editMenu.add(this.editGoTo);
	this.editMenu.addSeparator();
	this.editMenu.add(this.editUpOneFloor);
	this.editMenu.add(this.editDownOneFloor);
	this.editMenu.add(this.editUpOneLevel);
	this.editMenu.add(this.editDownOneLevel);
	this.editMenu.add(this.editAddLevel);
	this.editMenu.add(this.editRemoveLevel);
	this.editMenu.add(this.editResizeLevel);
	this.editMenu.add(this.editToggleLayer);
	this.editMenu.add(this.editMazePreferences);
	this.playMenu.add(this.playPlay);
	this.playMenu.add(this.playEdit);
	this.gameMenu.add(this.gameEquipment);
	this.gameMenu.add(this.gameInventory);
	this.gameMenu.add(this.gameUse);
	this.gameMenu.add(this.gameReset);
	this.gameMenu.add(this.gameShowScore);
	this.gameMenu.add(this.gameShowTable);
	this.debugMenu.add(this.debugViewImageCache);
	this.debugMenu.add(this.debugViewMonsterCache);
	this.debugMenu.add(this.debugResetPreferences);
	if (!System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
	    this.helpMenu.add(this.helpAbout);
	}
	this.helpMenu.add(this.helpGeneralHelp);
	this.helpMenu.add(this.helpObjectHelp);
	this.mainMenuBar.add(this.fileMenu);
	this.mainMenuBar.add(this.editMenu);
	this.mainMenuBar.add(this.playMenu);
	this.mainMenuBar.add(this.gameMenu);
	this.mainMenuBar.add(this.debugMenu);
	this.mainMenuBar.add(this.helpMenu);
    }

    private void setInitialMenuState() {
	this.fileNew.setEnabled(true);
	this.fileOpen.setEnabled(true);
	this.fileClose.setEnabled(false);
	this.fileSave.setEnabled(false);
	this.fileSaveAs.setEnabled(false);
	this.fileExit.setEnabled(true);
	this.editUndo.setEnabled(false);
	this.editRedo.setEnabled(false);
	this.editCutLevel.setEnabled(false);
	this.editCopyLevel.setEnabled(false);
	this.editPasteLevel.setEnabled(false);
	this.editInsertLevelFromClipboard.setEnabled(false);
	this.editPreferences.setEnabled(true);
	this.editClearHistory.setEnabled(false);
	this.editGoTo.setEnabled(false);
	this.editUpOneFloor.setEnabled(false);
	this.editDownOneFloor.setEnabled(false);
	this.editUpOneLevel.setEnabled(false);
	this.editDownOneLevel.setEnabled(false);
	this.editAddLevel.setEnabled(false);
	this.editRemoveLevel.setEnabled(false);
	this.editResizeLevel.setEnabled(false);
	this.editToggleLayer.setEnabled(false);
	this.editMazePreferences.setEnabled(false);
	this.playPlay.setEnabled(false);
	this.playEdit.setEnabled(false);
	this.gameEquipment.setEnabled(false);
	this.gameInventory.setEnabled(false);
	this.gameUse.setEnabled(false);
	this.gameReset.setEnabled(false);
	this.gameShowScore.setEnabled(false);
	this.gameShowTable.setEnabled(false);
	this.debugViewImageCache.setEnabled(true);
	this.debugViewMonsterCache.setEnabled(true);
	this.debugResetPreferences.setEnabled(true);
	this.helpAbout.setEnabled(true);
	this.helpObjectHelp.setEnabled(true);
    }

    private class EventHandler implements ActionListener {
	public EventHandler() {
	    // TODO Auto-generated constructor stub
	}

	// Handle menus
	@Override
	public void actionPerformed(final ActionEvent e) {
	    try {
		final Application app = Fantastle5.getApplication();
		final MazeEditor me = app.getEditor();
		boolean loaded = false;
		final String cmd = e.getActionCommand();
		if (cmd.equals("New...")) {
		    loaded = me.newMaze();
		    app.getMazeManager().setLoaded(loaded);
		} else if (cmd.equals("Open...")) {
		    loaded = app.getMazeManager().loadMaze();
		    app.getMazeManager().setLoaded(loaded);
		} else if (cmd.equals("Close")) {
		    // Close the window
		    if (app.getMode() == Application.STATUS_EDITOR) {
			app.getEditor().handleCloseWindow();
		    } else if (app.getMode() == Application.STATUS_GAME) {
			boolean saved = true;
			int status = 0;
			if (app.getMazeManager().getDirty()) {
			    status = app.getMazeManager().showSaveDialog();
			    if (status == JOptionPane.YES_OPTION) {
				saved = app.getMazeManager().saveMaze();
			    } else if (status == JOptionPane.CANCEL_OPTION) {
				saved = false;
			    } else {
				app.getMazeManager().setDirty(false);
			    }
			}
			if (saved) {
			    app.getGameManager().hideOutput();
			    app.getGUIManager().showGUI();
			}
		    }
		} else if (cmd.equals("Save")) {
		    if (app.getMazeManager().getLoaded()) {
			app.getMazeManager().saveMaze();
		    } else {
			Messager.showDialog("No Maze Opened");
		    }
		} else if (cmd.equals("Save As...")) {
		    if (app.getMazeManager().getLoaded()) {
			app.getMazeManager().saveMazeAs();
		    } else {
			Messager.showDialog("No Maze Opened");
		    }
		} else if (cmd.equals("Exit")) {
		    // Exit program
		    if (app.getMazeManager().quitHandler()) {
			System.exit(0);
		    }
		} else if (cmd.equals("Undo")) {
		    // Undo most recent action
		    me.undo();
		} else if (cmd.equals("Redo")) {
		    // Redo most recent undone action
		    me.redo();
		} else if (cmd.equals("Cut Level")) {
		    // Cut Level
		    final int level = app.getEditor().getLocationManager().getEditorLocationW();
		    app.getMazeManager().getMaze().cutLevel(level);
		    app.getEditor().fixLimits();
		    app.getEditor().updateEditorLevelAbsolute(level);
		} else if (cmd.equals("Copy Level")) {
		    // Copy Level
		    final int level = app.getEditor().getLocationManager().getEditorLocationW();
		    app.getMazeManager().getMaze().copyLevel(level);
		} else if (cmd.equals("Paste Level")) {
		    // Paste Level
		    final int level = app.getEditor().getLocationManager().getEditorLocationW();
		    app.getMazeManager().getMaze().pasteLevel(level);
		    app.getEditor().fixLimits();
		    me.redrawEditor();
		} else if (cmd.equals("Insert Level From Clipboard")) {
		    // Insert Level From Clipboard
		    app.getMazeManager().getMaze().insertLevelFromClipboard();
		    app.getEditor().fixLimits();
		} else if (cmd.equals("Preferences...")) {
		    // Show preferences dialog
		    app.getPrefsManager().showPrefs();
		} else if (cmd.equals("Clear History")) {
		    // Clear undo/redo history, confirm first
		    final int res = Messager.showConfirmDialog("Are you sure you want to clear the history?", "Editor");
		    if (res == JOptionPane.YES_OPTION) {
			me.clearHistory();
		    }
		} else if (cmd.equals("Go To...")) {
		    // Go To
		    me.goToHandler();
		} else if (cmd.equals("Up One Floor")) {
		    // Go up one floor
		    me.updateEditorPosition(0, 0, 1, 0);
		} else if (cmd.equals("Down One Floor")) {
		    // Go down one floor
		    me.updateEditorPosition(0, 0, -1, 0);
		} else if (cmd.equals("Up One Level")) {
		    // Go up one level
		    me.updateEditorPosition(0, 0, 0, 1);
		} else if (cmd.equals("Down One Level")) {
		    // Go down one level
		    me.updateEditorPosition(0, 0, 0, -1);
		} else if (cmd.equals("Add a Level...")) {
		    // Add a level
		    me.addLevel();
		} else if (cmd.equals("Remove a Level...")) {
		    // Remove a level
		    me.removeLevel();
		} else if (cmd.equals("Resize Current Level...")) {
		    // Resize level
		    me.resizeLevel();
		} else if (cmd.equals("Toggle Layer")) {
		    // Toggle current layer
		    me.toggleLayer();
		} else if (cmd.equals("Maze Preferences...")) {
		    // Set Maze Preferences
		    me.setMazePrefs();
		} else if (cmd.equals("Play")) {
		    // Play the current maze
		    final boolean proceed = app.getGameManager().newGame();
		    if (proceed) {
			app.getGameManager().playMaze();
		    }
		} else if (cmd.equals("Edit")) {
		    // Edit the current maze
		    me.editMaze();
		} else if (cmd.equals("Show Equipment...")) {
		    if (!app.getGameManager().usingAnItem()) {
			app.getGameManager().showEquipmentDialog();
		    }
		} else if (cmd.equals("Show Inventory...")) {
		    if (!app.getGameManager().usingAnItem()) {
			app.getGameManager().showInventoryDialog();
		    }
		} else if (cmd.equals("Use an Item...")) {
		    if (!app.getGameManager().usingAnItem()) {
			app.getGameManager().setUsingAnItem(true);
			app.getGameManager().showUseDialog();
		    }
		} else if (cmd.equals("Reset Current Level")) {
		    if (!app.getGameManager().usingAnItem()) {
			final int result = Messager
				.showConfirmDialog("Are you sure you want to reset the current level?", "Fantastle");
			if (result == JOptionPane.YES_OPTION) {
			    app.getGameManager().resetCurrentLevel();
			}
		    }
		} else if (cmd.equals("Show Current Score")) {
		    app.getGameManager().showCurrentScore();
		} else if (cmd.equals("Show Score Table")) {
		    app.getGameManager().showScoreTable();
		} else if (cmd.equals("View Image Cache")) {
		    ImageCache.viewCache();
		} else if (cmd.equals("View Monster Cache")) {
		    MonsterImageCache.viewMonsterCache();
		} else if (cmd.equals("Reset Preferences")) {
		    app.resetPreferences();
		    Messager.showDialog("Preferences reset to defaults.");
		} else if (cmd.equals("About Fantastle...")) {
		    app.getAboutDialog().showAboutDialog();
		} else if (cmd.equals("Fantastle Help")) {
		    app.getGeneralHelpManager().showHelp();
		} else if (cmd.equals("Fantastle Object Help")) {
		    app.getObjectHelpManager().showHelp();
		}
		MenuManager.this.checkFlags();
	    } catch (final Exception ex) {
		Fantastle5.debug(ex);
	    }
	}
    }
}
