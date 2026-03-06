import java.util.Scanner;

public class Bhaskara {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        System.out.print("Digite A: ");
        double a = reader.nextDouble();
        System.out.print("Digite B: ");
        double b = reader.nextDouble();
        System.out.print("Digite C: ");
        double c = reader.nextDouble();

        double delta = (b * b) - (4 * a * c);

        if (delta < 0)
            System.out.println("Sem raizes reais");
        else {
            double x1 = ((-b + Math.sqrt(delta)) / (2 * a));
            double x2 = ((-b - Math.sqrt(delta)) / (2 * a));
            System.out.println("X1: " + x1 + "\nX2: " + x2);
        }

        reader.close();
    }
}