import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import java.util.ArrayList;

public class PointSET {

    private final SET<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        pointSet = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (Point2D point : pointSet) StdDraw.point(point.x(), point.y());
        StdDraw.show();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.IllegalArgumentException();
        ArrayList<Point2D> array = new ArrayList<>();
        for (Point2D point : pointSet) {
            if (point.x() <= rect.xmax() && point.x() >= rect.xmin() && point.y() <= rect.ymax() && point.y() >= rect.ymin()) array.add(point);
        }
        return array;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        if (pointSet.isEmpty()) return null;
        double nearest = Double.POSITIVE_INFINITY;
        Point2D nearestPoint = null;
        for (Point2D point : pointSet) {
            if (point.distanceSquaredTo(p) < nearest) {
                nearestPoint = point;
                nearest = point.distanceSquaredTo(p);
            }
        }
        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET set = new PointSET();
        In filename = new In(args[0]);
        while (!filename.isEmpty()) {
            double x = filename.readDouble();
            double y = filename.readDouble();
            set.insert(new Point2D(x, y));
        }

//        set.insert(new Point2D(1, 0.25));
//        set.insert(new Point2D(0.5, 0));
//        set.insert(new Point2D(0.25, 0.25));
//        set.insert(new Point2D(0, 0.75));
//        set.insert(new Point2D(0, 0.75));
//        set.insert(new Point2D(0.25, 0.25));
//        set.insert(new Point2D(0, 0.25));
//        set.insert(new Point2D(0.5, 0));
//        set.insert(new Point2D(0.75, 0.25));

        StdOut.println(set.nearest(new Point2D(0.486, 0.73)));
    }
}