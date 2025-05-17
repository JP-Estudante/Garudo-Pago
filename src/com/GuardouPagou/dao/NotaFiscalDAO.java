package com.GuardouPagou.dao;

import com.GuardouPagou.models.DatabaseConnection;
import com.GuardouPagou.models.NotaFiscal;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class NotaFiscalDAO {
    
    public boolean existeNotaFiscal(String numeroNota) throws SQLException {
        String sql = "SELECT COUNT(*) FROM notas_fiscais WHERE numero_nota = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numeroNota);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public int inserirNotaFiscal(NotaFiscal nota) throws SQLException {
        String sql = "INSERT INTO notas_fiscais (numero_nota, data_emissao, marca) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Define os parâmetros da query
            stmt.setString(1, nota.getNumeroNota());
            stmt.setDate(2, Date.valueOf(nota.getDataEmissao()));
            stmt.setString(3, nota.getMarca());
            
            // Executa a inserção
            int affectedRows = stmt.executeUpdate();
            
            // Verifica se a inserção foi bem-sucedida e retorna o ID gerado
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Retorna o ID gerado
                    }
                }
            }
            return -1; // Retorna -1 se a inserção falhar
        }
    }
    
    // Método adicional recomendado
    public List<NotaFiscal> listarNotasFiscais() throws SQLException {
        List<NotaFiscal> notas = new ArrayList<>();
        String sql = "SELECT * FROM notas_fiscais";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                NotaFiscal nota = new NotaFiscal();
                nota.setId(rs.getInt("id"));
                nota.setNumeroNota(rs.getString("numero_nota"));
                nota.setDataEmissao(rs.getDate("data_emissao").toLocalDate());
                nota.setMarca(rs.getString("marca"));
                notas.add(nota);
            }
        }
        return notas;
    }
}