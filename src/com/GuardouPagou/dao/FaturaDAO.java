package com.GuardouPagou.dao;

import com.GuardouPagou.models.DatabaseConnection;
import com.GuardouPagou.models.Fatura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FaturaDAO {

    public void inserirFaturas(List<Fatura> faturas, int notaFiscalId) throws SQLException {
        String sql = "INSERT INTO faturas (nota_fiscal_id, numero_fatura, vencimento, valor, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

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

    // Adicione este novo método para marcar uma fatura específica como emitida
    public boolean marcarFaturaIndividualComoEmitida(int faturaId) throws SQLException {
        String sql = "UPDATE faturas SET status = 'Emitida' WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, faturaId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

// Adicione este novo método para verificar se todas as faturas de uma nota fiscal foram emitidas
    public boolean todasFaturasDaNotaEmitidas(int notaFiscalId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM faturas WHERE nota_fiscal_id = ? AND status != 'Emitida'";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notaFiscalId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // Retorna true se a contagem de 'Não Emitida' for zero
                }
            }
        }
        return false;
    }

    public ObservableList<Fatura> listarFaturasPorPeriodo(LocalDate dataInicial, LocalDate dataFinal) throws SQLException {
        ObservableList<Fatura> faturas = FXCollections.observableArrayList();
        String sql = "SELECT f.id, f.nota_fiscal_id, n.numero_nota, f.numero_fatura, f.vencimento, f.valor, f.status, m.nome AS marca, m.cor AS marca_cor "
                + "FROM faturas f "
                + "JOIN notas_fiscais n ON f.nota_fiscal_id = n.id "
                + "LEFT JOIN marcas m ON n.marca_id = m.id "
                + "WHERE n.arquivada = FALSE AND f.vencimento BETWEEN ? AND ? "
                + "ORDER BY f.vencimento";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(dataInicial));
            stmt.setDate(2, Date.valueOf(dataFinal));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Fatura fatura = new Fatura();
                fatura.setId(rs.getInt("id"));
                fatura.setNumeroNota(rs.getString("numero_nota"));
                fatura.setNumeroFatura(rs.getInt("numero_fatura"));
                fatura.setVencimento(rs.getDate("vencimento").toLocalDate());
                fatura.setValor(rs.getDouble("valor"));
                fatura.setStatus(rs.getString("status"));
                // Popula os dados da marca para exibição correta na tabela
                fatura.setMarca(rs.getString("marca"));
                fatura.setMarcaColor(rs.getString("marca_cor"));
                faturas.add(fatura);
            }
        }
        return faturas;
    }

    // MÉTODO CORRIGIDO
    public ObservableList<Fatura> listarFaturasPorMarcas(List<String> nomesMarcas) throws SQLException {
        ObservableList<Fatura> faturas = FXCollections.observableArrayList();
        if (nomesMarcas == null || nomesMarcas.isEmpty()) {
            return faturas;  // vazio
        }

        // 1) Monta "?, ?, ?" conforme o tamanho da lista
        String placeholders = String.join(
                ",",
                Collections.nCopies(nomesMarcas.size(), "?")
        );

        // 2) SQL com IN-list dinâmica
        String sql = "SELECT f.id, f.nota_fiscal_id, n.numero_nota, f.numero_fatura, "
                + "       f.vencimento, f.valor, f.status, "
                + "       m.nome AS marca, m.cor AS marca_cor "
                + "FROM faturas f "
                + "JOIN notas_fiscais n ON f.nota_fiscal_id = n.id "
                + "JOIN marcas m       ON n.marca_id = m.id "
                + "WHERE n.arquivada = FALSE "
                + "  AND m.nome IN (" + placeholders + ") "
                + "ORDER BY n.numero_nota, f.numero_fatura";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 3) seta cada parâmetro da lista
            for (int i = 0; i < nomesMarcas.size(); i++) {
                stmt.setString(i + 1, nomesMarcas.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Fatura f = new Fatura();
                    f.setId(rs.getInt("id"));
                    f.setNotaFiscalId(rs.getInt("nota_fiscal_id"));
                    f.setNumeroNota(rs.getString("numero_nota"));
                    f.setNumeroFatura(rs.getInt("numero_fatura"));
                    f.setVencimento(rs.getDate("vencimento").toLocalDate());
                    f.setValor(rs.getDouble("valor"));
                    f.setStatus(rs.getString("status"));
                    f.setMarca(rs.getString("marca"));
                    f.setMarcaColor(rs.getString("marca_cor"));
                    faturas.add(f);
                }
            }
        }
        return faturas;
    }

    public ObservableList<Fatura> listarFaturas(boolean exibirSomenteArquivadas) throws SQLException {
        ObservableList<Fatura> faturas = FXCollections.observableArrayList();

        StringBuilder sql = new StringBuilder()
                .append("SELECT f.id, f.nota_fiscal_id, n.numero_nota, f.numero_fatura, ")
                .append("f.vencimento, f.valor, f.status, ")
                .append("m.nome      AS marca, ")
                .append("m.cor       AS marca_cor, ")
                .append("n.arquivada ")
                .append("FROM faturas f ")
                .append("JOIN notas_fiscais n ON f.nota_fiscal_id = n.id ")
                .append("LEFT JOIN marcas m ON n.marca_id = m.id ");

        if (exibirSomenteArquivadas) {
            sql.append("WHERE n.arquivada = TRUE ");
        } else {
            sql.append("WHERE n.arquivada = FALSE ");
        }

        sql.append("ORDER BY n.numero_nota ASC, f.numero_fatura ASC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString());
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Fatura f = new Fatura();
                f.setId(rs.getInt("id"));
                f.setNotaFiscalId(rs.getInt("nota_fiscal_id"));
                f.setNumeroNota(rs.getString("numero_nota"));
                f.setNumeroFatura(rs.getInt("numero_fatura"));
                f.setVencimento(rs.getDate("vencimento").toLocalDate());
                f.setValor(rs.getDouble("valor"));
                f.setStatus(rs.getString("status"));
                f.setMarca(rs.getString("marca"));
                f.setMarcaColor(rs.getString("marca_cor"));
                faturas.add(f);
            }
        }

        return faturas;
    }
}
