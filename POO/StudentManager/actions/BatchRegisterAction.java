package actions;

import models.Student;
import utils.InputHelper;

public class BatchRegisterAction implements MenuAction {
    public int execute(Student[] students, int totalStudents) {
        int maxStudents = students.length;

        if (totalStudents >= maxStudents) {
            System.out.println("\nVoce nao pode cadastrar mais alunos, numero maximo atingido.");
            return totalStudents;
        }

        int maxAllowed = maxStudents - totalStudents;
        int amount = InputHelper.readInt("\nDigite a quantidade de alunos que deseja cadastrar (N > 0): ", 1, maxAllowed);

        RegisterAction register = new RegisterAction();

        for (int i = 0; i < amount; i++) {
            System.out.printf("\n------ CADASTRO [%d/%d] ------\n", i + 1, amount);
            totalStudents = register.execute(students, totalStudents);
        }

        return totalStudents;
    }
}
