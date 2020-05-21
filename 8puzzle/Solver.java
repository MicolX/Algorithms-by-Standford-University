import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class Solver {

    private Node goalNode;
 //   private Board initialBoard;
    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new java.lang.IllegalArgumentException();
 //       initialBoard = initial;
        if (initial.isGoal()) {
            solvable = true;
            goalNode = new Node(initial, null, 0);
        } else {
            Node initialNode = new Node(initial, null, 0);
            Node initialTwin = new Node(initial.twin(), null, 0);
            MinPQ<Node> gameTree = new MinPQ<>(nodePriority());
            MinPQ<Node> twinTree = new MinPQ<>(nodePriority());

            for (Board board : initialNode.board.neighbors()) {
                if (board.isGoal()) {
                    solvable = true;
                    goalNode = new Node(board, initialNode, initialNode.move+1);
                    return;
                }

                gameTree.insert(new Node(board, initialNode, initialNode.move+1));
            }

            if (initialTwin.board.isGoal()) {
                solvable = false;
                return;
            } else {
                for (Board tb : initialTwin.board.neighbors()) {
                    if (tb.isGoal()) {
                       // goalNode = new Node(tb, initialTwin, initialTwin.move+1);
                        solvable = false;
                        return;
                    }

                    twinTree.insert(new Node(tb, initialTwin, initialTwin.move+1));
                }
            }

            while (true) {
                Node node = gameTree.delMin();
                for (Board board : node.board.neighbors()) {
                    if (board.isGoal()) {
                        solvable = true;
                        goalNode = new Node(board, node, node.move+1);
                        return;
                    } else {
                        if (!board.equals(node.predecessor.board)) gameTree.insert(new Node(board, node, node.move+1));
                    }
                }

                Node twin = twinTree.delMin();
                for (Board board : twin.board.neighbors()) {
                    if (board.isGoal()) {
                        solvable = false;
                        return;
                    } else {
                        if (!board.equals(twin.predecessor.board)) twinTree.insert(new Node(board, twin, twin.move+1));
                    }
                }
            }
        }
    }


    // is the initial board solvable?
    public boolean isSolvable() {
//        Node node = goalNode;
//        while (node.predecessor != null) {
//            node = node.predecessor;
//        }
//        return node.board.equals(initialBoard);
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return goalNode.move;
        else return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) {

            int length = goalNode.move+1;
            Board[] array = new Board[length];
            Node node = goalNode;

            while (true) {
                array[node.move] = node.board;
                if (node.predecessor != null) node = node.predecessor;
                else break;
            }

            return Arrays.asList(array);
        }
        else return null;
    }

    private Comparator<Node> nodePriority() {
        return new NodePriority();
    }

    private class NodePriority implements Comparator<Node> {
        public int compare(Node a, Node b) {
            int priorityA = a.manhattan + a.move;
            int priorityB = b.manhattan + b.move;
            if (priorityA > priorityB) return 1;
            else if (priorityA < priorityB) return -1;
            else {
                if (a.move > b.move) return -1;
                else if (a.move < b.move) return 1;
                else return 0;

            }
        }
    }

    private class Node {
        Board board;
        Node predecessor;
        int move;
        int manhattan;

        public Node(Board board, Node predecessor, int move) {
            this.board = board;
            this.predecessor = predecessor;
            this.move = move;
            this.manhattan = board.manhattan();
        }

    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}