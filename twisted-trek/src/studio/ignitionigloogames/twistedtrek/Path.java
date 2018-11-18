/* Twisted Trek: A Dual-World Action RPG */
package studio.ignitionigloogames.twistedtrek;

import java.util.List;

public class Path {
    private static PathFinder pf = new PathFinder();
    private final List<Point> points;

    public List<Point> points() {
	return this.points;
    }

    public Path(final Creature creature, final int x, final int y) {
	this.points = Path.pf.findPath(creature, new Point(creature.x, creature.y, creature.z),
		new Point(x, y, creature.z), 300);
    }
}
