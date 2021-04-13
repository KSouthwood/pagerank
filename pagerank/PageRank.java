package pagerank;

import Jama.Matrix;
import java.util.Arrays;

public class PageRank {
    private static final double PRECISION = 0.01;
    private static final int USERS = 100;
    private Matrix L;   // Probability matrix of users moving websites
    private Matrix r;   // Matrix for initial set of evenly distributed users
    private final Matrix J;   // Matrix of all 1.00 used in calculating damped matrix
    private final int rows;

    PageRank(double[][] probabilities) {
        rows = probabilities.length;
        int cols = probabilities[0].length;
        L = new Matrix(probabilities);
        J = new Matrix(rows, cols, 1.00);
        distributeUsers(probabilities.length);
    }

    private void distributeUsers(int len) {
        double[] users = new double[len];
        Arrays.fill(users, USERS * 1.00 / len);
        r = new Matrix(users, 1).transpose();
    }

    public void iterateMatrixNTimes(int iterations) {
        while (iterations > 0) {
            r = L.times(r);
            iterations--;
        }
    }

    /**
     * Iterate the matrix until the difference between the current and previous iteration is less
     * than the defined precision
     */
    public void iterateMatrixToNorm() {
        Matrix nextR;
        boolean iterate = true;

        while (iterate) {
            nextR = L.times(r);
            if (r.minus(nextR).normInf() < PRECISION) {
                iterate = false;
            }
            r = nextR;
        }
    }

    /**
     * Transform the probability matrix using a damping value
     * @param d - damping value to use
     */
    public void dampMatrix(double d) {
        L = L.times(d)
                .plus(J.times((1.0 - d) / rows));
    }

    public void print() {
        r.print(0, 3);
    }

    public void printL() {
        L.print(0, 3);
    }

    double[] getMatrix() {
        return r.getRowPackedCopy();
    }
}
