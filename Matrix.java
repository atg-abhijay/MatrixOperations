import java.util.Scanner;
public class Matrix{
  private String name;
  private String dimensions;
  private double[][] matrixEntries;
  
  public Matrix(String name, double[][] entries) {
    // constructor which requires a name and a 2D array of double values
    this.name = name;
    this.dimensions = entries.length + "x" + entries[0].length;
    this.matrixEntries = new double[entries.length][entries[0].length];
    for(int i = 0; i < this.matrixEntries.length; i++) {
      for(int j = 0; j < this.matrixEntries[0].length; j++) {
        this.matrixEntries[i][j] = entries[i][j];
      }
    }
  }
  
  public Matrix(String name, String size, boolean randomMatrix) {
    /* constructor which requires name, size of the matrix in the
       form "axb" and a boolean value asking whether the user wants
       a randomly generated matrix or wants to make one themselves */
    this.name = name;
    this.dimensions = size;
    String[] dimensions = size.split("x");
    int height = Integer.parseInt(dimensions[0]);
    int width = Integer.parseInt(dimensions[1]);
    if(height < 0 || width < 0) {
      throw new IllegalArgumentException("The dimensions for the matrix cannot be negative!");
    }
    Scanner reader = new Scanner(System.in);
    if(!randomMatrix) {
      this.matrixEntries = new double[height][width];
      System.out.println("****** Welcome to the Matrix Builder! ****** \n");
      for(int i = 0; i < height; i++) {
        System.out.println("Please enter row " + (i+1) + " of the matrix " + this.name);
        for(int j = 0; j < width; j++) {
          this.matrixEntries[i][j] = reader.nextDouble();
        }
      }
    }
    else {
      System.out.println("Please enter the upper bound for the entries");
      int upperBound = reader.nextInt();
      this.matrixEntries = randomArray(height, width, upperBound);
    }
  }
  
  
  // ****** PRINTING A MATRIX ******
  
  
  public void printMatrix() {
    System.out.println("Matrix " + this.name + "(" + this.dimensions + ") -");
    for(int i = 0; i < this.matrixEntries.length; i++) {
      for(int j = 0; j < this.matrixEntries[0].length; j++) {
        // intial approach
        /* String number = "" + this.matrixEntries[i][j];
        number = number.replace('.', ',');
        String[] splitNumber = number.split(",");
        if(splitNumber[1].equals("0")) {
          System.out.print(splitNumber[0] + " ");
        }
        else{
        System.out.print(this.matrixEntries[i][j] + " ");
        } */
        double number = this.matrixEntries[i][j];
        /* if the number leaves no remainder when divided
           by the number obtained after parsing it to an int
           that means that the number is an int itself and
           therefore we can just print the number parsed to an int */
        if(number % ((int) number) == 0 || number == 0) {
          System.out.print((int) number + " ");
        }
        else{
          /* the method turnIntoFraction converts the double value
             to a fraction and stores the numerator and denominator separately */
          double[] numberAsFraction = turnIntoFraction(number);
          System.out.print((int) numberAsFraction[0] + "/" + (int) numberAsFraction[1] + " ");
        }
      }
      System.out.println();
    }
  }
  
  
  // ****** MULTIPLYING TWO MATRICES ******
  
  
  public Matrix multiplyMatrices(Matrix secondMatrix, String productName) {
    if(this.matrixEntries[0].length != secondMatrix.matrixEntries.length) {
      throw new IllegalArgumentException("The 2 matrices cannot be multiplied! Their dimensions are not suitable for multiplication!");
    }
    double[][] productEntries = new double[this.matrixEntries.length][secondMatrix.matrixEntries[0].length];
    for(int i = 0; i < productEntries.length; i++) {
      for(int j = 0; j < productEntries[0].length; j++) {
        for(int k = 0; k < this.matrixEntries[0].length; k++) {
          //    **** THE MOST KEY PIECE OF CODE. EVERYTHING COMES FROM HERE *******
          // THIS FORMULA CAN BE USED FOR PRODUCT OF ANY 2 MATRICES OF ANY SIZE
          //                     AB[i][j] += A[i][k] * B[k][j]
          productEntries[i][j] += this.matrixEntries[i][k] * secondMatrix.matrixEntries[k][j];
        }
      }
    }
    Matrix product = new Matrix(productName, productEntries);
    return product;
  }
  
  
  // ****** METHOD FOR FINDING DETERMINANT ******
  
  
  public int determinant() {
    if(this.matrixEntries.length != this.matrixEntries[0].length) {
      throw new IllegalArgumentException("Determinant can only be found for a square matrice!");
    }
    int length = this.matrixEntries.length;
    int determinant = 0;
    // calculating determinant if the matrix is 2x2
    if(length <= 2) {
      for(int j = 0; j < length; j++) {
        determinant += (int) (Math.pow(-1, 0+j)) * this.matrixEntries[0][j] * this.matrixEntries[1][length - j - 1];
      }
      return determinant;
    }
    
    Matrix[][] cofactors = this.allCofactors();
    for(int j = 0; j < length; j++) {
      // ***** Most important step - RECURSION *****
      determinant += (int) (Math.pow(-1, 0+j)) * this.matrixEntries[0][j] * cofactors[0][j].determinant();
    }
    return determinant;
  }
  
  
  // ****** METHOD FOR SOLUTION OF SYSTEM ******
  
  
  public void solutionOfSystem(Matrix constantVals) {
    if(constantVals.matrixEntries[0].length != 1) {
      throw new IllegalArgumentException("The constant valued matrix provided has wrong dimensions!");
    }
    /* if we have a system of the form AX = B, where A is the
       coefficient matrix, X is the solution matrix and B is 
       the constant valued matrix, then if A has a non-zero
       determinant, we can find inverse of A and the UNIQUE
       solution of the system can be given as X = ((A)^-1) * B or
                  X = (1/(det(A))) * adjoint(A) * B */
    int determinant = this.determinant();
    System.out.println("Determinant: " + determinant);
    /* if the determinant is zero and all of the entries in 
       B are zero, then the system has infinitely many solutions.
       if even one of the entries in B is non-zero when the
       determinant is zero, then the system has no solution */
    if(determinant == 0) {
      int numZeroesInB = 0;
      for(int i = 0; i < constantVals.matrixEntries.length; i++) {
        if(constantVals.matrixEntries[i][0] == 0) {
          numZeroesInB++;
        }
      }
      
      if(numZeroesInB == this.matrixEntries.length) {
        System.out.println("The system has infinitely many solutions!");
        return;
      }
      else{
        System.out.println("The system has no solution!");
        return;
      }
    }
    
    System.out.println("The system has a unique solution - ");
    Matrix adjoint = this.adjointOfMatrix();
    Matrix adjointxB = adjoint.multiplyMatrices(constantVals, "AdjointXB");
    String[] solution = new String[this.matrixEntries[0].length];
    double[] leastForm = new double[2];
    for(int i = 0; i < solution.length; i++) {
      /* the method gcd finds the greatest common divisor
         and divides both the values by this divisor */
      leastForm = gcd(adjointxB.matrixEntries[i][0], determinant);
      System.out.println("x" + (i+1) + " = " + leastForm[0] + "/" + leastForm[1]);
    }
  }
  
  // ****** ROW REDUCING A MATRIX ******
  
  public Matrix rowReduce() {
    /* this method is used to carry a
       matrix into Reduced Row Echelon Form */
    int numRows = this.matrixEntries.length;
    int numElements = this.matrixEntries[0].length;
    double[][] rows = new double[numRows][numElements];
    for(int i = 0; i < rows.length; i++){
      for(int j = 0; j < rows[0].length; j++) {
        rows[i][j] = this.matrixEntries[i][j];
      }
    }
    for(int i = 0; i < rows.length; i++) {
      carryIntoRREF(rows, i);
    }
    Matrix rowReduced = new Matrix("RREF of matrix " + this.name, rows);
    return rowReduced;
  }
  
  // ****** TRANSPOSING A MATRIX ******
  
  public void transpose() {
    double[][] transposed = new double[this.matrixEntries[0].length][this.matrixEntries.length];
    int height = 0;
    int width = 0;
    for(int i = 0; i < this.matrixEntries.length; i++) {
      for(int j = 0; j < this.matrixEntries[0].length; j++) {
        transposed[height][width] = this.matrixEntries[i][j];
        height++;
        if(height == transposed.length) {
          height = 0;
          width++;
        }
      }
    }
    this.matrixEntries = transposed;// should we just change the pointer or copy the contents from transposed into this.matrixEntries?
    this.dimensions = transposed.length + "x" + transposed[0].length;
  }
  
  // ********* Private Helper Methods *********
  
  private static double[][] randomArray(int height, int width, int upperBound) {
    double[][] random = new double[height][width];
    for(int i = 0; i < random.length; i++) {
      for(int j = 0; j < random[0].length; j++) {
        random[i][j] = (int) (Math.random() * upperBound);
      }
    }
    return random;
  }
  
  private double[][] partialCofactors(double[][] A, int rowNumber, int columnNumber) {
    /* this method finds the cofactor matrix for an entry in 
       the 2D array A with position (rowNumber, columnNumber) */
    if(A.length != A[0].length) {
      throw new IllegalArgumentException("Cofactors can only be found for square matrices!");
    }
    double[][] D = new double[A.length - 1][A.length - 1];
    int index1 = 0;
    int index2 = 0;
    for(int i = 0; i < A.length; i++) {
      for (int j = 0; j < A.length; j++) {
        if (i != rowNumber && j != columnNumber){
          D[index1][index2] = A[i][j];
          index2++;
          if(index2 == A.length - 1) {
            index1++;
            index2 = 0;
          }
        }
      }
    }
    return D;
  }
  
  private Matrix[][] allCofactors() {
    /* generating the complete Cofactor 
       matrix for a given matrix */ 
    int length = this.matrixEntries.length;
    double[][] Z = new double[length - 1][length - 1];
    Matrix[][] cofactors = new Matrix[length][length];
    for(int i = 0; i < length; i++) {
      for (int j = 0; j < length; j++){
        Z = partialCofactors(this.matrixEntries, i, j);
        cofactors[i][j] = new Matrix("a", Z);
      }
    }
    return cofactors;
  }
  
  private Matrix adjointOfMatrix() {
    int length = this.matrixEntries.length;
    double[][] adjointValues = new double[length][length];
    Matrix[][] cofactors = this.allCofactors();
    for(int i = 0; i < length; i++){
      for (int j = 0; j < length; j++) {
        adjointValues[j][i] = (int) Math.pow(-1, i+j) * cofactors[i][j].determinant();
      }
    }
    Matrix adjoint = new Matrix("Adjoint", adjointValues);
    return adjoint;
  }
  
  /* There are 3 elementary row operations in a matrix - 
         1. Adding to a row a multiple of another row
         2. Multiplying a row with a non-zero constant
         3. Interchanging two rows */
  private static double[] addRows(double[] row1, double coefficient, double[] row2) {
    double[] sumOfRows = new double[row1.length];
    for(int i = 0; i < sumOfRows.length; i++) {
      sumOfRows[i] = row1[i] + (coefficient * row2[i]);
    }
    return sumOfRows;
  }
  
  private static double[] multiplyByConstant(double[] row, double constant) {
    double[] updatedRow = new double[row.length];
    for(int i = 0; i < updatedRow.length; i++) {
      updatedRow[i] = constant * row[i];
    }
    return updatedRow;
  }
  
  private static void switchRows(double[][] rows, int numRow1, int numRow2) {
    double[] tempRow = new double[rows[0].length];
    for(int i = 0; i < tempRow.length; i++) {
      tempRow[i] = rows[numRow1][i];
    }
    rows[numRow1] = rows[numRow2];
    rows[numRow2] = tempRow;
  }
  
  private static void carryIntoRREF(double[][] rows, int numRow) {
    /* The procedure of carrying into RREF is carried out
       row by row. Once we have made all the modifications
       to a row, we move on to the next row. Start from Row 1.
       Then, repeat steps 1-3 for the remaining rows(by ignoring Row 1) */

    // STEP 1 - Identify the leftmost non-zero column 

    /* let k (nonZero below) be a non-zero entry
       in that column. Move the row containing k
       to the top of the matrix */
    search:
    for(int j = 0; j < rows[0].length; j++) { // starting point of i will change
      for(int i = numRow; i < rows.length; i++) {
        if(rows[i][j] != 0) {
          if(i != numRow) {
            /* we switch the rows only if the i value obtained
               is not that of the row under consideration */
            switchRows(rows, i, numRow);// 0 will change
          }
          printArray(rows);
          break search;
        }
      }
    }
    
    /* we find the first non-zero entry
       in the row indexed numRow and store
       its position in the row and its value */
    double nonZero = 1;// ??? values to set for nonZero and position
    int position = 0;
    for(int j = 0; j < rows[0].length; j++) {
      if(rows[numRow][j] != 0) {
        nonZero = rows[numRow][j];
        position = j;
        break;
      }
    }
    // STEP 2 -

    /* Multiply the first row (of the resulting
       matrix from step 1, that is, row containing
       k) by its inverse (1/k) */
    rows[numRow] = multiplyByConstant(rows[numRow], 1/nonZero);
    /* by multiplying by a constant, we will make the first
       non-zero entry of that row to be 1, which is known as
       the leading 1 */
    roundingOff(rows[numRow]);
    printArray(rows);
    // STEP 3 - 

    /* Using elementary row operations, make every entry above
       and below the leading 1(column of leading 1) to be zero */
    makeLeadingColumn(rows, numRow, position);
  }
  
  private static void makeLeadingColumn(double[][] rows, int numRow, int position) {
    double coefficient = 0;
    int index = 0;
    for(int i = 0; i < rows.length; i++) {
      if(i != numRow) {
        if(rows[i][position] != 0 && rows[numRow][position] != 0) { // doubtful about this condition???
          coefficient = (-rows[i][position]) / rows[numRow][position];
          rows[i] = addRows(rows[i], coefficient, rows[numRow]);
          roundingOff(rows[i]);
        }
      }
    }
    printArray(rows);
  }
  
  public static void printArray(double[][] array) {
    System.out.println("\n");
    for(int i = 0; i < array.length; i++) {
      for(int j = 0; j < array[0].length; j++) {
        /* String number = "" + array[i][j];
        number = number.replace('.', ',');
        String[] splitNumber = number.split(",");
        if(splitNumber[1].equals("0")) {
          System.out.print(splitNumber[0] + " ");
        }
        else{
          //int decimalLength = splitNumber[1].length();
          //int withoutDecimal = (int) (array[i][j] * Math.pow(10, decimalLength));
          //int denominator = (int) (Math.pow(10, decimalLength));
          //int[] lowestForm = gcd(withoutDecimal, denominator);
          //System.out.println(lowestForm[0] + "/" + lowestForm[1]);
          System.out.print(array[i][j] + " ");
        } */
        double number = array[i][j];
        if(number % ((int) number) == 0 || number == 0) {
          System.out.print((int) number + " ");
        }
        else {
          System.out.print(number + " ");
        }
      }
      System.out.println();
    }
  }
  
  /* private static boolean nonZeroRow(double[] row) {
    for(int i = 0; i < row.length; i++) {
      if(row[i] != 0) {
        return true;
      }
    }
    return false;
  } */
  
  public static void roundingOff(double[] row) {
    double difference = 0;
    int closestVal = 0;
    double tolerance = 0.00001;
    for(int i = 0; i < row.length; i++) {
      if(row[i] % ((int) row[i]) != 0) {
        int power = 0;
        double number = row[i];
        //double greaterThanZero = -1;
        while(Math.abs(number) < 0) { //greaterThanZero
          power++;
          //double number = row[i];
          number = number * Math.pow(10, power);
          //greaterThanZero = number;
        }
        closestVal = (int) Math.round(number); // greaterThanZero
        difference = Math.abs(closestVal) - Math.abs(number); //greaterThanZero
        //difference = closestVal - row[i];
        if(Math.abs(difference) < tolerance) {
          row[i] = closestVal/Math.pow(10, power);
          //row[i] = closestVal;
        }
      }
    }
  }
  
  public static double[] gcd(double numer, double denom) {
    double least = 0;
    double[] lowestForm = new double[2];
    if(numer % denom == 0) {
      lowestForm[0] = numer/denom;
      lowestForm[1] = 1;
      return lowestForm;
    }
    else if(denom % numer == 0) {
      lowestForm[0] = 1;
      lowestForm[1] = denom/numer;
      return lowestForm;
    }
    
    if(Math.abs(numer) < Math.abs(denom)) {
      least = Math.abs(numer);
    }
    else{
      least = Math.abs(denom);
    }
    
    double gcd = 1;
    for(int i = 2; i < least/2; i++) {
      if((numer) % i == 0 && (denom) % i == 0) {
        gcd = i;
      }
    }
    lowestForm[0] = (numer)/gcd;
    lowestForm[1] = (denom)/gcd;
    return lowestForm;
  }
  
  public static void realRoundingOff(double[] row) {
    double difference = 0;
    double tolerance = 0.00001;
    for(int i = 0; i < row.length; i++) {
      if(row[i] % ((int) row[i]) != 0) {
        double original = row[i];
        
        
      }
    }
  }
  
  public static double[] turnIntoFraction(double entry) {
    double tolerance = 0.00001;
    //double base = entry; // 2.4522222
    //double oneUp = entry * 10; // 24.522222
    double value = 0;
    double difference = 1;
    double closestVal = 0;
    /* int power = 1;
    double value = oneUp - base; // 22.07000
    double closestVal = (int) Math.round(value); // 22.0
    double difference = Math.abs(closestVal) - Math.abs(value); // 0.7000 */
    /* while(Math.abs(difference) > tolerance) {
      base *= 10;
      System.out.println("Base: " + base);
      oneUp *= 10;
      System.out.println("oneUp: " + oneUp);
      power++;
      value = oneUp - base;
      System.out.println("Value: " + value);
      closestVal = (int) Math.round(value);
      System.out.println("Closest Value: " + closestVal);
      difference = Math.abs(closestVal) - Math.abs(value);
      System.out.println("Difference: " + difference);
      System.out.println("\n");
    } */
    int i = 0;
    int j = 0;
    search:
    for(i = 0; i < 25; i++) {
      double base = entry;
      base *= Math.pow(10, i);
      //System.out.println("Base: " + base);
      for(j = i + 1; j < 26; j++) {
        double oneUp = entry;
        oneUp *= Math.pow(10, j);
        //System.out.println("oneUp: " + oneUp);
        if(Math.abs(difference) > tolerance) {
          value = oneUp - base;
          //System.out.println("Value: " + value);
          closestVal = (int) Math.round(value);
          //System.out.println("Closest Value: " + closestVal);
          difference = Math.abs(closestVal) - Math.abs(value);
          //System.out.println("Difference: " + difference);
          if(Math.abs(difference) < tolerance) {
            break search;
          }
        }
        //System.out.println("\n");
      }
    }
    //double[] fraction = {closestVal, Math.pow(10, power) - Math.pow(10, power - 1)};
    //double[] fraction = {closestVal, Math.pow(10, j) - Math.pow(10, i)};
    double[] fraction = gcd(closestVal, (int) (Math.pow(10, j) - Math.pow(10, i)));
    return fraction;
  }
}