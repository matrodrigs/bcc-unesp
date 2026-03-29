package menus;

import core.DeliverySystem;
import core.cli.Answers;
import core.cli.MenuRegistry;
import core.cli.question.QuestionType;
import models.Client;

public class RegisterClientMenu {
    private enum Field {
        NAME("name"), ID("id"), EMAIL("email");

        final String key;
        Field(String key) { this.key = key; }
        @Override public String toString() { return key; }
    }

    private static final String ID_TAKEN = "Erro: Este ID já está em uso! Tente outro.";

    public static void setup(MenuRegistry menu, DeliverySystem system) {
        menu.option("Cadastrar Cliente", () -> system.getClients().size())
            .ask(Field.NAME, "Nome do cliente:", QuestionType.TEXT)
            .ask(Field.ID, "ID:", QuestionType.NUMBER, v -> isIdAvailable(v, system), ID_TAKEN)
            .ask(Field.EMAIL, "Email:", QuestionType.TEXT)
            .handle(answers -> handle(answers, system));
    }

    private static void handle(Answers answers, DeliverySystem system) {
        String name = answers.getString(Field.NAME);
        Integer id = answers.getInteger(Field.ID);
        String email = answers.getString(Field.EMAIL);

        System.out.println("\nCadastrando cliente...");
        system.register(new Client(name, id, email));
    }

    private static boolean isIdAvailable(Object value, DeliverySystem system) {
        return !system.isIdTaken((Integer) value);
    }
}
