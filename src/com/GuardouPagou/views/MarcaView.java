package com.GuardouPagou.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

public class MarcaView {

    private BorderPane root;
    private Label lblTitulo, lblSubtitle;
    private HBox pillFields;
    private TextField tfNome;
    private ColorPicker colorPicker;
    private TextArea taDescricao;
    private Label lblDescCounter;
    private Button btnLimpar, btnGravar;
    private Label lblId;

    public MarcaView() {
        criarUI();
    }

    private void criarUI() {
        // ——— RAIZ ———
        root = new BorderPane();
        root.setStyle("-fx-background-color: #323437; -fx-padding: 20;");

        // ——— HEADER ———
        lblTitulo = new Label("Cadastro de Marcas");
        lblTitulo.setFont(Font.font("Poppins", FontWeight.BOLD, 24));
        lblTitulo.setTextFill(Color.web("#F0A818"));

        lblSubtitle = new Label("Dados da nova marca");
        lblSubtitle.setFont(Font.font("Poppins", FontWeight.NORMAL, 14));
        lblSubtitle.setTextFill(Color.web("#7890A8"));

        Separator sep1 = new Separator();
        sep1.setStyle("-fx-background-color: #7890A8;");

        VBox headerBox = new VBox(5, lblTitulo, lblSubtitle, sep1);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(headerBox, Priority.ALWAYS);

        // ——— CAMPOS “PILL” ———
        pillFields = new HBox(15);
        pillFields.setAlignment(Pos.CENTER_LEFT);
        pillFields.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(pillFields, Priority.ALWAYS);

        // ID
        lblId = new Label();
        lblId.setTextFill(Color.web("#181848"));
        Label idLabel = new Label("# ID - Marca:");
        idLabel.getStyleClass().add("field-subtitle");
        VBox idBox = new VBox(3, idLabel, lblId);
        idBox.getStyleClass().add("pill-field");
        idBox.setPrefWidth(100);

        // Nome da Marca
        tfNome = new TextField();
        tfNome.setPromptText("Digite um nome");
        tfNome.setMaxWidth(Double.MAX_VALUE);
        Label nomeLabel = new Label("Nome da Marca:");
        nomeLabel.getStyleClass().add("field-subtitle");
        VBox nomeBox = new VBox(3, nomeLabel, tfNome);
        nomeBox.getStyleClass().add("pill-field");
        HBox.setHgrow(nomeBox, Priority.ALWAYS);

        // Cor
        colorPicker = new ColorPicker();
        colorPicker.setMaxWidth(Double.MAX_VALUE);
        Label corLabel = new Label("Cor:");
        corLabel.getStyleClass().add("field-subtitle");
        VBox corBox = new VBox(3, corLabel, colorPicker);
        corBox.getStyleClass().add("pill-field");
        colorPicker.getStyleClass().add("pill-color-picker");
        HBox.setHgrow(corBox, Priority.ALWAYS);

        pillFields.getChildren().addAll(idBox, nomeBox, corBox);

        // ——— DESCRIÇÃO ———
        Label descLabel = new Label("Descrição");
        descLabel.getStyleClass().add("field-subtitle");

        taDescricao = new TextArea();
        taDescricao.setPromptText("Descrição (opcional, até 500 caracteres)");
        taDescricao.setWrapText(true);
        taDescricao.getStyleClass().add("text-area-pill");
        taDescricao.setMaxWidth(Double.MAX_VALUE);
        taDescricao.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(taDescricao, Priority.ALWAYS);

        lblDescCounter = new Label("0/500");
        lblDescCounter.getStyleClass().add("desc-counter");
        HBox counterBox = new HBox(lblDescCounter);
        counterBox.setAlignment(Pos.CENTER_RIGHT);

        VBox descBox = new VBox(5, descLabel, taDescricao, counterBox);
        descBox.setStyle(
                "-fx-background-color: #BDBDBD; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 10;"
        );
        descBox.setMaxWidth(Double.MAX_VALUE);
        descBox.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(descBox, Priority.ALWAYS);

        // ——— BOTÕES ———
        btnLimpar = new Button("Limpar");
        btnLimpar.getStyleClass().addAll("modal-button", "icon-clean");
        btnLimpar.setFont(Font.font("Poppins", FontWeight.BOLD, 16));
        btnLimpar.setPrefSize(120, 40);

        btnGravar = new Button("Gravar");
        btnGravar.getStyleClass().addAll("modal-button", "icon-save");
        btnGravar.setFont(Font.font("Poppins", FontWeight.BOLD, 16));
        btnGravar.setPrefSize(120, 40);

        HBox buttonBox = new HBox(10, btnLimpar, btnGravar);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        // ——— CONTAINER PRINCIPAL ———
        VBox container = new VBox(15,
                headerBox,
                pillFields,
                descBox,
                buttonBox
        );
        container.setAlignment(Pos.TOP_LEFT);
        container.setMaxWidth(Double.MAX_VALUE);
        container.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(container, Priority.ALWAYS);

        // ——— MAIN CONTAINER PARA PREENCHER TELA ———
        HBox mainContainer = new HBox(container);
        mainContainer.setAlignment(Pos.TOP_LEFT);
        mainContainer.setFillHeight(true);
        HBox.setHgrow(container, Priority.ALWAYS);

        root.setCenter(mainContainer);
    }

    // === GETTERS ===
    public BorderPane getRoot() { return root; }
    public TextField getNomeField() { return tfNome; }
    public TextArea getDescricaoArea() { return taDescricao; }
    public ColorPicker getCorPicker() { return colorPicker; }
    public Button getLimparButton() { return btnLimpar; }
    public Button getSalvarButton() { return btnGravar; }
    public void setNextId(int id) { lblId.setText(String.format("%04d", id)); }
    public Label getDescCounterLabel() { return lblDescCounter; }
}
