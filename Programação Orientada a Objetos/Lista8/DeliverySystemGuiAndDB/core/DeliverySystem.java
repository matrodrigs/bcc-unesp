package core;

import db.ConnectionFactory;
import db.dao.ClientDAO;
import db.dao.DeliveryManDAO;
import db.dao.OrderDAO;
import db.dao.RestaurantDAO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private final ClientDAO clientDAO = new ClientDAO();
    private final DeliveryManDAO deliveryManDAO = new DeliveryManDAO();
    private final RestaurantDAO restaurantDAO = new RestaurantDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    public DeliverySystem() {
        ConnectionFactory.initializeDatabase();
        loadPersistedData();
    }

    public void register(Client client) {
        upsertClientInMemory(client);
        clientDAO.save(client);
    }

    public void register(Restaurant restaurant) {
        upsertRestaurantInMemory(restaurant);
        restaurantDAO.save(restaurant);
    }

    public void register(DeliveryMan deliveryMan) {
        upsertDeliveryManInMemory(deliveryMan);
        deliveryManDAO.save(deliveryMan);
    }

    public void createOrder(Order order) {        
        orders.add(order);
        orderDAO.save(order);
    }

    public int getNextOrderId() {
        return orders.stream()
                .map(Order::getId)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;
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
            orderDAO.save(order);
            deliveryManDAO.save(deliveryMan);
        }
    }

    public Order advanceOrderStatus(Integer orderId) {
        Order order = getOrderById(orderId);
        
        if (order != null) {
            order.updateStatus();
            
            if (order.getStatus() == OrderStatus.ENTREGUE && order.getDeliveryMan() != null) {
                order.getDeliveryMan().setAvailable(true);
                deliveryManDAO.save(order.getDeliveryMan());
            }
            orderDAO.save(order);
        }
        
        return order;
    }

    private void loadPersistedData() {
        clients = clientDAO.findAll();
        restaurants = restaurantDAO.findAll();
        deliveryMen = deliveryManDAO.findAll();

        Map<Integer, Client> clientsById = clients.stream()
                .collect(Collectors.toMap(Client::getId, client -> client));
        Map<Integer, Restaurant> restaurantsById = restaurants.stream()
                .collect(Collectors.toMap(Restaurant::getId, restaurant -> restaurant));
        Map<Integer, DeliveryMan> deliveryMenById = deliveryMen.stream()
                .collect(Collectors.toMap(DeliveryMan::getId, deliveryMan -> deliveryMan));
        orders = orderDAO.findAll(clientsById, restaurantsById, deliveryMenById);
    }

    private void upsertClientInMemory(Client client) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getId() == client.getId()) {
                clients.set(i, client);
                orders.forEach(order -> order.updateClientReference(client));
                return;
            }
        }
        clients.add(client);
    }

    private void upsertRestaurantInMemory(Restaurant restaurant) {
        for (int i = 0; i < restaurants.size(); i++) {
            if (restaurants.get(i).getId() == restaurant.getId()) {
                restaurants.set(i, restaurant);
                orders.forEach(order -> order.updateRestaurantReference(restaurant));
                return;
            }
        }
        restaurants.add(restaurant);
    }

    private void upsertDeliveryManInMemory(DeliveryMan deliveryMan) {
        for (int i = 0; i < deliveryMen.size(); i++) {
            if (deliveryMen.get(i).getId() == deliveryMan.getId()) {
                deliveryMan.setAvailable(deliveryMen.get(i).isAvailable());
                deliveryMen.set(i, deliveryMan);
                orders.forEach(order -> order.updateDeliveryManReference(deliveryMan));
                return;
            }
        }
        deliveryMen.add(deliveryMan);
    }
}
