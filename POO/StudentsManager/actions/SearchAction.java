package actions;

import models.*;
import utils.InputHelper;

public class SearchAction implements MenuAction {
    public int execute(Student[] students, int totalStudents) {
        if (totalStudents == 0) {
            System.out.println("\nSem alunos cadastrados...");
            return totalStudents;
        }

        String ra = InputHelper.readString("\nDigite o RA que deseja procurar: ");
        boolean found = false;

        for (int i = 0; i < totalStudents; i++) {
            if (ra.equals(students[i].getRa())) {
                Student s = students[i];
                Address a = students[i].getAddress();
                Birthday b = students[i].getBirthday();

                InputHelper.drawHeader("DADOS DO ALUNO", 40);
                System.out.printf("%-6s %-20s | %-4s %s\n", "Nome:", s.getName(), "RA:", s.getRa());
                System.out.printf("%-6s %-20s | %-4s %s\n", "RG:", s.getRg(), "Cel:", s.getCellphoneNumber());
                System.out.printf("CR: %.2f\n", students[i].getCr());
                InputHelper.drawLine('-', 40);
                System.out.printf("NASCIMENTO: %d/%d/%d\n", b.getDay(), b.getMonth(), b.getYear());
                System.out.printf("ENDEREÇO: %s, %d - %s (%s)\n", a.getStreet(), a.getNumber(), a.getCity(), a.getCep());
                InputHelper.drawLine('-', 40);

                found = true;
                break;
            }
        }

        if (!found)
            System.out.println("\nAluno nao encontrado no sistema!");

        return totalStudents;
    }
}
