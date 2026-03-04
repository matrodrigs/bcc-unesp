package actions;

import models.*;
import utils.InputHelper;
import java.util.Arrays;

public class ListAction implements MenuAction {
    public int execute (Student[] students, int totalStudents) {
        if (totalStudents == 0) {
            System.out.println("\nSem alunos cadastrados...");
            return totalStudents;
        }

        if (totalStudents > 1) {
            InputHelper.drawHeader("OPCOES DE ORDENACAO", 30);
            System.out.println("1. Por nome (A-Z)");
            System.out.println("2. Por RA (Crescente)");
            System.out.println("3. Por CR (Maior primeiro)");
            int sortOption = InputHelper.readInt("Escolha como listar: ", 1, 3);

            switch (sortOption) {
                case 1:
                    Arrays.sort(students, 0, totalStudents, (s1, s2) -> 
                        s1.getName().compareToIgnoreCase(s2.getName()));
                    break;
                case 2:
                    Arrays.sort(students, 0, totalStudents, (s1, s2) -> 
                        s1.getRa().compareTo(s2.getRa()));
                    break;
                case 3:
                    Arrays.sort(students, 0, totalStudents, (s1, s2) -> 
                        Float.compare(s2.getCr(), s1.getCr()));
                    break;
            }
        }
        
        System.out.printf("\n%-30s | %-12s | %-5s\n", "NOME", "RA", "CR");
        InputHelper.drawLine('-', 60);

        for (int i = 0; i < totalStudents; i++) {
            Student s = students[i];
            System.out.printf("%-30.30s | %-12s | %-5.2f\n", (i + 1) + ". " + s.getName(), s.getRa(), s.getCr());
        }
        
        InputHelper.drawLine('-', 60);

        return totalStudents;
    }
}
