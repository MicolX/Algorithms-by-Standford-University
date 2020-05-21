/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {

    private double percolationCount = 0.0;
    private int trials;
    private double[] percs;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new java.lang.IllegalArgumentException();
        else {
            this.trials = trials;
            this.percs = new double[trials];
            for (int i = 0; i < trials; i++) {
                Percolation perc = new Percolation(n);
                while (!perc.percolates()) {
                    perc.open(StdRandom.uniform(1, n+1), StdRandom.uniform(1, n+1));
                }
                percolationCount += (double) perc.numberOfOpenSites() / n * n;
                this.percs[i] = (double) perc.numberOfOpenSites() / n * n;
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return this.percolationCount / this.trials;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double total = 0.0;
        for (int i = 0; i < this.percs.length; i++) {
            total += (this.percs[i] - mean()) * (this.percs[i] - mean());
        }
        return Math.sqrt(total / (this.trials - 1));
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(this.trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(this.trials);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, t);
        System.out.printf("mean = %f\n", ps.mean());
        System.out.printf("stddev = %f\n", ps.stddev());
        System.out.printf("95%% confidence interval = [%f, %f]", ps.confidenceLo(), ps.confidenceHi());
    }
}
