import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int EXTENDED_ASCII = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {

        // initialize an ASCII table
        char[] ascii = new char[EXTENDED_ASCII];
        for (int i = 0; i < ascii.length; i++) ascii[i] = (char) i;

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int position = 0;
            for (int a = 0; a < EXTENDED_ASCII; a++) {
                if (ascii[a] == c) {
                    position = a;
                    char cc = (char) a;
                    BinaryStdOut.write(cc);
                    break;
                }
            }

            for (int i = position; i > 0; i--) {
                ascii[i] = ascii[i-1];
            }
            ascii[0] = c;
        }

        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] ascii = new char[EXTENDED_ASCII];
        for (int i = 0; i < ascii.length; i++) ascii[i] = (char) i;
        
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char ch = ascii[c];
            BinaryStdOut.write(ch);

            for (int i = c; i > 0; i--) {
                ascii[i] = ascii[i-1];
            }
            ascii[0] = ch;
        }
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) MoveToFront.encode();
        else if (args[0].equals("+")) MoveToFront.decode();
        
        // encode();
        // decode();
    }

}