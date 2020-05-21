import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet {

    private final Digraph hyper;
    private final ST<String, Bag<Integer>> words;
    private final String[] synSets;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new java.lang.IllegalArgumentException();

        In synSet = new In(synsets);
        In hyperNym = new In(hypernyms);

        String[] synLines = synSet.readAllLines();
        String[] hyperLines = hyperNym.readAllLines();

        hyper = new Digraph(hyperLines.length);
        words = new ST<>();
        synSets = new String[synLines.length];

        for (String s : hyperLines) {
            String[] vertex = s.split(",");
            for (int b = 1; b < vertex.length; b++) hyper.addEdge(Integer.parseInt(vertex[0]), Integer.parseInt(vertex[b]));
        }

        DirectedCycle detect = new DirectedCycle(hyper);
        if (detect.hasCycle()) throw new java.lang.IllegalArgumentException("Not a DAG!");

        for (String s : synLines) {
            String[] sentence = s.split(",");
            String[] word = sentence[1].split(" ");

            synSets[Integer.parseInt(sentence[0])] = sentence[1];

            for (String w : word) {
                if (words.contains(w)) words.get(w).add(Integer.parseInt(sentence[0]));
                else {
                    Bag<Integer> bag = new Bag<>();
                    bag.add(Integer.parseInt(sentence[0]));
                    words.put(w, bag);
                }
            }
        }

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return words.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return words.contains(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !words.contains(nounA) || !words.contains(nounB)) throw new java.lang.IllegalArgumentException();
        SAP sap = new SAP(hyper);
        return sap.length(words.get(nounA), words.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !words.contains(nounA) || !words.contains(nounB)) throw new java.lang.IllegalArgumentException();
        SAP sap = new SAP(hyper);
        int ancestor = sap.ancestor(words.get(nounA), words.get(nounB));
        return synSets[ancestor];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        StdOut.println(wordnet.distance("wardroom", "tyrannid"));
        StdOut.println(wordnet.sap("wardroom", "tyrannid"));
    }
}