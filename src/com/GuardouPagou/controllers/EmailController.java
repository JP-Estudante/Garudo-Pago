package com.GuardouPagou.controllers;

import com.GuardouPagou.dao.EmailDAO;
import com.GuardouPagou.views.EmailView;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.SQLException;
import java.util.Optional;

public class EmailController {

    private final EmailView view;
    private final EmailDAO emailDAO;

    public EmailController(EmailView view) {
        this.view = view;
        this.emailDAO = new EmailDAO();
        configurarEventos();
        carregarEmails();
    }

    private void configurarEventos() {
        // Habilita/desabilita o botão remover com base na seleção da lista
        view.getEmailListView().getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> view.getBtnRemover().setDisable(newSelection == null)
        );

        // Ação do botão Adicionar
        view.getBtnAdicionar().setOnAction(e -> adicionarEmail());

        // Ação do botão Remover
        view.getBtnRemover().setOnAction(e -> removerEmail());
    }

    private void carregarEmails() {
        ObservableList<String> emails = emailDAO.listarEmails();
        view.getEmailListView().setItems(emails);
    }

    private void adicionarEmail() {
        String email = view.getEmailField().getText().trim();

        if (email.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo vazio", "Por favor, digite um e-mail.");
            return;
        }

        if (!validarEmail(email)) {
            mostrarAlerta(Alert.AlertType.ERROR, "E-mail inválido", "O formato do e-mail digitado não é válido.");
            return;
        }

        try {
            if (emailDAO.inserirEmail(email)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "E-mail cadastrado com sucesso!");
                carregarEmails(); // Atualiza a lista
                view.getEmailField().clear();
            }
        } catch (SQLException e) {
            // Verifica se o erro é de violação de chave única
            if (e.getSQLState().equals("23505")) {
                mostrarAlerta(Alert.AlertType.ERROR, "E-mail duplicado", "Este e-mail já está cadastrado.");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Banco de Dados", "Ocorreu um erro ao salvar o e-mail: " + e.getMessage());
            }
        }
    }

    private void removerEmail() {
        String emailSelecionado = view.getEmailListView().getSelectionModel().getSelectedItem();
        if (emailSelecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Nenhum e-mail selecionado", "Por favor, selecione um e-mail na lista para remover.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Remoção");
        confirmacao.setHeaderText("Remover E-mail");
        confirmacao.setContentText("Tem certeza que deseja remover o e-mail '" + emailSelecionado + "'?");

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            if (emailDAO.excluirEmail(emailSelecionado)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "E-mail removido com sucesso.");
                carregarEmails(); // Atualiza a lista
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível remover o e-mail.");
            }
        }
    }

    private boolean validarEmail(String email) {
        // Regex simples para validação de e-mail
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}