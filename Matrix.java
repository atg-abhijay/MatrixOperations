import java.util.*;
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
      System.out.println("Please enter the upper bound for the entries of matrix " + this.name);
      int upperBound = reader.nextInt();
      //int upperBound = 50;
      this.matrixEntries = randomArray(height, width, upperBound);
    }
  }
  
  public void setName(String newName) {
    this.name = newName;
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
          //System.out.print(number + " ");
        }
      }
      System.out.println();
    }
    System.out.println();
  }
  

  // ****** ADDING/SUBTRACTING TWO MATRICES ******


  public Matrix addMatrices(Matrix secondMatrix, String name, boolean subtract) {
    boolean height = this.matrixEntries.length == secondMatrix.matrixEntries.length;
    boolean width = this.matrixEntries[0].length == secondMatrix.matrixEntries[0].length;
    if(!height || !width) {
      throw new IllegalArgumentException("The matrices should have the same dimensions to be added!");
    }
    int k = 1;
    if(subtract) {
      k = -1;
    }
    double[][] entries = new double[this.matrixEntries.length][this.matrixEntries[0].length];
    for(int i = 0; i < entries.length; i++) {
      for(int j = 0; j < entries[0].length; j++) {
        entries[i][j] = this.matrixEntries[i][j] + k * secondMatrix.matrixEntries[i][j];
      }
    }
    Matrix addition = new Matrix(name, entries);
    return addition;
  }


  // ****** MULTIPLYING MATRIX BY CONSTANT ******


  public Matrix multiplyByNumber(double constant) {
    double[][] entries = new double[this.matrixEntries.length][this.matrixEntries[0].length];
    for(int i = 0; i < entries.length; i++) {
      for(int j = 0; j < entries[0].length; j++) {
        entries[i][j] = constant * this.matrixEntries[i][j];
      }
    }
    Matrix amplified = new Matrix("amplified", entries);
    return amplified;
  }


  // ****** MAKE IDENTITY MATRIX ******


  public static Matrix makeIdentity(int size) {
    double[][] entries = new double[size][size];
    for(int i = 0; i < size; i++) {
      for(int j = 0; j < size; j++) {
        if(i == j) {
          entries[i][j] = 1;
        }
      }
    }
    Matrix identity = new Matrix("Identity", entries);
    return identity;
  }


  // ****** RAISE A MATRIX TO POWER ******


  public Matrix raiseToPower(int power) {
    if(this.matrixEntries.length != this.matrixEntries[0].length) {
      throw new IllegalArgumentException("Only square matrices can be raised to powers!");
    }
    //double[][] entries = new double[this.matrixEntries.length][this.matrixEntries.length];
    Matrix aToPower = makeIdentity(this.matrixEntries.length);
    for(int i = 0; i < power; i++) {
      aToPower = aToPower.multiplyMatrices(this, "aToPower");
    }
    aToPower.setName(this.name + "ToPower" + power);
    return aToPower;
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
  

  // ****** INVERSE OF A MATRIX ******


  public Matrix inverse() {
    if(this.matrixEntries.length != this.matrixEntries[0].length) {
      throw new IllegalArgumentException("Inverse can only be found for a square matrix!");
    }
    /* the inverse of a matrix A is given by
            A^-1 = (1/det(A)) * adj(A) */
    int determinant = this.determinant();
    if(determinant == 0) {
      System.out.println("Inverse does not exist!\n");
      Matrix identity = makeIdentity(this.matrixEntries.length);
      return identity.addMatrices(identity, "Zero", true);
    }
    Matrix inverse = this.adjointOfMatrix().multiplyByNumber(1.0/determinant);
    inverse.setName(this.name + " inverse");
    return inverse;
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


  // ****** COMPARING ENTRIES OF MATRIX TO A VALUE ******


  public Matrix compare(double value) {
    double[][] entries = new double[this.matrixEntries.length][this.matrixEntries[0].length];
    /* we compare the individual entries to
       a value and if the entry is larger 
       than the "value" then we store 1 at its
       position in a new matrix. if it is less
       than the "value", then 0, if equal to the
       "value" then we store 0.5 at that position */
    for(int i = 0; i < entries.length; i++) {
      for(int j = 0; j < entries[0].length; j++) {
        if(this.matrixEntries[i][j] < value) {
          entries[i][j] = 0;
        }
        else if (this.matrixEntries[i][j] > value){
          entries[i][j] = 1;
        }
        else {
          entries[i][j] = 0.5;
        }
        //System.out.println(entries[i][j]);
      }
    }
    Matrix booleanValues = new Matrix("Boolean Valued", entries);
    return booleanValues;
  }


  // ****** ADDING A ROW/COLUMN TO MATRIX ******


  public Matrix add(String rowOrColumn, int position) {
    /* "position" here is treated as starting from 1.
        the position does not start from zero. to
        accomodate for that in terms of arrays, we
        subtract 1 and use the term position - 1 */
    if(!rowOrColumn.equals("r") && !rowOrColumn.equals("c")){
      throw new IllegalArgumentException("You must choose between a row and a column!");
    }
    Scanner reader = new Scanner(System.in);
    /* adding a row to a matrix is straightforward
       and direct. if we want to add a column though
       we first take the transpose of the matrix and
       add a ROW at the position where we wanted to
       add a column and then take the transpose of the
       matrix once again. adding a column to a matrix
       is equivalent to adding a row to the transpose
       of that matrix */

    // we assume that a row is to be added
    String rowColumn = "row";
    boolean column = rowOrColumn.equals("c");
    /* if a column is to be added, we take
       the transpose of the matrix */
    if(column) {
      this.transpose();
      rowColumn = "column";
    }
    /* irrespective of whether we want to add
       a row or a column, we are essentially only
       adding a row. therefore the number of rows
       in "entries" is one more than the original */
    double[][] entries = new double[this.matrixEntries.length + 1][this.matrixEntries[0].length];
    int index1 = 0;
    int index2 = 0;
    /* first we copy the original matrix into our larger array entries.
       we leave the row where a new row has to be added empty
       by the condition: if index1 == position - 1; index1++
       by doing this we skip over the row where new entries
       are supposed to be added */
    for(int i = 0; i < this.matrixEntries.length; i++) {
      for(int j = 0; j < this.matrixEntries[0].length; j++) {
        if(index1 == position - 1) {
          index1++;
        }
        entries[index1][index2] = this.matrixEntries[i][j];
        //printArray(entries);
        index2++;
        if(index2 == entries[0].length) {
          index1++;
          index2 = 0;
        }
      }
    }
    //printArray(entries);
      
    System.out.println("Please enter the " + rowColumn +  " to be appended");
    for(int j = 0; j < entries[0].length; j++) {
      entries[position - 1][j] = reader.nextDouble();
    }
    //printArray(entries);

    Matrix modifiedMatrix = new Matrix("Modified", entries);
    //printArray(modifiedMatrix.matrixEntries);

    /* if we wanted to add a column, we need to
       take tranpose once again to obtain
       the correct matrix */
    if(column) {
      modifiedMatrix.transpose();
    }
    return modifiedMatrix;
  }
  

  // ********* MATRIX AS A SUDOKU ********** 

  public void solveSudoku() {
    Stack<Integer> lastRowChanged = new Stack<Integer>();
    Stack<Integer> lastColumnChanged = new Stack<Integer>();
    Stack<Integer> lastValidVal = new Stack<Integer>();

    for(int i = 0; i < this.matrixEntries.length; i++) {
      for(int j = 0; j < this.matrixEntries.length; j++) {
        if(this.matrixEntries[i][j] == 0) {
          //this.insertAndBackTrack(i,j,lastRowChanged, lastColumnChanged, lastValidVal);
          boolean ableToInsert = false;
          for(int k = 1; k < 10; k++) {
            if(this.isValid(i,j,k)) {
              this.matrixEntries[i][j] = k;
              lastRowChanged.push(i);
              lastColumnChanged.push(j);
              lastValidVal.push(k);
              ableToInsert = true;
              this.printMatrix();
              System.out.println("\n\n");
              break;
            }
          }

          if(!ableToInsert) {
            backTrack(lastRowChanged.peek(), lastColumnChanged.peek(), lastRowChanged, lastColumnChanged, lastValidVal);
            search:
            for(int p = 0; p < this.matrixEntries.length; p++) {
              for(int q = 0; q < this.matrixEntries.length; q++) {
                if(this.matrixEntries[p][q] == 0) {
                    if(j == this.matrixEntries.length - 1 && p != 0) {
                        i = p-1;
                    }
                    else {
                        i = p;
                    }
                    j = q-1;
                    break search;
                }
              }
            }
          }                
        }
      }
    }
    System.out.println("\n\n");
  }


  public void printAsSudoku() {
    // Compute the number of digits necessary to print out each number in the Sudoku puzzle
    int digits = (int) Math.floor(Math.log(this.matrixEntries.length) / Math.log(10)) + 1;

    // Create a dashed line to separate the boxes
    int size = (int) Math.sqrt(this.matrixEntries.length);
    int lineLength = (digits + 1) * this.matrixEntries.length + 2 * size - 3;
    StringBuffer line = new StringBuffer();
    for(int lineInit = 0; lineInit < lineLength; lineInit++) {
      line.append('-');
    }

    // Go through the sudoku, printing out its values separated by spaces
    for(int i = 0; i < this.matrixEntries.length; i++ ) {
      for(int j = 0; j < this.matrixEntries.length; j++ ) {
        printFixedWidth(String.valueOf(this.matrixEntries[i][j]), digits);
        // Print the vertical lines between boxes 
        if((j < this.matrixEntries.length-1) && ((j+1) % size == 0)) {
          System.out.print( " |" );
          System.out.print( " " );
        }
      }
      
      System.out.println();
      // Print the horizontal line between boxes
      if((i < this.matrixEntries.length-1) && ((i+1) % size == 0)) {
        System.out.println(line.toString());
      }

    }
  }


  // ****** SORTING MATRIX BASED ON A SINGLE ROW/COLUMN ******


  public Matrix sort(String columnOrRow, int position) {
    boolean row = columnOrRow.equals("r");
    if (row) {
      this.transpose();
    }

    if (position > this.matrixEntries[0].length) {
      if(row) {
        System.out.println("You are trying to access a row that does not exist!");
      }
      else {
        System.out.println("You are trying to access a column that does not exist!");
      }
      return makeIdentity(1);
    }

    Double[][] elements = new Double[this.matrixEntries.length][this.matrixEntries[0].length];
    for(int i = 0; i < elements.length; i++) {
      for(int j = 0; j < elements[0].length; j++) {
        elements[i][j] = this.matrixEntries[i][j];
      }
    }
    double max = 0;

    double[] changedRC = new double[elements.length];
    Double[][] arrays = new Double[elements.length][elements[0].length];        
    for(int i = 0; i < changedRC.length; i++) {
      double num = elements[i][position - 1];                              
        if(max < num) {
          max = num;                                  
        }
      changedRC[i] = num;
    }

    for(int j = 0; j < elements.length; j++) {
      arrays[j] = elements[j];
    }

    int maxNumDigits = (int) Math.log10(max) + 1;        
    LinkedList<LinkedList<Double>> bucketsForRC = new LinkedList<LinkedList<Double>>();
    LinkedList<LinkedList<Double[]>> bucketsForArray = new LinkedList<LinkedList<Double[]>>();
    for(int p = 0; p < 10; p++) {                           
      bucketsForRC.add(new LinkedList<Double>());
      bucketsForArray.add(new LinkedList<Double[]>());
    }
    

    for(int j = 1; j <= maxNumDigits; j++) {        
      for(int k = 0; k < changedRC.length; k++) {                 
        double number = changedRC[k];                                                  
        int digit = (int) ((number % Math.pow(10, j)) / Math.pow(10,j-1));
        bucketsForRC.get(digit).add(number);
        bucketsForArray.get(digit).add(arrays[k]);                                        
      }                                                                    

      double[] partiallySorted = new double[changedRC.length];
      Double[][] parSorted = new Double[elements.length][elements[0].length];
      int numAt = 0;                                          

      search:
      for(int m = 0; m < 10; m++) {                           
        while(!bucketsForRC.get(m).isEmpty()) {                                  
          partiallySorted[numAt] = bucketsForRC.get(m).pollFirst();   
          parSorted[numAt] = bucketsForArray.get(m).pollFirst();
          numAt++;                                                            
        }
        if(numAt == changedRC.length) {                                         
          break search;                                                       
        }
      }
      changedRC = partiallySorted;
      arrays = parSorted;
    }

    double[][] finalVals = new double[arrays.length][arrays[0].length];
    for(int i = 0; i < finalVals.length; i++) {
      for(int j = 0; j < finalVals[0].length; j++) {
        finalVals[i][j] = arrays[i][j];
      }
    }
    Matrix sorted = new Matrix(this.name + "Sorted", finalVals);
    if (row) {
      sorted.transpose();
    }
    
    return sorted;
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
    for(int j = 0; j < rows[0].length; j++) {
      for(int i = numRow; i < rows.length; i++) {
        if(rows[i][j] != 0) {
          if(i != numRow) {
            /* we switch the rows only if the i value obtained
               is not that of the row under consideration */
            switchRows(rows, i, numRow);
          }
          printArray(rows);
          break search;
        }
      }
    }
    
    /* we find the first non-zero entry
       in the row indexed numRow and store
       its position in the row and its value */
    double nonZero = 1;
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
        double number = array[i][j];
        if(number % ((int) number) == 0 || number == 0) {
          System.out.print((int) number + " ");
        }
        else {
          //double[] numAsFraction = turnIntoFraction(number);
          //System.out.print((int) numAsFraction[0] + "/" + (int) numAsFraction[1] + "  ");
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

  public static double[][] turnRowIntoFraction(double[] row) {
    double[][] entriesAsFractions = new double[row.length][2];
    for(int i = 0; i < row.length; i++) {
      double[] fraction = turnIntoFraction(row[i]);
      entriesAsFractions[i][0] = fraction[0];
      entriesAsFractions[i][1] = fraction[1];
    }
    return entriesAsFractions;
  }


  // Methods for working with Sudoku

  
  private void backTrack(int currentRow, int currentColumn, Stack<Integer> lastRowChanged, Stack<Integer> lastColumnChanged, Stack<Integer> lastValidVal) {
    for(int p = lastValidVal.peek()+1; p < 10; p++) {
      if(this.isValid(currentRow, currentColumn, p)) {
        this.matrixEntries[currentRow][currentColumn] = p;
        lastValidVal.pop();
        lastValidVal.push(p);
        return;
      }
    }

    lastRowChanged.pop(); lastColumnChanged.pop(); lastValidVal.pop();
    this.matrixEntries[currentRow][currentColumn] = 0;
    this.printMatrix();
    System.out.println("\n\n");
    this.backTrack(lastRowChanged.peek(), lastColumnChanged.peek(), lastRowChanged, lastColumnChanged, lastValidVal);

    this.printMatrix();
    System.out.println("(" + currentRow + "," + currentColumn + ")");
    System.out.println("\n");
    //this.solve();
    return;
  }

  private boolean isValid(int row, int column, int number) {
      /* checks if the number occurs
          only once in its row */
      for(int j = 0; j < this.matrixEntries.length; j++) {
        if (j != column) {
          if(number == this.matrixEntries[row][j]) {
            //System.out.println("Number: " + number);
            //System.out.println("Grid row value (" + row + "," + j + "): " + this.Grid[row][j]);
            return false;
          }
        }
      }

      /* checks if the number occurs
          only once in its column */
      for(int i = 0; i < this.matrixEntries.length; i++) {
        if (i != row) {
          if(number == this.matrixEntries[i][column]) {
            //System.out.println("Number: " + number);
            //System.out.println("Grid column value(" + i + "," + column + "): " + this.Grid[i][column]);
            return false;
          }
        }
      }

      /* checks if the number occurs
          only once in its box */
      int size = (int) Math.sqrt(this.matrixEntries.length);
      int boxRow = (row/size) * size;
      int boxColumn = (column/size) * size;

      for(int k = boxRow; k < boxRow + size; k++) {
        for(int p = boxColumn; p < boxColumn + size; p++) {
          if (k != row && p != column) {
            if(number == this.matrixEntries[k][p]) {
              //System.out.println("Number: " + number);
              //System.out.println("Grid box value(" + k + "," + p + "): " + this.Grid[k][p]);
              return false;
            }
          }
        }
      }
      return true;
    }


    private void printFixedWidth(String text, int width) {
      for(int i = 0; i < width - text.length(); i++) {
        System.out.print(" ");
        System.out.print(text);
      }
    }


}