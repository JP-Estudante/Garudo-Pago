package com.GuardouPagou.controllers;

import com.GuardouPagou.dao.NotaFiscalDAO;
import com.GuardouPagou.dao.NotaFiscalArquivadaDAO;
import com.GuardouPagou.views.ArquivadasView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArquivadasController {
    private static final Logger LOGGER = Logger.getLogger(ArquivadasController.class.getName());

    private final ArquivadasView view;
    private final NotaFiscalDAO notaFiscalDAO;
    private final ObservableList<NotaFiscalArquivadaDAO> listaNotasArquivadas;

    public ArquivadasController(ArquivadasView view) {
        this.view = view;
        this.notaFiscalDAO = new NotaFiscalDAO();
        this.listaNotasArquivadas = FXCollections.observableArrayList();

        view.getTabelaNotasArquivadas().setItems(listaNotasArquivadas);
        configurarEventos();
        carregarNotasArquivadas();
    }

    private void configurarEventos() {
        // Usa referência de método para evitar parâmetro "e" não usado
        view.getBtnBuscar().setOnAction(this::onBuscarAction);
        view.getBtnLimparBusca().setOnAction(this::onLimparBuscaAction);
    }

    private void onBuscarAction(ActionEvent ignored) {
        buscarNotas();
    }

    private void onLimparBuscaAction(ActionEvent ignored) {
        limparBuscaECarregar();
    }

    private void carregarNotasArquivadas() {
        try {
            listaNotasArquivadas.setAll(
                    notaFiscalDAO.listarNotasFiscaisArquivadasComContagem(null)
            );
        } catch (SQLException ex) {
            // Log mais robusto em vez de printStackTrace()
            LOGGER.log(Level.SEVERE, "Erro ao carregar notas arquivadas", ex);
            mostrarAlertaErro("Erro ao carregar notas arquivadas: " + ex.getMessage());
        }
    }

    private void buscarNotas() {
        String numeroNota = view.getSearchNumeroNotaField().getText().trim();
        String marca       = view.getSearchMarcaField().getText().trim();
        LocalDate data     = view.getSearchDataArquivamentoPicker().getValue();

        Map<String,Object> filtros = new HashMap<>();
        if (!numeroNota.isEmpty()) filtros.put("numero_nota", numeroNota);
        if (!marca.isEmpty())       filtros.put("marca", marca);
        if (data != null)           filtros.put("data_arquivamento", data);

        try {
            listaNotasArquivadas.setAll(
                    notaFiscalDAO.listarNotasFiscaisArquivadasComContagem(
                            filtros.isEmpty() ? null : filtros
                    )
            );
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar notas arquivadas", ex);
            mostrarAlertaErro("Erro ao buscar notas arquivadas: " + ex.getMessage());
        }
    }

    private void limparBuscaECarregar() {
        view.getSearchNumeroNotaField().clear();
        view.getSearchMarcaField().clear();
        view.getSearchDataArquivamentoPicker().setValue(null);
        carregarNotasArquivadas();
    }

    private void mostrarAlertaErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
