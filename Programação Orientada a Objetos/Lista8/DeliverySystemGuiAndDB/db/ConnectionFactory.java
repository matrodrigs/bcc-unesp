package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class ConnectionFactory {
    private static final String URL = "jdbc:hsqldb:file:db/dados/bancodados;shutdown=true";
    private static final String USER = "SA";
    private static final String PASSWORD = "";

    private ConnectionFactory() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            try (Connection connection = getConnection();
                    Statement statement = connection.createStatement()) {
                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS USERS ("
                                + "id INTEGER PRIMARY KEY, "
                                + "name VARCHAR(120) NOT NULL, "
                                + "email VARCHAR(160) NOT NULL"
                                + ")");
                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS RESTAURANTS ("
                                + "id INTEGER PRIMARY KEY, "
                                + "name VARCHAR(120) NOT NULL, "
                                + "email VARCHAR(160) NOT NULL, "
                                + "nickname VARCHAR(120) NOT NULL"
                                + ")");
                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS COURIERS ("
                                + "id INTEGER PRIMARY KEY, "
                                + "name VARCHAR(120) NOT NULL, "
                                + "email VARCHAR(160) NOT NULL, "
                                + "transport_mode VARCHAR(40) NOT NULL, "
                                + "available BOOLEAN NOT NULL"
                                + ")");
                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS RESTAURANT_MENU_ITEMS ("
                                + "restaurant_id INTEGER NOT NULL, "
                                + "item_position INTEGER NOT NULL, "
                                + "dish_name VARCHAR(160) NOT NULL, "
                                + "value DECIMAL(12,2) NOT NULL, "
                                + "PRIMARY KEY (restaurant_id, item_position), "
                                + "FOREIGN KEY (restaurant_id) REFERENCES RESTAURANTS(id) ON DELETE CASCADE"
                                + ")");
                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS ORDERS ("
                                + "id INTEGER PRIMARY KEY, "
                                + "client_id INTEGER NOT NULL, "
                                + "restaurant_id INTEGER NOT NULL, "
                                + "courier_id INTEGER, "
                                + "status VARCHAR(40) NOT NULL, "
                                + "total_value DECIMAL(12,2) NOT NULL, "
                                + "created_at TIMESTAMP NOT NULL, "
                                + "FOREIGN KEY (client_id) REFERENCES USERS(id), "
                                + "FOREIGN KEY (restaurant_id) REFERENCES RESTAURANTS(id), "
                                + "FOREIGN KEY (courier_id) REFERENCES COURIERS(id)"
                                + ")");
                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS ORDER_ITEMS ("
                                + "order_id INTEGER NOT NULL, "
                                + "item_position INTEGER NOT NULL, "
                                + "dish_name VARCHAR(160) NOT NULL, "
                                + "value DECIMAL(12,2) NOT NULL, "
                                + "PRIMARY KEY (order_id, item_position), "
                                + "FOREIGN KEY (order_id) REFERENCES ORDERS(id) ON DELETE CASCADE"
                                + ")");
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new IllegalStateException("Erro ao inicializar o banco HSQLDB.", e);
        }
    }
}
