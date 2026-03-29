package menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.DeliverySystem;
import core.cli.Answers;
import core.cli.MenuRegistry;
import core.cli.question.QuestionType;
import models.Client;
import models.MenuItem;
import models.Order;
import models.Restaurant;

public class CreateOrderMenu {
    private enum Field {
        CLIENT_ID("client_id"), RESTAURANT_ID("restaurant_id"),
        MENU("menu"), ITEM("item");

        final String key;
        Field(String key) { this.key = key; }
        @Override public String toString() { return key; }
    }

    private static final String CLIENT_NOT_FOUND = "Erro: Este ID não corresponde a um cliente cadastrado. Tente outro.";
    private static final String RESTAURANT_NOT_FOUND = "Erro: Este ID não corresponde a um restaurante cadastrado. Tente outro.";
    private static final String DISH_NOT_FOUND = "Erro: Este prato não existe no cardápio do restaurante selecionado!";

    public static void setup(MenuRegistry menu, DeliverySystem system) {
        menu.option("Criar novo pedido")
            .when(() -> !system.getClients().isEmpty() && !system.getRestaurants().isEmpty())
            .ask(Field.CLIENT_ID, "ID do Cliente:", QuestionType.NUMBER, v -> isClientValid(v, system), CLIENT_NOT_FOUND)
            .ask(Field.RESTAURANT_ID, "ID do Restaurante:", QuestionType.NUMBER, v -> isRestaurantValid(v, system), RESTAURANT_NOT_FOUND)
            .ask(Field.MENU, "Quantos itens têm o pedido?", 1, 10, sub -> sub
                .ask(Field.ITEM, "Nome do item:", QuestionType.TEXT,
                    (v, answers) -> isDishValid(v, answers, system), DISH_NOT_FOUND)
            )
            .handle(answers -> handle(answers, system));
    }

    private static void handle(Answers answers, DeliverySystem system) {
        Integer clientId = answers.getInteger(Field.CLIENT_ID);
        Integer restId = answers.getInteger(Field.RESTAURANT_ID);

        Restaurant rest = system.getRestaurantById(restId);
        Client client = system.getClientById(clientId);

        List<MenuItem> orderItems = new ArrayList<>();
        for (Answers item : answers.getList(Field.MENU)) {
            String dishName = item.getString(Field.ITEM);
            orderItems.add(new MenuItem(dishName, rest.getDishPrice(dishName)));
        }

        Integer newOrderId = system.getOrders().size() + 1;
        Order order = new Order(newOrderId, client, rest, orderItems);

        system.createOrder(order);
        System.out.println(order.getOrderSummary());
    }

    private static boolean isClientValid(Object value, DeliverySystem system) {
        return system.isIdTaken((Integer) value, Client.class);
    }

    private static boolean isRestaurantValid(Object value, DeliverySystem system) {
        return system.isIdTaken((Integer) value, Restaurant.class);
    }

    private static boolean isDishValid(Object value, Map<String, Object> answers, DeliverySystem system) {
        Integer restId = (Integer) answers.get(Field.RESTAURANT_ID.key);
        Restaurant rest = system.getRestaurantById(restId);
        return rest.hasDish((String) value);
    }
}
