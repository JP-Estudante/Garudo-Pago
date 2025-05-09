package com.GuardouPagou.dao;

import com.GuardouPagou.models.DatabaseConnection;
import com.GuardouPagou.models.Fatura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FaturaDAO {
    public void inserirFaturas(List<Fatura> faturas, int notaFiscalId) throws SQLException {
        String sql = "INSERT INTO faturas (nota_fiscal_id, numero_fatura, vencimento, valor, status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (Fatura fatura : faturas) {
                stmt.setInt(1, notaFiscalId);
                stmt.setInt(2, fatura.getNumeroFatura());
                stmt.setDate(3, Date.valueOf(fatura.getVencimento()));
                stmt.setDouble(4, fatura.getValor());
                stmt.setString(5, "Não Emitida");
                stmt.addBatch();
            }
            
            stmt.executeBatch();
        }
    }

    public ObservableList<Fatura> listarFaturas() throws SQLException {
        ObservableList<Fatura> faturas = FXCollections.observableArrayList();
        String sql = "SELECT f.id, f.nota_fiscal_id, n.numero_nota, f.numero_fatura, f.vencimento, f.valor, f.status " +
                     "FROM faturas f " +
                     "JOIN notas_fiscais n ON f.nota_fiscal_id = n.id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Fatura fatura = new Fatura();
                fatura.setId(rs.getInt("id"));
                fatura.setNumeroNota(rs.getString("numero_nota"));
                fatura.setNumeroFatura(rs.getInt("numero_fatura"));
                fatura.setVencimento(rs.getDate("vencimento").toLocalDate());
                fatura.setValor(rs.getDouble("valor"));
                fatura.setStatus(rs.getString("status"));
                faturas.add(fatura);
            }
        }
        return faturas;
    }
}