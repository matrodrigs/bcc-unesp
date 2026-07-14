package db.dao;

import db.ConnectionFactory;
import models.DeliveryMan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryManDAO {
    public List<DeliveryMan> findAll() {
        List<DeliveryMan> deliveryMen = new ArrayList<>();
        String sql = "SELECT id, name, email, transport_mode, available FROM COURIERS ORDER BY id";

        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                DeliveryMan deliveryMan = new DeliveryMan(
                        resultSet.getString("name"),
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("transport_mode"));
                deliveryMan.setAvailable(resultSet.getBoolean("available"));
                deliveryMen.add(deliveryMan);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao carregar entregadores.", e);
        }

        return deliveryMen;
    }

    public void save(DeliveryMan deliveryMan) {
        String update = "UPDATE COURIERS SET name = ?, email = ?, transport_mode = ?, available = ? WHERE id = ?";
        String insert = "INSERT INTO COURIERS (name, email, transport_mode, available, id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(update)) {
                fill(statement, deliveryMan);
                statement.setInt(5, deliveryMan.getId());
                if (statement.executeUpdate() > 0) {
                    return;
                }
            }

            try (PreparedStatement statement = connection.prepareStatement(insert)) {
                fill(statement, deliveryMan);
                statement.setInt(5, deliveryMan.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao salvar entregador.", e);
        }
    }

    private void fill(PreparedStatement statement, DeliveryMan deliveryMan) throws SQLException {
        statement.setString(1, deliveryMan.getName());
        statement.setString(2, deliveryMan.getEmail());
        statement.setString(3, deliveryMan.getTransportMode());
        statement.setBoolean(4, deliveryMan.isAvailable());
    }
}
