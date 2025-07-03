package com.GuardouPagou.dao;

import com.GuardouPagou.models.AlertaEmail;
import com.GuardouPagou.models.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class AlertaEmailDAO {

    public ObservableList<AlertaEmail> listarEmails() throws SQLException {
        ObservableList<AlertaEmail> emails = FXCollections.observableArrayList();
        String sql = "SELECT id, email FROM alerta_emails ORDER BY email";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                AlertaEmail a = new AlertaEmail();
                a.setId(rs.getInt("id"));
                a.setEmail(rs.getString("email"));
                emails.add(a);
            }
        }
        return emails;
    }

    public void inserirEmail(String email) throws SQLException {
        String sql = "INSERT INTO alerta_emails(email) VALUES (?) ON CONFLICT DO NOTHING";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.executeUpdate();
        }
    }

    public void removerEmail(int id) throws SQLException {
        String sql = "DELETE FROM alerta_emails WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
