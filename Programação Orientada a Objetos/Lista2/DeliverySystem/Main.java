import core.DeliverySystem;
import core.cli.MenuRegistry;
import menus.*;

public class Main {
    public static void main(String[] args) {
        DeliverySystem system = new DeliverySystem();
        MenuRegistry menu = new MenuRegistry();

        RegisterClientMenu.setup(menu, system);
        RegisterRestaurantMenu.setup(menu, system);
        RegisterDeliveryManMenu.setup(menu, system);
        CreateOrderMenu.setup(menu, system);
        AssignDeliveryManMenu.setup(menu, system);
        UpdateOrderStatusMenu.setup(menu, system);
        ListOrdersMenu.setup(menu, system);

        menu.run();
    }
}
