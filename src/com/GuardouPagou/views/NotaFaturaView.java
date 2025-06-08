package com.GuardouPagou.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class NotaFaturaView {

    private final BorderPane root;

    // Campos principais
    private final TextField numeroNotaField;
    private final DatePicker dataEmissaoPicker;
    private final ComboBox<String> marcaComboBox;

    // Spinner de nº de faturas
    private final Spinner<Integer> spinnerFaturas;
    // Container para as linhas de vencimento/valor
    private final VBox vencimentosColumn;
    private final VBox valoresColumn;

    // Botões
    private final Button btnLimpar;
    private final Button btnGravar;

    public NotaFaturaView() {
        numeroNotaField = new TextField();
        dataEmissaoPicker = new DatePicker();
        marcaComboBox = new ComboBox<>();
        spinnerFaturas = new Spinner<>(1, 100, 1);
        vencimentosColumn = new VBox(10);
        valoresColumn = new VBox(10);
        btnLimpar = new Button("Limpar");
        btnGravar = new Button("Gravar");
        root = new BorderPane();
        criarUI();
    }

    private void criarUI() {
        // Carrega o CSS de estilos
        root.getStylesheets().add(getClass().getResource("button-style.css").toExternalForm());

        // Estilo base do modal
        root.setStyle("-fx-background-color: #323437; -fx-padding: 20;");

        // ——— HEADER ———
        Label titulo = new Label("Cadastro de Faturas");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titulo.setTextFill(Color.web("#F0A818"));

        Label sub1 = new Label("Dados da Nota Fiscal Eletrônica");
        sub1.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        sub1.setTextFill(Color.web("#7890A8"));

        Separator sep1 = new Separator();
        sep1.setStyle("-fx-background-color: #7890A8;");

        VBox headerBox = new VBox(5, titulo, sub1, sep1);

        // ——— DADOS DA NOTA ———
        HBox dadosNota = new HBox(15);
        dadosNota.setPadding(new Insets(10, 0, 10, 0));
        dadosNota.setAlignment(Pos.CENTER_LEFT);
        dadosNota.getChildren().addAll(
                wrapField(numeroNotaField, "# Número da NF-e", "Digite o nº da NF-e"),
                wrapDatePicker(dataEmissaoPicker, "Data de Emissão", "DD/MM/AAAA"),
                wrapComboBox(marcaComboBox, "Marca", "Selecione")
        );

        // ——— SUBTÍTULO FATURAS ———
        Label sub2 = new Label("Dados da Fatura");
        sub2.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        sub2.setTextFill(Color.web("#7890A8"));

        Separator sep2 = new Separator();
        sep2.setStyle("-fx-background-color: #7890A8;");

        // ——— SEÇÃO FATURAS ———
        HBox faturasSection = new HBox(20);
        faturasSection.setAlignment(Pos.TOP_LEFT);

        // --- painel esquerdo: spinner + botões ---
        VBox leftPanel = new VBox(15);
        leftPanel.setAlignment(Pos.TOP_CENTER);

        Label lblSpinner = new Label("# Nº de Faturas");
        lblSpinner.setTextFill(Color.web("#BDBDBD"));
        spinnerFaturas.setPrefWidth(60);
        spinnerFaturas.setStyle("-fx-background-color: #BDBDBD; -fx-font-size: 14px;");

        // Aplica nossa classe CSS aos botões
        btnLimpar.getStyleClass().add("modal-button");
        btnGravar.getStyleClass().add("modal-button");
        btnLimpar.setPrefWidth(100);
        btnGravar.setPrefWidth(100);

        leftPanel.getChildren().addAll(lblSpinner, spinnerFaturas, btnLimpar, btnGravar);

        // --- painel direito: colunas scrolláveis ---
        vencimentosColumn.setPadding(new Insets(10));
        vencimentosColumn.setStyle("-fx-background-color: #7890A8; -fx-background-radius: 5;");
        valoresColumn.setPadding(new Insets(10));
        valoresColumn.setStyle("-fx-background-color: #7890A8; -fx-background-radius: 5;");

        ScrollPane spVenc = new ScrollPane(vencimentosColumn);
        spVenc.setFitToWidth(true);
        spVenc.setPrefSize(200, 200);

        ScrollPane spVal = new ScrollPane(valoresColumn);
        spVal.setFitToWidth(true);
        spVal.setPrefSize(200, 200);

        faturasSection.getChildren().addAll(leftPanel, spVenc, spVal);

        // ——— AGRUPA TUDO ———
        VBox container = new VBox(10, headerBox, dadosNota, sub2, sep2, faturasSection);
        root.setCenter(container);
    }

    private VBox wrapField(TextField field, String labelText, String prompt) {
        Label lbl = new Label(labelText);
        lbl.setTextFill(Color.web("#323437"));
        field.setPromptText(prompt);
        field.setPrefWidth(150);
        field.setStyle(
                "-fx-background-color: transparent; "
                + "-fx-text-fill: #323437; "
                + "-fx-prompt-text-fill: #323437; "
                + "-fx-border-width: 0;"
        );
        VBox box = new VBox(5, lbl, field);
        box.setStyle(
                "-fx-background-color: #BDBDBD; "
                + "-fx-background-radius: 10; "
                + "-fx-padding: 10;"
        );
        return box;
    }

    private VBox wrapDatePicker(DatePicker dp, String labelText, String prompt) {
        Label lbl = new Label(labelText);
        lbl.setTextFill(Color.web("#323437"));
        dp.setPromptText(prompt);
        dp.setPrefWidth(150);
        dp.setStyle(
                "-fx-background-color: transparent; "
                + "-fx-text-fill: #323437; "
                + "-fx-border-width: 0;"
        );
        VBox box = new VBox(5, lbl, dp);
        box.setStyle(
                "-fx-background-color: #BDBDBD; "
                + "-fx-background-radius: 10; "
                + "-fx-padding: 10;"
        );
        return box;
    }

    private VBox wrapComboBox(ComboBox<String> cb, String labelText, String prompt) {
        Label lbl = new Label(labelText);
        lbl.setTextFill(Color.web("#323437"));
        cb.setPromptText(prompt);
        cb.setPrefWidth(150);
        cb.setStyle(
                "-fx-background-color: transparent; "
                + "-fx-text-fill: #323437; "
                + "-fx-border-width: 0;"
        );
        VBox box = new VBox(5, lbl, cb);
        box.setStyle(
                "-fx-background-color: #BDBDBD; "
                + "-fx-background-radius: 10; "
                + "-fx-padding: 10;"
        );
        return box;
    }

    // === GETTERS para o controller ===
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

    public Spinner<Integer> getSpinnerFaturas() {
        return spinnerFaturas;
    }

    public VBox getVencimentosColumn() {
        return vencimentosColumn;
    }

    public VBox getValoresColumn() {
        return valoresColumn;
    }

    public Button getBtnLimpar() {
        return btnLimpar;
    }

    public Button getBtnGravar() {
        return btnGravar;
    }
}
