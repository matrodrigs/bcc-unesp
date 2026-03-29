package menus;

import core.DeliverySystem;
import core.cli.Answers;
import core.cli.MenuRegistry;
import core.cli.question.QuestionType;
import models.DeliveryMan;
import models.Order;
import models.OrderStatus;

public class AssignDeliveryManMenu {
    private enum Field {
        ORDER_ID("order_id"), DELIVERY_MAN_ID("delivery_man_id");

        final String key;
        Field(String key) { this.key = key; }
        @Override public String toString() { return key; }
    }

    private static final String ORDER_NOT_FOUND = "Erro: Pedido não encontrado no sistema!";
    private static final String ORDER_NOT_PREPARING = "Erro: O pedido precisa estar 'EM PREPARO' para receber um entregador!";
    private static final String ORDER_HAS_DELIVERY_MAN = "Erro: Este pedido já possui um entregador!";
    private static final String DELIVERY_MAN_NOT_FOUND = "Erro: Entregador não encontrado!";

    public static void setup(MenuRegistry menu, DeliverySystem system) {
        menu.option("Atribuir entregador a pedido")
            .when(() -> hasPendingOrders(system) && hasAvailableDeliveryMen(system))
            .ask(Field.ORDER_ID, "Número do Pedido (Apenas EM PREPARO):", QuestionType.NUMBER,
                v -> validateOrder(v, system))
            .ask(Field.DELIVERY_MAN_ID, "ID do Entregador:", QuestionType.NUMBER,
                v -> validateDeliveryMan(v, system))
            .handle(answers -> handle(answers, system));
    }

    private static void handle(Answers answers, DeliverySystem system) {
        Integer orderId = answers.getInteger(Field.ORDER_ID);
        Integer deliveryManId = answers.getInteger(Field.DELIVERY_MAN_ID);

        system.assignDeliveryManToOrder(orderId, deliveryManId);

        String deliveryManName = system.getDeliveryManById(deliveryManId).getName();
        System.out.println("\nEntregador " + deliveryManName + " atribuído! Pedido Nº " + orderId + " saiu para entrega.");
    }

    private static boolean hasPendingOrders(DeliverySystem system) {
        return system.getOrders().stream()
            .anyMatch(order -> order.getDeliveryMan() == null && order.getStatus() == OrderStatus.EM_PREPARO);
    }

    private static boolean hasAvailableDeliveryMen(DeliverySystem system) {
        return system.getDeliveryMen().stream()
            .anyMatch(deliveryMan -> deliveryMan.isAvailable());
    }

    private static String validateOrder(Object value, DeliverySystem system) {
        Order order = system.getOrderById((Integer) value);

        if (order == null) return ORDER_NOT_FOUND;
        if (order.getStatus() != OrderStatus.EM_PREPARO) return ORDER_NOT_PREPARING;
        if (order.getDeliveryMan() != null) return ORDER_HAS_DELIVERY_MAN;

        return null;
    }

    private static String validateDeliveryMan(Object value, DeliverySystem system) {
        DeliveryMan dm = system.getDeliveryManById((Integer) value);

        if (dm == null) return DELIVERY_MAN_NOT_FOUND;
        if (!dm.isAvailable()) return "Erro: O entregador " + dm.getName() + " já está ocupado em outra entrega!";

        return null;
    }
}
