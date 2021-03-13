package pagerank;

import Jama.Matrix;
import java.text.DecimalFormat;
import java.util.Arrays;

public class Main {
    // test matrix for use to check against examples in project descriptions
    private static final double[][] L0 = new double[][]{
            {        0, 1.0 / 2.0, 1.0 / 3.0,         0,         0,         0},
            {1.0 / 3.0,         0,         0,         0, 1.0 / 2.0, 1.0 / 3.0},
            {1.0 / 3.0, 1.0 / 2.0,         0,       1.0,         0,         0},
            {1.0 / 3.0,         0, 1.0 / 3.0,         0, 1.0 / 2.0, 1.0 / 3.0},
            {        0,         0,         0,         0,         0, 1.0 / 3.0},
            {        0,         0, 1.0 / 3.0,         0,         0,         0}
    };

    // real matrix for use in actual testing to pass tests (Stage 1 & 2)
    private static final double[][] L1 = new double[][]{
            {        0, 1.0 / 2.0, 1.0 / 3.0,         0,         0,         0},
            {1.0 / 3.0,         0,         0,         0, 1.0 / 2.0,         0},
            {1.0 / 3.0, 1.0 / 2.0,         0,       1.0,         0, 1.0 / 2.0},
            {1.0 / 3.0,         0, 1.0 / 3.0,         0, 1.0 / 2.0, 1.0 / 2.0},
            {        0,         0,         0,         0,         0,         0},
            {        0,         0, 1.0 / 3.0,         0,         0,         0}
    };

    // real matrix for use in actual testing to pass tests (Stage 3)
    private static final double[][] L2 = new double[][]{
            {        0, 1.0 / 2.0, 1.0 / 3.0,         0,         0,         0,         0},
            {1.0 / 3.0,         0,         0,         0, 1.0 / 2.0,         0,         0},
            {1.0 / 3.0, 1.0 / 2.0,         0,       1.0,         0, 1.0 / 3.0,         0},
            {1.0 / 3.0,         0, 1.0 / 3.0,         0, 1.0 / 2.0, 1.0 / 3.0,         0},
            {        0,         0,         0,         0,         0,         0,         0},
            {        0,         0, 1.0 / 3.0,         0,         0,         0,         0},
            {        0,         0,         0,         0,         0, 1.0 / 3.0,       1.0}
    };

    public static void main(String[] args) {
        double[][] lToUse = L2;
        // distribute 100 users across the number of websites in the matrix
        double[] users = new double[lToUse.length];
        for (int i = 0; i < users.length; i++) {
            users[i] = 1.00;
            users[i] = 100 * users[i] / lToUse.length;
        }

        Matrix r = new Matrix(users, 1);
        Matrix L = new Matrix(lToUse);

//        powerIterateStageTwo(r, L);
        powerIterateStage3(r, L);
    }

    /**
     * Iterate the matrix once, 10 more times, then to norm - printing out after each step.
     * @param r - initial users per website, usually evenly spread out across all
     * @param l - probabilities of moving from one website to another
     */
    private static void powerIterateStageTwo(Matrix r, Matrix l) {
        r = iterateMatrixNTimes(r, l, 1);
        print1DMatrix(r);
        r = iterateMatrixNTimes(r, l, 10);
        print1DMatrix(r);
        r = iterateMatrixToNorm(r, l);
        print1DMatrix(r);
    }

    /**
     * Iterate the matrix to norm, first without damping the probability matrix, then after damping the matrix.
     * Print out the probability matrix, then the PageRank vectors without and with damping.
     * @param r - initial users per website
     * @param l - probabilities of moving from one website to another
     */
    private static void powerIterateStage3(Matrix r, Matrix l) {
        double[][] j = new double[l.getRowDimension()][l.getColumnDimension()];
        for (double[] row : j) {
            Arrays.fill(row, 1.0);
        }
        Matrix J = new Matrix(j);

        double d = 0.5; // damping value

        // J matrix times 1 minus damping value divided by number of sites
        Matrix M0 = J.times((1.0 - d) / J.getRowDimension());
        Matrix M1 = l.times(d); // probability matrix times damping value
        Matrix M = M1.plus(M0); // add the two to get our new damped probability matrix
        Matrix undamped = iterateMatrixToNorm(r, l);
        Matrix damped = iterateMatrixToNorm(r, M);
        l.print(0, 3);
        undamped.print(0, 3);
        damped.print(0, 3);
    }

    private static void print1DMatrix(Matrix pMatrix) {
        double[] res = pMatrix.getColumnPackedCopy();
        for (double x : res) {
            System.out.println(new DecimalFormat("#0.000").format(x));
        }
        System.out.println();
    }

    private static Matrix iterateMatrixNTimes(Matrix r, Matrix l, int iterations) {
        Matrix r0 = r;
        if (r.getRowDimension() == 1) {
            r0 = r.transpose();
        }

        for (int i = 0; i < iterations; i++) {
            r0 = l.times(r0);
        }
        return r0;
    }

    private static Matrix iterateMatrixToNorm(Matrix r, Matrix l) {
        Matrix prevR = r;
        if (r.getRowDimension() == 1) {
            prevR = r.transpose();
        }

        Matrix nextR = null;
        boolean iterate = true;

        while (iterate) {
            nextR = iterateMatrixNTimes(prevR, l, 1);
            if (prevR.minus(nextR).normInf() < 0.01) {
                iterate = false;
            }
            prevR = nextR;
        }

        return nextR;
    }
}
