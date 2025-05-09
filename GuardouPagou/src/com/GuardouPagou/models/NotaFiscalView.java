package com.GuardouPagou.models;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class NotaFiscalView {
    private final VBox root;
    private final TextField numeroNotaField;
    private final DatePicker dataEmissaoPicker;
    private final ComboBox<String> marcaComboBox;
    private final Button adicionarFaturaButton;
    private final Button salvarButton;
    private final VBox faturasContainer;

    public NotaFiscalView() {
        root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #BDBDBD;");

        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setStyle(
            "-fx-background-color: #323437; " +
            "-fx-border-color: #C88200; " +
            "-fx-border-width: 2; " +
            "-fx-background-radius: 10; " +
            "-fx-border-radius: 10;"
        );

        Label titleLabel = new Label("CADASTRO DE NOTA FISCAL");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.web("#F0A818"));

        numeroNotaField = new TextField();
        numeroNotaField.setPromptText("Digite o número da nota");
        styleField(numeroNotaField);

        dataEmissaoPicker = new DatePicker();
        styleField(dataEmissaoPicker);

        marcaComboBox = new ComboBox<>();
        marcaComboBox.setPromptText("Selecione a marca");
        styleField(marcaComboBox);

        faturasContainer = new VBox(10);
        faturasContainer.setPadding(new Insets(10));

        adicionarFaturaButton = new Button("Adicionar Nova Fatura");
        styleButton(adicionarFaturaButton, "#F0A818");

        salvarButton = new Button("Salvar");
        styleButton(salvarButton, "#F0A818");

        HBox buttonBox = new HBox(10, adicionarFaturaButton, salvarButton);
        buttonBox.setAlignment(Pos.CENTER);

        panel.getChildren().addAll(
            titleLabel,
            createLabeledField("Número da Nota*:", numeroNotaField),
            createLabeledField("Data de Emissão*:", dataEmissaoPicker),
            createLabeledField("Marca*:", marcaComboBox),
            createLabeledField("Faturas:", faturasContainer),
            buttonBox
        );

        root.getChildren().add(panel);
    }

    private void styleField(Control field) {
        field.setStyle(
            "-fx-background-color: #2A2A2A; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-border-color: #4A4A4A; " +
            "-fx-border-width: 1; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5;"
        );
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(
                    "-fx-background-color: #2A2A2A; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-border-color: #F0A818; " +
                    "-fx-border-width: 1; " +
                    "-fx-background-radius: 5; " +
                    "-fx-border-radius: 5;"
                );
            } else {
                field.setStyle(
                    "-fx-background-color: #2A2A2A; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-border-color: #4A4A4A; " +
                    "-fx-border-width: 1; " +
                    "-fx-background-radius: 5; " +
                    "-fx-border-radius: 5;"
                );
            }
        });
    }

    private void styleButton(Button button, String backgroundColor) {
        button.setStyle(
            "-fx-background-color: " + backgroundColor + "; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5;"
        );
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #FFC107; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: " + backgroundColor + "; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5;"
        ));
    }

    private VBox createLabeledField(String labelText, Control field) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Arial", 14));
        label.setTextFill(Color.web("#BDBDBD"));
        return new VBox(5, label, field);
    }

    private VBox createLabeledField(String labelText, VBox field) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Arial", 14));
        label.setTextFill(Color.web("#BDBDBD"));
        return new VBox(5, label, field);
    }

    public VBox getRoot() {
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

    public Button getSalvarButton() {
        return salvarButton;
    }

    public VBox getFaturasContainer() {
        return faturasContainer;
    }
}