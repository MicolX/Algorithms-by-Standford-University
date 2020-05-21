/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[] status;
    private int totalN;
    private int interval;
    private int openCount = 0;
    private WeightedQuickUnionUF wQUF;

    private int toOneD(int x, int y) {
        return (x-1) * this.interval + y;
    }

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) throw new java.lang.IllegalArgumentException();
        else {
            this.interval = n;
            this.totalN = n * n;
            this.wQUF = new WeightedQuickUnionUF(totalN+2);
            this.status = new boolean[totalN + 2];
            for (int x = 0; x < totalN + 2; x++) {
                if (x == 0 || x == totalN+1) this.status[x] = true;
                else this.status[x] = false;
            }
        }

    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || col < 1 || row > this.interval || col > this.interval) throw new java.lang.IllegalArgumentException();
        else {
            int n = toOneD(row, col);

            if (!this.status[n]) {
                this.status[n] = true;

                // upper side
                if (row == 1) {
                    wQUF.union(n, 0);   // connect to virtual top
                    if (this.status[n+this.interval]) wQUF.union(n+this.interval, n); // connect to down
                }

                // right bottom corner
                else if (n == totalN) {
                    wQUF.union(totalN+1, n); // connected by virtual bottom
                    if (this.status[n-1]) wQUF.union(n, n-1); // connect to left
                    if (this.status[n-this.interval]) wQUF.union(n, n-this.interval); // connect to up
                }

                // left bottom corner
                else if (row == this.interval && col == 1) {
                    wQUF.union(totalN+1, n); // connected by virtual bottom
                    if (this.status[n+1]) wQUF.union(n, n+1);   // connect to right
                    if (this.status[n-this.interval]) wQUF.union(n, n-this.interval); // connect to up
                }

                // right side
                else if (col == this.interval) {
                    if (this.status[n-1]) wQUF.union(n, n-1); // connect to left
                    if (this.status[n+this.interval]) wQUF.union(n+this.interval, n); // connect to down
                    if (this.status[n-this.interval]) wQUF.union(n, n-this.interval); // connect to up
                }

                // bottom side
                else if (row == this.interval) {
                    wQUF.union(totalN+1, n); // connected by virtual bottom
                    if (this.status[n-1]) wQUF.union(n, n-1); // connect to left
                    if (this.status[n+1]) wQUF.union(n, n+1);   // connect to right
                    if (this.status[n-this.interval]) wQUF.union(n, n-this.interval); // connect to up
                }

                // left side
                else if (col == 1) {
                    if (this.status[n+1]) wQUF.union(n, n+1);   // connect to right
                    if (this.status[n+this.interval]) wQUF.union(n+this.interval, n); // connect to down
                    if (this.status[n-this.interval]) wQUF.union(n, n-this.interval); // connect to up
                }

                // middle part
                else {
                    if (this.status[n+this.interval]) wQUF.union(n+this.interval, n); // connect to down
                    if (this.status[n-1]) wQUF.union(n, n-1); // connect to left
                    if (this.status[n+1]) wQUF.union(n, n+1);   // connect to right
                    if (this.status[n-this.interval]) wQUF.union(n, n-this.interval); // connect to up
                }

                this.openCount++;
            }

        }

    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || col < 1 || row > this.interval || col > this.interval) throw new java.lang.IllegalArgumentException();
        else {
            int n = toOneD(row, col);
            return this.status[n];
        }

    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || col < 1 || row > this.interval || col > this.interval) throw new java.lang.IllegalArgumentException();
        else {
            // if connected to root, yes
            int n = toOneD(row, col);
            return wQUF.connected(n, 0);
        }
    }

    // number of open sites
    public int numberOfOpenSites() {
        return this.openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        // if bottom is connected to top, yes
        return wQUF.connected(0, totalN+1);
    }

    public static void main(String[] args) {

    }
}
