import java.util.*;
public class TestingMatrix {
  public static void main(String[] args) {
    //double[][] entries = {{0,1,0}, {0,0,1}, {1,0,0}};
    /* Matrix A = new Matrix("A", "4x3", true);
    Matrix B = new Matrix("B", "3x4", true);
    A.printMatrix();
    B.printMatrix();
    Matrix product = A.multiplyMatrices(B, "AB");
    product.printMatrix(); */
    Scanner reader = new Scanner(System.in);
    String initialDisplay = "Welcome to the program that lets you work with Matrices!";
    System.out.println(initialDisplay + "\n");
    String[] options = new String[6];
    options[0] = "The list of available methods are:\n 1. printMatrix()\n 2. addMatrices(Matrix secondMatrix, String name, boolean subtract)";
    options[1] = " 3. multiplyByNumber(double constant)\n 4. makeIdentity(int size)";
    options[2] = " 5. raiseToPower(int power)\n 6. multiplyMatrices(Matrix secondMatrix, String productName)";
    options[3] = " 7. int determinant()\n 8. void solutionOfSystem(Matrix constantVals)";
    options[4] = " 9. inverse()\n 10. rowReduce()\n 11. void transpose()";
    options[5] = " 12. Matrix compare(double value)\n 13. add(String rowOrColumn, int position)\n 14. setName(String newName)";
    for(int i = 0; i < options.length; i++) {
      System.out.println(options[i]);
    }
    while(!reader.next().equals("exit")) {
      System.out.println("Random matrix or not? (true/false)");
      boolean random = Boolean.parseBoolean(reader.next());
      System.out.println("Size of matrix (axb)");
      String size = reader.next();
      Matrix C = new Matrix("C", size, random);
      Matrix reduced = C.rowReduce();
      reduced.printMatrix();
    }

  }

  public static double[] addingFractions(double num1, double num2) {
    double[] fraction1 = Matrix.turnIntoFraction(num1);
    double[] fraction2 = Matrix.turnIntoFraction(num2);
    double[] sumOfFractions = new double[2];
    sumOfFractions[0] = fraction1[0] * fraction2[1] + fraction1[1] * fraction2[0];
    sumOfFractions[1] = fraction1[1] * fraction2[1];
    return sumOfFractions;
  }
  
}