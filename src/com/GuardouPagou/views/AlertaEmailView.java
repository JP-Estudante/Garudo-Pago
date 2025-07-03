package com.GuardouPagou.views;

import com.GuardouPagou.models.AlertaEmail;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Objects;

public class AlertaEmailView {
    private final BorderPane root;
    private final TableView<AlertaEmail> tabela;
    private final TextField tfEmail;
    private final Button btnAdicionar;
    private final Button btnRemover;

    public AlertaEmailView() {
        root = new BorderPane();
        tabela = new TableView<>();
        tfEmail = new TextField();
        btnAdicionar = new Button("Adicionar");

        btnRemover = new Button("Remover Selecionado");
        criarUI();
    }

    private void criarUI() {
        // estilo do modal
        root.setStyle("-fx-background-color: #323437; -fx-padding: 20;");

        // cabeçalho
        Label titulo = new Label("Destinatários de Alertas");
        titulo.setFont(Font.font("Poppins", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web("#F0A818"));
        VBox header = new VBox(5, titulo, new Separator());
        header.setAlignment(Pos.CENTER_LEFT);

        // configuração da tabela
        tabela.getStyleClass().add("table-padrao");
        // faz as colunas preencherem 100% da largura disponível
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<AlertaEmail, String> colEmail = new TableColumn<>("E-mail");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        // permite que a coluna cresça até preencher tudo
        colEmail.setMaxWidth(Double.MAX_VALUE);
        tabela.getColumns().add(colEmail);

        // controles de entrada
        tfEmail.setPromptText("email@exemplo.com");
        btnAdicionar.getStyleClass().add("modal-button");
        btnRemover.getStyleClass().add("modal-button");
        HBox controls = new HBox(10, tfEmail, btnAdicionar, btnRemover);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(10, 0, 0, 0));

        // monta layout
        root.setTop(header);
        root.setCenter(tabela);
        root.setBottom(controls);
    }

    public BorderPane getRoot() {
        return root;
    }

    public TableView<AlertaEmail> getTabela() {
        return tabela;
    }

    public TextField getTfEmail() {
        return tfEmail;
    }

    public Button getBtnAdicionar() {
        return btnAdicionar;
    }

    public Button getBtnRemover() {
        return btnRemover;
    }
}
