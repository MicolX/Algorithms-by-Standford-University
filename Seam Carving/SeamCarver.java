import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;


public class SeamCarver {

    private static final double BORDER_ENERGY = 1000.0;
    private int[][] picture;
    private double[][] energy;
    private boolean isTransposed = false;
    private boolean isForVertical = false;


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new java.lang.IllegalArgumentException();
        this.energy = new double[picture.width()][picture.height()];
        this.picture = new int[picture.width()][picture.height()];

        for (int y = 0; y < picture.height(); y++) {
            for (int x = 0; x < picture.width(); x++) {
                this.picture[x][y] = picture.getRGB(x, y);
            }
        }

        for (int y = 0; y < picture.height(); y++) {
            for (int x = 0; x < picture.width(); x++) {
                this.energy[x][y] = energy(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        if (isTransposed) transpose();
        Picture pic = new Picture(width(), height());
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                pic.setRGB(x, y, picture[x][y]);
            }
        }
        return pic;
    }

    // width of current picture
    public int width() {
        return picture.length;
    }

    // height of current picture
    public int height() {
        return picture[0].length;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x >= width() || y >= height()) throw new java.lang.IllegalArgumentException();

        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) return BORDER_ENERGY;

        double redLeft = (picture[x-1][y] >> 16) & 0xFF;
        double greenLeft = (picture[x-1][y] >> 8) & 0xFF;
        double blueLeft = picture[x-1][y] & 0xFF;

        double redRight = (picture[x+1][y] >> 16) & 0xFF;
        double greenRight = (picture[x+1][y] >> 8) & 0xFF;
        double blueRight = picture[x+1][y] & 0xFF;

        double redUp = (picture[x][y-1] >> 16) & 0xFF;
        double greenUp = (picture[x][y-1] >> 8) & 0xFF;
        double blueUp = picture[x][y-1] & 0xFF;

        double redDown = (picture[x][y+1] >> 16) & 0xFF;
        double greenDown = (picture[x][y+1] >> 8) & 0xFF;
        double blueDown = picture[x][y+1] & 0xFF;

        double rowDiff = (redRight - redLeft) * (redRight - redLeft) + (greenRight - greenLeft) * (greenRight - greenLeft) + (blueRight - blueLeft) * (blueRight - blueLeft);
        double colDiff = (redUp - redDown) * (redUp - redDown) + (greenUp - greenDown) * (greenUp - greenDown) + (blueUp - blueDown) * (blueUp - blueDown);

        return Math.sqrt(rowDiff + colDiff);
    }

    // sequence of indices for isTransposed seam
    public int[] findHorizontalSeam() {
        if ((!isForVertical && isTransposed) || (isForVertical && !isTransposed)) transpose();

        int[] seam = new int[width()];

        if (height() == 1) return seam;

        int[][] edgeTo = new int[width()][height()];

        double[][] distTo = new double[width()][height()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (x == 0) distTo[x][y] = BORDER_ENERGY;
                else distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                // relax
                if (x == width() - 1) break;

                if (distTo[x+1][y] > distTo[x][y] + energy[x+1][y]) {
                    distTo[x+1][y] = distTo[x][y] + energy[x+1][y];
                    edgeTo[x+1][y] = y;
                }

                if (y == 0) {
                    if (distTo[x+1][y+1] > distTo[x][y] + energy[x+1][y+1]) {
                        distTo[x+1][y+1] = distTo[x][y] + energy[x+1][y+1];
                        edgeTo[x+1][y+1] = y;
                    }
                } else if (y == height() - 1) {
                    if (distTo[x+1][y-1] > distTo[x][y] + energy[x+1][y-1]) {
                        distTo[x+1][y-1] = distTo[x][y] + energy[x+1][y-1];
                        edgeTo[x+1][y-1] = y;
                    }
                } else {
                    if (distTo[x+1][y-1] > distTo[x][y] + energy[x+1][y-1]) {
                        distTo[x+1][y-1] = distTo[x][y] + energy[x+1][y-1];
                        edgeTo[x+1][y-1] = y;
                    }

                    if (distTo[x+1][y+1] > distTo[x][y] + energy[x+1][y+1]) {
                        distTo[x+1][y+1] = distTo[x][y] + energy[x+1][y+1];
                        edgeTo[x+1][y+1] = y;
                    }
                }
            }
        }

        double min = Double.POSITIVE_INFINITY;
        int end = 0;
        for (int i = 1; i < height() - 1; i++) {
            if (distTo[width()-1][i] < min) {
                end = i;
                min = distTo[width()-1][i];
            }
        }

        for (int j = width()-1; j >= 0; j--) {
            seam[j] = end;
            end = edgeTo[j][end];
        }

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
//        int[] seam = new int[height()];
//
//        if (width() == 1) return seam;
//
//        int[][] edgeTo = new int[width()][height()];
//
//        double[][] distTo = new double[width()][height()];
//        for (int y = 0; y < height(); y++) {
//            for (int x = 0; x < width(); x++) {
//                if (y == 0) distTo[x][y] = BORDER_ENERGY;
//                else distTo[x][y] = Double.POSITIVE_INFINITY;
//            }
//        }
//
//        for (int y = 0; y < height(); y++) {
//            for (int x = 0; x < width(); x++) {
//                // relax
//                if (y == height() - 1) break;
//
//                if (distTo[x][y+1] > distTo[x][y] + energy[x][y+1]) {
//                    distTo[x][y+1] = distTo[x][y] + energy[x][y+1];
//                    edgeTo[x][y+1] = x;
//                }
//
//                if (x == 0) {
//                    if (distTo[x+1][y+1] > distTo[x][y] + energy[x+1][y+1]) {
//                        distTo[x+1][y+1] = distTo[x][y] + energy[x+1][y+1];
//                        edgeTo[x+1][y+1] = x;
//                    }
//                } else if (x == width() - 1) {
//                    if (distTo[x-1][y+1] > distTo[x][y] + energy[x-1][y+1]) {
//                        distTo[x-1][y+1] = distTo[x][y] + energy[x-1][y+1];
//                        edgeTo[x-1][y+1] = x;
//                    }
//                } else {
//                    if (distTo[x-1][y+1] > distTo[x][y] + energy[x-1][y+1]) {
//                        distTo[x-1][y+1] = distTo[x][y] + energy[x-1][y+1];
//                        edgeTo[x-1][y+1] = x;
//                    }
//
//                    if (distTo[x+1][y+1] > distTo[x][y] + energy[x+1][y+1]) {
//                        distTo[x+1][y+1] = distTo[x][y] + energy[x+1][y+1];
//                        edgeTo[x+1][y+1] = x;
//                    }
//                }
//            }
//        }
//
//        double min = Double.POSITIVE_INFINITY;
//        int end = 0;
//        for (int i = 1; i < width() - 1; i++) {
//            if (distTo[i][height()-1] < min) {
//                end = i;
//                min = distTo[i][height()-1];
//            }
//        }
//
//        for (int j = height()-1; j >= 0; j--) {
//            seam[j] = end;
//            end = edgeTo[end][j];
//        }
//
//        return seam;

        isForVertical = true;
        if (!isTransposed) transpose();
        int[] seam = findHorizontalSeam();
        isForVertical = false;
        return seam;

    }


    // remove isTransposed seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if ((!isForVertical && isTransposed) || (isForVertical && !isTransposed)) transpose();

        check(seam);

        int[][] newPic = new int[width()][height()-1];
        double[][] newEnergy = new double[width()][height()-1];

        for (int x = 0; x < width(); x++) {
            System.arraycopy(picture[x], 0, newPic[x], 0, seam[x]);
            System.arraycopy(picture[x], seam[x]+1, newPic[x], seam[x], picture[x].length - seam[x] - 1);

            System.arraycopy(energy[x], 0, newEnergy[x], 0, seam[x]);
            System.arraycopy(energy[x], seam[x]+1, newEnergy[x], seam[x], energy[x].length - seam[x] - 1);
        }

        this.picture = newPic;


        // recalculate the energy of the seam
        for (int x = 1; x < width() - 1; x++) {
            if (seam[x] == 0) newEnergy[x][seam[x]] = 1000.0;
            else if (seam[x] == height() - 1) {
                newEnergy[x][seam[x]] = 1000.0;
                newEnergy[x][seam[x]-1] = energy(x, seam[x]-1);
            } else {
                newEnergy[x][seam[x]-1] = energy(x, seam[x]-1);
                newEnergy[x][seam[x]] = energy(x, seam[x]);
            }

        }

        this.energy = newEnergy;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        isForVertical = true;
        if (!isTransposed) transpose();
        removeHorizontalSeam(seam);
        isForVertical = false;
    }

    private void check(int[] seam) {

        if (height() <= 1) throw new java.lang.IllegalArgumentException("height <= 1 !");

        if (seam == null) throw new java.lang.IllegalArgumentException("seam is null!");
     //   if (seam.length < 2) throw new java.lang.IllegalArgumentException("seam.length is less than 2!");
        if (seam.length != width()) throw new java.lang.IllegalArgumentException("seam.length is not equal to width!");


        for (int i = 1; i < seam.length; i++) {
            if (seam[0] < 0 || seam[i] < 0) throw new java.lang.IllegalArgumentException("elements in seam is less than 0");
            if (seam[i] >= height()) throw new java.lang.IllegalArgumentException("element in seam is larger than height");
            if (Math.abs(seam[i] - seam[i - 1]) > 1) throw new java.lang.IllegalArgumentException("difference between elements in seam is larger than 1");

        }

    }

    private void transpose() {
     //   StdOut.println("transpose!");
        isTransposed = !isTransposed;

        int[][] after = new int[picture[0].length][picture.length];
        double[][] newEnergy = new double[picture[0].length][picture.length];

        for (int y = 0; y < picture[0].length; y++) {
            for (int x = 0; x < picture.length; x++) {
                after[y][x] = picture[x][y];
                newEnergy[y][x] = energy[x][y];
            }
        }
        picture = after;
        energy = newEnergy;
    }

    private void printEnergy() {
        if (isTransposed) transpose();
        for (int row = 0; row < energy[0].length; row++) {
            for (int col = 0; col < energy.length; col++) {
                StdOut.printf("%9.2f", energy[col][row]);
                if (col == energy.length - 1) StdOut.println();
            }
        }
        StdOut.println();
    }

    private void printSeam(int[] s) {
        StdOut.print("[ ");
        for (int i : s) StdOut.printf("%d ", i);
        StdOut.print("]");
        StdOut.println();
    }


    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);

        SeamCarver carver = new SeamCarver(picture);
        StdOut.println("width = "+carver.width()+" height = "+carver.height());

        carver.printEnergy();

        int[] a = new int[carver.width()];
        carver.removeHorizontalSeam(a);

        StdOut.println("width = "+carver.width()+" height = "+carver.height());
        carver.printEnergy();


//        StdOut.println("OPERATION ONE");
//
//        StdOut.println("find vertical seam.....");
//
//        int[] one = carver.findVerticalSeam();
//
//        carver.printSeam(one);
//
//        StdOut.println("remove vertical seam......");
//
//        carver.removeVerticalSeam(one);
//
//        carver.printEnergy();
//
//        StdOut.println("find horizontal seam.....");
//
//        int[] two = carver.findHorizontalSeam();
//
//        carver.printSeam(two);
//
//        StdOut.println("remove horizontal seam.....");
//
//        carver.removeHorizontalSeam(two);
//
//        carver.printEnergy();
//
//        StdOut.println("OPERATION TWO");
//
//        StdOut.println("find vertical seam.....");
//
//        int[] three = carver.findVerticalSeam();
//
//        carver.printSeam(three);
//
//        StdOut.println("remove vertical seam......");
//
//        carver.removeVerticalSeam(three);
//
//        carver.printEnergy();
//
//        StdOut.println("find horizontal seam.....");
//
//        int[] four = carver.findHorizontalSeam();
//
//        carver.printSeam(four);
//
//        StdOut.println("remove horizontal seam.....");
//
//        carver.removeHorizontalSeam(four);
//
//        carver.printEnergy();
//
//        StdOut.println("OPERATION THREE");
//
//        StdOut.println("find vertical seam.....");
//
//        int[] five = carver.findVerticalSeam();
//
//        carver.printSeam(five);
//
//        StdOut.println("remove vertical seam......");
//
//        carver.removeVerticalSeam(five);
//
//        carver.printEnergy();
//
//        StdOut.println("find horizontal seam.....");
//
//        int[] six = carver.findHorizontalSeam();
//
//        carver.printSeam(six);
//
//        StdOut.println("remove horizontal seam.....");
//
//        carver.removeHorizontalSeam(six);
//
//        carver.printEnergy();

//        for (int j = 0; j < 2; j++) {
//            int[] seam = carver.findVerticalSeam();
//
//            StdOut.printf("find vertical seam: [ ");
//            for (int e : seam) StdOut.printf("%d ", e);
//            StdOut.printf("]");
//            StdOut.println();
//
//            StdOut.println("before vertical remove:");
//            carver.printEnergy(carver.energy);
//            carver.removeVerticalSeam(seam);
//            StdOut.println("after vertical remove:");
//            carver.printEnergy(carver.energy);
//        }
//
//        for (int i = 0; i < 3; i++) {
//            int[] seam = carver.findHorizontalSeam();
//
//            StdOut.printf("find Horizontal seam: [ ");
//            for (int e : seam) StdOut.printf("%d ", e);
//            StdOut.printf("]");
//            StdOut.println();
//
//            StdOut.println("before Horizontal remove:");
//            carver.printEnergy(carver.energy);
//            carver.removeHorizontalSeam(seam);
//            StdOut.println("after Horizontal remove:");
//            carver.printEnergy(carver.energy);
//        }


    }
}
