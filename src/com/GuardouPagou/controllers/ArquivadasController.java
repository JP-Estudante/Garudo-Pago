package com.GuardouPagou.controllers;

import com.GuardouPagou.dao.NotaFiscalDAO;
import com.GuardouPagou.dao.NotaFiscalArquivadaDAO;
import com.GuardouPagou.views.ArquivadasView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.SQLException;
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

        // Associa dados à tabela
        view.getTabelaNotasArquivadas().setItems(listaNotasArquivadas);
        // Carrega dados iniciais
        carregarNotasArquivadas();
    }

    private void carregarNotasArquivadas() {
        try {
            listaNotasArquivadas.setAll(
                    notaFiscalDAO.listarNotasFiscaisArquivadasComContagem(null)
            );
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar notas arquivadas", ex);
            mostrarAlertaErro("Erro ao carregar notas arquivadas: " + ex.getMessage());
        }
    }

    private void mostrarAlertaErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
