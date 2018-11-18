/* Import2: An RPG */
package studio.ignitionigloogames.twistedtrek.import2.maze;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import studio.ignitionigloogames.commondialogs.CommonDialogs;
import studio.ignitionigloogames.ioutils.FilenameChecker;
import studio.ignitionigloogames.twistedtrek.import2.Application;
import studio.ignitionigloogames.twistedtrek.import2.Import2;
import studio.ignitionigloogames.twistedtrek.import2.maze.abc.AbstractMazeObject;
import studio.ignitionigloogames.twistedtrek.import2.maze.games.GameFinder;
import studio.ignitionigloogames.twistedtrek.import2.maze.games.GameLoadTask;
import studio.ignitionigloogames.twistedtrek.import2.maze.games.GameSaveTask;

public final class MazeManager {
    // Fields
    private Maze gameMaze;
    private boolean loaded, isDirty;
    private static final String MAC_PREFIX = "HOME";
    private static final String WIN_PREFIX = "APPDATA";
    private static final String UNIX_PREFIX = "HOME";
    private static final String MAC_DIR = "/Library/Application Support/Putty Software/Import2/Games/";
    private static final String WIN_DIR = "\\Putty Software\\Import2\\Games\\";
    private static final String UNIX_DIR = "/.puttysoftware/tallertower/games/";

    // Constructors
    public MazeManager() {
	this.loaded = false;
	this.isDirty = false;
	this.gameMaze = null;
    }

    // Methods
    public Maze getMaze() {
	return this.gameMaze;
    }

    public void setMaze(final Maze newMaze) {
	this.gameMaze = newMaze;
    }

    public void handleDeferredSuccess(final boolean value, final boolean versionError, final File triedToLoad) {
	if (value) {
	    this.setLoaded(true);
	}
	if (versionError) {
	    triedToLoad.delete();
	}
	this.setDirty(false);
	Import2.getApplication().getGameManager().stateChanged();
	Import2.getApplication().getMenuManager().checkFlags();
    }

    public AbstractMazeObject getMazeObject(final int x, final int y, final int z, final int e) {
	try {
	    return this.gameMaze.getCell(x, y, z, e);
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    return null;
	}
    }

    public static int showSaveDialog() {
	String type, source;
	final Application app = Import2.getApplication();
	final int mode = app.getMode();
	if (mode == Application.STATUS_GAME) {
	    type = "game";
	    source = "Import2";
	} else {
	    // Not in the game or editor, so abort
	    return JOptionPane.NO_OPTION;
	}
	return CommonDialogs.showYNCConfirmDialog("Do you want to save your " + type + "?", source);
    }

    public boolean getLoaded() {
	return this.loaded;
    }

    public void setLoaded(final boolean status) {
	final Application app = Import2.getApplication();
	this.loaded = status;
	app.getMenuManager().checkFlags();
    }

    public boolean getDirty() {
	return this.isDirty;
    }

    public void setDirty(final boolean newDirty) {
	final Application app = Import2.getApplication();
	this.isDirty = newDirty;
	final JFrame frame = app.getOutputFrame();
	if (frame != null) {
	    frame.getRootPane().putClientProperty("Window.documentModified", Boolean.valueOf(newDirty));
	}
	app.getMenuManager().checkFlags();
    }

    public boolean loadGame() {
	int status = 0;
	boolean saved = true;
	String filename;
	final GameFinder gf = new GameFinder();
	if (this.getDirty()) {
	    status = MazeManager.showSaveDialog();
	    if (status == JOptionPane.YES_OPTION) {
		saved = MazeManager.saveGame();
	    } else if (status == JOptionPane.CANCEL_OPTION) {
		saved = false;
	    } else {
		this.setDirty(false);
	    }
	}
	if (saved) {
	    final String gameDir = MazeManager.getGameDirectory();
	    final String[] rawChoices = new File(gameDir).list(gf);
	    if (rawChoices != null) {
		final String[] choices = new String[rawChoices.length];
		// Strip extension
		for (int x = 0; x < choices.length; x++) {
		    choices[x] = MazeManager.getNameWithoutExtension(rawChoices[x]);
		}
		final String returnVal = CommonDialogs.showInputDialog("Select a Game", "Load Game", choices,
			choices[0]);
		if (returnVal != null) {
		    int index = -1;
		    for (int x = 0; x < choices.length; x++) {
			if (returnVal.equals(choices[x])) {
			    index = x;
			    break;
			}
		    }
		    if (index != -1) {
			final File file = new File(gameDir + rawChoices[index]);
			filename = file.getAbsolutePath();
			MazeManager.loadFile(filename);
		    } else {
			// Result not found
			if (this.loaded) {
			    return true;
			}
		    }
		} else {
		    // User cancelled
		    if (this.loaded) {
			return true;
		    }
		}
	    } else {
		CommonDialogs.showErrorDialog("No Games Found!", "Load Game");
		if (this.loaded) {
		    return true;
		}
	    }
	}
	return false;
    }

    private static void loadFile(final String filename) {
	if (!FilenameChecker.isFilenameOK(MazeManager.getNameWithoutExtension(MazeManager.getFileNameOnly(filename)))) {
	    CommonDialogs.showErrorDialog("The file you selected contains illegal characters in its\n"
		    + "name. These characters are not allowed: /?<>\\:|\"\n"
		    + "Files named con, nul, or prn are illegal, as are files\n"
		    + "named com1 through com9 and lpt1 through lpt9.", "Load");
	} else {
	    final GameLoadTask llt = new GameLoadTask(filename);
	    llt.start();
	}
    }

    public static boolean saveGame() {
	String filename = "";
	String extension;
	String returnVal = "\\";
	while (!FilenameChecker.isFilenameOK(returnVal)) {
	    returnVal = CommonDialogs.showTextInputDialog("Name?", "Save Game");
	    if (returnVal != null) {
		extension = Extension.getGameExtensionWithPeriod();
		final File file = new File(MazeManager.getGameDirectory() + returnVal + extension);
		filename = file.getAbsolutePath();
		if (!FilenameChecker.isFilenameOK(returnVal)) {
		    CommonDialogs.showErrorDialog("The file name you entered contains illegal characters.\n"
			    + "These characters are not allowed: /?<>\\:|\"\n"
			    + "Files named con, nul, or prn are illegal, as are files\n"
			    + "named com1 through com9 and lpt1 through lpt9.", "Save Game");
		} else {
		    // Make sure folder exists
		    if (!file.getParentFile().exists()) {
			final boolean okay = file.getParentFile().mkdirs();
			if (!okay) {
			    Import2.getErrorLogger().logError(new IOException("Cannot create game folder!"));
			}
		    }
		    MazeManager.saveFile(filename);
		}
	    } else {
		break;
	    }
	}
	return false;
    }

    private static void saveFile(final String filename) {
	final String sg = "Saved Game";
	Import2.getApplication().showMessage("Saving " + sg + " file...");
	final GameSaveTask lst = new GameSaveTask(filename);
	lst.start();
    }

    private static String getGameDirectoryPrefix() {
	final String osName = System.getProperty("os.name");
	if (osName.indexOf("Mac OS X") != -1) {
	    // Mac OS X
	    return System.getenv(MazeManager.MAC_PREFIX);
	} else if (osName.indexOf("Windows") != -1) {
	    // Windows
	    return System.getenv(MazeManager.WIN_PREFIX);
	} else {
	    // Other - assume UNIX-like
	    return System.getenv(MazeManager.UNIX_PREFIX);
	}
    }

    private static String getGameDirectoryName() {
	final String osName = System.getProperty("os.name");
	if (osName.indexOf("Mac OS X") != -1) {
	    // Mac OS X
	    return MazeManager.MAC_DIR;
	} else if (osName.indexOf("Windows") != -1) {
	    // Windows
	    return MazeManager.WIN_DIR;
	} else {
	    // Other - assume UNIX-like
	    return MazeManager.UNIX_DIR;
	}
    }

    private static String getGameDirectory() {
	final StringBuilder b = new StringBuilder();
	b.append(MazeManager.getGameDirectoryPrefix());
	b.append(MazeManager.getGameDirectoryName());
	return b.toString();
    }

    private static String getNameWithoutExtension(final String s) {
	String ext = null;
	final int i = s.lastIndexOf('.');
	if (i > 0 && i < s.length() - 1) {
	    ext = s.substring(0, i);
	} else {
	    ext = s;
	}
	return ext;
    }

    private static String getFileNameOnly(final String s) {
	String fno = null;
	final int i = s.lastIndexOf(File.separatorChar);
	if (i > 0 && i < s.length() - 1) {
	    fno = s.substring(i + 1);
	} else {
	    fno = s;
	}
	return fno;
    }
}
