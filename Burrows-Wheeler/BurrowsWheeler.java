import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform() {
        String input = BinaryStdIn.readString();

        CircularSuffixArray cfa = new CircularSuffixArray(input);

        int first = 0;
        char[] chars = input.toCharArray();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            if (cfa.index(i) == 0) {
                first = i;
                sb.append(chars[input.length()-1]);
            } else {
                sb.append(chars[cfa.index(i)-1]);
            }
        }

        BinaryStdOut.write(first);
        BinaryStdOut.write(sb.toString());

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int ASCII = 256;

        char[] back = s.toCharArray();

        int[] ascii = new int[ASCII];
        char[] front = new char[back.length];
        Queue<Integer>[] radixQueues = new Queue[ASCII];

        // ascii[]       key: char in extended-ascii, value: how many of this char in back[]
        for (int i = 0; i < back.length; i++) {
            ascii[back[i]]++;

            // this is creating a Queue[], the index is the char, the value is a queue which stores the index of the char in back[]
            if (radixQueues[back[i]] == null) {
                Queue<Integer> q = new Queue<>();
                q.enqueue(Integer.valueOf(i));
                radixQueues[back[i]] = q;
            } else radixQueues[back[i]].enqueue(Integer.valueOf(i));
            
        }

        // put each initial char in their right position in the front[]
        int initial = 0;
        for (int i= 0; i < ASCII; i++) {
            if (ascii[i] != 0) {
                front[initial] = (char) i;
                initial += ascii[i];
            }
        }

        // fill the front[] according to each char's initial position
        char c = front[0];
        for (int i = 0; i < front.length; i++) {
            if (front[i] == c) continue;
            if (front[i] == 0) front[i] = c;
            if (front[i] != 0 && front[i] != c) c = front[i];
        }


        // build next[]
        int[] next = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            Queue<Integer> q = radixQueues[front[i]];
            next[i] = q.dequeue();
        }
        

        // build the original string according to next[]
        StringBuilder sb = new StringBuilder();
        sb.append(front[first]);
        int i = first;
        while (next[i] != first) {
            sb.append(front[next[i]]);
            i = next[i];
        }

        // if there's a repetition in the string
        if (sb.length() != s.length()) {
            String pattern = sb.toString();

            while (sb.length() < s.length()) sb.append(pattern);
        }
        
        BinaryStdOut.write(sb.toString());
        BinaryStdOut.close();
    }


    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) BurrowsWheeler.transform();
        if (args[0].equals("+")) BurrowsWheeler.inverseTransform();
    }

}