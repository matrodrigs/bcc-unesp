package utils;

import java.util.Scanner;

public class InputHelper {
    private static Scanner reader = new Scanner(System.in);

    public static int readInt(String prompt, int min, int max) {
        int value = 0;
        boolean valid = false;

        do {
            try {
                if (prompt != null)
                    System.out.print(prompt);
                value = reader.nextInt();
                reader.nextLine();

                if (value >= min && value <= max) {
                    valid = true;
                } else {
                    System.out.println("\n[!] Digite um valor entre " + min + " e " + max + ".");
                }
            } catch (Exception e) {
                System.out.println("\n[!] Digite apenas numeros inteiros.");
                reader.nextLine();
            }
        } while (!valid);

        return value;
    }

    public static float readFloat(String prompt, float min, float max) {
        float value = 0;
        boolean valid = false;

        do {
            try {
                if (prompt != null)
                    System.out.print(prompt);
                value = reader.nextFloat();
                reader.nextLine();

                if (value >= min && value <= max) {
                    valid = true;
                } else {
                    System.out.println("\n[!] Digite um valor entre " + min + " e " + max + ".");
                }
            } catch (Exception e) {
                System.out.println("\n[!] Digite apenas numeros reais.");
                reader.nextLine();
            }
        } while (!valid);

        return value;
    }

    public static String readString(String prompt) {
        System.out.print(prompt);
        return reader.nextLine();
    }

    public static String readPhone(String prompt) {
        String phone;
        boolean valid = false;

        do {
            phone = readString(prompt);

            if (phone.matches("\\d{10,11}")) {
                valid = true;
            } else {
                System.out.println("\n[!] Digite um telefone valido (apenas numeros, com DDD, 10 a 11 digitos).");
            }
        } while (!valid);

        return phone;
    }

    public static void drawLine(char character, int width) {
        for (int i = 0; i < width; i++) {
            System.out.print(character);
        }
        System.out.println();
    }

    public static void drawHeader(String title, int totalWidth) {
        int sideDashes = (totalWidth - title.length() - 2) / 2;

        System.out.println();
        
        for (int i = 0; i < sideDashes; i++) 
            System.out.print("-");

        System.out.print(" " + title + " ");

        for (int i = 0; i < sideDashes; i++) 
            System.out.print("-");

        System.out.println();
    }
}