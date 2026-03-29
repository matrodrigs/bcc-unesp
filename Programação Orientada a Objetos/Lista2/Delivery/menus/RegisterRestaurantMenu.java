package menus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import core.DeliverySystem;
import core.cli.Answers;
import core.cli.MenuRegistry;
import core.cli.question.QuestionType;
import models.MenuItem;
import models.Restaurant;

public class RegisterRestaurantMenu {
    private enum Field {
        NAME("name"), ID("id"), EMAIL("email"), NICKNAME("nickname"),
        MENU("menu"), ITEM("item"), PRICE("price");

        final String key;
        Field(String key) { this.key = key; }
        @Override public String toString() { return key; }
    }

    private static final String ID_TAKEN = "Erro: Este ID já está em uso! Tente outro.";
    private static final String PRICE_INVALID = "Erro: O valor do item deve ser maior que zero!";

    public static void setup(MenuRegistry menu, DeliverySystem system) {
        menu.option("Cadastrar Restaurante", () -> system.getRestaurants().size())
            .ask(Field.NAME, "Nome do restaurante:", QuestionType.TEXT)
            .ask(Field.ID, "ID:", QuestionType.NUMBER, v -> isIdAvailable(v, system), ID_TAKEN)
            .ask(Field.EMAIL, "Email:", QuestionType.TEXT)
            .ask(Field.NICKNAME, "Nome fantasia:", QuestionType.TEXT)
            .ask(Field.MENU, "Quantos itens deseja?", 1, 20, sub -> sub
                .ask(Field.ITEM, "Nome do item:", QuestionType.TEXT)
                .ask(Field.PRICE, "Valor (ex: 45,90):", QuestionType.DECIMAL, v -> isPricePositive(v), PRICE_INVALID)
            )
            .handle(answers -> handle(answers, system));
    }

    private static void handle(Answers answers, DeliverySystem system) {
        String name = answers.getString(Field.NAME);
        Integer id = answers.getInteger(Field.ID);
        String email = answers.getString(Field.EMAIL);
        String nickname = answers.getString(Field.NICKNAME);

        List<MenuItem> menuItems = new ArrayList<>();
        for (Answers item : answers.getList(Field.MENU)) {
            menuItems.add(new MenuItem(item.getString(Field.ITEM), item.getDecimal(Field.PRICE)));
        }

        System.out.println("\nCadastrando restaurante...");
        system.register(new Restaurant(name, id, email, nickname, menuItems));
    }

    private static boolean isIdAvailable(Object value, DeliverySystem system) {
        return !system.isIdTaken((Integer) value);
    }

    private static boolean isPricePositive(Object value) {
        return ((BigDecimal) value).compareTo(BigDecimal.ZERO) > 0;
    }
}
