package com.GuardouPagou.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Objects;

public class NotaFiscalDetalhesView {

    private final BorderPane root;
    private final TextField numeroNotaField;
    private final DatePicker dataEmissaoPicker;
    private final ComboBox<String> marcaComboBox;
    private final VBox vencimentosColumn;
    private final VBox valoresColumn;
    private final VBox statusColumn;
    private final Button btnVoltar;
    private final Button btnEditar;

    public NotaFiscalDetalhesView() {
        numeroNotaField = new TextField();
        dataEmissaoPicker = new DatePicker();
        marcaComboBox = new ComboBox<>();
        vencimentosColumn = new VBox(10);
        valoresColumn = new VBox(10);
        statusColumn = new VBox(10);
        btnVoltar = new Button("Voltar");
        btnEditar = new Button("Editar");
        root = new BorderPane();
        criarUI();
    }

    private void criarUI() {
        root.getStyleClass().addAll("nota-fatura-root", "detalhes-nota-root");
        root.setStyle("-fx-background-color: #323437; -fx-padding: 20;");

        Label titulo = new Label("Detalhes da Nota Fiscal");
        titulo.setFont(Font.font("Poppins", FontWeight.BOLD, 24));
        titulo.setTextFill(Color.web("#F0A818"));

        Label sub1 = new Label("Dados da Nota Fiscal Eletrônica");
        sub1.setFont(Font.font("Poppins", FontWeight.NORMAL, 16));
        sub1.setTextFill(Color.web("#7890A8"));

        Separator sep1 = new Separator();
        sep1.setStyle("-fx-background-color: #7890A8;");

        VBox headerBox = new VBox(8, titulo, sub1, sep1);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        // Dados da nota
        Label lblNumero = new Label("# Número da NF-e");
        lblNumero.getStyleClass().add("field-subtitle");
        numeroNotaField.setEditable(false);
        numeroNotaField.setPrefWidth(250);
        numeroNotaField.getStyleClass().add("read-only-field");
        VBox numeroBox = new VBox(6, lblNumero, numeroNotaField);
        numeroBox.getStyleClass().add("pill-field");

        Label lblData = new Label("Data de Emissão");
        lblData.getStyleClass().add("field-subtitle");
        dataEmissaoPicker.setDisable(true);
        dataEmissaoPicker.setPrefWidth(250);
        VBox dataBox = new VBox(6, lblData, dataEmissaoPicker);
        dataBox.getStyleClass().add("pill-field");

        Label lblMarca = new Label("Marca");
        lblMarca.getStyleClass().add("field-subtitle");
        marcaComboBox.setDisable(true);
        marcaComboBox.setPrefWidth(250);
        VBox marcaBox = new VBox(6, lblMarca, marcaComboBox);
        marcaBox.getStyleClass().add("pill-field");

        HBox dadosNota = new HBox(20, numeroBox, dataBox, marcaBox);
        dadosNota.setPadding(new Insets(15, 0, 15, 0));
        dadosNota.setAlignment(Pos.CENTER_LEFT);

        Label sub2 = new Label("Dados de cada Fatura");
        sub2.setFont(Font.font("Poppins", FontWeight.NORMAL, 16));
        sub2.setTextFill(Color.web("#7890A8"));

        Separator sep2 = new Separator();
        sep2.setStyle("-fx-background-color: #7890A8;");

        VBox faturasHeaderBox = new VBox(8, sub2, sep2);
        faturasHeaderBox.setAlignment(Pos.CENTER_LEFT);

        // Colunas faturas
        vencimentosColumn.setPadding(new Insets(10));
        vencimentosColumn.setStyle("-fx-background-color: #7890A8; -fx-background-radius: 5;");
        valoresColumn.setPadding(new Insets(10));
        valoresColumn.setStyle("-fx-background-color: #7890A8; -fx-background-radius: 5;");
        statusColumn.setPadding(new Insets(10));
        statusColumn.setStyle("-fx-background-color: #7890A8; -fx-background-radius: 5;");

        ScrollPane spVenc = new ScrollPane(vencimentosColumn);
        spVenc.setFitToWidth(true);
        spVenc.setPrefSize(200, 250);

        ScrollPane spVal = new ScrollPane(valoresColumn);
        spVal.setFitToWidth(true);
        spVal.setPrefSize(200, 250);

        ScrollPane spStatus = new ScrollPane(statusColumn);
        spStatus.setFitToWidth(true);
        spStatus.setPrefSize(200, 250);

        spVal.vvalueProperty().bindBidirectional(spVenc.vvalueProperty());
        spStatus.vvalueProperty().bindBidirectional(spVenc.vvalueProperty());

        HBox faturasSection = new HBox(25, spVenc, spVal, spStatus);
        faturasSection.setAlignment(Pos.TOP_LEFT);

        // Botões: ícone e fonte maior
        btnVoltar.getStyleClass().add("modal-button");
        ImageView backIcon = new ImageView(Objects.requireNonNull(getClass().getResource("/icons/back.png")).toExternalForm());
        backIcon.setFitHeight(18);
        backIcon.setFitWidth(18);
        btnVoltar.setGraphic(backIcon);
        btnVoltar.setFocusTraversable(false);
        btnVoltar.setFont(Font.font("Poppins", FontWeight.NORMAL, 16));

        btnEditar.getStyleClass().add("modal-button");
        ImageView editIcon = new ImageView(Objects.requireNonNull(getClass().getResource("/icons/edit.png")).toExternalForm());
        editIcon.setFitHeight(20);
        editIcon.setFitWidth(20);
        btnEditar.setGraphic(editIcon);
        btnEditar.setFocusTraversable(false);
        btnEditar.setFont(Font.font("Poppins", FontWeight.NORMAL, 16));

        HBox botoes = new HBox(10, btnVoltar, btnEditar);
        botoes.setAlignment(Pos.CENTER_RIGHT);
        botoes.setPadding(new Insets(15, 0, 0, 0));

        VBox container = new VBox(12, headerBox, dadosNota, faturasHeaderBox, faturasSection, botoes);
        container.setAlignment(Pos.TOP_LEFT);
        root.setCenter(container);
    }

    public BorderPane getRoot() { return root; }
    public TextField getNumeroNotaField() { return numeroNotaField; }
    public DatePicker getDataEmissaoPicker() { return dataEmissaoPicker; }
    public ComboBox<String> getMarcaComboBox() { return marcaComboBox; }
    public VBox getVencimentosColumn() { return vencimentosColumn; }
    public VBox getValoresColumn() { return valoresColumn; }
    public VBox getStatusColumn() { return statusColumn; }
    public Button getBtnVoltar() { return btnVoltar; }
    public Button getBtnEditar() { return btnEditar; }
}