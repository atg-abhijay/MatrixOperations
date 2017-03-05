public class TestingMatrix {
  public static void main(String[] args) {
    double[][] entries = {{0,1,0}, {0,0,1}, {1,0,0}};
    Matrix A = new Matrix("A", "4x3", true);
    Matrix B = new Matrix("B", "3x4", true);
    A.printMatrix();
    B.printMatrix();
    Matrix product = A.multiplyMatrices(B, "AB");
    product.printMatrix();
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