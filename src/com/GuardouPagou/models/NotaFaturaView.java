package com.GuardouPagou.models;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.paint.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.util.StringConverter;

public class NotaFaturaView {

    private final BorderPane root;
    private final TextField numeroNotaField;
    private final DatePicker dataEmissaoPicker;
    private final ComboBox<String> marcaComboBox;
    private final Button adicionarFaturaButton;
    private final VBox faturasContainer;
    private final Button salvarButton;

    public NotaFaturaView() {
        numeroNotaField = new TextField();
        dataEmissaoPicker = new DatePicker();
        marcaComboBox = new ComboBox<>();
        adicionarFaturaButton = new Button("+ Adicionar Nova Fatura");
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

        // Campos principais (alinhados horizontalmente)
        HBox camposBox = new HBox(15);
        camposBox.setAlignment(Pos.CENTER_LEFT);

        // Número da Nota
        VBox numeroNotaBox = new VBox(5);
        Label numeroNotaLabel = new Label("Número da Nota*:");
        numeroNotaLabel.setStyle(
                "-fx-font-family: Arial; "
                + "-fx-font-size: 16px; "
                + "-fx-text-fill: #BDBDBD;"
        );
        numeroNotaField.setPromptText("Nº da Nota Fiscal");
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
        numeroNotaField.setPrefWidth(150);
        numeroNotaBox.getChildren().addAll(numeroNotaLabel, numeroNotaField);

        // Data de Emissão
        VBox dataEmissaoBox = new VBox(5);
        Label dataEmissaoLabel = new Label("Data de Emissão*:");
        dataEmissaoLabel.setStyle(
                "-fx-font-family: Arial; "
                + "-fx-font-size: 16px; "
                + "-fx-text-fill: #BDBDBD;"
        );
        // Configurar formato de data brasileiro (dd-MM-yy)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", new Locale("pt", "BR"));
        dataEmissaoPicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                try {
                    return LocalDate.parse(string, formatter);
                } catch (Exception e) {
                    return null;
                }
            }
        });
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
        dataEmissaoPicker.setPrefWidth(150);
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
                + "-fx-border-radius: 5; "
                + "-fx-prompt-text-fill: #BDBDBD;"
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
                        + "-fx-border-radius: 5; "
                        + "-fx-prompt-text-fill: #BDBDBD;"
                );
            } else {
                marcaComboBox.setStyle(
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
        marcaComboBox.setPrefWidth(150);
        marcaBox.getChildren().addAll(marcaLabel, marcaComboBox);

        // Botão Adicionar Nova Fatura
        VBox adicionarBox = new VBox(5);
        Label placeholderLabel = new Label("");
        placeholderLabel.setStyle("-fx-font-size: 16px;");
        String buttonStyle
                = "-fx-background-color: #F0A818; "
                + "-fx-text-fill: #000000; "
                + "-fx-font-family: Arial; "
                + "-fx-font-weight: bold; "
                + "-fx-font-size: 14px; "
                + "-fx-background-radius: 5;";
        adicionarFaturaButton.setStyle(buttonStyle);
        adicionarFaturaButton.setOnMouseEntered(e -> adicionarFaturaButton.setStyle(
                "-fx-background-color: #FFC107; "
                + "-fx-text-fill: #000000; "
                + "-fx-font-family: Arial; "
                + "-fx-font-weight: bold; "
                + "-fx-font-size: 14px; "
                + "-fx-background-radius: 5;"
        ));
        adicionarFaturaButton.setOnMouseExited(e -> adicionarFaturaButton.setStyle(buttonStyle));
        adicionarFaturaButton.setPrefWidth(150);
        adicionarBox.getChildren().addAll(placeholderLabel, adicionarFaturaButton);

        camposBox.getChildren().addAll(numeroNotaBox, dataEmissaoBox, marcaBox, adicionarBox);

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
