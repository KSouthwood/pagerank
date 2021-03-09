package pagerank;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import java.text.DecimalFormat;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Ranking r = new Ranking();
        r.printMatrix();
        System.out.println();
        r.printEigenvalues();
    }
}

class Ranking {
    private double[][] L;

    Ranking() {
        L = new double[][]{
                {0, 1.0 / 2.0, 1.0 / 3.0, 0, 0, 0},
                {1.0 / 3.0, 0, 0, 0, 1.0 / 2.0, 0},
                {1.0 / 3.0, 1.0 / 2.0, 0, 1.0, 0, 1.0 / 2.0},
                {1.0 / 3.0, 0, 1.0 / 3.0, 0, 1.0 / 2.0, 1.0 / 2.0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 1.0 / 3.0, 0, 0, 0}
                };
        }

    public void printMatrix() {
        Matrix m = new Matrix(L);
        m.print(0, 3);
    }

    public void printEigenvalues() {
        Matrix m = new Matrix(L);
        // find eigenvalues
        EigenvalueDecomposition eigen = m.eig();
        final double[] realPart = eigen.getRealEigenvalues();
        // find eigenvectors
        Matrix evectors = eigen.getV();
//        System.out.println(Arrays.toString(realPart));
//        evectors.print(5, 3);

        // find value of 1 in realPart
        int colToGet = -1;
        for (int col = 0; col < realPart.length; col++) {
            if (Math.abs(realPart[col] - 1.0) < 1E-3) {
                colToGet = col;
                break;
            }
        }

        double[] principalEvector = new double[evectors.getRowDimension()];
        for (int row = 0; row < evectors.getRowDimension(); row++) {
            principalEvector[row] = evectors.get(row, colToGet);
        }

        double sum = 0.00;
        for (double value : principalEvector) {
            sum += value;
        }

        double[] res = new double[principalEvector.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = 100 * principalEvector[i] / sum;
        }

        for (double x : res) {
            System.out.println(new DecimalFormat("#0.000").format(x));
        }

    }
}