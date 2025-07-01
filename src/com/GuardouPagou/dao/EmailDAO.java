package com.GuardouPagou.dao;

import com.GuardouPagou.models.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailDAO {

    private static final Logger LOGGER = Logger.getLogger(EmailDAO.class.getName());

    /**
     * Insere um novo e-mail na tabela de alertas.
     * @param email O e-mail a ser inserido.
     * @return true se a inserção for bem-sucedida, false caso contrário.
     * @throws SQLException se ocorrer um erro de banco de dados (ex: e-mail duplicado).
     */
    public boolean inserirEmail(String email) throws SQLException {
        String sql = "INSERT INTO emails_alerta (email) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Lista todos os e-mails de alerta cadastrados, em ordem alfabética.
     * @return Uma ObservableList de Strings contendo os e-mails.
     */
    public ObservableList<String> listarEmails() {
        ObservableList<String> emails = FXCollections.observableArrayList();
        String sql = "SELECT email FROM emails_alerta ORDER BY email ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                emails.add(rs.getString("email"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar e-mails de alerta.", e);
        }
        return emails;
    }

    /**
     * Exclui um e-mail da tabela de alertas.
     * @param email O e-mail a ser excluído.
     * @return true se a exclusão for bem-sucedida, false caso contrário.
     */
    public boolean excluirEmail(String email) {
        String sql = "DELETE FROM emails_alerta WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir e-mail.", e);
            return false;
        }
    }
}