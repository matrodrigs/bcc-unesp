package db.dao;

import db.ConnectionFactory;
import models.Client;
import models.DeliveryMan;
import models.MenuItem;
import models.Order;
import models.OrderStatus;
import models.Restaurant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDAO {
    public List<Order> findAll(Map<Integer, Client> clients, Map<Integer, Restaurant> restaurants,
            Map<Integer, DeliveryMan> deliveryMen) {
        List<Order> orders = new ArrayList<>();
        String ordersSql = "SELECT id, client_id, restaurant_id, courier_id, status, total_value, created_at FROM ORDERS ORDER BY id";
        String itemsSql = "SELECT dish_name, value FROM ORDER_ITEMS WHERE order_id = ? ORDER BY item_position";

        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(ordersSql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int orderId = resultSet.getInt("id");
                List<MenuItem> items = loadItems(connection, itemsSql, orderId);
                DeliveryMan deliveryMan = null;
                int deliveryManId = resultSet.getInt("courier_id");
                if (!resultSet.wasNull()) {
                    deliveryMan = deliveryMen.get(deliveryManId);
                }

                Order order = new Order(
                        orderId,
                        clients.get(resultSet.getInt("client_id")),
                        restaurants.get(resultSet.getInt("restaurant_id")),
                        items,
                        resultSet.getBigDecimal("total_value"),
                        OrderStatus.valueOf(resultSet.getString("status")),
                        resultSet.getTimestamp("created_at").toLocalDateTime(),
                        deliveryMan);
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao carregar pedidos.", e);
        }

        return orders;
    }

    public void save(Order order) {
        String update = "UPDATE ORDERS SET client_id = ?, restaurant_id = ?, courier_id = ?, status = ?, total_value = ?, created_at = ? WHERE id = ?";
        String insert = "INSERT INTO ORDERS (client_id, restaurant_id, courier_id, status, total_value, created_at, id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String deleteItems = "DELETE FROM ORDER_ITEMS WHERE order_id = ?";
        String insertItem = "INSERT INTO ORDER_ITEMS (order_id, item_position, dish_name, value) VALUES (?, ?, ?, ?)";

        try (Connection connection = ConnectionFactory.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement statement = connection.prepareStatement(update)) {
                    fill(statement, order);
                    statement.setInt(7, order.getId());
                    if (statement.executeUpdate() == 0) {
                        try (PreparedStatement insertStatement = connection.prepareStatement(insert)) {
                            fill(insertStatement, order);
                            insertStatement.setInt(7, order.getId());
                            insertStatement.executeUpdate();
                        }
                    }
                }

                try (PreparedStatement statement = connection.prepareStatement(deleteItems)) {
                    statement.setInt(1, order.getId());
                    statement.executeUpdate();
                }

                try (PreparedStatement statement = connection.prepareStatement(insertItem)) {
                    int position = 0;
                    for (MenuItem item : order.getItems()) {
                        statement.setInt(1, order.getId());
                        statement.setInt(2, position++);
                        statement.setString(3, item.getDishName());
                        statement.setBigDecimal(4, item.getValue());
                        statement.addBatch();
                    }
                    statement.executeBatch();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao salvar pedido.", e);
        }
    }

    private List<MenuItem> loadItems(Connection connection, String sql, int orderId) throws SQLException {
        List<MenuItem> items = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(new MenuItem(
                            resultSet.getString("dish_name"),
                            resultSet.getBigDecimal("value")));
                }
            }
        }
        return items;
    }

    private void fill(PreparedStatement statement, Order order) throws SQLException {
        statement.setInt(1, order.getClient().getId());
        statement.setInt(2, order.getRestaurant().getId());
        if (order.getDeliveryMan() == null) {
            statement.setNull(3, java.sql.Types.INTEGER);
        } else {
            statement.setInt(3, order.getDeliveryMan().getId());
        }
        statement.setString(4, order.getStatus().name());
        statement.setBigDecimal(5, order.getTotalValue());
        statement.setTimestamp(6, Timestamp.valueOf(order.getCreatedAt()));
    }
}
