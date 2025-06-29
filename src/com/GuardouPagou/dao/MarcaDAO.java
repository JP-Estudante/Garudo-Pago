package com.GuardouPagou.dao;

import com.GuardouPagou.models.DatabaseConnection;
import com.GuardouPagou.models.Marca;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class MarcaDAO {
    public void inserirMarca(String nome, String descricao, String cor) throws SQLException {
        String sql = "INSERT INTO marcas (nome, descricao, cor) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, descricao.isEmpty() ? null : descricao); // Salva null se descrição vazia
            stmt.setString(3, cor);
            stmt.executeUpdate();
        }
    }
    
    public void excluirMarca(int id) throws SQLException {
        String sql = "DELETE FROM marcas WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public ObservableList<Marca> listarMarcas() throws SQLException {
        ObservableList<Marca> marcas = FXCollections.observableArrayList();
        String sql = "SELECT * FROM marcas ORDER BY nome ASC"; // Ordenação alfabética
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Marca marca = new Marca();
                marca.setId(rs.getInt("id"));
                marca.setNome(rs.getString("nome"));
                marca.setDescricao(rs.getString("descricao"));
                marca.setCor(rs.getString("cor"));
                marcas.add(marca);
            }
        }
        return marcas;
    }
    
    public int getNextId() throws SQLException {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 AS next_id FROM marcas";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("next_id");
            }
            return 1;
        }
    }

    public Integer obterIdPorNome(String nome) throws SQLException {
        String sql = "SELECT id FROM marcas WHERE nome = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return null;
    }
}
