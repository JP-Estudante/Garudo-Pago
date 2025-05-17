package com.GuardouPagou.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/guardoupagou";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1235"; // Substitua pela sua senha

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco: " + e.getMessage());
            throw e;
        }
    }

    // Método de teste
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Conexao bem-sucedida!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}