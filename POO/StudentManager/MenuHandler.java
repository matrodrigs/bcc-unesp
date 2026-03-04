import actions.*;
import models.Student;
import utils.InputHelper;

public class MenuHandler {
    public void start(Student[] students, int totalStudents, int maxStudents) {
        int option = 0;

        do {
            InputHelper.drawHeader("MENU", 30);
            System.out.printf("1. Cadastrar novo aluno [%d/%d]\n", totalStudents, maxStudents);
            System.out.println("2. Procurar aluno pelo RA");
            System.out.println("3. Listar alunos cadastrados");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            option = InputHelper.readInt(null, 0, 3);

            switch (option) {
                case 1:
                    if (totalStudents < maxStudents) {
                        int amount = InputHelper.readInt("\nDigite a quantidade de alunos que deseja cadastrar (N > 0): ", 1, maxStudents - totalStudents);

                        for (int i = 0; i < amount; i++) {
                            System.out.printf("\n------ CADASTRO [%d/%d] ------\n", i + 1, amount);
                            totalStudents = new RegisterAction().execute(students, totalStudents);
                        }
                    }
                    else
                        System.out.println("\nVoce nao pode cadastrar mais alunos, numero maximo atingido.");
                    break;
                case 2:
                    if (totalStudents == 0)
                        System.out.println("\nSem alunos cadastrados...");
                    else
                        new SearchAction().execute(students, totalStudents);
                    break;
                case 3:
                    if (totalStudents == 0)
                        System.out.println("\nSem alunos cadastrados...");
                    else
                        new ListAction().execute(students, totalStudents);
                    break;
            }
        } while (option != 0);
    }
}