package menus;

import core.DeliverySystem;
import core.cli.Answers;
import core.cli.MenuRegistry;
import core.cli.question.QuestionType;
import models.Order;
import models.OrderStatus;

public class UpdateOrderStatusMenu {
    private enum Field {
        ORDER_ID("order_id");

        final String key;
        Field(String key) { this.key = key; }
        @Override public String toString() { return key; }
    }

    private static final String ORDER_NOT_FOUND = "Erro: Pedido não encontrado no sistema!";
    private static final String ORDER_ALREADY_DELIVERED = "Erro: Este pedido já foi entregue e não pode ser mais atualizado!";
    private static final String ORDER_NEEDS_DELIVERY_MAN = "Erro: O pedido está em preparo. Atribua um entregador antes de enviá-lo para entrega!";

    public static void setup(MenuRegistry menu, DeliverySystem system) {
        menu.option("Atualizar status de pedido")
            .when(() -> hasUpdatableOrders(system))
            .ask(Field.ORDER_ID, "Número do Pedido:", QuestionType.NUMBER,
                v -> validateOrder(v, system))
            .handle(answers -> handle(answers, system));
    }

    private static void handle(Answers answers, DeliverySystem system) {
        Integer orderId = answers.getInteger(Field.ORDER_ID);

        Order updatedOrder = system.advanceOrderStatus(orderId);

        System.out.println("\nStatus do pedido Nº " + orderId + " atualizado para: " + updatedOrder.getStatus());
        if (updatedOrder.getStatus() == OrderStatus.ENTREGUE && updatedOrder.getDeliveryMan() != null) {
            System.out.println("Sucesso! O entregador " + updatedOrder.getDeliveryMan().getName() + " está livre para novas entregas.");
        }
    }

    private static boolean hasUpdatableOrders(DeliverySystem system) {
        return system.getOrders().stream().anyMatch(order -> {
            if (order.getStatus() == OrderStatus.ENTREGUE) return false;
            if (order.getStatus() == OrderStatus.EM_PREPARO && order.getDeliveryMan() == null) return false;
            return true;
        });
    }

    private static String validateOrder(Object value, DeliverySystem system) {
        Order order = system.getOrderById((Integer) value);

        if (order == null) return ORDER_NOT_FOUND;
        if (order.getStatus() == OrderStatus.ENTREGUE) return ORDER_ALREADY_DELIVERED;
        if (order.getStatus() == OrderStatus.EM_PREPARO && order.getDeliveryMan() == null) return ORDER_NEEDS_DELIVERY_MAN;

        return null;
    }
}
