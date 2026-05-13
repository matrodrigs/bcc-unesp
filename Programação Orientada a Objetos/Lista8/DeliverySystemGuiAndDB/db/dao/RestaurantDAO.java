package db.dao;

import db.ConnectionFactory;
import models.MenuItem;
import models.Restaurant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RestaurantDAO {
    public List<Restaurant> findAll() {
        Map<Integer, Restaurant> restaurants = new LinkedHashMap<>();
        String restaurantsSql = "SELECT id, name, email, nickname FROM RESTAURANTS ORDER BY id";
        String menuSql = "SELECT dish_name, value FROM RESTAURANT_MENU_ITEMS WHERE restaurant_id = ? ORDER BY item_position";

        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(restaurantsSql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                restaurants.put(id, new Restaurant(
                        resultSet.getString("name"),
                        id,
                        resultSet.getString("email"),
                        resultSet.getString("nickname"),
                        new ArrayList<>()));
            }

            try (PreparedStatement menuStatement = connection.prepareStatement(menuSql)) {
                for (Restaurant restaurant : restaurants.values()) {
                    menuStatement.setInt(1, restaurant.getId());
                    try (ResultSet menuResultSet = menuStatement.executeQuery()) {
                        while (menuResultSet.next()) {
                            restaurant.getMenu().add(new MenuItem(
                                    menuResultSet.getString("dish_name"),
                                    menuResultSet.getBigDecimal("value")));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao carregar restaurantes.", e);
        }

        return new ArrayList<>(restaurants.values());
    }

    public void save(Restaurant restaurant) {
        String update = "UPDATE RESTAURANTS SET name = ?, email = ?, nickname = ? WHERE id = ?";
        String insert = "INSERT INTO RESTAURANTS (name, email, nickname, id) VALUES (?, ?, ?, ?)";
        String deleteMenu = "DELETE FROM RESTAURANT_MENU_ITEMS WHERE restaurant_id = ?";
        String insertMenu = "INSERT INTO RESTAURANT_MENU_ITEMS (restaurant_id, item_position, dish_name, value) VALUES (?, ?, ?, ?)";

        try (Connection connection = ConnectionFactory.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement statement = connection.prepareStatement(update)) {
                    fill(statement, restaurant);
                    statement.setInt(4, restaurant.getId());
                    if (statement.executeUpdate() == 0) {
                        try (PreparedStatement insertStatement = connection.prepareStatement(insert)) {
                            fill(insertStatement, restaurant);
                            insertStatement.setInt(4, restaurant.getId());
                            insertStatement.executeUpdate();
                        }
                    }
                }

                try (PreparedStatement statement = connection.prepareStatement(deleteMenu)) {
                    statement.setInt(1, restaurant.getId());
                    statement.executeUpdate();
                }

                try (PreparedStatement statement = connection.prepareStatement(insertMenu)) {
                    int position = 0;
                    for (MenuItem item : restaurant.getMenu()) {
                        statement.setInt(1, restaurant.getId());
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
            throw new IllegalStateException("Erro ao salvar restaurante.", e);
        }
    }

    private void fill(PreparedStatement statement, Restaurant restaurant) throws SQLException {
        statement.setString(1, restaurant.getName());
        statement.setString(2, restaurant.getEmail());
        statement.setString(3, restaurant.getNickname());
    }
}
