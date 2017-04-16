import java.util.*;
public class TestingMatrix {
  public static void main(String[] args) {    
    Matrix A = new Matrix("A", "5x4", true);
    A.printMatrix();
    A.sort("r", 3).printMatrix();
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