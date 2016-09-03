package csula.cs4660.exercises;

/**
 * Created by Claudia Seidel on 8/30/2016.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileRead {
    private int[][] numbers;
    /**
     * Read the file and store the content to 2d array of int
     * @param file read file
     */
    public FileRead(File file) {
        // TODO: read the file content and store content into numbers
        Scanner input;

        try {
            input = new Scanner(file);
            //numbers = new int[5][8];
            int counter1 = 0;
            int counter2 = 0;
            int length = 0;
            ArrayList<int[]> rows = new ArrayList<int[]>();

            while(input.hasNextLine()){
                String[] line = input.nextLine().split(" ");
                length = line.length;
                int[] row = new int[length];

                for (int i = 0; i < length; i++) {
                    row[i] = Integer.parseInt(line[i]);
                }
                rows.add(row);
            }

            numbers = new int[rows.size()][length];
            for (int i = 0; i < rows.size(); i++) {
                for (int j = 0; j < length; j++) {
                    int[] currRow = rows.get(i);
                    numbers[i][j] = currRow[j];
                }
            }

            //input.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Read the file assuming following by the format of split by space and next
     * line. Display the sum for each line and tell me
     * which line has the highest mean.
     *
     * lineNumber starts with 0 (programming friendly!)
     */
    public int mean(int lineNumber) {
        int length = numbers[lineNumber].length;
        int avg = 0;
        for (int i = 0; i < length; i++) {
            avg += numbers[lineNumber][i];
        }
        avg /= length;
        return avg;
    }

    public int max(int lineNumber) {
        int max = 0;
        int length = numbers[lineNumber].length;
        for (int i = 0; i < length; i++) {
            if (numbers[lineNumber][i] > max) {
                max = numbers[lineNumber][i];
            }
        }
        return max;
    }

    public int min(int lineNumber) {
        int min = 0;
        int length = numbers[lineNumber].length;
        for (int i = 0; i < length; i++) {
            if (numbers[lineNumber][i] < min) {
                min = numbers[lineNumber][i];
            }
        }
        return min;
    }

    public int sum(int lineNumber) {
        int length = numbers[lineNumber].length;
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += numbers[lineNumber][i];
        }
        return sum;
    }
}
