import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


public class FastCollinearPoints {

    private final ArrayList<LineSegment> lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {

        if (points == null) throw new java.lang.IllegalArgumentException();

        Arrays.sort(points);

        if (points[0] == null) throw new java.lang.IllegalArgumentException();
        for (int i = 1; i < points.length; i++) {
            if (points[i-1].compareTo(points[i]) == 0 || points[i] == null) throw new java.lang.IllegalArgumentException();
        }

        lineSegments = new ArrayList<>();

        for (int indexOfOrigin = 0; indexOfOrigin < points.length; indexOfOrigin++) {
            Point checkPoint = points[indexOfOrigin];
            Point[] others = new Point[points.length-1];

            for (int j = 0; j < points.length; j++) {
                if (j > indexOfOrigin) others[j-1] = points[j];
                if (j < indexOfOrigin) others[j] = points[j];
            }

            Arrays.sort(others, checkPoint.slopeOrder());

            int count = 2;
            for (int k = 1; k < others.length; k++) {
                double slope1 = checkPoint.slopeTo(others[k-1]);
                double slope2 = checkPoint.slopeTo(others[k]);

                if (slope1 == slope2) {
                    count++;
                    if (k == others.length - 1) {
                        if (count >= 4 && checkPoint.compareTo(others[k-count+2]) < 0) {
                            lineSegments.add(new LineSegment(checkPoint, others[k]));
                        }
                    }
                } else {
                    if (count >= 4 && checkPoint.compareTo(others[k-count+1]) < 0) {
                        lineSegments.add(new LineSegment(checkPoint, others[k-1]));
                    }
                    count = 2;
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

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In("/Users/Michael/Downloads/collinear/input40.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}