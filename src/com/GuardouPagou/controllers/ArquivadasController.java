package com.GuardouPagou.controllers;

import com.GuardouPagou.dao.NotaFiscalDAO; 
import com.GuardouPagou.dao.NotaFiscalArquivadaDAO;
import com.GuardouPagou.views.ArquivadasView; 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ArquivadasController {
    private ArquivadasView view;
    private NotaFiscalDAO notaFiscalDAO; // DAO para buscar as notas arquivadas
    private ObservableList<NotaFiscalArquivadaDAO> listaNotasArquivadas;

    public ArquivadasController(ArquivadasView view) {
        this.view = view;
        this.notaFiscalDAO = new NotaFiscalDAO(); // Instancie seu DAO
        this.listaNotasArquivadas = FXCollections.observableArrayList();
        view.getTabelaNotasArquivadas().setItems(listaNotasArquivadas);
        configurarEventos();
        carregarNotasArquivadas();
    }

    private void configurarEventos() {
        view.getBtnBuscar().setOnAction(e -> buscarNotas());
        view.getBtnLimparBusca().setOnAction(e -> limparBuscaECarregar()); // Opcional
    }

    private void carregarNotasArquivadas() {
        try {
            // Este método precisará ser criado/adaptado no NotaFiscalDAO
            listaNotasArquivadas.setAll(notaFiscalDAO.listarNotasFiscaisArquivadasComContagem(null));
        } catch (SQLException ex) {
            mostrarAlertaErro("Erro ao carregar notas arquivadas: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void buscarNotas() {
        String numeroNota = view.getSearchNumeroNotaField().getText().trim();
        String marca = view.getSearchMarcaField().getText().trim();
        LocalDate dataArquivamento = view.getSearchDataArquivamentoPicker().getValue();

        Map<String, Object> filtros = new HashMap<>();
        if (!numeroNota.isEmpty()) {
            filtros.put("numero_nota", numeroNota);
        }
        if (!marca.isEmpty()) {
            filtros.put("marca", marca);
        }
        if (dataArquivamento != null) {
            filtros.put("data_arquivamento", dataArquivamento);
        }

        try {
            // Método adaptado para aceitar filtros
            listaNotasArquivadas.setAll(notaFiscalDAO.listarNotasFiscaisArquivadasComContagem(filtros.isEmpty() ? null : filtros));
        } catch (SQLException ex) {
            mostrarAlertaErro("Erro ao buscar notas arquivadas: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void limparBuscaECarregar() {
        view.getSearchNumeroNotaField().clear();
        view.getSearchMarcaField().clear();
        view.getSearchDataArquivamentoPicker().setValue(null);
        carregarNotasArquivadas(); // Recarrega todos os dados
    }

    private void mostrarAlertaErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}