package com.GuardouPagou.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    private static final String URL      = "jdbc:postgresql://localhost:5432/guardoupagou";
    private static final String USER     = "postgres";
    private static final String PASSWORD = "admin"; // Substitua pela sua senha

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao conectar ao banco: {0}", e.getMessage());
            LOGGER.log(Level.SEVERE, "Stack trace:", e);
            throw e;
        }
    }

    // Método de teste
    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            LOGGER.info("Conexão bem-sucedida!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Falha ao testar conexão com o banco", e);
        }
    }
}
