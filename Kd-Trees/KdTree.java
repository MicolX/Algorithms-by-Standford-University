import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.ArrayList;

public class KdTree {

    private Node root;
    private int count;

    private static class Node {
        private final Point2D point;
        private Node left, right;
        private RectHV rect;
        private final boolean vertical;

        private Node(Point2D p, boolean vertical) {
            this.point = p;
            this.vertical = vertical;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        count = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // number of points in the set
    public int size() {
        return count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        root = put(root, p, null);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        Node node = root;
        while (node != null) {
            if (node.point.equals(p)) {
                return true;
            }

            if (node.vertical) {
                if (p.x() < node.point.x()) node = node.left;
                else node = node.right;
            } else {
                if (p.y() < node.point.y()) node = node.left;
                else node = node.right;
            }

        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
   //     StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 10));
        privateDraw(root, null, true);
    }

    private void privateDraw(Node node, Node prev, boolean greater) {
        if (node == null) return;
        if (prev != null) {
            if (greater) {
                if (node.vertical) {
                    StdDraw.setPenRadius(0.01);
                    node.point.draw();
                    StdDraw.setPenRadius();
                    StdDraw.textRight(node.point.x(), node.point.y(), node.point.toString());
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.line(node.point.x(), prev.point.y(), node.point.x(), 1.0);
                    StdDraw.show();
                } else {
                    StdDraw.setPenRadius(0.01);
                    node.point.draw();
                    StdDraw.setPenRadius();
                    StdDraw.textRight(node.point.x(), node.point.y(), node.point.toString());
                    StdDraw.setPenColor(StdDraw.BLUE);
                    StdDraw.line(prev.point.x(), node.point.y(), 1.0, node.point.y());
                    StdDraw.show();
                }
            } else {
                if (node.vertical) {
                    StdDraw.setPenRadius(0.01);
                    node.point.draw();
                    StdDraw.setPenRadius();
                    StdDraw.textRight(node.point.x(), node.point.y(), node.point.toString());
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.line(node.point.x(), prev.point.y(), node.point.x(), 0.0);
                    StdDraw.show();
                } else {
                    StdDraw.setPenRadius(0.01);
                    node.point.draw();
                    StdDraw.setPenRadius();
                    StdDraw.textRight(node.point.x(), node.point.y(), node.point.toString());
                    StdDraw.setPenColor(StdDraw.BLUE);
                    StdDraw.line(prev.point.x(), node.point.y(), 0.0, node.point.y());
                    StdDraw.show();
                }
            }
        } else {
            StdDraw.setPenRadius(0.01);
            node.point.draw();
            StdDraw.setPenRadius();
            StdDraw.textRight(node.point.x(), node.point.y(), node.point.toString());
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), 0.0, node.point.x(), 1.0);
            StdDraw.show();
        }
//        StdDraw.setPenColor(Color.yellow);
//        StdDraw.rectangle(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
        privateDraw(node.right, node, true);
        privateDraw(node.left, node, false);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.IllegalArgumentException();
        ArrayList<Point2D> pointsInRange = new ArrayList<>();
        if (!isEmpty()) searchForRange(root, rect, pointsInRange);
        return pointsInRange;
    }

    private void searchForRange(Node node, RectHV rect, ArrayList<Point2D> array) {
        if (node.rect.intersects(rect)) {
            if (rect.contains(node.point)) array.add(node.point);
            if (node.left != null && node.left.rect.intersects(rect)) searchForRange(node.left, rect, array);
            if (node.right != null && node.right.rect.intersects(rect)) searchForRange(node.right, rect, array);
        } else return;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        if (isEmpty()) return null;
        Point2D answer = root.point;
        double dist = p.distanceSquaredTo(root.point);

        if (root.right == null) answer = searchForNearest(root.left, dist, p, answer);
        else if (root.left == null) answer = searchForNearest(root.right, dist, p, answer);
        else {
            if (p.x() < root.point.x()) {
                answer = searchForNearest(root.left, dist, p, answer);
                if (answer.distanceSquaredTo(p) > root.right.rect.distanceSquaredTo(p)) {
                    Point2D another = searchForNearest(root.right, p.distanceSquaredTo(answer), p, answer);
                    if (another.distanceSquaredTo(p) < answer.distanceSquaredTo(p)) {
                        answer = another;
                    }
                }
            }
            else if (p.x() > root.point.x()) {
                answer = searchForNearest(root.right, dist, p, answer);
                if (answer.distanceSquaredTo(p) > root.left.rect.distanceSquaredTo(p)) {
                    Point2D another = searchForNearest(root.left, p.distanceSquaredTo(answer), p, answer);
                    if (another.distanceSquaredTo(p) < answer.distanceSquaredTo(p)) {
                        answer = another;
                    }
                }
            }
            else {
                answer = searchForNearest(root.left, dist, p, answer);
                Point2D another = searchForNearest(root.right, dist, p, answer);
                if (another.distanceSquaredTo(p) < answer.distanceSquaredTo(p)) answer = another;
            }
        }

        return answer;
    }

    private Point2D searchForNearest(Node searchNode, double distance, Point2D queryPoint, Point2D answerPoint) {
        if (searchNode != null) {
            if (searchNode.rect.distanceSquaredTo(queryPoint) < distance) {
                if (searchNode.point.distanceSquaredTo(queryPoint) < distance) {
                    distance = searchNode.point.distanceSquaredTo(queryPoint);
                    answerPoint = searchNode.point;

                }

                if (searchNode.vertical) {
                    if (queryPoint.x() <= searchNode.point.x()) {
                        if (searchNode.left != null && searchNode.left.rect.distanceSquaredTo(queryPoint) < distance) answerPoint = searchForNearest(searchNode.left, distance, queryPoint, answerPoint);
                        if (searchNode.right != null && searchNode.right.rect.distanceSquaredTo(queryPoint) < answerPoint.distanceSquaredTo(queryPoint)) answerPoint = searchForNearest(searchNode.right, answerPoint.distanceSquaredTo(queryPoint), queryPoint, answerPoint);
                    } else {
                        if (searchNode.right != null && searchNode.right.rect.distanceSquaredTo(queryPoint) < distance) answerPoint = searchForNearest(searchNode.right, distance, queryPoint, answerPoint);
                        if (searchNode.left != null && searchNode.left.rect.distanceSquaredTo(queryPoint) < answerPoint.distanceSquaredTo(queryPoint)) answerPoint = searchForNearest(searchNode.left, answerPoint.distanceSquaredTo(queryPoint), queryPoint, answerPoint);
                    }
                } else {
                    if (queryPoint.y() <= searchNode.point.y()) {
                        if (searchNode.left != null && searchNode.left.rect.distanceSquaredTo(queryPoint) < distance) answerPoint = searchForNearest(searchNode.left, distance, queryPoint, answerPoint);
                        if (searchNode.right != null && searchNode.right.rect.distanceSquaredTo(queryPoint) < answerPoint.distanceSquaredTo(queryPoint)) answerPoint = searchForNearest(searchNode.right, answerPoint.distanceSquaredTo(queryPoint), queryPoint, answerPoint);
                    } else {
                        if (searchNode.right != null && searchNode.right.rect.distanceSquaredTo(queryPoint) < distance) answerPoint = searchForNearest(searchNode.right, distance, queryPoint, answerPoint);
                        if (searchNode.left != null && searchNode.left.rect.distanceSquaredTo(queryPoint) < answerPoint.distanceSquaredTo(queryPoint)) answerPoint = searchForNearest(searchNode.left, answerPoint.distanceSquaredTo(queryPoint), queryPoint, answerPoint);
                    }
                }

            }
        }
        return answerPoint;
    }

    private Node put(Node node, Point2D p, Node prev) {
        if (node == null) {
            count++;
            if (prev == null) {
                Node newNode = new Node(p, true);
                newNode.rect = new RectHV(0, 0, 1, 1);
                return newNode;
            } else {
                Node newNode = new Node(p, !prev.vertical);
                if (newNode.vertical) {
                    if (newNode.point.y() > prev.point.y()) newNode.rect = new RectHV(prev.rect.xmin(), prev.point.y(), prev.rect.xmax(), prev.rect.ymax());
                    else if (newNode.point.y() < prev.point.y()) newNode.rect = new RectHV(prev.rect.xmin(), prev.rect.ymin(), prev.rect.xmax(), prev.point.y());
                    else newNode.rect = prev.rect;
                } else {
                    if (newNode.point.x() > prev.point.x()) newNode.rect = new RectHV(prev.point.x(), prev.rect.ymin(), prev.rect.xmax(), prev.rect.ymax());
                    else if (newNode.point.x() < prev.point.x()) newNode.rect = new RectHV(prev.rect.xmin(), prev.rect.ymin(), prev.point.x(), prev.rect.ymax());
                    else newNode.rect = prev.rect;
                }
                return newNode;
            }

        }
        if (node.vertical) {
            if (p.x() > node.point.x()) node.right = put(node.right, p, node);
            if (p.x() < node.point.x()) node.left = put(node.left, p, node);
            if (Double.compare(p.x(), node.point.x()) == 0) {
                if (Double.compare(p.y(), node.point.y()) == 0) return node;
                else node.right = put(node.right, p, node);
            }

        } else {
            if (p.y() > node.point.y()) node.right = put(node.right, p, node);
            if (p.y() < node.point.y()) node.left = put(node.left, p, node);
            if (Double.compare(p.y(), node.point.y()) == 0) {
                if (Double.compare(p.x(), node.point.x()) == 0) return node;
                else node.right = put(node.right, p, node);
            }
        }
        return node;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree tree = new KdTree();
        In filename = new In(args[0]);
        while (!filename.isEmpty()) {
            double x = filename.readDouble();
            double y = filename.readDouble();
            tree.insert(new Point2D(x, y));
        }
//        tree.insert(new Point2D(0.7 ,0.2));
//        tree.insert(new Point2D(0.5 ,0.4));
//        tree.insert(new Point2D(0.2 ,0.3));
//        tree.insert(new Point2D(0.4 ,0.7));
//        tree.insert(new Point2D(0.9 ,0.6));

    //    tree.draw();

//        RectHV rect = new RectHV(0,0,0.3,0.4);
//        for (Point2D p : tree.range(rect)) StdOut.println(p);

        StdOut.println(tree.nearest(new Point2D(0.011, 0.848)));
    }
}
