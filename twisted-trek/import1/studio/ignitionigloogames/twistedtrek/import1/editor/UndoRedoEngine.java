/* Import1: A Maze-Solving Game */
package studio.ignitionigloogames.twistedtrek.import1.editor;

import studio.ignitionigloogames.twistedtrek.import1.generic.MazeObject;

public class UndoRedoEngine {
    // Fields
    private final LinkStack undoHistory;
    private final LinkStack redoHistory;
    private MazeObject object;
    private int destX, destY, destZ, destW, destE;

    // Constructors
    public UndoRedoEngine() {
	this.undoHistory = new LinkStack();
	this.redoHistory = new LinkStack();
	this.object = null;
	this.destX = -1;
	this.destY = -1;
	this.destZ = -1;
	this.destW = -1;
	this.destE = -1;
    }

    // Public methods
    public void undo() {
	if (!this.undoHistory.isEmpty()) {
	    final Link entry = this.undoHistory.pop();
	    this.object = entry.mo;
	    this.destX = entry.coordX;
	    this.destY = entry.coordY;
	    this.destZ = entry.coordZ;
	    this.destW = entry.coordW;
	    this.destE = entry.coordE;
	} else {
	    this.object = null;
	    this.destX = -1;
	    this.destY = -1;
	    this.destZ = -1;
	    this.destW = -1;
	    this.destE = -1;
	}
    }

    public void redo() {
	if (!this.redoHistory.isEmpty()) {
	    final Link entry = this.redoHistory.pop();
	    this.object = entry.mo;
	    this.destX = entry.coordX;
	    this.destY = entry.coordY;
	    this.destZ = entry.coordZ;
	    this.destW = entry.coordW;
	    this.destE = entry.coordE;
	} else {
	    this.object = null;
	    this.destX = -1;
	    this.destY = -1;
	    this.destZ = -1;
	    this.destW = -1;
	    this.destE = -1;
	}
    }

    public boolean tryUndo() {
	return !this.undoHistory.isEmpty();
    }

    public boolean tryRedo() {
	return !this.redoHistory.isEmpty();
    }

    public boolean tryBoth() {
	return this.undoHistory.isEmpty() && this.redoHistory.isEmpty();
    }

    public void updateUndoHistory(final MazeObject obj, final int x, final int y, final int z, final int w,
	    final int e) {
	this.undoHistory.push(obj, x, y, z, w, e);
    }

    public void updateRedoHistory(final MazeObject obj, final int x, final int y, final int z, final int w,
	    final int e) {
	this.redoHistory.push(obj, x, y, z, w, e);
    }

    public MazeObject getObject() {
	return this.object;
    }

    public int getX() {
	return this.destX;
    }

    public int getY() {
	return this.destY;
    }

    public int getZ() {
	return this.destZ;
    }

    public int getW() {
	return this.destW;
    }

    public int getE() {
	return this.destE;
    }

    // Inner classes
    private class Link {
	// Fields
	public MazeObject mo;
	public int coordX, coordY, coordZ, coordW, coordE;
	public Link next;

	public Link(final MazeObject obj, final int x, final int y, final int z, final int w, final int e) {
	    this.mo = obj;
	    this.coordX = x;
	    this.coordY = y;
	    this.coordZ = z;
	    this.coordW = w;
	    this.coordE = e;
	    this.next = null;
	}
    }

    private class LinkList {
	// Fields
	private Link first;

	public LinkList() {
	    this.first = null;
	}

	public boolean isEmpty() {
	    return this.first == null;
	}

	public void insertFirst(final MazeObject obj, final int x, final int y, final int z, final int w, final int e) {
	    final Link newLink = new Link(obj, x, y, z, w, e);
	    newLink.next = this.first;
	    this.first = newLink;
	}

	public Link deleteFirst() {
	    final Link temp = this.first;
	    this.first = this.first.next;
	    return temp;
	}
    }

    private class LinkStack {
	// Fields
	private final LinkList theList;

	public LinkStack() {
	    this.theList = new LinkList();
	}

	public void push(final MazeObject obj, final int x, final int y, final int z, final int w, final int e) {
	    this.theList.insertFirst(obj, x, y, z, w, e);
	}

	public Link pop() {
	    return this.theList.deleteFirst();
	}

	public boolean isEmpty() {
	    return this.theList.isEmpty();
	}
    }
}
