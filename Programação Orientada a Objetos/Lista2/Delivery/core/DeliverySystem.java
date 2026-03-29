package core;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import models.Client;
import models.DeliveryMan;
import models.Order;
import models.OrderStatus;
import models.Restaurant;

@Getter
public class DeliverySystem {
    private List<Client> clients = new ArrayList<>();
    private List<DeliveryMan> deliveryMen = new ArrayList<>();
    private List<Restaurant> restaurants = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();

    public void register(Client client) {
        clients.add(client);
    }

    public void register(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    public void register(DeliveryMan deliveryMan) {
        deliveryMen.add(deliveryMan);
    }

    public void createOrder(Order order) {        
        orders.add(order);
        
    }

    public boolean isIdTaken(int id, Class<?> tipo) {
        if (tipo == Client.class) {
            return clients.stream().anyMatch(c -> c.getId() == id);
        }
        if (tipo == Restaurant.class) {
            return restaurants.stream().anyMatch(r -> r.getId() == id);
        }
        if (tipo == DeliveryMan.class) {
            return deliveryMen.stream().anyMatch(d -> d.getId() == id);
        }
        return false;
    }

    public boolean isIdTaken(int id) {
        return isIdTaken(id, Client.class) || 
               isIdTaken(id, Restaurant.class) || 
               isIdTaken(id, DeliveryMan.class);
    }

    public Restaurant getRestaurantById(int id) {
        return restaurants.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Client getClientById(int id) {
        return clients.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Order getOrderById(int id) {
        return orders.stream()
                .filter(o -> o.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public DeliveryMan getDeliveryManById(int id) {
        return deliveryMen.stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void assignDeliveryManToOrder(Integer orderId, Integer deliveryManId) {
        Order order = getOrderById(orderId);
        DeliveryMan deliveryMan = getDeliveryManById(deliveryManId);

        if (order != null && deliveryMan != null) {
            order.setDeliveryMan(deliveryMan);
            order.updateStatus(); 
            deliveryMan.setAvailable(false);
        }
    }

    public Order advanceOrderStatus(Integer orderId) {
        Order order = getOrderById(orderId);
        
        if (order != null) {
            order.updateStatus();
            
            if (order.getStatus() == OrderStatus.ENTREGUE && order.getDeliveryMan() != null) {
                order.getDeliveryMan().setAvailable(true);
            }
        }
        
        return order;
    }
}