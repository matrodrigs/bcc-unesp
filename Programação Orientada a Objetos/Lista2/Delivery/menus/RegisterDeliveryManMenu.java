package menus;

import core.DeliverySystem;
import core.cli.Answers;
import core.cli.MenuRegistry;
import core.cli.question.QuestionType;
import models.DeliveryMan;

public class RegisterDeliveryManMenu {
    private enum Field {
        NAME("name"), ID("id"), EMAIL("email"), VEHICLE("vehicle");

        final String key;
        Field(String key) { this.key = key; }
        @Override public String toString() { return key; }
    }

    private static final String ID_TAKEN = "Erro: Este ID já está em uso! Tente outro.";
    private static final String VEHICLE_INVALID = "Erro: Veículo inválido! Digite apenas 'Moto' ou 'Bicicleta'.";

    public static void setup(MenuRegistry menu, DeliverySystem system) {
        menu.option("Cadastrar Entregador", () -> system.getDeliveryMen().size())
            .ask(Field.NAME, "Nome do entregador:", QuestionType.TEXT)
            .ask(Field.ID, "ID:", QuestionType.NUMBER, v -> isIdAvailable(v, system), ID_TAKEN)
            .ask(Field.EMAIL, "Email:", QuestionType.TEXT)
            .ask(Field.VEHICLE, "Veículo [Moto/Bicicleta]:", QuestionType.TEXT, v -> isVehicleValid(v), VEHICLE_INVALID)
            .handle(answers -> handle(answers, system));
    }

    private static void handle(Answers answers, DeliverySystem system) {
        String name = answers.getString(Field.NAME);
        Integer id = answers.getInteger(Field.ID);
        String email = answers.getString(Field.EMAIL);
        String vehicle = answers.getString(Field.VEHICLE);

        System.out.println("\nCadastrando entregador...");
        system.register(new DeliveryMan(name, id, email, vehicle));
    }

    private static boolean isIdAvailable(Object value, DeliverySystem system) {
        return !system.isIdTaken((Integer) value);
    }

    private static boolean isVehicleValid(Object value) {
        String vehicle = (String) value;
        return vehicle.equalsIgnoreCase("Moto") || vehicle.equalsIgnoreCase("Bicicleta");
    }
}
