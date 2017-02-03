public class TestingMatrix {
  public static void main(String[] args) {
    double[][] entries = {{1,3,5,0,0}, {9,9,7,8,2}, {8,6,6,2,1}};
    Matrix A = new Matrix("A", "3x4", true);
    A.setName("First Time");
    A.printMatrix();
    System.out.println();
    /* A.transpose();
    A.printMatrix();
    System.out.println(); */
    //Matrix C = A.compare(30); */
    //C.printMatrix();
    Matrix D = A.add("r", 2);
    D.printMatrix();
    Matrix E = D.add("c", 3);
    E.printMatrix();

    /* Matrix B = A.rowReduce();
    //B.transpose();
    B.printMatrix();
    System.out.println(); */

    /* A.transpose();
    Matrix C = A.rowReduce();
    C.printMatrix();
    System.out.println(); */

    //System.out.println(1.0/11);
    /* double[] result = Matrix.gcd(-48, 11);
    System.out.println(result[0] + "/" + result[1]); */

    /* double test = 18.0/19;
    System.out.println(test);
    double[] fraction = Matrix.turnIntoFraction(test);
    System.out.println((int) fraction[0] + "/" + (int) fraction[1]); */
    /* double[] lowestForm = Matrix.gcd(fraction[0], fraction[1]);
    System.out.println("Lowest form: " + lowestForm[0] + "/" + lowestForm[1]); */
    System.out.println();
    /* double[] sumOfFractions = addingFractions(4.75, 5.32);
    System.out.println(sumOfFractions[0] + "/" + sumOfFractions[1]); */
    /* double[] row = {19, 18, 15, 11, 5};
    for(int i  = 0; i < row.length; i++) {
      row[i] = row[i]/19;
      System.out.print(row[i] + " ");
    }
    System.out.println();
    double[][] entriesAsFractions = Matrix.turnRowIntoFraction(row);
    for(int i = 0; i < entriesAsFractions.length; i++) {
      System.out.print(entriesAsFractions[i][0] + "/" + entriesAsFractions[i][1] + " ");
    } */

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
    /* double[]numbers = {2.4999999999, -0.999999999999, 0.0000000001};
    Matrix.roundingOff(numbers);
    for(int i = 0; i < numbers.length; i++) {
      System.out.print(numbers[i] + " ");
    }
    System.out.println();
    double x = 1.3636363636363613;
    System.out.println("1. " + x);
    double y = x * 100;
    System.out.println("2. " + y);
    double difference = y - x;
    double[] abc = {difference};
    Matrix.roundingOff(abc);
    System.out.println("3. " + difference);
    double[] result = Matrix.gcd(abc[0], 99);
    System.out.println("4. " + result[0] + "/" + result[1]);
    System.out.println("5. " + abc[0]); */
    
    /* double z = 1.0/3.0;
    double w = z * 10;
    System.out.println((w - z)/9); *?
    
    
    double v = 2.111111111;
    double[] vAsFraction = Matrix.turnIntoFraction(v);
    System.out.println(vAsFraction[0] + "/" + vAsFraction[1]);
    
    
    /* System.out.println();
    System.out.println(Math.round(-4.00000001));
    double x = 12/10.0;
    System.out.println(x); */
    //System.out.println("Determinant: " + A.determinant());
    /* A.transpose();
    A.printMatrix(); */
    /* Matrix B = new Matrix("B", randomArray(4, 1));
    B.printMatrix();
    A.solutionOfSystem(B); */
    
    /* double test = 0.0 % ((int) 0.0);
    System.out.println(test);
    
    Matrix B = new Matrix("B", "4x1");
    B.printMatrix();
    System.out.println();
    A.solutionOfSystem(B);
    System.out.println("\nRow-reduced form of augmented matrix - ");
    
    
    double[] lowestForm = Matrix.gcd(-56,56);
    System.out.println(lowestForm[0] + "/" + lowestForm[1]);
    //int numer = 129152;
    //int denom = 18184;
    int numer = 4;
    int denom = 4;
    int least = 0;
    if(numer < denom) {
      least = numer;
    }
    else{
      least = denom;
    }
    int gcd = 1;
    for(int i = 2; i < least/2; i++) {
      if(numer % i == 0 && denom % i == 0) {
        gcd = i;
      }
    }
    System.out.println((numer/gcd) + "/" + (denom/gcd));
    int z = 56/-2;
    System.out.println("z = " + z);
    int x = 27;
    System.out.println(x/2);
    double y = 5.67;
    //String number = "" + y;
    //System.out.println(number);
    String number = "5.0";
    String newNumber = number.replace('.', ',');
    //String[] splitNumber = new String[2];
    String[] splitNumber = newNumber.split(",");
    if(splitNumber[1].equals("0")) {
      System.out.print(splitNumber[0]);
    }
    //else{
      //System.out.print(newNumber + " ");
    //}
    //System.out.println(splitNumber.length);
    //System.out.println(splitNumber[1]);
    //System.out.println(Math.round(x));
    /* for(int i = 0; i < rowReduced.length; i++) {
      for(int j = 0; j < rowReduced[0].length; j++) {
        System.out.print((int) rowReduced[i][j] + " ");
      }
      System.out.println();
    } */
    /* Matrix[][] cofactors = A.allCofactors();
    for(int i = 0; i < cofactors.length; i++) {
      for(int j = 0; j < cofactors[0].length; j++) {
        cofactors[i][j].printMatrix();
      }
    } */
    /* Matrix B = new Matrix("B", randomArray(3,1));
    B.printMatrix();
    Matrix C = new Matrix("C", "2x2");
    C.printMatrix();
    Matrix AB = A.multiplyMatrices(B, "AB");
    AB.printMatrix();
    //Matrix mystery = C.multiplyMatrices(A.multiplyMatrices(B, "AB"),"mystery");
    //Matrix mystery2 = A.multiplyMatrices(B, "AB").multiplyMatrices(C, "mystery2");
    //mystery.printMatrix();
    //mystery2.printMatrix(); */
  
  /* public static double[][] randomArray(int height, int width) {
    double[][] random = new double[height][width];
    for(int i = 0; i < random.length; i++) {
      for(int j = 0; j < random[0].length; j++) {
        random[i][j] = (int) (Math.random() * 50);
      }
    }
    return random;
  } */