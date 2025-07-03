package com.GuardouPagou.controllers;

import com.GuardouPagou.dao.AlertaEmailDAO;
import com.GuardouPagou.models.AlertaEmail;
import com.GuardouPagou.views.AlertaEmailView;
import javafx.scene.control.Alert;

import java.sql.SQLException;

public class AlertaEmailController {
    private final AlertaEmailView view;
    private final AlertaEmailDAO dao;

    public AlertaEmailController(AlertaEmailView view) {
        this.view = view;
        this.dao = new AlertaEmailDAO();
        init();
    }

    private void init() {
        try {
            view.getTabela().setItems(dao.listarEmails());
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar e-mails: " + e.getMessage());
        }

        view.getBtnAdicionar().setOnAction(e -> adicionarEmail());
        view.getBtnRemover().setOnAction(e -> removerEmail());
    }

    private void adicionarEmail() {
        String email = view.getTfEmail().getText().trim();
        if (!validarEmail(email)) {
            mostrarErro("E-mail inv\u00e1lido");
            return;
        }
        try {
            dao.inserirEmail(email);
            view.getTabela().setItems(dao.listarEmails());
            view.getTfEmail().clear();
        } catch (SQLException e) {
            mostrarErro("N\u00e3o foi poss\u00edvel salvar: " + e.getMessage());
        }
    }

    private void removerEmail() {
        AlertaEmail selected = view.getTabela().getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            dao.removerEmail(selected.getId());
            view.getTabela().getItems().remove(selected);
        } catch (SQLException e) {
            mostrarErro("Erro ao remover: " + e.getMessage());
        }
    }

    private boolean validarEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private void mostrarErro(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg);
        a.showAndWait();
    }
}
