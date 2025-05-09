package com.GuardouPagou.models;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

public class NotaFaturaView {
    private final BorderPane root;
    private final TextField numeroNotaField;
    private final DatePicker dataEmissaoPicker;
    private final ComboBox<String> marcaComboBox;
    private final Button adicionarFaturaButton;
    private final VBox faturasContainer;
    private final Button salvarButton;

    public NotaFaturaView() {
        root = new BorderPane();
        numeroNotaField = new TextField();
        dataEmissaoPicker = new DatePicker();
        marcaComboBox = new ComboBox<>();
        adicionarFaturaButton = new Button("+ Adicionar Nova Fatura");
        faturasContainer = new VBox(10);
        salvarButton = new Button("Salvar");
        criarInterface();
    }

    private void criarInterface() {
        root.setStyle("-fx-background-color: #BDBDBD; -fx-padding: 20;");

        // Painel de formulário
        VBox formPanel = new VBox(15);
        formPanel.setStyle("-fx-background-color: #323437; -fx-border-color: #C88200; -fx-border-width: 2; -fx-padding: 20; -fx-background-radius: 10;");

        // Título
        Label titleLabel = new Label("CADASTRO DE NOTA FISCAL");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.web("#F0A818"));

        // Campos principais
        HBox fieldsBox = new HBox(15);
        fieldsBox.setAlignment(Pos.CENTER_LEFT);

        // Número da Nota
        VBox numeroNotaBox = new VBox(5);
        Label numeroNotaLabel = new Label("Número da Nota*:");
        numeroNotaLabel.setTextFill(Color.web("#BDBDBD"));
        numeroNotaLabel.setFont(Font.font("Arial", 14));
        numeroNotaField.setPromptText("Digite o número da nota");
        numeroNotaField.setStyle(
            "-fx-background-color: #2A2A2A; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-border-color: #4A4A4A; " +
            "-fx-border-width: 1; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5;"
        );
        numeroNotaField.setPrefWidth(150);
        numeroNotaField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                numeroNotaField.setStyle(
                    "-fx-background-color: #2A2A2A; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-border-color: #F0A818; " +
                    "-fx-border-width: 1; " +
                    "-fx-background-radius: 5; " +
                    "-fx-border-radius: 5;"
                );
            } else {
                numeroNotaField.setStyle(
                    "-fx-background-color: #2A2A2A; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-border-color: #4A4A4A; " +
                    "-fx-border-width: 1; " +
                    "-fx-background-radius: 5; " +
                    "-fx-border-radius: 5;"
                );
            }
        });
        numeroNotaBox.getChildren().addAll(numeroNotaLabel, numeroNotaField);

        // Data de Emissão
        VBox dataEmissaoBox = new VBox(5);
        Label dataEmissaoLabel = new Label("Data de Emissão*:");
        dataEmissaoLabel.setTextFill(Color.web("#BDBDBD"));
        dataEmissaoLabel.setFont(Font.font("Arial", 14));
        dataEmissaoPicker.setStyle(
            "-fx-background-color: #2A2A2A; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-border-color: #4A4A4A; " +
            "-fx-border-width: 1; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5;"
        );
        dataEmissaoPicker.setPrefWidth(150);
        dataEmissaoPicker.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                dataEmissaoPicker.setStyle(
                    "-fx-background-color: #2A2A2A; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-border-color: #F0A818; " +
                    "-fx-border-width: 1; " +
                    "-fx-background-radius: 5; " +
                    "-fx-border-radius: 5;"
                );
            } else {
                dataEmissaoPicker.setStyle(
                    "-fx-background-color: #2A2A2A; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-border-color: #4A4A4A; " +
                    "-fx-border-width: 1; " +
                    "-fx-background-radius: 5; " +
                    "-fx-border-radius: 5;"
                );
            }
        });
        dataEmissaoBox.getChildren().addAll(dataEmissaoLabel, dataEmissaoPicker);

        // Marca
        VBox marcaBox = new VBox(5);
        Label marcaLabel = new Label("Marca*:");
        marcaLabel.setTextFill(Color.web("#BDBDBD"));
        marcaLabel.setFont(Font.font("Arial", 14));
        marcaComboBox.setStyle(
            "-fx-background-color: #2A2A2A; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-border-color: #4A4A4A; " +
            "-fx-border-width: 1; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5;"
        );
        marcaComboBox.setPrefWidth(200);
        marcaComboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                marcaComboBox.setStyle(
                    "-fx-background-color: #2A2A2A; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-border-color: #F0A818; " +
                    "-fx-border-width: 1; " +
                    "-fx-background-radius: 5; " +
                    "-fx-border-radius: 5;"
                );
            } else {
                marcaComboBox.setStyle(
                    "-fx-background-color: #2A2A2A; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-border-color: #4A4A4A; " +
                    "-fx-border-width: 1; " +
                    "-fx-background-radius: 5; " +
                    "-fx-border-radius: 5;"
                );
            }
        });
        marcaBox.getChildren().addAll(marcaLabel, marcaComboBox);

        fieldsBox.getChildren().addAll(numeroNotaBox, dataEmissaoBox, marcaBox);

        // Botão Adicionar Fatura
        adicionarFaturaButton.setStyle(
            "-fx-background-color: #F0A818; " +
            "-fx-text-fill: #000000; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5;"
        );
        adicionarFaturaButton.setOnMouseEntered(e -> adicionarFaturaButton.setStyle(
            "-fx-background-color: #FFC107; " +
            "-fx-text-fill: #000000; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5;"
        ));
        adicionarFaturaButton.setOnMouseExited(e -> adicionarFaturaButton.setStyle(
            "-fx-background-color: #F0A818; " +
            "-fx-text-fill: #000000; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5;"
        ));

        // Botão Salvar
        HBox salvarBox = new HBox();
        salvarButton.setStyle(
            "-fx-background-color: #F0A818; " +
            "-fx-text-fill: #000000; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5;"
        );
        salvarButton.setOnMouseEntered(e -> salvarButton.setStyle(
            "-fx-background-color: #FFC107; " +
            "-fx-text-fill: #000000; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5;"
        ));
        salvarButton.setOnMouseExited(e -> salvarButton.setStyle(
            "-fx-background-color: #F0A818; " +
            "-fx-text-fill: #000000; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5;"
        ));
        salvarBox.getChildren().add(salvarButton);
        salvarBox.setAlignment(Pos.CENTER_RIGHT);

        formPanel.getChildren().addAll(fieldsBox, adicionarFaturaButton, faturasContainer, salvarBox);

        // Layout principal
        VBox mainBox = new VBox(20);
        mainBox.getChildren().addAll(titleLabel, formPanel);
        mainBox.setAlignment(Pos.TOP_CENTER);

        root.setCenter(mainBox);
    }

    // Getters
    public BorderPane getRoot() {
        return root;
    }

    public TextField getNumeroNotaField() {
        return numeroNotaField;
    }

    public DatePicker getDataEmissaoPicker() {
        return dataEmissaoPicker;
    }

    public ComboBox<String> getMarcaComboBox() {
        return marcaComboBox;
    }

    public Button getAdicionarFaturaButton() {
        return adicionarFaturaButton;
    }

    public VBox getFaturasContainer() {
        return faturasContainer;
    }

    public Button getSalvarButton() {
        return salvarButton;
    }
}