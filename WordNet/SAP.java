import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private final Digraph net;

    private class TwoBFS {

        private int[] distFromV;
        private int[] distFromW;
        private int ancestor = -1;

        private TwoBFS(int v, int w) {
            if (v != w) {
                Queue<Integer> vQ = new Queue<>();
                boolean[] markedFromV = new boolean[net.V()];
                distFromV = new int[net.V()];
                vQ.enqueue(v);
                markedFromV[v] = true;
                distFromV[v] = 0;

                while (!vQ.isEmpty()) {
                    int n = vQ.dequeue();
                    for (int adj : net.adj(n)) {
                        if (!markedFromV[adj]) {
                            vQ.enqueue(adj);
                            distFromV[adj] = distFromV[n] + 1;
                            markedFromV[adj] = true;
                        }
                    }
                }

                Queue<Integer> wQ = new Queue<>();
                boolean[] markedFromW = new boolean[net.V()];
                distFromW = new int[net.V()];
                wQ.enqueue(w);
                markedFromW[w] = true;
                distFromW[w] = 0;

                if (markedFromV[w]) {
                    ancestor = w;
                }

                while (!wQ.isEmpty()) {
                    int n = wQ.dequeue();
                    for (int adj : net.adj(n)) {
                        if (!markedFromW[adj]) {
                            wQ.enqueue(adj);
                            distFromW[adj] = distFromW[n] + 1;
                            markedFromW[adj] = true;
                        }

                        if (markedFromV[adj]) {
                            if (ancestor == -1 || distFromV[ancestor] + distFromW[ancestor] > distFromV[adj] + distFromW[adj]) ancestor = adj;
                        }
                    }
                }
            }
        }

        private TwoBFS(Iterable<Integer> v, Iterable<Integer> w) {
            Queue<Integer> vQ = new Queue<>();
            boolean[] markedFromV = new boolean[net.V()];
            distFromV = new int[net.V()];
            for (int a : v) {
                vQ.enqueue(a);
                markedFromV[a] = true;
                distFromV[a] = 0;
            }

            while (!vQ.isEmpty()) {
                int n = vQ.dequeue();
                for (int adj : net.adj(n)) {
                    if (!markedFromV[adj]) {
                        vQ.enqueue(adj);
                        distFromV[adj] = distFromV[n] + 1;
                        markedFromV[adj] = true;
                    }
                }
            }

            Queue<Integer> wQ = new Queue<>();
            boolean[] markedFromW = new boolean[net.V()];
            distFromW = new int[net.V()];
            for (int i : w) {
                if (markedFromV[i]) {
                    if (ancestor == -1 || distFromV[ancestor] > distFromV[i]) ancestor = i;
                }
                wQ.enqueue(i);
                markedFromW[i] = true;
                distFromW[i] = 0;
            }

            while (!wQ.isEmpty()) {
                int n = wQ.dequeue();
                for (int adj : net.adj(n)) {
                    if (!markedFromW[adj]) {
                        wQ.enqueue(adj);
                        markedFromW[adj] = true;
                        distFromW[adj] = distFromW[n] + 1;
                    }

                    if (markedFromV[adj]) {
                        if (ancestor == -1 || distFromV[ancestor] + distFromW[ancestor] > distFromV[adj] + distFromW[adj]) ancestor = adj;
                    }
                }
            }
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        net = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v == w) return 0;
        if (v > net.V() || w > net.V() || v < 0 || w < 0) throw new java.lang.IllegalArgumentException();

        TwoBFS bfs = new TwoBFS(v, w);
        if (bfs.ancestor != -1) {
            return bfs.distFromV[bfs.ancestor] + bfs.distFromW[bfs.ancestor];
        } else return -1;

    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v == w) return v;
        if (v > net.V() || w > net.V() || v < 0 || w < 0) throw new java.lang.IllegalArgumentException();


        TwoBFS bfs = new TwoBFS(v, w);
        if (bfs.ancestor != -1) {
            return bfs.ancestor;
        } else return -1;

    }


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w  == null) throw new java.lang.IllegalArgumentException();

        TwoBFS bfs = new TwoBFS(v, w);
        if (bfs.ancestor != -1) return bfs.distFromV[bfs.ancestor] + bfs.distFromW[bfs.ancestor];
        else return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new java.lang.IllegalArgumentException();

        TwoBFS bfs = new TwoBFS(v, w);
        if (bfs.ancestor != -1) return bfs.ancestor;
        else return -1;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);

        SAP sap = new SAP(G);

        StdOut.println("length = "+sap.length(0, 3));
        StdOut.println("ancestor = "+sap.ancestor(0, 3));
    }
}