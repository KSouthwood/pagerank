package pagerank;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice = -1;
        while (choice != 0) {
            System.out.println("Which stage do you wish to run:");
            System.out.println("1. Stage 1 (N/A)");
            System.out.println("2. Stage 2");
            System.out.println("3. Stage 3");
            System.out.println("4. Stage 4");
            System.out.println("5. Stage 5");
            System.out.println("0. Quit");

            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 0:
                    System.out.println("Thank you and goodbye!");
                    break;
                case 1:
                    stage1();
                    break;
                case 2:
                    powerIterateStage2();
                    break;
                case 3:
                    powerIterateStage3();
                    break;
                case 4:
                    stage4();
                    break;
                case 5:
                    stage5();
                    break;
                default:
                    System.out.println("Invalid choice. Choose again.");
                    break;
            }
        }
    }

    /**
     * Output the matrix, then output the eigenvalues
     */
    private static void stage1() {
        Matrix m = new Matrix(Main.L1);
        m.print(0, 3);
        System.out.println();

        // find eigenvalues
        EigenvalueDecomposition eigen = m.eig();
        final double[] realPart = eigen.getRealEigenvalues();
        // find eigenvectors
        Matrix eVectors = eigen.getV();

        // find value of 1 in realPart
        int colToGet = -1;
        for (int col = 0; col < realPart.length; col++) {
            if (Math.abs(realPart[col] - 1.0) < 1E-3) {
                colToGet = col;
                break;
            }
        }

        double[] principalEVector = new double[eVectors.getRowDimension()];
        for (int row = 0; row < eVectors.getRowDimension(); row++) {
            principalEVector[row] = eVectors.get(row, colToGet);
        }

        double sum = 0.00;
        for (double value : principalEVector) {
            sum += value;
        }

        double[] res = new double[principalEVector.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = 100 * principalEVector[i] / sum;
        }

        for (double x : res) {
            System.out.println(new DecimalFormat("#0.000").format(x));
        }
    }

    /**
     * Iterate the matrix once, 10 more times, then to norm - printing out after each step.
     *
     */
    private static void powerIterateStage2() {
        PageRank pr = new PageRank(Main.L1);
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
     */
    private static void powerIterateStage3() {
        double d = 0.5;
        PageRank pr = new PageRank(Main.L2);  // un-damped pagerank
        pr.printL();
        pr.iterateMatrixToNorm();
        pr.print();
        pr = new PageRank(Main.L2); // reset so we can damp it
        pr.dampMatrix(d);
        pr.iterateMatrixToNorm();
        pr.print();
    }

    /**
     * Read in the dimension of the matrix (an integer), the damping parameter (float), followed by reading in the
     * matrix line-by-line. Damp the matrix and iterate to the norm and output the result.
     */
    private static void stage4() {
        String[] input = scanner.nextLine().split("\\s");
        int dim = Integer.parseInt(input[0]);
        double damping = Double.parseDouble(input[1]);

        double[][] matrix = readMatrixFromInput(dim);

        PageRank pr = new PageRank(matrix);
        pr.dampMatrix(damping);
        pr.iterateMatrixToNorm();
        pr.print();
    }

    private static double[][] readMatrixFromInput(int dim) {
        String[] input;
        double[][] matrix = new double[dim][dim];
        for (int row = 0; row < dim; row++) {
            input = scanner.nextLine().split("\\s");
            for (int col = 0; col < dim; col++) {
                matrix[row][col] = Double.parseDouble(input[col]);
            }
        }
        return matrix;
    }

    private static void stage5() {
        int inputs = Integer.parseInt(scanner.nextLine());

        String[] website = scanner.nextLine().split("\\s");
        if (website.length != inputs) {
            System.out.println("Amount of websites doesn't match the expected number.");
            return;
        }

        double[][] matrix = readMatrixFromInput(inputs);

        String siteQuery = scanner.next();

        PageRank pr = new PageRank(matrix);
        pr.dampMatrix(0.5);
        pr.iterateMatrixToNorm();

        List<Map.Entry<String, Double>> rankings = new ArrayList<>();
        double[] pageRank = pr.getMatrix();
        int printOut = 5;

        for (int index = 0; index < inputs; index++) {
            if (!website[index].contains(siteQuery)) {
                rankings.add(Map.entry(website[index], pageRank[index]));
            } else {
                System.out.println(website[index]);
                printOut--;
            }

        }

        Comparator<Map.Entry<String, Double>> comparator =
                Map.Entry.<String, Double>comparingByValue().reversed().
                        thenComparing(Map.Entry.<String, Double>comparingByKey().reversed());
        rankings.sort(comparator);

        printOut = Math.min(printOut, rankings.size());
        for (int index = 0; index < printOut; index++) {
            System.out.println(rankings.get(index).getKey());
        }
    }
}