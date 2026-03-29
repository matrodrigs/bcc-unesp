import java.util.Scanner;
import java.lang.Math.sqrt;

class DeltaException extends Exception {
    public DeltaException(String message) {
        super(message);
    }
}

class Calculator {
    public static void bhaskara(double a, double b, double c) throws DeltaException {
        double delta = b * b - 4 * a * c;
        
        if (delta == 0) {
            throw new DeltaException("Essa equação tem apenas 1 raiz.");
        } else if (delta < 0) {
            throw new DeltaException("Essa equação NÃO tem raízes reais.");
        }

        double raiz1 = (-b + Math.sqrt(delta)) / (2 * a);
        double raiz2 = (-b - Math.sqrt(delta)) / (2 * a);

        System.out.printf("As raízes são: %.2f e %.2f", raiz1, raiz2);
    }
}

public class Solver {
    public static void main(String[] args) {
        double a, b, c;
        Scanner scanner = new Scanner(System.in);

        System.out.print("Coeficiente A: ");
        a = scanner.nextDouble();

        System.out.print("Coeficiente B: ");
        b = scanner.nextDouble();

        System.out.print("Coeficiente C: ");
        c = scanner.nextDouble();
        
        try {
            Calculator.bhaskara(a, b, c);
        } catch (DeltaException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        
        scanner.close();
    }
}