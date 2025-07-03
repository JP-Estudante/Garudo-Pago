package com.GuardouPagou.views;

import com.GuardouPagou.models.AlertaEmail;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Objects;
import java.util.regex.Pattern;

public class AlertaEmailView {
    // Constantes para facilitar ajustes de UI
    private static final double BUTTON_FONT_SIZE = 16;
    private static final double BUTTON_WIDTH = 150;

    // Padrão simples para validação de e-mail
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

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
        btnRemover = new Button("Remover");
        criarUI();
    }

    private void criarUI() {
        root.setStyle("-fx-background-color: #323437; -fx-padding: 20;");
        VBox header = createHeader();
        configureTable();
        HBox controls = createControls();

        root.setTop(header);
        root.setCenter(tabela);
        root.setBottom(controls);
    }

    private VBox createHeader() {
        Label title = new Label("Destinatários de Alertas");
        title.setFont(Font.font("Poppins", 20));
        title.setTextFill(Color.web("#F0A818"));
        VBox header = new VBox(5, title, new Separator());
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private void configureTable() {
        tabela.getStyleClass().add("table-padrao");
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.setPlaceholder(new Label("Nenhum e-mail cadastrado"));

        TableColumn<AlertaEmail, String> colEmail = new TableColumn<>("E-mail");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.prefWidthProperty().bind(tabela.widthProperty());
        tabela.getColumns().add(colEmail);
    }

    private HBox createControls() {
        tfEmail.setPromptText("email@exemplo.com");
        tfEmail.getStyleClass().add("pill-field");
        // estilo para placeholder e texto digitado
        tfEmail.setStyle("-fx-prompt-text-fill: #181848; -fx-text-inner-color: #181848;");
        tfEmail.setFont(Font.font("Poppins", BUTTON_FONT_SIZE));

        // vincula estado do botão 'Adicionar' à validade do e-mail
        BooleanBinding validEmail = Bindings.createBooleanBinding(
                () -> EMAIL_PATTERN.matcher(tfEmail.getText()).matches(),
                tfEmail.textProperty()
        );
        btnAdicionar.disableProperty().bind(validEmail.not());

        styleButtons();
        addIconsToButtons();

        // desabilita 'Remover' até selecionar linha
        btnRemover.disableProperty().bind(
                Bindings.isNull(tabela.getSelectionModel().selectedItemProperty())
        );

        HBox controls = new HBox(10);
        controls.setPadding(new Insets(10, 0, 0, 0));
        tfEmail.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(tfEmail, Priority.ALWAYS);

        VBox buttonBox = new VBox(10, btnAdicionar, btnRemover);
        buttonBox.setAlignment(Pos.CENTER);
        controls.getChildren().addAll(tfEmail, buttonBox);
        controls.setAlignment(Pos.CENTER_RIGHT);
        return controls;
    }

    private void styleButtons() {
        Font font = Font.font("Poppins", BUTTON_FONT_SIZE);
        btnAdicionar.setFont(font);
        btnRemover.setFont(font);
        btnAdicionar.setPrefWidth(BUTTON_WIDTH);
        btnRemover.setPrefWidth(BUTTON_WIDTH);
        btnAdicionar.getStyleClass().add("modal-button");
        btnRemover.getStyleClass().add("modal-button");
    }

    private void addIconsToButtons() {
        btnAdicionar.setGraphic(createIcon("/icons/user_add.png"));
        btnRemover.setGraphic(createIcon("/icons/user_remove.png"));
    }

    private ImageView createIcon(String resourcePath) {
        return new ImageView(new Image(
                Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm()
        ));
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