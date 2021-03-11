package pagerank;

import Jama.Matrix;

import java.text.DecimalFormat;

public class Main {
    // test matrix for use to check against examples in project descriptions
    private static final double[][] testL = new double[][]{
            {        0, 1.0 / 2.0, 1.0 / 3.0,         0,         0,         0},
            {1.0 / 3.0,         0,         0,         0, 1.0 / 2.0, 1.0 / 3.0},
            {1.0 / 3.0, 1.0 / 2.0,         0,       1.0,         0,         0},
            {1.0 / 3.0,         0, 1.0 / 3.0,         0, 1.0 / 2.0, 1.0 / 3.0},
            {        0,         0,         0,         0,         0, 1.0 / 3.0},
            {        0,         0, 1.0 / 3.0,         0,         0,         0}
    };

    // real matrix for use in actual testing to pass tests
    private static final double[][] realL = new double[][]{
            {        0, 1.0 / 2.0, 1.0 / 3.0,         0,         0,         0},
            {1.0 / 3.0,         0,         0,         0, 1.0 / 2.0,         0},
            {1.0 / 3.0, 1.0 / 2.0,         0,       1.0,         0, 1.0 / 2.0},
            {1.0 / 3.0,         0, 1.0 / 3.0,         0, 1.0 / 2.0, 1.0 / 2.0},
            {        0,         0,         0,         0,         0,         0},
            {        0,         0, 1.0 / 3.0,         0,         0,         0}
    };

    public static void main(String[] args) {
        // distribute 100 users across six websites
        double[] users = new double[6];
        for (int i = 0; i < users.length; i++) {
            users[i] = 1.00;
            users[i] = 100 * users[i] / 6;
        }

        Matrix r = new Matrix(users, 1);
        Matrix L = new Matrix(realL);

        r = iterateMatrixNTimes(r, L, 1);
        print1DMatrix(r);
        r = iterateMatrixNTimes(r, L, 10);
        print1DMatrix(r);
        r = iterateMatrixToNorm(r, L);
        print1DMatrix(r);
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
