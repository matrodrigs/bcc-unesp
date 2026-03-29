package core.cli.utils;

import java.io.IOException;

public class ConsoleHelper {
    public static void pauseAndClear() {
        System.out.print("\nAperte ENTER para continuar...");

        try {
            System.in.read();

            while (System.in.available() > 0) {
                System.in.read();
            }

            clearConsole();

        } catch (IOException e) {
            System.out.println("Erro ao ler entrada.");
        }
    }

    private static void clearConsole() {
        try {
            String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}
