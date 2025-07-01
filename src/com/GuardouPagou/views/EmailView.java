package com.GuardouPagou.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class EmailView {

    private final BorderPane root;
    private final ListView<String> emailListView;
    private final TextField emailField;
    private final Button btnAdicionar;
    private final Button btnRemover;

    public EmailView() {
        root = new BorderPane();
        emailListView = new ListView<>();
        emailField = new TextField();
        btnAdicionar = new Button("Adicionar");
        btnRemover = new Button("Remover");
        criarUI();
    }

    private void criarUI() {
        root.setStyle("-fx-background-color: #323437; -fx-padding: 20;");

        // --- Título ---
        Label titulo = new Label("Gerenciar E-mails para Alertas");
        titulo.setFont(Font.font("Poppins", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web("#F0A818"));
        BorderPane.setAlignment(titulo, Pos.CENTER);
        root.setTop(titulo);

        // --- Lista de E-mails ---
        emailListView.getStyleClass().add("text-area-pill"); // Reutilizando estilo
        emailListView.setPrefHeight(300);

        Label listaLabel = new Label("E-mails Cadastrados:");
        listaLabel.getStyleClass().add("field-subtitle");

        VBox listaContainer = new VBox(5, listaLabel, emailListView);
        listaContainer.setPadding(new Insets(20, 0, 0, 0));
        root.setCenter(listaContainer);

        // --- Painel de Ações na Base ---
        HBox painelAcoes = new HBox(10);
        painelAcoes.setAlignment(Pos.CENTER_LEFT);
        painelAcoes.setPadding(new Insets(15, 0, 0, 0));

        emailField.setPromptText("Digite um e-mail válido");
        emailField.setPrefWidth(250);
        HBox.setHgrow(emailField, Priority.ALWAYS);

        btnAdicionar.getStyleClass().addAll("modal-button", "icon-save");
        btnRemover.getStyleClass().addAll("modal-button", "icon-clean"); // Ícone de limpar servirá
        btnRemover.setDisable(true); // Desabilitado até um item ser selecionado

        painelAcoes.getChildren().addAll(emailField, btnAdicionar, btnRemover);
        root.setBottom(painelAcoes);
    }

    // Getters para o Controller
    public BorderPane getRoot() {
        return root;
    }

    public ListView<String> getEmailListView() {
        return emailListView;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public Button getBtnAdicionar() {
        return btnAdicionar;
    }

    public Button getBtnRemover() {
        return btnRemover;
    }
}