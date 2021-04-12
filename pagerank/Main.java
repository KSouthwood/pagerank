package pagerank;

import java.util.Scanner;

public class Main {
    // test matrix for use to check against examples in project descriptions
    private static final double[][] L0 = new double[][]{
            {0, 1.0 / 2.0, 1.0 / 3.0, 0, 0, 0},
            {1.0 / 3.0, 0, 0, 0, 1.0 / 2.0, 1.0 / 3.0},
            {1.0 / 3.0, 1.0 / 2.0, 0, 1.0, 0, 0},
            {1.0 / 3.0, 0, 1.0 / 3.0, 0, 1.0 / 2.0, 1.0 / 3.0},
            {0, 0, 0, 0, 0, 1.0 / 3.0},
            {0, 0, 1.0 / 3.0, 0, 0, 0}
    };

    // real matrix for use in actual testing to pass tests (Stage 1 & 2)
    private static final double[][] L1 = new double[][]{
            {0, 1.0 / 2.0, 1.0 / 3.0, 0, 0, 0},
            {1.0 / 3.0, 0, 0, 0, 1.0 / 2.0, 0},
            {1.0 / 3.0, 1.0 / 2.0, 0, 1.0, 0, 1.0 / 2.0},
            {1.0 / 3.0, 0, 1.0 / 3.0, 0, 1.0 / 2.0, 1.0 / 2.0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 1.0 / 3.0, 0, 0, 0}
    };

    // real matrix for use in actual testing to pass tests (Stage 3)
    private static final double[][] L2 = new double[][]{
            {0, 1.0 / 2.0, 1.0 / 3.0, 0, 0, 0, 0},
            {1.0 / 3.0, 0, 0, 0, 1.0 / 2.0, 0, 0},
            {1.0 / 3.0, 1.0 / 2.0, 0, 1.0, 0, 1.0 / 3.0, 0},
            {1.0 / 3.0, 0, 1.0 / 3.0, 0, 1.0 / 2.0, 1.0 / 3.0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1.0 / 3.0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1.0 / 3.0, 1.0}
    };

    public static void main(String[] args) {
//        powerIterateStageTwo(L1);
//        powerIterateStage3(L2);
        stage4();
    }

    /**
     * Iterate the matrix once, 10 more times, then to norm - printing out after each step.
     *
     * @param l - probabilities of moving from one website to another
     */
    private static void powerIterateStage2(double[][] l) {
        PageRank pr = new PageRank(l);
        pr.iterateMatrixNTimes(1);
        pr.print();
        pr.iterateMatrixNTimes(10);
        pr.print();
        pr.iterateMatrixToNorm();
        pr.print();
    }

    /**
     * Iterate the matrix to norm, first without damping the probability matrix, then after damping the matrix.
     * Print out the probability matrix, then the PageRank vectors without and with damping.
     *
     * @param l - probabilities of moving from one website to another
     */
    private static void powerIterateStage3(double[][] l) {
        double d = 0.5;
        PageRank pr = new PageRank(l);  // un-damped pagerank
        pr.printL();
        pr.iterateMatrixToNorm();
        pr.print();
        pr = new PageRank(l); // reset so we can damp it
        pr.dampMatrix(d);
        pr.iterateMatrixToNorm();
        pr.print();
    }

    /**
     * Read in the dimension of the matrix (an integer), the damping parameter (float), followed by reading in the
     * matrix line-by-line. Damp the matrix and iterate to the norm and output the result.
     */
    private static void stage4() {
        final Scanner scanner = new Scanner(System.in);
        String[] input = scanner.nextLine().split("\\s");
        int dim = Integer.parseInt(input[0]);
        double damping = Double.parseDouble(input[1]);

        double[][] matrix = new double[dim][dim];
        for (int row = 0; row < dim; row++) {
            input = scanner.nextLine().split("\\s");
            for (int col = 0; col < dim; col++) {
                matrix[row][col] = Double.parseDouble(input[col]);
            }
        }

        PageRank pr = new PageRank(matrix);
        pr.dampMatrix(damping);
        pr.iterateMatrixToNorm();
        pr.print();
    }
}