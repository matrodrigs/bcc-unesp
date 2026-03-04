import actions.MenuAction;
import actions.ActionFactory;
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

            MenuAction action = ActionFactory.create(option);
            if (action != null)
                totalStudents = action.execute(students, totalStudents);
        } while (option != 0);
    }
}
