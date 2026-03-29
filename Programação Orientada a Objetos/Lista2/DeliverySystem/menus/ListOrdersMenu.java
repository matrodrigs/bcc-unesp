package menus;

import core.DeliverySystem;
import core.cli.MenuRegistry;

public class ListOrdersMenu {
    public static void setup(MenuRegistry menu, DeliverySystem system) {
        menu.option("Listar pedidos")
            .when(() -> !system.getOrders().isEmpty())
            .handle(answers -> {
                System.out.println("\n=== Lista de Pedidos ===");
                system.getOrders().forEach(order -> System.out.println(order.getOrderSummary()));
            });
    }
}
