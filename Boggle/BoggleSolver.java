import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.HashSet;

public class BoggleSolver {

    private final RTries tries;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        tries = new RTries();
        for (String s : dictionary) tries.add(s);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        NodeChar[][] nodeChars = new NodeChar[board.rows()][board.cols()];
//        StdOut.println("col = "+board.cols()+", row = "+board.rows());
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                nodeChars[i][j] = new NodeChar(j, i, board.getLetter(i, j), null);
            }
        }

        HashSet<String> words = new HashSet<>();
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                dfs(board, nodeChars[row][col], words, new boolean[board.rows()][board.cols()],"");
            }
        }

        return words;
    }

    private void dfs(BoggleBoard board, NodeChar nodeChar, HashSet<String> words, boolean[][] marked, String s) {
        s += nodeChar.aChar;
//        StdOut.println(s);
        if (tries.contains(s)) {
            if (s.length() >= 3) {
                words.add(s);
//                StdOut.println("we have "+s+" !");
            }
        }

        for (NodeChar c : adjacent(nodeChar, board)) {
//            StdOut.println("checking "+s+c.aChar);
            if (tries.keysWithPrefix(s+c.aChar) && !marked[c.y][c.x]) {
//                StdOut.println("contain prefix: "+s+c.aChar);
//                marked[c.x][c.y] = true;
                dfs(board, c, words, resetMarked(c, board.rows(), board.cols()), s);
            }
        }
    }

    private boolean[][] resetMarked(NodeChar nodeChar, int rows, int cols) {
        boolean[][] booleans = new boolean[rows][cols];
        NodeChar i = nodeChar;
        while (i != null) {
            booleans[i.y][i.x] = true;
            i = i.from;
        }
        return booleans;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int score = 0;

        if (tries.contains(word)) {
            int length = word.length();

            if (length == 3 || length == 4) {
                score = 1;
            } else if (length == 5) {
                score = 2;
            } else if (length == 6) {
                score = 3;
            } else if (length == 7) {
                score = 4;
            } else if (length >= 8) {
                score = 5;
            }
        }

        return score;
    }

    private Iterable<NodeChar> adjacent(NodeChar node, BoggleBoard board) {

        if (node.x == 0 && node.y == 0) {
//            StdOut.println("左上角");
            Bag<NodeChar> bag = new Bag<>();
            if (node.x+1 < board.cols()) bag.add(new NodeChar(node.x+1, node.y, board.getLetter(node.y, node.x+1), node));
            if (node.y+1 < board.rows()) bag.add(new NodeChar(node.x, node.y+1, board.getLetter(node.y+1, node.x), node));
            if (node.x+1 < board.cols() && node.y+1 < board.rows()) bag.add(new NodeChar(node.x+1, node.y+1, board.getLetter(node.y+1, node.x+1), node));
            return bag;
        }

        if (node.x == board.cols()-1 && node.y == 0) {
//            StdOut.println("右上角");
            Bag<NodeChar> bag = new Bag<>();
            if (node.x-1 >= 0) bag.add(new NodeChar(node.x-1, node.y, board.getLetter(node.y, node.x-1), node));
            if (node.y+1 < board.rows()) bag.add(new NodeChar(node.x, node.y+1, board.getLetter(node.y+1, node.x), node));
            if (node.x-1 >= 0 && node.y+1 < board.rows()) bag.add(new NodeChar(node.x-1, node.y+1, board.getLetter(node.y+1, node.x-1), node));
            return bag;
        }

        if (node.x == 0 && node.y == board.rows() - 1) {
//            StdOut.println("左下角");
            Bag<NodeChar> bag = new Bag<>();
            if (node.x+1 < board.cols()) bag.add(new NodeChar(node.x+1, node.y, board.getLetter(node.y, node.x+1), node));
            if (node.y-1 >= 0) bag.add(new NodeChar(node.x, node.y-1, board.getLetter(node.y-1, node.x), node));
            if (node.x+1 < board.cols() && node.y-1 >= 0) bag.add(new NodeChar(node.x+1, node.y-1, board.getLetter(node.y-1, node.x+1), node));
            return bag;
        }

        if (node.x == board.cols() - 1 && node.y == board.rows() - 1) {
//            StdOut.println("右下角");
            Bag<NodeChar> bag = new Bag<>();
            if (node.x-1 >= 0) bag.add(new NodeChar(node.x-1, node.y, board.getLetter(node.y, node.x-1), node));
            if (node.y-1 >= 0) bag.add(new NodeChar(node.x, node.y-1, board.getLetter(node.y-1, node.x), node));
            if (node.x-1 >= 0 && node.y-1 >= 0) bag.add(new NodeChar(node.x-1, node.y-1, board.getLetter(node.y-1, node.x-1), node));
            return bag;
        }

        if (node.x == 0) {
//            StdOut.println("左边");
            Bag<NodeChar> bag = new Bag<>();
            if (node.x+1 < board.cols()) bag.add(new NodeChar(node.x+1, node.y, board.getLetter(node.y, node.x+1), node));
            if (node.y+1 < board.rows()) bag.add(new NodeChar(node.x, node.y+1, board.getLetter(node.y+1, node.x), node));
            if (node.y-1 >= 0) bag.add(new NodeChar(node.x, node.y-1, board.getLetter(node.y-1, node.x), node));
            if (node.x+1 < board.cols() && node.y-1 >= 0) bag.add(new NodeChar(node.x+1, node.y-1, board.getLetter(node.y-1, node.x+1), node));
            if (node.x+1 < board.cols() && node.y+1 < board.rows()) bag.add(new NodeChar(node.x+1, node.y+1, board.getLetter(node.y+1, node.x+1), node));
            return bag;
        }

        if (node.y == 0) {
//            StdOut.println("上边");
            Bag<NodeChar> bag = new Bag<>();
            if (node.x-1 >= 0) bag.add(new NodeChar(node.x-1, node.y, board.getLetter(node.y, node.x-1), node));
            if (node.x+1 < board.cols()) bag.add(new NodeChar(node.x+1, node.y, board.getLetter(node.y, node.x+1), node));
            if (node.y+1 < board.rows()) bag.add(new NodeChar(node.x, node.y+1, board.getLetter(node.y+1, node.x), node));
            if (node.x-1 >= 0 && node.y+1 < board.rows()) bag.add(new NodeChar(node.x-1, node.y+1, board.getLetter(node.y+1, node.x-1), node));
            if (node.x+1 < board.cols() && node.y+1 < board.rows()) bag.add(new NodeChar(node.x+1, node.y+1, board.getLetter(node.y+1, node.x+1), node));
            return bag;
        }

        if (node.x == board.cols() - 1) {
//            StdOut.println("右边");
            Bag<NodeChar> bag = new Bag<>();
            if (node.x-1 >= 0) bag.add(new NodeChar(node.x-1, node.y, board.getLetter(node.y, node.x-1), node));
            if (node.y-1 >= 0) bag.add(new NodeChar(node.x, node.y-1, board.getLetter(node.y-1, node.x), node));
            if (node.y+1 < board.rows()) bag.add(new NodeChar(node.x, node.y+1, board.getLetter(node.y+1, node.x), node));
            if (node.x-1 >= 0 && node.y-1 >= 0) bag.add(new NodeChar(node.x-1, node.y-1, board.getLetter(node.y-1, node.x-1), node));
            if (node.x-1 >= 0 && node.y+1 < board.rows()) bag.add(new NodeChar(node.x-1, node.y+1, board.getLetter(node.y+1, node.x-1), node));
            return bag;
        }

        if (node.y == board.rows() - 1) {
//            StdOut.println("下边");
            Bag<NodeChar> bag = new Bag<>();
            if (node.x-1 >= 0) bag.add(new NodeChar(node.x-1, node.y, board.getLetter(node.y, node.x-1), node));
            if (node.x+1 < board.cols()) bag.add(new NodeChar(node.x+1, node.y, board.getLetter(node.y, node.x+1), node));
            if (node.y-1 >= 0) bag.add(new NodeChar(node.x, node.y-1, board.getLetter(node.y-1, node.x), node));
            if (node.x-1 >= 0 && node.y-1 >= 0) bag.add(new NodeChar(node.x-1, node.y-1, board.getLetter(node.y-1, node.x-1), node));
            if (node.x+1 < board.cols() && node.y-1 >= 0) bag.add(new NodeChar(node.x+1, node.y-1, board.getLetter(node.y-1, node.x+1), node));
            return bag;
        }


//        StdOut.println("中间");
        Bag<NodeChar> bag = new Bag<>();
        if (node.x-1 >= 0) bag.add(new NodeChar(node.x-1, node.y, board.getLetter(node.y, node.x-1), node));
        if (node.x+1 < board.cols()) bag.add(new NodeChar(node.x+1, node.y, board.getLetter(node.y, node.x+1), node));
        if (node.y+1 < board.rows()) bag.add(new NodeChar(node.x, node.y+1, board.getLetter(node.y+1, node.x), node));
        if (node.y-1 >= 0) bag.add(new NodeChar(node.x, node.y-1, board.getLetter(node.y-1, node.x), node));
        if (node.x-1 >= 0 && node.y+1 < board.rows()) bag.add(new NodeChar(node.x-1, node.y+1, board.getLetter(node.y+1, node.x-1), node));
        if (node.x+1 < board.cols() && node.y+1 < board.rows()) bag.add(new NodeChar(node.x+1, node.y+1, board.getLetter(node.y+1, node.x+1), node));
        if (node.x-1 >= 0 && node.y-1 >= 0) bag.add(new NodeChar(node.x-1, node.y-1, board.getLetter(node.y-1, node.x-1), node));
        if (node.x+1 < board.cols() && node.y-1 >= 0) bag.add(new NodeChar(node.x+1, node.y-1, board.getLetter(node.y-1, node.x+1), node));
//        StdOut.println("bag size = "+bag.size());
        return bag;
    }

    private class NodeChar {
        private final int x;
        private final int y;
        private final String aChar;
        private final NodeChar from;

        private NodeChar(int j, int i, char c, NodeChar f) {
            x = j;
            y = i;
            from = f;
            if (c == "Q".charAt(0)) {
                aChar = c+"U";
            } else aChar = c+"";
        }
    }

    private class RTries {

        private static final int R = 26;        // extended ASCII

        private Node root;      // root of trie
        private int n;          // number of keys in trie

        // R-way trie node
        private class Node {
            private Node[] next = new Node[R];
            private boolean isString;
        }

        private RTries() {
        }

        private boolean contains(String key) {
            if (key == null) throw new IllegalArgumentException("argument to contains() is null");
            Node x = get(root, key, 0);
            if (x == null) return false;
            return x.isString;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[c-65], key, d+1);
        }


        private void add(String key) {
            if (key == null) throw new IllegalArgumentException("argument to add() is null");
            root = add(root, key, 0);
        }

        private Node add(Node x, String key, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                if (!x.isString) n++;
                x.isString = true;
            }
            else {
                char c = key.charAt(d);
                x.next[c-65] = add(x.next[c-65], key, d+1);
            }
            return x;
        }

        private int size() {
            return n;
        }


        private boolean isEmpty() {
            return size() == 0;
        }


        private boolean keysWithPrefix(String prefix) {
            Node x = get(root, prefix, 0);
            return x != null;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;

        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
