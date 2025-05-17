package com.GuardouPagou.models;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

public class MarcaView {

    private final BorderPane root;
    private final TextField nomeField;
    private final TextArea descricaoArea;
    private final ColorPicker corPicker;
    private final Button salvarButton;
    private final Button cancelarButton;

    public MarcaView() {
        nomeField = new TextField();
        descricaoArea = new TextArea();
        corPicker = new ColorPicker();
        salvarButton = new Button("Salvar");
        cancelarButton = new Button("Cancelar");
        root = new BorderPane();
        criarUI();
    }

    private void criarUI() {
        root.setStyle("-fx-background-color: #BDBDBD; -fx-padding: 20;");

        // Painel de formulário
        VBox formPanel = new VBox(15);
        formPanel.setPadding(new Insets(20));
        formPanel.setStyle(
                "-fx-background-color: #323437; "
                + "-fx-border-color: #C88200; "
                + "-fx-border-width: 2; "
                + "-fx-background-radius: 10; "
                + "-fx-border-radius: 10;"
        );

        // Título
        Label titulo = new Label("CADASTRO DE MARCA");
        titulo.setStyle(
                "-fx-font-family: Arial; "
                + "-fx-font-weight: bold; "
                + "-fx-font-size: 24px; "
                + "-fx-text-fill: #F0A818;"
        );

        // Campo Nome da Marca
        VBox nomeBox = new VBox(5);
        Label nomeLabel = new Label("Nome da Marca*:");
        nomeLabel.setStyle(
                "-fx-font-family: Arial; "
                + "-fx-font-size: 16px; "
                + "-fx-text-fill: #BDBDBD;"
        );
        nomeField.setPromptText("Digite o nome da marca");
        nomeField.setStyle(
                "-fx-background-color: #2A2A2A; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-size: 14px; "
                + "-fx-border-color: #4A4A4A; "
                + "-fx-border-width: 1; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5; "
                + "-fx-prompt-text-fill: #BDBDBD;"
        );
        nomeField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                nomeField.setStyle(
                        "-fx-background-color: #2A2A2A; "
                        + "-fx-text-fill: #FFFFFF; "
                        + "-fx-font-size: 14px; "
                        + "-fx-border-color: #F0A818; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5; "
                        + "-fx-prompt-text-fill: #BDBDBD;"
                );
            } else {
                nomeField.setStyle(
                        "-fx-background-color: #2A2A2A; "
                        + "-fx-text-fill: #FFFFFF; "
                        + "-fx-font-size: 14px; "
                        + "-fx-border-color: #4A4A4A; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5; "
                        + "-fx-prompt-text-fill: #BDBDBD;"
                );
            }
        });
        nomeField.setPrefWidth(400);
        nomeBox.getChildren().addAll(nomeLabel, nomeField);

        // Campo Descrição
        VBox descricaoBox = new VBox(5);
        Label descricaoLabel = new Label("Descrição:");
        descricaoLabel.setStyle(
                "-fx-font-family: Arial; "
                + "-fx-font-size: 16px; "
                + "-fx-text-fill: #BDBDBD;"
        );
        descricaoArea.setPromptText("Digite a descrição da marca");
        descricaoArea.setStyle(
                "-fx-control-inner-background: #2A2A2A; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-size: 14px; "
                + "-fx-border-color: #4A4A4A; "
                + "-fx-border-width: 1; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5; "
                + "-fx-prompt-text-fill: #BDBDBD;"
        );
        descricaoArea.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                descricaoArea.setStyle(
                        "-fx-control-inner-background: #2A2A2A; "
                        + "-fx-text-fill: #FFFFFF; "
                        + "-fx-font-size: 14px; "
                        + "-fx-border-color: #F0A818; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5; "
                        + "-fx-prompt-text-fill: #BDBDBD;"
                );
            } else {
                descricaoArea.setStyle(
                        "-fx-control-inner-background: #2A2A2A; "
                        + "-fx-text-fill: #FFFFFF; "
                        + "-fx-font-size: 14px; "
                        + "-fx-border-color: #4A4A4A; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5; "
                        + "-fx-prompt-text-fill: #BDBDBD;"
                );
            }
        });
        descricaoArea.setPrefWidth(400);
        descricaoArea.setPrefRowCount(6);
        descricaoBox.getChildren().addAll(descricaoLabel, descricaoArea);

        // Campo Cor
        VBox corBox = new VBox(5);
        Label corLabel = new Label("Cor*:");
        corLabel.setStyle(
                "-fx-font-family: Arial; "
                + "-fx-font-size: 16px; "
                + "-fx-text-fill: #BDBDBD;"
        );
        corPicker.setStyle(
                "-fx-background-color: #2A2A2A; "
                + "-fx-border-color: #4A4A4A; "
                + "-fx-border-width: 1; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5; "
                + "-fx-color-label-visible: false;"
        );
        corPicker.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                corPicker.setStyle(
                        "-fx-background-color: #2A2A2A; "
                        + "-fx-border-color: #F0A818; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5; "
                        + "-fx-color-label-visible: false;"
                );
            } else {
                corPicker.setStyle(
                        "-fx-background-color: #2A2A2A; "
                        + "-fx-border-color: #4A4A4A; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5; "
                        + "-fx-color-label-visible: false;"
                );
            }
        });
        corPicker.setPrefWidth(100); // Menor para melhor visibilidade
        corBox.getChildren().addAll(corLabel, corPicker);

        formPanel.getChildren().addAll(nomeBox, descricaoBox, corBox);

        // Botões
        HBox buttonBox = new HBox(10);
        salvarButton.setStyle(
                "-fx-background-color: #F0A818; "
                + "-fx-text-fill: #000000; "
                + "-fx-font-family: Arial; "
                + "-fx-font-weight: bold; "
                + "-fx-font-size: 14px; "
                + "-fx-background-radius: 5;"
        );
        salvarButton.setOnMouseEntered(e -> salvarButton.setStyle(
                "-fx-background-color: #FFC107; "
                + "-fx-text-fill: #000000; "
                + "-fx-font-family: Arial; "
                + "-fx-font-weight: bold; "
                + "-fx-font-size: 14px; "
                + "-fx-background-radius: 5;"
        ));
        salvarButton.setOnMouseExited(e -> salvarButton.setStyle(
                "-fx-background-color: #F0A818; "
                + "-fx-text-fill: #000000; "
                + "-fx-font-family: Arial; "
                + "-fx-font-weight: bold; "
                + "-fx-font-size: 14px; "
                + "-fx-background-radius: 5;"
        ));
        cancelarButton.setStyle(
                "-fx-background-color: #F0A818; "
                + "-fx-text-fill: #000000; "
                + "-fx-font-family: Arial; "
                + "-fx-font-weight: bold; "
                + "-fx-font-size: 14px; "
                + "-fx-background-radius: 5;"
        );
        cancelarButton.setOnMouseEntered(e -> cancelarButton.setStyle(
                "-fx-background-color: #FFC107; "
                + "-fx-text-fill: #000000; "
                + "-fx-font-family: Arial; "
                + "-fx-font-weight: bold; "
                + "-fx-font-size: 14px; "
                + "-fx-background-radius: 5;"
        ));
        cancelarButton.setOnMouseExited(e -> cancelarButton.setStyle(
                "-fx-background-color: #F0A818; "
                + "-fx-text-fill: #000000; "
                + "-fx-font-family: Arial; "
                + "-fx-font-weight: bold; "
                + "-fx-font-size: 14px; "
                + "-fx-background-radius: 5;"
        ));
        buttonBox.getChildren().addAll(salvarButton, cancelarButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        // Layout principal
        VBox mainBox = new VBox(20);
        mainBox.getChildren().addAll(titulo, formPanel, buttonBox);

        root.setCenter(mainBox);
    }

    // Getters
    public BorderPane getRoot() {
        return root;
    }

    public TextField getNomeField() {
        return nomeField;
    }

    public TextArea getDescricaoArea() {
        return descricaoArea;
    }

    public ColorPicker getCorPicker() {
        return corPicker;
    }

    public Button getSalvarButton() {
        return salvarButton;
    }

    public Button getCancelarButton() {
        return cancelarButton;
    }
}
