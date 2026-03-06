import java.util.Scanner;
import java.util.Random;

class Matrix {
    private int[][] data;

    //Construtor achei mt legal
    public Matrix(int rows, int cols) {
        this.data = new int[rows][cols];
    }

    public void set(int row, int col, int value) {
        this.data[row][col] = value;
    }

    public int get(int row, int col) {
        return this.data[row][col];
    }

    public void printMatrix() {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                System.out.printf("%d\t", data[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}

public class Matrices {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        Random generator = new Random();

        System.out.println("Digite a quantidade de linhas");
        int rows = reader.nextInt();
        System.out.println("Digite a quantidade de colunas");
        int cols = reader.nextInt();

        Matrix matrix1 = new Matrix(rows, cols);
        Matrix matrix2 = new Matrix(rows, cols);
        Matrix finalMatrix = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix1.set(i, j, generator.nextInt(1000));
                matrix2.set(i, j, generator.nextInt(1000));
            }
        }

        System.out.println("Matriz1 gerada:");
        matrix1.printMatrix();
        System.out.println("Matriz2 gerada:");
        matrix2.printMatrix();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int sum = matrix1.get(i, j) + matrix2.get(i, j);
                finalMatrix.set(i, j, sum);
            }
        }

        System.out.println("Soma concluida!");
        finalMatrix.printMatrix();
        reader.close();
    }
}
