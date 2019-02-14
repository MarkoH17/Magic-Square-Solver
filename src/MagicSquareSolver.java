// Program:		    MagicSquareSolver.java
// Author: 		    Mark Hedrick
// Last modified:	August 29, 2017
// Desc:		    Allows user to input size of magic square, and prints out calculated magic square to console, with the ability to write it to a CSV file

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class MagicSquareSolver {

    private static int row;
    private static int col;
    private static int colMax;

    public static void main(String[] args) {
        String size; //initialize the size of the puzzle

        Scanner userInput = new Scanner(System.in); //Create Scanner object to accept input from System.in

        while (true) { //Infinite loop to force user to input an odd number
            System.out.print("Enter size of square: "); //Prompt to enter the size of the square
            size = userInput.nextLine(); //Capture the result of the System.in when terminated by the new line character
            try { //Must surround in try loop in the event that input given is not an integer
                if (Integer.valueOf(size) % 2 == 1) { //determine if the number is odd by checking remainder
                    break; //if the number is odd, break the loop and continue
                } else {
                    System.out.println("Size of square must be an odd number!"); //Alert the user that the input must be an odd number
                }
            } catch (NumberFormatException nfe) { //Catch clause for non-integer input values
                System.out.println("Size must be an odd number!"); //Alert the user that the input must be a number
            }
        }

        int[][] intArray = new int[Integer.valueOf(size)][Integer.valueOf(size)]; //define the 2D integer array with size values from the user input

        row = 0; //set starting row to 0
        col = findCenterCell(intArray); //set starting column to the middle of the row
        intArray[row][col] = 1; //set the middle value in the first row to a zero

        colMax = intArray[0].length - 1; //determine the maximum column index

        solvePuzzle(intArray); //execute the solvePuzzle method and pass in the 2D integer array


        int sum = intArray.length * ((intArray.length * intArray.length) + 1) / 2; //calculate the sum that each row, column, and diag should add up to

        printPuzzle(intArray); //print out the puzzle
        //writeToCsv(intArray, "/Users/markhedrick/Desktop/output.csv"); //write out the puzzle to CSV for easy viewing and calculations in Excel or alike application
        //Uncomment the previous method to write to the path in the second parameter

        System.out.println("Values should add up to " + sum + " in each row, column, and corner diagonal."); //Alert the user to which value the row, column, and diag should add up to


    }

    private static int findCenterCell(int[][] array) {
        return array.length / 2; //return the middle cell in the int array by dividing the length of it in half
    }

    private static boolean cellExists(int r, int c) {
        return r - 1 >= 0 && c + 1 <= colMax; //return true if the cell up and to the right is within the predefined bounds of the int array
    }

    private static boolean cellIsZero(int r, int c, int[][] array) {
        return array[r][c] == 0; //return true if the value of the cell is zero
    }

    private static void setVal(int val, int r, int c, int[][] array) {
        array[r][c] = val; //assign the value val to position r,c in the array array
    }

    private static void solvePuzzle(int[][] array) {
        int currentVal; //initialize the integer 'currentVal'

        while (true) { //Infinite loop to solve the puzzle
            currentVal = array[row][col]; //determine the current value
            if (currentVal == array.length * array[array.length - 1].length) //condition when to break the infinite loop, i.e. when the current value is the maximum value in the magic square
                break; //break the infinite loop
            //System.out.println("CURRENT: (" + row + "),(" + col + ") [" + currentVal + "]"); //debug statement for iterating
            if (cellExists(row, col)) { //Condition to check whether or not the cell up and to the right exists
                if (cellIsZero(row - 1, col + 1, array)) { //Condition to check whether or not the cell up and to the right is a zero
                    row--; //move up a row
                    col++; //move to the right by 1 column
                    currentVal++; //increment the currentValue by 1
                    //System.out.println("SETTING: (" + row + "),(" + col + ") [" + currentVal + "]"); //debug statement for iterating
                    setVal(currentVal, row, col, array); //set the value in the specific row, column, and array
                } else { //Catch all instances where the value of the cell up and to the right is not a zero
                    row++; //move down a row
                    currentVal++; //increment the currentValue by 1
                    //System.out.println("SETTING: (" + row + "),(" + col + ") [" + currentVal + "]"); //debug statement for iterating
                    setVal(currentVal, row, col, array); //set the value in the specific row, column, and array
                }
            } else { //Catch all instances where the cell up and to the right does not exist
                if (col <= colMax - 1 && cellIsZero(array.length - 1, col + 1, array)) { //Condition to check whether or not the current cell is not in the last column and that the current cell's value is a zero
                    row = array.length - 1; //set the row position to the last row in the array
                    col++; //move to the right by 1 column
                    currentVal++; //increment the currentValue by 1
                    //System.out.println("SETTING: (" + row + "),(" + col + ") [" + currentVal + "]"); //debug statement for iterating
                    setVal(currentVal, row, col, array); //set the value in the specific row, column, and array
                } else if (row == 0 && col == colMax) { //Condition to check whether or not we are in the last row and furthest column
                    row++; //move down a row
                    currentVal++; //increment the currentValue by 1
                    //System.out.println("SETTING: (" + row + "),(" + col + ") [" + currentVal + "]"); //debug statement for iterating
                    setVal(currentVal, row, col, array); //set the value in the specific row, column, and array
                } else if (!cellIsZero(row, col, array) && col == colMax) { //Condition to check whether or not the current cell is not a zero, and if that cell is in the last column
                    row--; //move up a row
                    col = 0; //set the column position to the first column
                    currentVal++; //increment the currentValue by 1
                    //System.out.println("SETTING: (" + row + "),(" + col + ") [" + currentVal + "]"); //debug statement for iterating
                    setVal(currentVal, row, col, array); //set the value in the specific row, column, and array
                }
            }
        }
    }

    private static void printPuzzle(int[][] array) {
        for (int[] anArray : array) { //iterate through each row in the array
            for (int c = 0; c < array[array.length - 1].length; c++) { //iterate over each column in the array
                System.out.printf("%d\t", anArray[c]); //use a formatted string output to display the puzzle values, separating each value with a tab
            }
            System.out.println(); //after each row, terminate the line
        }
    }

    private static void writeToCsv(int[][] array, String filePath) {
        StringBuilder sb = new StringBuilder(); //define new StringBuilder Object
        for (int[] r : array) { //iterate through all of the rows in the passed in array, calling them r
            for (int c = 0; c < array[array.length - 1].length; c++) { //iterate through all of the columns in the passed in array, calling them c
                sb.append(r[c]); //append the value from row r and column c in the 2D integer array array
                if (c < array.length - 1) //if the current column is not the last column, append a comma to denote another value is coming
                    sb.append(","); //append a comma to the StringBuilder
            }
            sb.append("\n"); //After each row is iterated through, append a new line to the StringBuilder
        }
        BufferedWriter writer; //initialize the BufferedWriter
        try { //use try loop in the event that the file is locked
            writer = new BufferedWriter(new FileWriter(filePath)); //assign writer to use a new Buffered Writer that uses a new FileWriter with a handle on the parameter filePath
            writer.write(sb.toString()); //write the string builder to the BufferedWriter object writer
            writer.close(); //close the output stream of the BufferedWriter
        } catch (IOException e) { //In the event the file is unavailable for writing, print the stack trace of the IO exception
            e.printStackTrace();
        }
    }
}
