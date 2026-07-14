import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

class Array {
    private int[] values;
    private int index = 0;

    public Array() {
        this.values = new int[10];
    }

    public void addValue(int value) {
        this.values[index++] = value;
    }

    public int[] getValues() {
        return values;
    }
}

public class NumberManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Array array = new Array();

        System.out.println("Digite números inteiros (0 para parar):");

        while (true) {
            try {
                System.out.print("Valor: ");
                int valueToAdd = scanner.nextInt(); 

                array.addValue(valueToAdd); 

                if (valueToAdd == 0) {
                    System.out.println("Zero digitado. Encerrando...");
                    break; 
                }

            } catch (InputMismatchException e) {
                System.out.println("Erro: Valor inválido! Digite apenas números inteiros.");
                scanner.nextLine();

            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Erro: O array está cheio! Não cabem mais números.");
                break;
            }
        }

        System.out.println("\nResumo do Array:");
        System.out.println(Arrays.toString(array.getValues()));

        scanner.close();
    }
}