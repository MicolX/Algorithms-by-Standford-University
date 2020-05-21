import java.util.ArrayList;

public class CircularSuffixArray {
    private final int length;
    private final int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        
        this.length = s.length();
        char[] string = s.toCharArray();
        int count = 0;
        index = new int[length];
        int champ = 0;

        ArrayList<Integer> indexes = new ArrayList<>(); 
        for (int i = 0; i < index.length; i++) {
            indexes.add(i);
        }

        while (!indexes.isEmpty()) {
            if (indexes.size() == 1) {
                index[count] = (int) indexes.toArray()[0];
                break;
            }
            else {
                champ = compareSuffix(string, (int) indexes.toArray()[0], (int) indexes.toArray()[1]);
            }

            for (int i : indexes) {
                champ = compareSuffix(string, champ, i);
            }

            index[count++] = champ;
            indexes.remove(indexes.indexOf(champ));
        }

    }

    private int compareSuffix(char[] string, int a, int b) {
        if (a == b) return a;
        int originalA = a;
        int originalB = b;
        
        while (true) {
            if (string[a] < string[b]) {
                return originalA;
            }
            else if (string[b] < string[a]) {
                return originalB;
            }
            else {
                // when two suffixes are identical
                if ((originalA == 0 && a == string.length-1) || a == originalA - 1) {
                    return originalA;
                }

                if (a == string.length - 1) a = 0;
                else a++;

                if (b == string.length - 1) b = 0;
                else b++; 
            }
        }
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i >= length() || i < 0) throw new IllegalArgumentException();
        return index[i];
    }

    // private String printSuffix(String s, int i) {
    //     char[] chars = s.toCharArray();
    //     StringBuilder suffix = new StringBuilder("");
    //     int start = i;
    //     while (true) {
    //         if (start == 0 && i == s.length()-1) break; 
    //         suffix.append(chars[i]);
    //         if (i == s.length()-1) i = 0; 
    //         else i++;
    //         if (i == start) break;
    //     }
    //     return suffix.toString();
    // }

    // unit testing (required)
    public static void main(String[] args) {
        // String test = "ABRACADABRA!";

        // CircularSuffixArray a = new CircularSuffixArray(test);
        
        // for (int i = 0; i < test.length(); i++) {
        //     StdOut.println(a.printSuffix(test, i));
        // }

        // StdOut.println();

        // for (int i = 0; i < a.length; i++) {
        //     StdOut.println(a.printSuffix(test, a.index(i)));
        // }
    }

}