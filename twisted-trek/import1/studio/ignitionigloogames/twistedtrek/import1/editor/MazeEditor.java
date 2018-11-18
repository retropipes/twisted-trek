/* Import1: A Maze-Solving Game */
package studio.ignitionigloogames.twistedtrek.import1.editor;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import studio.ignitionigloogames.images.BufferedImageIcon;
import studio.ignitionigloogames.picturepicker.PicturePicker;
import studio.ignitionigloogames.twistedtrek.import1.Application;
import studio.ignitionigloogames.twistedtrek.import1.Import1;
import studio.ignitionigloogames.twistedtrek.import1.Messager;
import studio.ignitionigloogames.twistedtrek.import1.game.GameManager;
import studio.ignitionigloogames.twistedtrek.import1.generic.MazeObject;
import studio.ignitionigloogames.twistedtrek.import1.generic.MazeObjectList;
import studio.ignitionigloogames.twistedtrek.import1.maze.Maze;
import studio.ignitionigloogames.twistedtrek.import1.maze.Maze5;
import studio.ignitionigloogames.twistedtrek.import1.maze.MazeManager;
import studio.ignitionigloogames.twistedtrek.import1.objects.Empty;
import studio.ignitionigloogames.twistedtrek.import1.objects.EmptyVoid;
import studio.ignitionigloogames.twistedtrek.import1.objects.FinishTo;
import studio.ignitionigloogames.twistedtrek.import1.objects.InvisibleOneShotTeleport;
import studio.ignitionigloogames.twistedtrek.import1.objects.InvisibleTeleport;
import studio.ignitionigloogames.twistedtrek.import1.objects.MetalButton;
import studio.ignitionigloogames.twistedtrek.import1.objects.OneShotTeleport;
import studio.ignitionigloogames.twistedtrek.import1.objects.RandomInvisibleOneShotTeleport;
import studio.ignitionigloogames.twistedtrek.import1.objects.RandomInvisibleTeleport;
import studio.ignitionigloogames.twistedtrek.import1.objects.RandomOneShotTeleport;
import studio.ignitionigloogames.twistedtrek.import1.objects.RandomTeleport;
import studio.ignitionigloogames.twistedtrek.import1.objects.StairsDown;
import studio.ignitionigloogames.twistedtrek.import1.objects.StairsUp;
import studio.ignitionigloogames.twistedtrek.import1.objects.Teleport;
import studio.ignitionigloogames.twistedtrek.import1.objects.TreasureChest;
import studio.ignitionigloogames.twistedtrek.import1.objects.TwoWayTeleport;
import studio.ignitionigloogames.twistedtrek.import1.resourcemanagers.GraphicsManager;

public class MazeEditor {
    // Declarations
    private JFrame outputFrame;
    private final JFrame treasureFrame;
    private Container outputPane, secondaryPane, borderPane;
    private final Container treasurePane;
    private GridBagLayout gridbag;
    private GridBagConstraints c;
    private JScrollBar vertScroll, horzScroll;
    private final EventHandler mhandler;
    private final TeleportEventHandler thandler;
    private final TreasureEventHandler rhandler;
    private final MetalButtonEventHandler mbhandler;
    private final MazePreferencesManager mPrefs;
    private PicturePicker picker;
    private final PicturePicker treasurePicker;
    private final MazeObjectList objectList;
    private final String[] groundNames;
    private final String[] objectNames;
    private final MazeObject[] groundObjects;
    private final MazeObject[] objectObjects;
    private final BufferedImageIcon[] groundEditorAppearances;
    private final BufferedImageIcon[] objectEditorAppearances;
    private final String[] containableNames;
    private final MazeObject[] containableObjects;
    private final BufferedImageIcon[] containableEditorAppearances;
    private int TELEPORT_TYPE;
    private int currentObjectIndex;
    private UndoRedoEngine engine;
    private EditorLocationManager elMgr;
    EditorViewingWindowManager evMgr;
    private JLabel[][] drawGrid;
    private boolean mazeChanged;
    public static final int TELEPORT_TYPE_GENERIC = 0;
    public static final int TELEPORT_TYPE_INVISIBLE_GENERIC = 1;
    public static final int TELEPORT_TYPE_RANDOM = 2;
    public static final int TELEPORT_TYPE_RANDOM_INVISIBLE = 3;
    public static final int TELEPORT_TYPE_ONESHOT = 4;
    public static final int TELEPORT_TYPE_INVISIBLE_ONESHOT = 5;
    public static final int TELEPORT_TYPE_RANDOM_ONESHOT = 6;
    public static final int TELEPORT_TYPE_RANDOM_INVISIBLE_ONESHOT = 7;
    public static final int TELEPORT_TYPE_TWOWAY = 8;
    public static final int STAIRS_UP = 0;
    public static final int STAIRS_DOWN = 1;

    public MazeEditor() {
	this.mPrefs = new MazePreferencesManager();
	this.mhandler = new EventHandler();
	this.mbhandler = new MetalButtonEventHandler();
	this.thandler = new TeleportEventHandler();
	this.engine = new UndoRedoEngine();
	this.objectList = Import1.getApplication().getObjects();
	this.groundNames = this.objectList.getAllGroundLayerNames();
	this.objectNames = this.objectList.getAllObjectLayerNames();
	this.groundObjects = this.objectList.getAllGroundLayerObjects();
	this.objectObjects = this.objectList.getAllObjectLayerObjects();
	this.groundEditorAppearances = this.objectList.getAllGroundLayerEditorAppearances();
	this.objectEditorAppearances = this.objectList.getAllObjectLayerEditorAppearances();
	// Set up treasure picker
	this.rhandler = new TreasureEventHandler();
	this.containableNames = this.objectList.getAllContainableNames();
	this.containableObjects = this.objectList.getAllContainableObjects();
	this.containableEditorAppearances = this.objectList.getAllContainableObjectEditorAppearances();
	this.treasureFrame = new JFrame("Treasure Chest Contents");
	this.treasureFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	this.treasurePicker = new PicturePicker(this.containableEditorAppearances, this.containableNames);
	this.treasurePane = this.treasurePicker.getPicker();
	this.treasureFrame.setContentPane(this.treasurePane);
	this.treasureFrame.addWindowListener(this.rhandler);
	this.treasureFrame.pack();
	this.mazeChanged = true;
    }

    public void mazeChanged() {
	this.mazeChanged = true;
    }

    public EditorViewingWindowManager getViewManager() {
	return this.evMgr;
    }

    public EditorLocationManager getLocationManager() {
	return this.elMgr;
    }

    public void updateEditorPosition(final int x, final int y, final int z, final int w) {
	this.elMgr.offsetEditorLocationW(w);
	this.evMgr.offsetViewingWindowLocationX(x);
	this.evMgr.offsetViewingWindowLocationY(y);
	this.elMgr.offsetEditorLocationZ(z);
	if (w != 0) {
	    // Level Change
	    this.fixLimits();
	    this.setUpGUI();
	}
	this.checkMenus();
	this.redrawEditor();
    }

    public void updateEditorLevelAbsolute(final int w) {
	this.elMgr.setEditorLocationW(w);
	// Level Change
	this.fixLimits();
	this.setUpGUI();
	this.checkMenus();
	this.redrawEditor();
    }

    private void checkMenus() {
	final Application app = Import1.getApplication();
	final Maze m = app.getMazeManager().getMaze();
	if (app.getMazeManager().getMaze().getLevels() == m.getMinLevels()) {
	    app.getMenuManager().disableRemoveLevel();
	} else {
	    app.getMenuManager().enableRemoveLevel();
	}
	if (app.getMazeManager().getMaze().getLevels() == m.getMaxLevels()) {
	    app.getMenuManager().disableAddLevel();
	} else {
	    app.getMenuManager().enableAddLevel();
	}
	try {
	    if (app.getMazeManager().getMaze().is3rdDimensionWraparoundEnabled(this.elMgr.getEditorLocationW())) {
		app.getMenuManager().enableDownOneFloor();
	    } else {
		if (this.elMgr.getEditorLocationZ() == this.elMgr.getMinEditorLocationZ()) {
		    app.getMenuManager().disableDownOneFloor();
		} else {
		    app.getMenuManager().enableDownOneFloor();
		}
	    }
	    if (app.getMazeManager().getMaze().is3rdDimensionWraparoundEnabled(this.elMgr.getEditorLocationW())) {
		app.getMenuManager().enableUpOneFloor();
	    } else {
		if (this.elMgr.getEditorLocationZ() == this.elMgr.getMaxEditorLocationZ()) {
		    app.getMenuManager().disableUpOneFloor();
		} else {
		    app.getMenuManager().enableUpOneFloor();
		}
	    }
	    if (this.elMgr.getEditorLocationW() == this.elMgr.getMinEditorLocationW()) {
		app.getMenuManager().disableDownOneLevel();
	    } else {
		app.getMenuManager().enableDownOneLevel();
	    }
	    if (this.elMgr.getEditorLocationW() == this.elMgr.getMaxEditorLocationW()) {
		app.getMenuManager().disableUpOneLevel();
	    } else {
		app.getMenuManager().enableUpOneLevel();
	    }
	} catch (final NullPointerException npe) {
	    app.getMenuManager().disableDownOneFloor();
	    app.getMenuManager().disableUpOneFloor();
	    app.getMenuManager().disableDownOneLevel();
	    app.getMenuManager().disableUpOneLevel();
	}
	if (!this.engine.tryUndo()) {
	    app.getMenuManager().disableUndo();
	} else {
	    app.getMenuManager().enableUndo();
	}
	if (!this.engine.tryRedo()) {
	    app.getMenuManager().disableRedo();
	} else {
	    app.getMenuManager().enableRedo();
	}
	if (this.engine.tryBoth()) {
	    app.getMenuManager().disableClearHistory();
	} else {
	    app.getMenuManager().enableClearHistory();
	}
    }

    public void toggleLayer() {
	if (this.elMgr.getEditorLocationE() == Maze.LAYER_GROUND) {
	    this.elMgr.setEditorLocationE(Maze.LAYER_OBJECT);
	} else {
	    this.elMgr.setEditorLocationE(Maze.LAYER_GROUND);
	}
	this.updatePicker();
	this.redrawEditor();
    }

    public void setMazePrefs() {
	this.mPrefs.loadPrefs();
	this.mPrefs.showPrefs();
    }

    public void redrawEditor() {
	if (this.elMgr.getEditorLocationE() == Maze.LAYER_GROUND) {
	    this.redrawGround();
	} else {
	    this.redrawGroundAndObjects();
	}
    }

    private void redrawGround() {
	// Draw the maze in edit mode
	final Application app = Import1.getApplication();
	int x, y, w;
	int xFix, yFix;
	w = this.elMgr.getEditorLocationW();
	for (x = this.evMgr.getViewingWindowLocationX(); x <= this.evMgr.getLowerRightViewingWindowLocationX(); x++) {
	    for (y = this.evMgr.getViewingWindowLocationY(); y <= this.evMgr
		    .getLowerRightViewingWindowLocationY(); y++) {
		xFix = x - this.evMgr.getViewingWindowLocationX();
		yFix = y - this.evMgr.getViewingWindowLocationY();
		try {
		    final String name1 = app.getMazeManager().getMaze()
			    .getCell(y, x, this.elMgr.getEditorLocationZ(), w, Maze.LAYER_GROUND)
			    .editorRenderHook(y, x, this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW());
		    this.drawGrid[xFix][yFix].setIcon(GraphicsManager.getImage(name1));
		} catch (final ArrayIndexOutOfBoundsException ae) {
		    this.drawGrid[xFix][yFix].setIcon(GraphicsManager.getImage("Void"));
		} catch (final NullPointerException np) {
		    this.drawGrid[xFix][yFix].setIcon(GraphicsManager.getImage("Void"));
		}
	    }
	}
	this.outputFrame.pack();
	this.outputFrame.setTitle(
		"Editor (Ground Layer) - Floor " + (this.elMgr.getEditorLocationZ() + 1) + " Level " + (w + 1));
	this.showOutput();
    }

    private void redrawGroundAndObjects() {
	// Draw the maze in edit mode
	final Application app = Import1.getApplication();
	int x, y, w;
	int xFix, yFix;
	w = this.elMgr.getEditorLocationW();
	for (x = this.evMgr.getViewingWindowLocationX(); x <= this.evMgr.getLowerRightViewingWindowLocationX(); x++) {
	    for (y = this.evMgr.getViewingWindowLocationY(); y <= this.evMgr
		    .getLowerRightViewingWindowLocationY(); y++) {
		xFix = x - this.evMgr.getViewingWindowLocationX();
		yFix = y - this.evMgr.getViewingWindowLocationY();
		try {
		    final String name1 = app.getMazeManager().getMaze()
			    .getCell(y, x, this.elMgr.getEditorLocationZ(), w, Maze.LAYER_GROUND)
			    .editorRenderHook(y, x, this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW());
		    final String name2 = app.getMazeManager().getMaze()
			    .getCell(y, x, this.elMgr.getEditorLocationZ(), w, Maze.LAYER_OBJECT)
			    .editorRenderHook(y, x, this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW());
		    this.drawGrid[xFix][yFix].setIcon(GraphicsManager.getCompositeImage(name1, name2));
		} catch (final ArrayIndexOutOfBoundsException ae) {
		    this.drawGrid[xFix][yFix].setIcon(GraphicsManager.getImage("Void"));
		} catch (final NullPointerException np) {
		    this.drawGrid[xFix][yFix].setIcon(GraphicsManager.getImage("Void"));
		}
	    }
	}
	this.outputFrame.pack();
	this.outputFrame.setTitle(
		"Editor (Object Layer) - Floor " + (this.elMgr.getEditorLocationZ() + 1) + " Level " + (w + 1));
	this.showOutput();
    }

    public void editObject(final int x, final int y) {
	final Application app = Import1.getApplication();
	this.currentObjectIndex = this.picker.getPicked();
	final int xOffset = this.vertScroll.getValue() - this.vertScroll.getMinimum();
	final int yOffset = this.horzScroll.getValue() - this.horzScroll.getMinimum();
	final int gridX = x / GraphicsManager.getGraphicSize() + this.evMgr.getViewingWindowLocationX() - xOffset
		+ yOffset;
	final int gridY = y / GraphicsManager.getGraphicSize() + this.evMgr.getViewingWindowLocationY() + xOffset
		- yOffset;
	try {
	    app.getGameManager().setSavedMazeObject(app.getMazeManager().getMaze().getCell(gridX, gridY,
		    this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW(), this.elMgr.getEditorLocationE()));
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    return;
	}
	MazeObject[] choices = null;
	if (this.elMgr.getEditorLocationE() == Maze.LAYER_GROUND) {
	    choices = this.groundObjects;
	} else {
	    choices = this.objectObjects;
	}
	final MazeObject mo = choices[this.currentObjectIndex];
	this.elMgr.setEditorLocationX(gridX);
	this.elMgr.setEditorLocationY(gridY);
	mo.editorPlaceHook();
	try {
	    this.checkTwoWayTeleportPair(this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW());
	    this.updateUndoHistory(app.getGameManager().getSavedMazeObject(), gridX, gridY,
		    this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW(), this.elMgr.getEditorLocationE());
	    app.getMazeManager().getMaze().setCell(mo, gridX, gridY, this.elMgr.getEditorLocationZ(),
		    this.elMgr.getEditorLocationW(), this.elMgr.getEditorLocationE());
	    this.checkStairPair(this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW());
	    app.getMazeManager().setDirty(true);
	    this.checkMenus();
	    this.redrawEditor();
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    app.getMazeManager().getMaze().setCell(app.getGameManager().getSavedMazeObject(), gridX, gridY,
		    this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW(), this.elMgr.getEditorLocationE());
	    this.redrawEditor();
	}
    }

    public void probeObjectProperties(final int x, final int y) {
	final Application app = Import1.getApplication();
	final int xOffset = this.vertScroll.getValue() - this.vertScroll.getMinimum();
	final int yOffset = this.horzScroll.getValue() - this.horzScroll.getMinimum();
	final int gridX = x / GraphicsManager.getGraphicSize() + this.evMgr.getViewingWindowLocationX() - xOffset
		+ yOffset;
	final int gridY = y / GraphicsManager.getGraphicSize() + this.evMgr.getViewingWindowLocationY() + xOffset
		- yOffset;
	try {
	    final MazeObject mo = app.getMazeManager().getMaze().getCell(gridX, gridY, this.elMgr.getEditorLocationZ(),
		    this.elMgr.getEditorLocationW(), this.elMgr.getEditorLocationE());
	    this.elMgr.setEditorLocationX(gridX);
	    this.elMgr.setEditorLocationY(gridY);
	    mo.editorProbeHook();
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    final EmptyVoid ev = new EmptyVoid();
	    ev.determineCurrentAppearance(gridX, gridY, this.elMgr.getEditorLocationZ(),
		    this.elMgr.getEditorLocationW());
	    ev.editorProbeHook();
	}
    }

    public void editObjectProperties(final int x, final int y) {
	final Application app = Import1.getApplication();
	final int xOffset = this.vertScroll.getValue() - this.vertScroll.getMinimum();
	final int yOffset = this.horzScroll.getValue() - this.horzScroll.getMinimum();
	final int gridX = x / GraphicsManager.getGraphicSize() + this.evMgr.getViewingWindowLocationX() - xOffset
		+ yOffset;
	final int gridY = y / GraphicsManager.getGraphicSize() + this.evMgr.getViewingWindowLocationY() + xOffset
		- yOffset;
	try {
	    final MazeObject mo = app.getMazeManager().getMaze().getCell(gridX, gridY, this.elMgr.getEditorLocationZ(),
		    this.elMgr.getEditorLocationW(), this.elMgr.getEditorLocationE());
	    this.elMgr.setEditorLocationX(gridX);
	    this.elMgr.setEditorLocationY(gridY);
	    if (!mo.defersSetProperties()) {
		final MazeObject mo2 = mo.editorPropertiesHook();
		if (mo2 == null) {
		    Messager.showMessage("This object has no properties");
		} else {
		    this.checkTwoWayTeleportPair(this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW());
		    this.updateUndoHistory(app.getGameManager().getSavedMazeObject(), gridX, gridY,
			    this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW(),
			    this.elMgr.getEditorLocationE());
		    app.getMazeManager().getMaze().setCell(mo2, gridX, gridY, this.elMgr.getEditorLocationZ(),
			    this.elMgr.getEditorLocationW(), this.elMgr.getEditorLocationE());
		    this.checkStairPair(this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW());
		    this.checkMenus();
		    app.getMazeManager().setDirty(true);
		}
	    } else {
		mo.editorPropertiesHook();
	    }
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    // Do nothing
	}
    }

    private void checkStairPair(final int z, final int w) {
	final Application app = Import1.getApplication();
	final MazeObject mo1 = app.getMazeManager().getMaze().getCell(this.elMgr.getEditorLocationX(),
		this.elMgr.getEditorLocationY(), z, w, Maze.LAYER_OBJECT);
	final String name1 = mo1.getName();
	String name2, name3;
	try {
	    final MazeObject mo2 = app.getMazeManager().getMaze().getCell(this.elMgr.getEditorLocationX(),
		    this.elMgr.getEditorLocationY(), z + 1, w, Maze.LAYER_OBJECT);
	    name2 = mo2.getName();
	} catch (final ArrayIndexOutOfBoundsException e) {
	    name2 = "";
	}
	try {
	    final MazeObject mo3 = app.getMazeManager().getMaze().getCell(this.elMgr.getEditorLocationX(),
		    this.elMgr.getEditorLocationY(), z - 1, w, Maze.LAYER_OBJECT);
	    name3 = mo3.getName();
	} catch (final ArrayIndexOutOfBoundsException e) {
	    name3 = "";
	}
	if (!name1.equals("Stairs Up")) {
	    if (name2.equals("Stairs Down")) {
		this.unpairStairs(MazeEditor.STAIRS_UP, z, w);
	    } else if (!name1.equals("Stairs Down")) {
		if (name3.equals("Stairs Up")) {
		    this.unpairStairs(MazeEditor.STAIRS_DOWN, z, w);
		}
	    }
	}
    }

    private void reverseCheckStairPair(final int z, final int w) {
	final Application app = Import1.getApplication();
	final MazeObject mo1 = app.getMazeManager().getMaze().getCell(this.elMgr.getEditorLocationX(),
		this.elMgr.getEditorLocationY(), z, w, Maze.LAYER_OBJECT);
	final String name1 = mo1.getName();
	String name2, name3;
	try {
	    final MazeObject mo2 = app.getMazeManager().getMaze().getCell(this.elMgr.getEditorLocationX(),
		    this.elMgr.getEditorLocationY(), z + 1, w, Maze.LAYER_OBJECT);
	    name2 = mo2.getName();
	} catch (final ArrayIndexOutOfBoundsException e) {
	    name2 = "";
	}
	try {
	    final MazeObject mo3 = app.getMazeManager().getMaze().getCell(this.elMgr.getEditorLocationX(),
		    this.elMgr.getEditorLocationY(), z - 1, w, Maze.LAYER_OBJECT);
	    name3 = mo3.getName();
	} catch (final ArrayIndexOutOfBoundsException e) {
	    name3 = "";
	}
	if (name1.equals("Stairs Up")) {
	    if (!name2.equals("Stairs Down")) {
		this.pairStairs(MazeEditor.STAIRS_UP, z, w);
	    } else if (name1.equals("Stairs Down")) {
		if (!name3.equals("Stairs Up")) {
		    this.pairStairs(MazeEditor.STAIRS_DOWN, z, w);
		}
	    }
	}
    }

    public void pairStairs(final int type) {
	final Application app = Import1.getApplication();
	switch (type) {
	case STAIRS_UP:
	    try {
		app.getMazeManager().getMaze().setCell(new StairsDown(), this.elMgr.getEditorLocationX(),
			this.elMgr.getEditorLocationY(), this.elMgr.getEditorLocationZ() + 1,
			this.elMgr.getEditorLocationW(), Maze.LAYER_OBJECT);
	    } catch (final ArrayIndexOutOfBoundsException e) {
		// Do nothing
	    }
	    break;
	case STAIRS_DOWN:
	    try {
		app.getMazeManager().getMaze().setCell(new StairsUp(), this.elMgr.getEditorLocationX(),
			this.elMgr.getEditorLocationY(), this.elMgr.getEditorLocationZ() - 1,
			this.elMgr.getEditorLocationW(), Maze.LAYER_OBJECT);
	    } catch (final ArrayIndexOutOfBoundsException e) {
		// Do nothing
	    }
	    break;
	default:
	    break;
	}
    }

    private void pairStairs(final int type, final int z, final int w) {
	final Application app = Import1.getApplication();
	switch (type) {
	case STAIRS_UP:
	    try {
		app.getMazeManager().getMaze().setCell(new StairsDown(), this.elMgr.getEditorLocationX(),
			this.elMgr.getEditorLocationY(), z + 1, w, Maze.LAYER_OBJECT);
	    } catch (final ArrayIndexOutOfBoundsException e) {
		// Do nothing
	    }
	    break;
	case STAIRS_DOWN:
	    try {
		app.getMazeManager().getMaze().setCell(new StairsUp(), this.elMgr.getEditorLocationX(),
			this.elMgr.getEditorLocationY(), z - 1, w, Maze.LAYER_OBJECT);
	    } catch (final ArrayIndexOutOfBoundsException e) {
		// Do nothing
	    }
	    break;
	default:
	    break;
	}
    }

    private void unpairStairs(final int type, final int z, final int w) {
	final Application app = Import1.getApplication();
	switch (type) {
	case STAIRS_UP:
	    try {
		app.getMazeManager().getMaze().setCell(new Empty(), this.elMgr.getEditorLocationX(),
			this.elMgr.getEditorLocationY(), z + 1, w, Maze.LAYER_OBJECT);
	    } catch (final ArrayIndexOutOfBoundsException e) {
		// Do nothing
	    }
	    break;
	case STAIRS_DOWN:
	    try {
		app.getMazeManager().getMaze().setCell(new Empty(), this.elMgr.getEditorLocationX(),
			this.elMgr.getEditorLocationY(), z - 1, w, Maze.LAYER_OBJECT);
	    } catch (final ArrayIndexOutOfBoundsException e) {
		// Do nothing
	    }
	    break;
	default:
	    break;
	}
    }

    private void checkTwoWayTeleportPair(final int z, final int w) {
	final Application app = Import1.getApplication();
	final MazeObject mo1 = app.getMazeManager().getMaze().getCell(this.elMgr.getEditorLocationX(),
		this.elMgr.getEditorLocationY(), z, w, Maze.LAYER_OBJECT);
	final String name1 = mo1.getName();
	String name2;
	int destX, destY, destZ, destW;
	if (name1.equals("Two-Way Teleport")) {
	    final TwoWayTeleport twt = (TwoWayTeleport) mo1;
	    destX = twt.getDestinationRow();
	    destY = twt.getDestinationColumn();
	    destZ = twt.getDestinationFloor();
	    destW = twt.getDestinationLevel();
	    final MazeObject mo2 = app.getMazeManager().getMaze().getCell(destX, destY, destZ, destW,
		    Maze.LAYER_OBJECT);
	    name2 = mo2.getName();
	    if (name2.equals("Two-Way Teleport")) {
		MazeEditor.unpairTwoWayTeleport(destX, destY, destZ, destW);
	    }
	}
    }

    private void reverseCheckTwoWayTeleportPair(final int z, final int w) {
	final Application app = Import1.getApplication();
	final MazeObject mo1 = app.getMazeManager().getMaze().getCell(this.elMgr.getEditorLocationX(),
		this.elMgr.getEditorLocationY(), z, w, Maze.LAYER_OBJECT);
	final String name1 = mo1.getName();
	String name2;
	int destX, destY, destZ, destW;
	if (name1.equals("Two-Way Teleport")) {
	    final TwoWayTeleport twt = (TwoWayTeleport) mo1;
	    destX = twt.getDestinationRow();
	    destY = twt.getDestinationColumn();
	    destZ = twt.getDestinationFloor();
	    destW = twt.getDestinationLevel();
	    final MazeObject mo2 = app.getMazeManager().getMaze().getCell(destX, destY, destZ, destW,
		    Maze.LAYER_OBJECT);
	    name2 = mo2.getName();
	    if (!name2.equals("Two-Way Teleport")) {
		this.pairTwoWayTeleport(destX, destY, destZ, destW);
	    }
	}
    }

    public void pairTwoWayTeleport(final int destX, final int destY, final int destZ, final int destW) {
	final Application app = Import1.getApplication();
	app.getMazeManager().getMaze().setCell(
		new TwoWayTeleport(this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(),
			this.elMgr.getCameFromZ(), this.elMgr.getCameFromW()),
		destX, destY, destZ, destW, Maze.LAYER_OBJECT);
    }

    private static void unpairTwoWayTeleport(final int destX, final int destY, final int destZ, final int destW) {
	final Application app = Import1.getApplication();
	app.getMazeManager().getMaze().setCell(new Empty(), destX, destY, destZ, destW, Maze.LAYER_OBJECT);
    }

    public MazeObject editTeleportDestination(final int type) {
	final Application app = Import1.getApplication();
	String input1 = null, input2 = null;
	this.TELEPORT_TYPE = type;
	int destX = 0, destY = 0;
	switch (type) {
	case TELEPORT_TYPE_GENERIC:
	case TELEPORT_TYPE_INVISIBLE_GENERIC:
	case TELEPORT_TYPE_ONESHOT:
	case TELEPORT_TYPE_INVISIBLE_ONESHOT:
	case TELEPORT_TYPE_TWOWAY:
	    Messager.showMessage("Click to set teleport destination");
	    break;
	case TELEPORT_TYPE_RANDOM:
	case TELEPORT_TYPE_RANDOM_INVISIBLE:
	case TELEPORT_TYPE_RANDOM_ONESHOT:
	case TELEPORT_TYPE_RANDOM_INVISIBLE_ONESHOT:
	    input1 = Messager.showTextInputDialog("Random row range:", "Editor");
	    break;
	default:
	    break;
	}
	if (input1 != null) {
	    switch (type) {
	    case TELEPORT_TYPE_RANDOM:
	    case TELEPORT_TYPE_RANDOM_INVISIBLE:
	    case TELEPORT_TYPE_RANDOM_ONESHOT:
	    case TELEPORT_TYPE_RANDOM_INVISIBLE_ONESHOT:
		input2 = Messager.showTextInputDialog("Random column range:", "Editor");
		break;
	    default:
		break;
	    }
	    if (input2 != null) {
		try {
		    destX = Integer.parseInt(input1);
		    destY = Integer.parseInt(input2);
		} catch (final NumberFormatException nf) {
		    Messager.showDialog("Row and column ranges must be integers.");
		}
		switch (type) {
		case TELEPORT_TYPE_RANDOM:
		    return new RandomTeleport(destX, destY);
		case TELEPORT_TYPE_RANDOM_INVISIBLE:
		    return new RandomInvisibleTeleport(destX, destY);
		case TELEPORT_TYPE_RANDOM_ONESHOT:
		    return new RandomOneShotTeleport(destX, destY);
		case TELEPORT_TYPE_RANDOM_INVISIBLE_ONESHOT:
		    return new RandomInvisibleOneShotTeleport(destX, destY);
		default:
		    break;
		}
	    }
	} else {
	    switch (type) {
	    case TELEPORT_TYPE_GENERIC:
	    case TELEPORT_TYPE_INVISIBLE_GENERIC:
	    case TELEPORT_TYPE_ONESHOT:
	    case TELEPORT_TYPE_INVISIBLE_ONESHOT:
	    case TELEPORT_TYPE_TWOWAY:
		this.horzScroll.removeAdjustmentListener(this.mhandler);
		this.vertScroll.removeAdjustmentListener(this.mhandler);
		this.secondaryPane.removeMouseListener(this.mhandler);
		this.horzScroll.addAdjustmentListener(this.thandler);
		this.vertScroll.addAdjustmentListener(this.thandler);
		this.secondaryPane.addMouseListener(this.thandler);
		this.elMgr.setCameFromZ(this.elMgr.getEditorLocationZ());
		this.elMgr.setCameFromW(this.elMgr.getEditorLocationW());
		app.getMenuManager().disableDownOneLevel();
		app.getMenuManager().disableUpOneLevel();
		break;
	    default:
		break;
	    }
	}
	return null;
    }

    public MazeObject editMetalButtonTarget() {
	Messager.showMessage("Click to set metal button target");
	final Application app = Import1.getApplication();
	this.horzScroll.removeAdjustmentListener(this.mhandler);
	this.vertScroll.removeAdjustmentListener(this.mhandler);
	this.secondaryPane.removeMouseListener(this.mhandler);
	this.horzScroll.addAdjustmentListener(this.mbhandler);
	this.vertScroll.addAdjustmentListener(this.mbhandler);
	this.secondaryPane.addMouseListener(this.mbhandler);
	this.elMgr.setCameFromZ(this.elMgr.getEditorLocationZ());
	this.elMgr.setCameFromW(this.elMgr.getEditorLocationW());
	app.getMenuManager().disableDownOneLevel();
	app.getMenuManager().disableUpOneLevel();
	return null;
    }

    public MazeObject editTreasureChestContents() {
	Messager.showMessage("Pick treasure chest contents");
	this.setDefaultContents();
	this.disableOutput();
	this.treasureFrame.setVisible(true);
	return null;
    }

    private void setDefaultContents() {
	TreasureChest tc = null;
	MazeObject contents = null;
	int contentsIndex = 0;
	final Application app = Import1.getApplication();
	try {
	    tc = (TreasureChest) app.getMazeManager().getMazeObject(this.elMgr.getEditorLocationX(),
		    this.elMgr.getEditorLocationY(), this.elMgr.getCameFromZ(), this.elMgr.getCameFromW(),
		    Maze.LAYER_OBJECT);
	    contents = tc.getInsideObject();
	    for (int x = 0; x < this.containableObjects.length; x++) {
		if (contents.getName().equals(this.containableObjects[x].getName())) {
		    contentsIndex = x;
		    break;
		}
	    }
	} catch (final ClassCastException cce) {
	    // Do nothing
	} catch (final NullPointerException npe) {
	    // Do nothing
	}
	this.treasurePicker.selectLastPickedChoice(contentsIndex);
    }

    public MazeObject editFinishToDestination() {
	String input1 = null;
	int destW = 0;
	input1 = Messager.showTextInputDialog("Destination Level:", "Editor");
	if (input1 != null) {
	    try {
		destW = Integer.parseInt(input1) - 1;
		return new FinishTo(destW);
	    } catch (final NumberFormatException nf) {
		Messager.showDialog("Destination level must be an integer greater than 0.");
	    }
	}
	return null;
    }

    public void setTeleportDestination(final int x, final int y) {
	final Application app = Import1.getApplication();
	final int xOffset = this.vertScroll.getValue() - this.vertScroll.getMinimum();
	final int yOffset = this.horzScroll.getValue() - this.horzScroll.getMinimum();
	final int destX = x / GraphicsManager.getGraphicSize() + this.evMgr.getViewingWindowLocationX() - xOffset
		+ yOffset;
	final int destY = y / GraphicsManager.getGraphicSize() + this.evMgr.getViewingWindowLocationY() + xOffset
		- yOffset;
	final int destZ = this.elMgr.getEditorLocationZ();
	final int destW = this.elMgr.getEditorLocationW();
	try {
	    app.getMazeManager().getMaze().getCell(destX, destY, destZ, destW, Maze.LAYER_OBJECT);
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    this.horzScroll.removeAdjustmentListener(this.thandler);
	    this.vertScroll.removeAdjustmentListener(this.thandler);
	    this.secondaryPane.removeMouseListener(this.thandler);
	    this.horzScroll.addAdjustmentListener(this.mhandler);
	    this.vertScroll.addAdjustmentListener(this.mhandler);
	    this.secondaryPane.addMouseListener(this.mhandler);
	    return;
	}
	switch (this.TELEPORT_TYPE) {
	case TELEPORT_TYPE_GENERIC:
	    app.getMazeManager().getMaze().setCell(new Teleport(destX, destY, destZ, destW),
		    this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(), this.elMgr.getCameFromZ(),
		    this.elMgr.getCameFromW(), Maze.LAYER_OBJECT);
	    break;
	case TELEPORT_TYPE_INVISIBLE_GENERIC:
	    app.getMazeManager().getMaze().setCell(new InvisibleTeleport(destX, destY, destZ, destW),
		    this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(), this.elMgr.getCameFromZ(),
		    this.elMgr.getCameFromW(), Maze.LAYER_OBJECT);
	    break;
	case TELEPORT_TYPE_ONESHOT:
	    app.getMazeManager().getMaze().setCell(new OneShotTeleport(destX, destY, destZ, destW),
		    this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(), this.elMgr.getCameFromZ(),
		    this.elMgr.getCameFromW(), Maze.LAYER_OBJECT);
	    break;
	case TELEPORT_TYPE_INVISIBLE_ONESHOT:
	    app.getMazeManager().getMaze().setCell(new InvisibleOneShotTeleport(destX, destY, destZ, destW),
		    this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(), this.elMgr.getCameFromZ(),
		    this.elMgr.getCameFromW(), Maze.LAYER_OBJECT);
	    break;
	case TELEPORT_TYPE_TWOWAY:
	    app.getMazeManager().getMaze().setCell(new TwoWayTeleport(destX, destY, destZ, destW),
		    this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(), this.elMgr.getCameFromZ(),
		    this.elMgr.getCameFromW(), Maze.LAYER_OBJECT);
	    this.pairTwoWayTeleport(destX, destY, destZ, destW);
	    break;
	default:
	    break;
	}
	this.horzScroll.removeAdjustmentListener(this.thandler);
	this.vertScroll.removeAdjustmentListener(this.thandler);
	this.secondaryPane.removeMouseListener(this.thandler);
	this.horzScroll.addAdjustmentListener(this.mhandler);
	this.vertScroll.addAdjustmentListener(this.mhandler);
	this.secondaryPane.addMouseListener(this.mhandler);
	this.checkMenus();
	Messager.showMessage("Destination set.");
	app.getMazeManager().setDirty(true);
	this.redrawEditor();
    }

    public void setMetalButtonTarget(final int x, final int y) {
	final Application app = Import1.getApplication();
	final int xOffset = this.vertScroll.getValue() - this.vertScroll.getMinimum();
	final int yOffset = this.horzScroll.getValue() - this.horzScroll.getMinimum();
	final int destX = x / GraphicsManager.getGraphicSize() + this.evMgr.getViewingWindowLocationX() - xOffset
		+ yOffset;
	final int destY = y / GraphicsManager.getGraphicSize() + this.evMgr.getViewingWindowLocationY() + xOffset
		- yOffset;
	final int destZ = this.elMgr.getEditorLocationZ();
	final int destW = this.elMgr.getEditorLocationW();
	try {
	    app.getMazeManager().getMaze().getCell(destX, destY, destZ, destW, Maze.LAYER_OBJECT);
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    this.horzScroll.removeAdjustmentListener(this.mbhandler);
	    this.vertScroll.removeAdjustmentListener(this.mbhandler);
	    this.secondaryPane.removeMouseListener(this.mbhandler);
	    this.horzScroll.addAdjustmentListener(this.mhandler);
	    this.vertScroll.addAdjustmentListener(this.mhandler);
	    this.secondaryPane.addMouseListener(this.mhandler);
	    return;
	}
	app.getMazeManager().getMaze().setCell(new MetalButton(destX, destY, destZ, destW),
		this.elMgr.getEditorLocationX(), this.elMgr.getEditorLocationY(), this.elMgr.getCameFromZ(),
		this.elMgr.getCameFromW(), Maze.LAYER_OBJECT);
	this.horzScroll.removeAdjustmentListener(this.mbhandler);
	this.vertScroll.removeAdjustmentListener(this.mbhandler);
	this.secondaryPane.removeMouseListener(this.mbhandler);
	this.horzScroll.addAdjustmentListener(this.mhandler);
	this.vertScroll.addAdjustmentListener(this.mhandler);
	this.secondaryPane.addMouseListener(this.mhandler);
	this.checkMenus();
	Messager.showMessage("Target set.");
	app.getMazeManager().setDirty(true);
	this.redrawEditor();
    }

    public void setTreasureChestContents() {
	this.enableOutput();
	final Application app = Import1.getApplication();
	final MazeObject contents = this.containableObjects[this.treasurePicker.getPicked()];
	app.getMazeManager().getMaze().setCell(new TreasureChest(contents), this.elMgr.getEditorLocationX(),
		this.elMgr.getEditorLocationY(), this.elMgr.getCameFromZ(), this.elMgr.getCameFromW(),
		Maze.LAYER_OBJECT);
	this.checkMenus();
	Messager.showMessage("Contents set.");
	app.getMazeManager().setDirty(true);
	this.redrawEditor();
    }

    public void setPlayerLocation() {
	final Application app = Import1.getApplication();
	app.getGameManager().getPlayerManager().setPlayerLocation(this.elMgr.getEditorLocationX(),
		this.elMgr.getEditorLocationY(), this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW());
	app.getMazeManager().getMaze().setStartRow(this.elMgr.getEditorLocationW(), this.elMgr.getEditorLocationY());
	app.getMazeManager().getMaze().setStartColumn(this.elMgr.getEditorLocationW(), this.elMgr.getEditorLocationX());
	app.getMazeManager().getMaze().setStartFloor(this.elMgr.getEditorLocationW(), this.elMgr.getEditorLocationZ());
    }

    public void editMaze() {
	final Application app = Import1.getApplication();
	if (app.getMazeManager().getLoaded()) {
	    app.getGUIManager().hideGUI();
	    app.setInGame(false);
	    // Reset game state
	    app.getGameManager().resetGameState();
	    // Create the managers
	    if (this.mazeChanged) {
		this.elMgr = new EditorLocationManager();
		this.evMgr = new EditorViewingWindowManager();
		final int startW = app.getMazeManager().getMaze().getStartLevel();
		final int mazeSizeX = app.getMazeManager().getMaze().getRows(startW);
		final int mazeSizeY = app.getMazeManager().getMaze().getColumns(startW);
		app.getEditor().getViewManager().halfOffsetMaximumViewingWindowLocation(mazeSizeX, mazeSizeY);
		this.mazeChanged = false;
	    }
	    // Reset the level
	    this.elMgr.setEditorLocationZ(0);
	    this.elMgr.setEditorLocationW(0);
	    this.elMgr.setLimitsFromMaze(app.getMazeManager().getMaze());
	    this.evMgr.halfOffsetMaximumViewingWindowLocationsFromMaze(app.getMazeManager().getMaze());
	    this.setUpGUI();
	    this.clearHistory();
	    this.checkMenus();
	    // Make sure message area is attached to border pane
	    this.borderPane.removeAll();
	    this.borderPane.add(app.getGameManager().getMessageLabel(), BorderLayout.SOUTH);
	    this.borderPane.add(this.outputPane, BorderLayout.CENTER);
	    this.borderPane.add(this.picker.getPicker(), BorderLayout.EAST);
	    this.redrawEditor();
	} else {
	    Messager.showDialog("No Maze Opened");
	}
    }

    public boolean newMaze() {
	final Application app = Import1.getApplication();
	boolean success = true;
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
	    app.getGameManager().getPlayerManager().resetPlayerLocation();
	    app.getMazeManager().setMaze(new Maze5());
	    success = this.addLevelInternal(true);
	    if (success) {
		app.getMazeManager().clearLastUsedFilenames();
		this.clearHistory();
		app.getGameManager().invalidateScore();
	    }
	} else {
	    success = false;
	}
	if (success) {
	    this.mazeChanged = true;
	}
	return success;
    }

    public void fixLimits() {
	// Fix limits
	final Application app = Import1.getApplication();
	if (app.getMazeManager().getMaze() != null && this.elMgr != null && this.evMgr != null) {
	    this.elMgr.setLimitsFromMaze(app.getMazeManager().getMaze());
	    this.evMgr.halfOffsetMaximumViewingWindowLocationsFromMaze(app.getMazeManager().getMaze());
	}
    }

    public boolean addLevel() {
	return this.addLevelInternal(false);
    }

    private boolean addLevelInternal(final boolean flag) {
	final Application app = Import1.getApplication();
	int levelSizeX, levelSizeY, levelSizeZ;
	final int absoluteRCLimit = 64;
	final int absoluteFLimit = 8;
	String msg = null;
	if (flag) {
	    msg = "New Maze";
	} else {
	    msg = "New Level";
	}
	boolean success = true;
	String input1, input2, input3;
	input1 = Messager.showTextInputDialog("Number of rows?", msg);
	if (input1 != null) {
	    input2 = Messager.showTextInputDialog("Number of columns?", msg);
	    if (input2 != null) {
		input3 = Messager.showTextInputDialog("Number of floors?", msg);
		if (input3 != null) {
		    try {
			levelSizeX = Integer.parseInt(input1);
			levelSizeY = Integer.parseInt(input2);
			levelSizeZ = Integer.parseInt(input3);
			if (levelSizeX < 2 || levelSizeY < 2) {
			    throw new NumberFormatException("Rows and columns must be at least 2.");
			}
			if (levelSizeX > absoluteRCLimit || levelSizeY > absoluteRCLimit) {
			    throw new NumberFormatException(
				    "Rows and columns must be less than or equal to " + absoluteRCLimit + ".");
			}
			if (levelSizeZ < 1) {
			    throw new NumberFormatException("Floors must be at least 1.");
			}
			if (levelSizeZ > absoluteFLimit) {
			    throw new NumberFormatException(
				    "Floors must be less than or equal to " + absoluteFLimit + ".");
			}
			success = app.getMazeManager().getMaze().addLevel(levelSizeX, levelSizeY, levelSizeZ);
			if (success) {
			    this.fixLimits();
			    final int levelCount = app.getMazeManager().getMaze().getLevels();
			    if (!flag) {
				this.evMgr.setViewingWindowLocationX(0 - (this.evMgr.getViewingWindowSizeX() - 1) / 2);
				this.evMgr.setViewingWindowLocationY(0 - (this.evMgr.getViewingWindowSizeY() - 1) / 2);
			    }
			    app.getMazeManager().getMaze().fillLevel(levelCount - 1,
				    app.getPrefsManager().getEditorDefaultFill(), new Empty());
			    // Save the entire level
			    app.getMazeManager().getMaze().saveLevel(levelCount - 1);
			    this.checkMenus();
			}
		    } catch (final NumberFormatException nf) {
			Messager.showDialog(nf.getMessage());
			success = false;
		    }
		} else {
		    // User cancelled
		    success = false;
		}
	    } else {
		// User cancelled
		success = false;
	    }
	} else {
	    // User cancelled
	    success = false;
	}
	return success;
    }

    public boolean resizeLevel() {
	final Application app = Import1.getApplication();
	int levelSizeX, levelSizeY, levelSizeZ;
	final int absoluteRCLimit = 64;
	final int absoluteFLimit = 8;
	final String msg = "Resize Level";
	boolean success = true;
	String input1, input2, input3;
	input1 = Messager.showTextInputDialogWithDefault("Number of rows?", msg,
		Integer.toString(app.getMazeManager().getMaze().getRows()));
	if (input1 != null) {
	    input2 = Messager.showTextInputDialogWithDefault("Number of columns?", msg,
		    Integer.toString(app.getMazeManager().getMaze().getColumns()));
	    if (input2 != null) {
		input3 = Messager.showTextInputDialogWithDefault("Number of floors?", msg,
			Integer.toString(app.getMazeManager().getMaze().getFloors()));
		if (input3 != null) {
		    try {
			levelSizeX = Integer.parseInt(input1);
			levelSizeY = Integer.parseInt(input2);
			levelSizeZ = Integer.parseInt(input3);
			if (levelSizeX < 2 || levelSizeY < 2) {
			    throw new NumberFormatException("Rows and columns must be at least 2.");
			}
			if (levelSizeX > absoluteRCLimit || levelSizeY > absoluteRCLimit) {
			    throw new NumberFormatException(
				    "Rows and columns must be less than or equal to " + absoluteRCLimit + ".");
			}
			if (levelSizeZ < 1) {
			    throw new NumberFormatException("Floors must be at least 1.");
			}
			if (levelSizeZ > absoluteFLimit) {
			    throw new NumberFormatException(
				    "Floors must be less than or equal to " + absoluteFLimit + ".");
			}
			app.getMazeManager().getMaze().resize(levelSizeX, levelSizeY, levelSizeZ,
				this.elMgr.getEditorLocationW());
			this.fixLimits();
			this.evMgr.setViewingWindowLocationX(0 - (this.evMgr.getViewingWindowSizeX() - 1) / 2);
			this.evMgr.setViewingWindowLocationY(0 - (this.evMgr.getViewingWindowSizeY() - 1) / 2);
			// Save the entire level
			app.getMazeManager().getMaze().saveLevel(this.elMgr.getEditorLocationW());
			this.checkMenus();
			// Redraw
			this.redrawEditor();
		    } catch (final NumberFormatException nf) {
			Messager.showDialog(nf.getMessage());
			success = false;
		    }
		} else {
		    // User cancelled
		    success = false;
		}
	    } else {
		// User cancelled
		success = false;
	    }
	} else {
	    // User cancelled
	    success = false;
	}
	return success;
    }

    public boolean removeLevel() {
	final Application app = Import1.getApplication();
	int level;
	boolean success = true;
	String input;
	input = Messager.showTextInputDialog("Level Number (1-" + app.getMazeManager().getMaze().getLevels() + ")?",
		"Remove Level");
	if (input != null) {
	    try {
		level = Integer.parseInt(input);
		if (level < 1 || level > app.getMazeManager().getMaze().getLevels()) {
		    throw new NumberFormatException("Level number must be in the range 1 to "
			    + app.getMazeManager().getMaze().getLevels() + ".");
		}
		success = app.getMazeManager().getMaze().removeLevel(level);
		if (success) {
		    this.fixLimits();
		    if (level == this.elMgr.getEditorLocationW() + 1) {
			// Deleted current level - go to level 1
			this.updateEditorLevelAbsolute(0);
		    }
		    this.checkMenus();
		}
	    } catch (final NumberFormatException nf) {
		Messager.showDialog(nf.getMessage());
		success = false;
	    }
	} else {
	    // User cancelled
	    success = false;
	}
	return success;
    }

    public void goToHandler() {
	int locX, locY, locZ, locW;
	final String msg = "Go To...";
	String input1, input2, input3, input4;
	input1 = Messager.showTextInputDialog("Row?", msg);
	if (input1 != null) {
	    input2 = Messager.showTextInputDialog("Column?", msg);
	    if (input2 != null) {
		input3 = Messager.showTextInputDialog("Floor?", msg);
		if (input3 != null) {
		    input4 = Messager.showTextInputDialog("Level?", msg);
		    if (input4 != null) {
			try {
			    locX = Integer.parseInt(input1) - 1;
			    locY = Integer.parseInt(input2) - 1;
			    locZ = Integer.parseInt(input3) - 1;
			    locW = Integer.parseInt(input4) - 1;
			    this.updateEditorPosition(locX, locY, locZ, locW);
			} catch (final NumberFormatException nf) {
			    Messager.showDialog(nf.getMessage());
			}
		    }
		}
	    }
	}
    }

    public boolean isOutputVisible() {
	if (this.outputFrame == null) {
	    return false;
	} else {
	    return this.outputFrame.isVisible();
	}
    }

    public void showOutput() {
	final Application app = Import1.getApplication();
	this.outputFrame.setJMenuBar(app.getMenuManager().getMainMenuBar());
	app.getMenuManager().setEditorMenus();
	this.outputFrame.setVisible(true);
    }

    public void hideOutput() {
	if (this.outputFrame != null) {
	    this.outputFrame.setVisible(false);
	}
    }

    void disableOutput() {
	this.outputFrame.setEnabled(false);
    }

    void enableOutput() {
	this.outputFrame.setEnabled(true);
	this.checkMenus();
    }

    public JFrame getOutputFrame() {
	if (this.outputFrame != null && this.outputFrame.isVisible()) {
	    return this.outputFrame;
	} else {
	    return null;
	}
    }

    public void exitEditor() {
	final Application app = Import1.getApplication();
	// Hide the editor
	this.hideOutput();
	final MazeManager mm = app.getMazeManager();
	final GameManager gm = app.getGameManager();
	// Save the entire level
	mm.getMaze().save();
	// Reset the viewing window
	gm.resetViewingWindowAndPlayerLocation();
	Import1.getApplication().getGUIManager().showGUI();
    }

    private void setUpGUI() {
	// Destroy the old GUI, if one exists
	if (this.outputFrame != null) {
	    this.outputFrame.dispose();
	}
	final Application app = Import1.getApplication();
	this.outputFrame = new JFrame("Editor");
	this.outputPane = new Container();
	this.secondaryPane = new Container();
	this.borderPane = new Container();
	this.borderPane.setLayout(new BorderLayout());
	this.outputFrame.setContentPane(this.borderPane);
	this.outputFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.drawGrid = new JLabel[this.evMgr.getViewingWindowSizeX()][this.evMgr.getViewingWindowSizeY()];
	for (int x = 0; x < this.evMgr.getViewingWindowSizeX(); x++) {
	    for (int y = 0; y < this.evMgr.getViewingWindowSizeY(); y++) {
		this.drawGrid[x][y] = new JLabel();
		// Mac OS X-specific fix to make draw grid line up properly
		if (System.getProperty("os.name").startsWith("Mac OS X")) {
		    this.drawGrid[x][y].setBorder(new EmptyBorder(0, 0, 0, 0));
		}
		this.secondaryPane.add(this.drawGrid[x][y]);
	    }
	}
	this.borderPane.add(this.outputPane, BorderLayout.CENTER);
	this.borderPane.add(app.getGameManager().getMessageLabel(), BorderLayout.SOUTH);
	this.gridbag = new GridBagLayout();
	this.c = new GridBagConstraints();
	this.outputPane.setLayout(this.gridbag);
	this.outputFrame.setResizable(false);
	this.c.fill = GridBagConstraints.BOTH;
	this.secondaryPane
		.setLayout(new GridLayout(this.evMgr.getViewingWindowSizeX(), this.evMgr.getViewingWindowSizeY()));
	this.horzScroll = new JScrollBar(Adjustable.HORIZONTAL, this.evMgr.getMinimumViewingWindowLocationY(),
		this.evMgr.getViewingWindowSizeY(), this.evMgr.getMinimumViewingWindowLocationY(),
		this.evMgr.getMaximumViewingWindowLocationY());
	this.vertScroll = new JScrollBar(Adjustable.VERTICAL, this.evMgr.getMinimumViewingWindowLocationX(),
		this.evMgr.getViewingWindowSizeX(), this.evMgr.getMinimumViewingWindowLocationX(),
		this.evMgr.getMaximumViewingWindowLocationX());
	this.c.gridx = 0;
	this.c.gridy = 0;
	this.gridbag.setConstraints(this.secondaryPane, this.c);
	this.outputPane.add(this.secondaryPane);
	this.c.gridx = 1;
	this.c.gridy = 0;
	this.c.gridwidth = GridBagConstraints.REMAINDER;
	this.gridbag.setConstraints(this.vertScroll, this.c);
	this.outputPane.add(this.vertScroll);
	this.c.gridx = 0;
	this.c.gridy = 1;
	this.c.gridwidth = 1;
	this.c.gridheight = GridBagConstraints.REMAINDER;
	this.gridbag.setConstraints(this.horzScroll, this.c);
	this.outputPane.add(this.horzScroll);
	this.horzScroll.addAdjustmentListener(this.mhandler);
	this.vertScroll.addAdjustmentListener(this.mhandler);
	this.secondaryPane.addMouseListener(this.mhandler);
	this.outputFrame.addWindowListener(this.mhandler);
	this.updatePicker();
	this.borderPane.add(this.picker.getPicker(), BorderLayout.EAST);
    }

    public void undo() {
	final Application app = Import1.getApplication();
	this.engine.undo();
	final MazeObject obj = this.engine.getObject();
	final int x = this.engine.getX();
	final int y = this.engine.getY();
	final int z = this.engine.getZ();
	final int w = this.engine.getW();
	final int e = this.engine.getE();
	this.elMgr.setEditorLocationX(x);
	this.elMgr.setEditorLocationY(y);
	this.elMgr.setCameFromZ(z);
	this.elMgr.setCameFromW(w);
	if (x != -1 && y != -1 && z != -1 && w != -1) {
	    final MazeObject oldObj = app.getMazeManager().getMazeObject(x, y, z, w, e);
	    if (!obj.getName().equals(new StairsUp().getName()) && !obj.getName().equals(new StairsDown().getName())) {
		if (obj.getName().equals(new TwoWayTeleport().getName())) {
		    app.getMazeManager().getMaze().setCell(obj, x, y, z, w, e);
		    this.reverseCheckTwoWayTeleportPair(z, w);
		    this.checkStairPair(z, w);
		} else {
		    this.checkTwoWayTeleportPair(z, w);
		    app.getMazeManager().getMaze().setCell(obj, x, y, z, w, e);
		    this.checkStairPair(z, w);
		}
	    } else {
		app.getMazeManager().getMaze().setCell(obj, x, y, z, w, e);
		this.reverseCheckStairPair(z, w);
	    }
	    this.updateRedoHistory(oldObj, x, y, z, w, e);
	    this.checkMenus();
	    this.redrawEditor();
	} else {
	    Messager.showMessage("Nothing to undo");
	}
    }

    public void redo() {
	final Application app = Import1.getApplication();
	this.engine.redo();
	final MazeObject obj = this.engine.getObject();
	final int x = this.engine.getX();
	final int y = this.engine.getY();
	final int z = this.engine.getZ();
	final int w = this.engine.getW();
	final int e = this.engine.getE();
	this.elMgr.setEditorLocationX(x);
	this.elMgr.setEditorLocationY(y);
	this.elMgr.setCameFromZ(z);
	this.elMgr.setCameFromW(w);
	if (x != -1 && y != -1 && z != -1 && w != -1) {
	    final MazeObject oldObj = app.getMazeManager().getMazeObject(x, y, z, w, e);
	    if (!obj.getName().equals(new StairsUp().getName()) && !obj.getName().equals(new StairsDown().getName())) {
		if (obj.getName().equals(new TwoWayTeleport().getName())) {
		    app.getMazeManager().getMaze().setCell(obj, x, y, z, w, e);
		    this.reverseCheckTwoWayTeleportPair(z, w);
		    this.checkStairPair(z, w);
		} else {
		    this.checkTwoWayTeleportPair(z, w);
		    app.getMazeManager().getMaze().setCell(obj, x, y, z, w, e);
		    this.checkStairPair(z, w);
		}
	    } else {
		app.getMazeManager().getMaze().setCell(obj, x, y, z, w, e);
		this.reverseCheckStairPair(z, w);
	    }
	    this.updateUndoHistory(oldObj, x, y, z, w, e);
	    this.checkMenus();
	    this.redrawEditor();
	} else {
	    Messager.showMessage("Nothing to redo");
	}
    }

    public void clearHistory() {
	this.engine = new UndoRedoEngine();
	this.checkMenus();
    }

    private void updateUndoHistory(final MazeObject obj, final int x, final int y, final int z, final int w,
	    final int e) {
	this.engine.updateUndoHistory(obj, x, y, z, w, e);
    }

    private void updateRedoHistory(final MazeObject obj, final int x, final int y, final int z, final int w,
	    final int e) {
	this.engine.updateRedoHistory(obj, x, y, z, w, e);
    }

    public void updatePicker() {
	BufferedImageIcon[] newImages = null;
	String[] newNames = null;
	if (this.elMgr.getEditorLocationE() == Maze.LAYER_GROUND) {
	    newImages = this.groundEditorAppearances;
	    newNames = this.groundNames;
	} else {
	    newImages = this.objectEditorAppearances;
	    newNames = this.objectNames;
	}
	if (this.picker != null) {
	    this.picker.updatePicker(newImages, newNames);
	} else {
	    this.picker = new PicturePicker(newImages, newNames);
	}
    }

    public void handleCloseWindow() {
	try {
	    final Application app = Import1.getApplication();
	    boolean success = false;
	    int status = JOptionPane.DEFAULT_OPTION;
	    if (app.getMazeManager().getDirty()) {
		status = app.getMazeManager().showSaveDialog();
		if (status == JOptionPane.YES_OPTION) {
		    success = app.getMazeManager().saveMaze();
		    if (success) {
			this.exitEditor();
		    }
		} else if (status == JOptionPane.NO_OPTION) {
		    app.getMazeManager().setDirty(false);
		    this.exitEditor();
		}
	    } else {
		this.exitEditor();
	    }
	} catch (final Exception ex) {
	    Import1.debug(ex);
	}
    }

    private class EventHandler implements AdjustmentListener, MouseListener, WindowListener {
	public EventHandler() {
	    // TODO Auto-generated constructor stub
	}

	// handle scrollbars
	@Override
	public void adjustmentValueChanged(final AdjustmentEvent e) {
	    try {
		final MazeEditor me = MazeEditor.this;
		final Adjustable src = e.getAdjustable();
		final int dir = src.getOrientation();
		final int value = src.getValue();
		int relValue = 0;
		switch (dir) {
		case Adjustable.HORIZONTAL:
		    relValue = value - me.evMgr.getViewingWindowLocationY();
		    me.updateEditorPosition(0, relValue, 0, 0);
		    break;
		case Adjustable.VERTICAL:
		    relValue = value - me.evMgr.getViewingWindowLocationX();
		    me.updateEditorPosition(relValue, 0, 0, 0);
		    break;
		default:
		    break;
		}
	    } catch (final Exception ex) {
		Import1.debug(ex);
	    }
	}

	// handle mouse
	@Override
	public void mousePressed(final MouseEvent e) {
	    // Do nothing
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
	    // Do nothing
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	    try {
		final MazeEditor me = MazeEditor.this;
		final int x = e.getX();
		final int y = e.getY();
		if (e.isAltDown()) {
		    me.editObjectProperties(x, y);
		} else if (e.isShiftDown()) {
		    me.probeObjectProperties(x, y);
		} else {
		    me.editObject(x, y);
		}
	    } catch (final Exception ex) {
		Import1.debug(ex);
	    }
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	    // Do nothing
	}

	@Override
	public void mouseExited(final MouseEvent e) {
	    // Do nothing
	}

	// Handle windows
	@Override
	public void windowActivated(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowClosed(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowClosing(final WindowEvent we) {
	    MazeEditor.this.handleCloseWindow();
	}

	@Override
	public void windowDeactivated(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowDeiconified(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowIconified(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowOpened(final WindowEvent we) {
	    // Do nothing
	}
    }

    private class TreasureEventHandler implements WindowListener {
	public TreasureEventHandler() {
	    // TODO Auto-generated constructor stub
	}

	// Handle windows
	@Override
	public void windowActivated(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowClosed(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowClosing(final WindowEvent we) {
	    MazeEditor.this.setTreasureChestContents();
	}

	@Override
	public void windowDeactivated(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowDeiconified(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowIconified(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowOpened(final WindowEvent we) {
	    // Do nothing
	}
    }

    private class TeleportEventHandler implements AdjustmentListener, MouseListener {
	public TeleportEventHandler() {
	    // TODO Auto-generated constructor stub
	}

	// handle scroll bars
	@Override
	public void adjustmentValueChanged(final AdjustmentEvent e) {
	    try {
		final MazeEditor me = MazeEditor.this;
		final Adjustable src = e.getAdjustable();
		final int dir = src.getOrientation();
		final int value = src.getValue();
		int relValue = 0;
		switch (dir) {
		case Adjustable.HORIZONTAL:
		    relValue = value - me.evMgr.getViewingWindowLocationY();
		    me.updateEditorPosition(0, relValue, 0, 0);
		    break;
		case Adjustable.VERTICAL:
		    relValue = value - me.evMgr.getViewingWindowLocationX();
		    me.updateEditorPosition(relValue, 0, 0, 0);
		    break;
		default:
		    break;
		}
	    } catch (final Exception ex) {
		Import1.debug(ex);
	    }
	}

	// handle mouse
	@Override
	public void mousePressed(final MouseEvent e) {
	    // Do nothing
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
	    // Do nothing
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	    try {
		final int x = e.getX();
		final int y = e.getY();
		MazeEditor.this.setTeleportDestination(x, y);
	    } catch (final Exception ex) {
		Import1.debug(ex);
	    }
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	    // Do nothing
	}

	@Override
	public void mouseExited(final MouseEvent e) {
	    // Do nothing
	}
    }

    private class MetalButtonEventHandler implements AdjustmentListener, MouseListener {
	public MetalButtonEventHandler() {
	    // TODO Auto-generated constructor stub
	}

	// handle scroll bars
	@Override
	public void adjustmentValueChanged(final AdjustmentEvent e) {
	    try {
		final MazeEditor me = MazeEditor.this;
		final Adjustable src = e.getAdjustable();
		final int dir = src.getOrientation();
		final int value = src.getValue();
		int relValue = 0;
		switch (dir) {
		case Adjustable.HORIZONTAL:
		    relValue = value - me.evMgr.getViewingWindowLocationY();
		    me.updateEditorPosition(0, relValue, 0, 0);
		    break;
		case Adjustable.VERTICAL:
		    relValue = value - me.evMgr.getViewingWindowLocationX();
		    me.updateEditorPosition(relValue, 0, 0, 0);
		    break;
		default:
		    break;
		}
	    } catch (final Exception ex) {
		Import1.debug(ex);
	    }
	}

	// handle mouse
	@Override
	public void mousePressed(final MouseEvent e) {
	    // Do nothing
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
	    // Do nothing
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	    try {
		final int x = e.getX();
		final int y = e.getY();
		MazeEditor.this.setMetalButtonTarget(x, y);
	    } catch (final Exception ex) {
		Import1.debug(ex);
	    }
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	    // Do nothing
	}

	@Override
	public void mouseExited(final MouseEvent e) {
	    // Do nothing
	}
    }
}
