package db.dao;

import db.ConnectionFactory;
import models.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT id, name, email FROM USERS ORDER BY id";

        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                clients.add(new Client(
                        resultSet.getString("name"),
                        resultSet.getInt("id"),
                        resultSet.getString("email")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao carregar clientes.", e);
        }

        return clients;
    }

    public void save(Client client) {
        String update = "UPDATE USERS SET name = ?, email = ? WHERE id = ?";
        String insert = "INSERT INTO USERS (name, email, id) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(update)) {
                statement.setString(1, client.getName());
                statement.setString(2, client.getEmail());
                statement.setInt(3, client.getId());
                if (statement.executeUpdate() > 0) {
                    return;
                }
            }

            try (PreparedStatement statement = connection.prepareStatement(insert)) {
                statement.setString(1, client.getName());
                statement.setString(2, client.getEmail());
                statement.setInt(3, client.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao salvar cliente.", e);
        }
    }
}
