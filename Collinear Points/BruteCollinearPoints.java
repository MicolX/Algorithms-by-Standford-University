import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private final ArrayList<LineSegment> lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.IllegalArgumentException();

        for (Point a: points) {
            if (a == null) throw new java.lang.IllegalArgumentException();
        }

        Arrays.sort(points, Point::compareTo);

        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(points[i-1]) == 0) throw new java.lang.IllegalArgumentException();
        }

        lineSegments = new ArrayList<>();
        for (int a = 0; a < points.length - 3; a++) {
            for (int b = a + 1; b < points.length - 2; b++) {
                double slope1 = points[a].slopeTo(points[b]);
                for (int c = b + 1; c < points.length -  1; c++) {
                    double slope2 = points[a].slopeTo(points[c]);
                    if (slope1 != slope2) continue;
                    for (int d = c + 1; d < points.length; d++) {
                        double slope3 = points[a].slopeTo(points[d]);
                        if (slope1 != slope3) continue;
                        else lineSegments.add(new LineSegment(points[a], points[d]));
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] segs = new LineSegment[lineSegments.size()];
        for (int i = 0; i < segs.length; i++) {
            segs[i] = lineSegments.get(i);
        }
        return segs;

    }

}