package com.GuardouPagou.views;

import com.GuardouPagou.dao.NotaFiscalArquivadaDAO;
import com.GuardouPagou.dao.NotaFiscalDAO;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ArquivadasView {

    private static final Logger LOGGER = Logger.getLogger(ArquivadasView.class.getName());

    private static final double MARCA_FONT_SIZE = 22;

    private BorderPane root;
    private TableView<NotaFiscalArquivadaDAO> tabelaNotasArquivadas;
    private TextField searchNumeroNotaField;
    private TextField searchMarcaField;
    private DatePicker searchDataArquivamentoPicker;
    private Button btnBuscar;
    private Button btnLimparBusca;

    public ArquivadasView() {
        criarUI();
    }

    private void criarUI() {
        // Cria o root
        root = new BorderPane();
        root.setPadding(new Insets(20));

        // Título
        Label titulo = new Label("Listagem de Arquivadas");
        titulo.getStyleClass().add("h2");
        titulo.setTextFill(Color.web("#181848"));
        HBox titleBox = new HBox(titulo);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        // Tabela de notas arquivadas
        tabelaNotasArquivadas = new TableView<>();
        tabelaNotasArquivadas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        ViewUtils.aplicarEstiloPadrao(tabelaNotasArquivadas);

        // Configura colunas
        TableColumn<NotaFiscalArquivadaDAO, String> colNumeroNota = new TableColumn<>("Número Nota Fiscal");
        colNumeroNota.setCellValueFactory(new PropertyValueFactory<>("numeroNota"));
        colNumeroNota.setPrefWidth(180);

        TableColumn<NotaFiscalArquivadaDAO, Integer> colQtdFaturas = new TableColumn<>("Qtd. Faturas");
        colQtdFaturas.setCellValueFactory(new PropertyValueFactory<>("quantidadeFaturas"));
        colQtdFaturas.setPrefWidth(100);

        TableColumn<NotaFiscalArquivadaDAO, String> colMarca = criarColunaMarca();

        TableColumn<NotaFiscalArquivadaDAO, LocalDate> colDataArquivamento = new TableColumn<>("Data de Arquivamento");
        colDataArquivamento.setCellValueFactory(new PropertyValueFactory<>("dataArquivamento"));
        colDataArquivamento.setPrefWidth(140);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colDataArquivamento.setCellFactory(ignored -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate data, boolean empty) {
                super.updateItem(data, empty);
                setText((empty || data == null) ? "" : data.format(fmt));
            }
        });

        tabelaNotasArquivadas.getColumns().setAll(colNumeroNota, colQtdFaturas, colMarca, colDataArquivamento);
        try {
            var notas = new NotaFiscalDAO().listarNotasFiscaisArquivadasComContagem(null);
            tabelaNotasArquivadas.setItems(FXCollections.observableArrayList(notas));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar notas arquivadas", e);
        }

        // Permite que a tabela expanda horizontalmente
        tabelaNotasArquivadas.setMinWidth(Region.USE_COMPUTED_SIZE);
        tabelaNotasArquivadas.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(tabelaNotasArquivadas, Priority.ALWAYS);

        // Container da tabela
        VBox tabelaContainer = new VBox(10, titleBox, tabelaNotasArquivadas);
        tabelaContainer.setAlignment(Pos.TOP_LEFT);
        tabelaContainer.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(tabelaNotasArquivadas, Priority.ALWAYS);

        // Main container para posicionamento
        HBox mainContainer = new HBox(20, tabelaContainer);
        mainContainer.setAlignment(Pos.TOP_LEFT);
        mainContainer.setFillHeight(true);
        HBox.setHgrow(tabelaContainer, Priority.ALWAYS);

        root.setCenter(mainContainer);
    }

    // Getters para o controller
    public BorderPane getRoot() {
        return root;
    }

    public TableView<NotaFiscalArquivadaDAO> getTabelaNotasArquivadas() {
        return tabelaNotasArquivadas;
    }

    public TextField getSearchNumeroNotaField() {
        return searchNumeroNotaField;
    }

    public TextField getSearchMarcaField() {
        return searchMarcaField;
    }

    public DatePicker getSearchDataArquivamentoPicker() {
        return searchDataArquivamentoPicker;
    }

    public Button getBtnBuscar() {
        return btnBuscar;
    }

    public Button getBtnLimparBusca() {
        return btnLimparBusca;
    }

    private TableColumn<NotaFiscalArquivadaDAO, String> criarColunaMarca() {
        TableColumn<NotaFiscalArquivadaDAO, String> colMarca = new TableColumn<>("Marca");
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colMarca.setPrefWidth(150);
        colMarca.setCellFactory(ignored -> new TableCell<>() {
            @Override
            protected void updateItem(String marca, boolean empty) {
                super.updateItem(marca, empty);
                if (empty || marca == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    String cor = getTableView().getItems().get(getIndex()).getMarcaColor();

                    if (cor != null && cor.matches("#[0-9A-Fa-f]{6}")) {
                        Text txtNode = new Text(marca);
                        txtNode.setFill(Color.web(cor));
                        txtNode.setStroke(Color.BLACK);
                        txtNode.setStrokeWidth(0.7);
                        txtNode.setFont(Font.font(getFont().getFamily(), FontWeight.BOLD, MARCA_FONT_SIZE));
                        setGraphic(txtNode);
                        setText(null);
                        setStyle("-fx-alignment: CENTER_LEFT; -fx-background-color: transparent;");
                        setFont(Font.font(getFont().getFamily(), FontWeight.BOLD, MARCA_FONT_SIZE));
                    } else {
                        setGraphic(null);
                        setText(marca);
                        setTextFill(Color.BLACK);
                        setFont(Font.font(getFont().getFamily(), FontWeight.BOLD, MARCA_FONT_SIZE));
                        setStyle(
                                "-fx-font-weight: bold; " +
                                        "-fx-alignment: CENTER_LEFT; " +
                                        "-fx-background-color: transparent;"
                        );
                    }
                    }
            }
        });
        return colMarca;
    }
}
