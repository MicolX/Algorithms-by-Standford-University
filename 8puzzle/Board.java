
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Board {

    private char[] board;
    private int dimension;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) throw new java.lang.IllegalArgumentException();
        else {
            dimension = blocks[0].length;
            board = new char[dimension*dimension];
            int count = 0;
            for (int row = 0; row < dimension; row++) {
                for (int col = 0; col < dimension; col++) {
                    board[count++] = (char) blocks[row][col];
                }
            }
        }
    }

    private Board(char[] charArray) {
        dimension = (int) Math.sqrt((double) charArray.length);
        board = new char[charArray.length];
        for (int i = 0; i < charArray.length; i++) board[i] = charArray[i];
    }


    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of blocks out of place
    public int hamming() {
        int count = 0;
        int num = 1;
        for (int i = 0; i < dimension*dimension; i++) {
            if ((int) board[i] == 0) {
                num++;
                continue;
            } else {
                if ((int) board[i] != num) count++;
            }
            num++;
        }
        return count;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int count = 0;
        int i = 1;
        for (int num = 0; num < dimension*dimension; num++) {
            if ((int) board[num] == 0) {
                i++;
                continue;
            } else {
                if ((int) board[num] != i) {
                    int correctY = (int) Math.ceil((double) board[num] / (double) dimension);
                    int correctX;
                    if (board[num] % dimension == 0) correctX = dimension;
                    else correctX = board[num] % dimension;

                    int currentY = (int) Math.ceil((double) i / (double) dimension);
                    int currentX;
                    if (i % dimension == 0) currentX = dimension;
                    else currentX = i % dimension;

                    count += Math.abs(correctX - currentX) + Math.abs(correctY - currentY);

                }
            }
            i++;
        }
        return count;
    }

    // is this board the goal board?
    public boolean isGoal() { return hamming() == 0; }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        Board newBoard = new Board(board);
        if (newBoard.board[0] != 0) {
            if (newBoard.board[1] != 0) exchange(newBoard,  0, "right");
            else exchange(newBoard, 0, "down");
        } else exchange(newBoard, dimension,  "right");

        return newBoard;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y.getClass() == this.getClass()) {
            Board bb = (Board) y;
            if (((Board) y).board.length != dimension*dimension) return false;

            for (int i = 0; i < bb.board.length; i++) {
                if (bb.board[i] != this.board[i]) return false;
            }
        } else return false;
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int length = dimension * dimension;
        ArrayList<Board> boards = new ArrayList<>();
        for (int index = 0; index < length; index++) {
            if ((int) board[index] == 0) {
                // upper left corner
                if (index == 0) {
                    boards.add(exchange(new Board(board), index, "down"));
                    boards.add(exchange(new Board(board), index, "right"));
                    break;
                } else if (index == dimension - 1) {    // upper right corner
                    boards.add(exchange(new Board(board), index, "left"));
                    boards.add(exchange(new Board(board), index, "down"));
                    break;
                } else if (index == dimension*dimension - dimension) { // bottom left corner
                    boards.add(exchange(new Board(board), index, "up"));
                    boards.add(exchange(new Board(board), index, "right"));
                    break;
                } else if (index == dimension * dimension - 1) {  // bottom right corner
                    boards.add(exchange(new Board(board), index, "up"));
                    boards.add(exchange(new Board(board), index, "left"));
                    break;
                } else if (index == dimension || index % dimension == 0) {   // left wall
                    boards.add(exchange(new Board(board), index, "up"));
                    boards.add(exchange(new Board(board), index, "down"));
                    boards.add(exchange(new Board(board), index, "right"));
                    break;
                } else if (index < dimension) {   // ceiling
                    boards.add(exchange(new Board(board), index, "left"));
                    boards.add(exchange(new Board(board), index, "down"));
                    boards.add(exchange(new Board(board), index, "right"));
                    break;
                } else if ((index + 1) % dimension == 0) {  // right wall
                    boards.add(exchange(new Board(board), index, "up"));
                    boards.add(exchange(new Board(board), index, "left"));
                    boards.add(exchange(new Board(board), index, "down"));
                    break;
                } else if (dimension*dimension - dimension <= index && index < dimension*dimension) {  // bottom
                    boards.add(exchange(new Board(board), index, "up"));
                    boards.add(exchange(new Board(board), index, "left"));
                    boards.add(exchange(new Board(board), index, "right"));
                    break;
                } else {  // middle
                    boards.add(exchange(new Board(board), index, "up"));
                    boards.add(exchange(new Board(board), index, "left"));
                    boards.add(exchange(new Board(board), index, "down"));
                    boards.add(exchange(new Board(board), index, "right"));
                    break;
                }
            }
        }
        return boards;
    }

    private Board exchange(Board theBoard, int index, String direction) {
        switch (direction) {
            case "up":
                if (index >= dimension) {
                    char temp = theBoard.board[index];
                    theBoard.board[index] = theBoard.board[index - dimension];
                    theBoard.board[index - dimension] = temp;
                } else throw new java.lang.ArrayIndexOutOfBoundsException("no way up");
                break;
            case "left":
                if (index == 0 || index == dimension || index % dimension == 0) throw new java.lang.ArrayIndexOutOfBoundsException("no way to left");
                else {
                    char temp = theBoard.board[index];
                    theBoard.board[index] = theBoard.board[index-1];
                    theBoard.board[index-1] = temp;
                }
                break;
            case "right":
                if ((index + 1) % dimension == 0) throw new java.lang.ArrayIndexOutOfBoundsException("no way to right");
                else {
                    char temp = theBoard.board[index];
                    theBoard.board[index] = theBoard.board[index+1];
                    theBoard.board[index+1] = temp;
                }
                break;
            case "down":
                if (index + dimension < dimension * dimension) {
                    char temp = theBoard.board[index];
                    theBoard.board[index] = theBoard.board[index + dimension];
                    theBoard.board[index + dimension] = temp;
                } else throw new java.lang.ArrayIndexOutOfBoundsException("no way down");
                break;
            default:
                throw new java.lang.IllegalArgumentException();
        }
        return theBoard;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension());
        sb.append("\n");
        for (int a = 0; a < board.length; a++) {
            sb.append(" ");
            sb.append((int) board[a]);
            if ((a+1) % dimension == 0) {
                sb.append("\n");
            } else {
                if ((int) board[a] < 10) sb.append(" ");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial.manhattan());
    }

}