package studio.ignitionigloogames.twistedtrek;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Line implements Iterable<Point> {
    private final List<Point> points;

    public List<Point> getPoints() {
	return this.points;
    }

    public Line(final int ox0, final int oy0, final int x1, final int y1) {
	int x0 = ox0;
	int y0 = oy0;
	this.points = new ArrayList<>();
	final int dx = Math.abs(x1 - x0);
	final int dy = Math.abs(y1 - y0);
	final int sx = x0 < x1 ? 1 : -1;
	final int sy = y0 < y1 ? 1 : -1;
	int err = dx - dy;
	while (true) {
	    this.points.add(new Point(x0, y0, 0));
	    if (x0 == x1 && y0 == y1) {
		break;
	    }
	    final int e2 = err * 2;
	    if (e2 > -dx) {
		err -= dy;
		x0 += sx;
	    }
	    if (e2 < dx) {
		err += dx;
		y0 += sy;
	    }
	}
    }

    @Override
    public Iterator<Point> iterator() {
	return this.points.iterator();
    }
}
