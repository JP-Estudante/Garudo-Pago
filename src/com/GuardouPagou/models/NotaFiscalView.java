package com.GuardouPagou.models;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.paint.Color;

public class NotaFiscalView {

    private final BorderPane root;
    private final TextField numeroNotaField;
    private final DatePicker dataEmissaoPicker;
    private final ComboBox<String> marcaComboBox;
    private final Button adicionarFaturaButton;
    private final VBox faturasContainer;
    private final Button salvarButton;

    public NotaFiscalView() {
        numeroNotaField = new TextField();
        dataEmissaoPicker = new DatePicker();
        marcaComboBox = new ComboBox<>();
        adicionarFaturaButton = new Button("Adicionar Nova Fatura");
        faturasContainer = new VBox(10);
        salvarButton = new Button("Salvar");
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
        Label titulo = new Label("CADASTRO DE NOTA FISCAL");
        titulo.setStyle(
                "-fx-font-family: Arial; "
                + "-fx-font-weight: bold; "
                + "-fx-font-size: 24px; "
                + "-fx-text-fill: #F0A818;"
        );

        // Campos principais
        VBox camposBox = new VBox(10);

        // Número da Nota
        VBox numeroNotaBox = new VBox(5);
        Label numeroNotaLabel = new Label("Número da Nota*:");
        numeroNotaLabel.setStyle(
                "-fx-font-family: Arial; "
                + "-fx-font-size: 16px; "
                + "-fx-text-fill: #BDBDBD;"
        );
        numeroNotaField.setPromptText("Digite o número da nota");
        numeroNotaField.setStyle(
                "-fx-background-color: #2A2A2A; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-size: 14px; "
                + "-fx-border-color: #4A4A4A; "
                + "-fx-border-width: 1; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5; "
                + "-fx-prompt-text-fill: #BDBDBD;"
        );
        numeroNotaField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                numeroNotaField.setStyle(
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
                numeroNotaField.setStyle(
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
        numeroNotaField.setPrefWidth(200);
        numeroNotaBox.getChildren().addAll(numeroNotaLabel, numeroNotaField);

        // Data de Emissão
        VBox dataEmissaoBox = new VBox(5);
        Label dataEmissaoLabel = new Label("Data de Emissão*:");
        dataEmissaoLabel.setStyle(
                "-fx-font-family: Arial; "
                + "-fx-font-size: 16px; "
                + "-fx-text-fill: #BDBDBD;"
        );
        dataEmissaoPicker.setStyle(
                "-fx-background-color: #2A2A2A; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-size: 14px; "
                + "-fx-border-color: #4A4A4A; "
                + "-fx-border-width: 1; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5;"
        );
        dataEmissaoPicker.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                dataEmissaoPicker.setStyle(
                        "-fx-background-color: #2A2A2A; "
                        + "-fx-text-fill: #FFFFFF; "
                        + "-fx-font-size: 14px; "
                        + "-fx-border-color: #F0A818; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5;"
                );
            } else {
                dataEmissaoPicker.setStyle(
                        "-fx-background-color: #2A2A2A; "
                        + "-fx-text-fill: #FFFFFF; "
                        + "-fx-font-size: 14px; "
                        + "-fx-border-color: #4A4A4A; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5;"
                );
            }
        });
        dataEmissaoPicker.setPrefWidth(200);
        dataEmissaoBox.getChildren().addAll(dataEmissaoLabel, dataEmissaoPicker);

        // Marca
        VBox marcaBox = new VBox(5);
        Label marcaLabel = new Label("Marca*:");
        marcaLabel.setStyle(
                "-fx-font-family: Arial; "
                + "-fx-font-size: 16px; "
                + "-fx-text-fill: #BDBDBD;"
        );
        marcaComboBox.setPromptText("Selecione uma marca");
        marcaComboBox.setStyle(
                "-fx-background-color: #2A2A2A; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-size: 14px; "
                + "-fx-border-color: #4A4A4A; "
                + "-fx-border-width: 1; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5;"               
        );
        marcaComboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                marcaComboBox.setStyle(
                        "-fx-background-color: #2A2A2A; "
                        + "-fx-text-fill: #FFFFFF; "
                        + "-fx-font-size: 14px; "
                        + "-fx-border-color: #F0A818; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5;"
                );
            } else {
                marcaComboBox.setStyle(
                        "-fx-background-color: #2A2A2A; "
                        + "-fx-text-fill: #FFFFFF; "
                        + "-fx-font-size: 14px; "
                        + "-fx-border-color: #4A4A4A; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5;"
                );
            }
        });
        marcaComboBox.setPrefWidth(200);
        marcaBox.getChildren().addAll(marcaLabel, marcaComboBox);

        // Botão Adicionar Nova Fatura
        String buttonStyle
                = "-fx-background-color: #F0A818 !important; "
                + "-fx-text-fill: #000000 !important; "
                + "-fx-font-family: Arial !important; "
                + "-fx-font-weight: bold !important; "
                + "-fx-font-size: 14px !important; "
                + "-fx-background-radius: 5 !important;";
        adicionarFaturaButton.setStyle(buttonStyle);
        adicionarFaturaButton.setOnMouseEntered(e -> adicionarFaturaButton.setStyle(buttonStyle));
        adicionarFaturaButton.setOnMouseExited(e -> adicionarFaturaButton.setStyle(buttonStyle));
        adicionarFaturaButton.setPrefWidth(200);

        camposBox.getChildren().addAll(numeroNotaBox, dataEmissaoBox, marcaBox, adicionarFaturaButton);

        // Container de faturas
        faturasContainer.setStyle(
                "-fx-background-color: #2A2A2A; "
                + "-fx-padding: 10; "
                + "-fx-border-color: #4A4A4A; "
                + "-fx-border-width: 1; "
                + "-fx-background-radius: 5;"
        );

        // Botão Salvar
        HBox salvarBox = new HBox();
        salvarBox.setAlignment(Pos.CENTER_RIGHT);
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
        salvarBox.getChildren().add(salvarButton);

        formPanel.getChildren().addAll(titulo, camposBox, faturasContainer, salvarBox);

        root.setCenter(formPanel);
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
