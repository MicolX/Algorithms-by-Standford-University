import edu.princeton.cs.algs4.StdIn;

public class Permutation {

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }

        for (int k = 0; k < n; k++) {
            System.out.println(queue.dequeue());
        }

    }
}
