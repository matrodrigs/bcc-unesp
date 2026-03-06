import java.util.Scanner;

class Math {
    public static double getFiboValue(int index) {
        double previousValue = 1, prepreviousValue = 0;
        for (int i = 0; i < index; i++) {
            if (i > 0) {
                double aux = previousValue;

                previousValue += prepreviousValue;
                prepreviousValue = aux;
            }
        }

        return previousValue;
    }
}

public class Fibonacci {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        System.out.print("Digite um indice: ");
        int index = reader.nextInt();

        double value = Math.getFiboValue(index);

        System.out.printf("O valor eh %.0f\n", value);

        reader.close();
    }
}