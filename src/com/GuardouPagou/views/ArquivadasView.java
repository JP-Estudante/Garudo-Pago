package com.GuardouPagou.views;

import com.GuardouPagou.dao.NotaFiscalArquivadaDAO;
import com.GuardouPagou.dao.NotaFiscalDAO;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ArquivadasView {

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
        // Root pane
        root = new BorderPane();
        root.setStyle("-fx-background-color: #BDBDBD; -fx-padding: 20;");

        // Container principal
        VBox containerPrincipal = new VBox(20);
        containerPrincipal.setPadding(new Insets(20));
        containerPrincipal.setStyle(
                "-fx-background-color: #323437; " +
                        "-fx-border-color: #C88200; " +
                        "-fx-border-width: 2; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-radius: 10;"
        );
        containerPrincipal.setAlignment(Pos.TOP_CENTER);

        // Título
        Label titulo = new Label("NOTAS FISCAIS ARQUIVADAS");
        titulo.setFont(Font.font("Poppins", FontWeight.BOLD, 24));
        titulo.setStyle("-fx-text-fill: #F0A818;");

        // Seção de busca
        HBox painelBusca = new HBox(10);
        painelBusca.setAlignment(Pos.CENTER_LEFT);
        painelBusca.setPadding(new Insets(0, 0, 10, 0));

        searchNumeroNotaField = new TextField();
        searchNumeroNotaField.setPromptText("Buscar por Nº Nota");
        searchNumeroNotaField.setPrefWidth(150);

        searchMarcaField = new TextField();
        searchMarcaField.setPromptText("Buscar por Marca");
        searchMarcaField.setPrefWidth(150);

        searchDataArquivamentoPicker = new DatePicker();
        searchDataArquivamentoPicker.setPromptText("Buscar por Data Arq.");
        searchDataArquivamentoPicker.setPrefWidth(180);

        btnBuscar = new Button("Buscar");
        btnBuscar.setStyle("-fx-background-color: #f0a818; -fx-text-fill: #000000; -fx-font-weight: bold;");

        btnLimparBusca = new Button("Limpar");
        btnLimparBusca.setStyle("-fx-background-color: #C88200; -fx-text-fill: #000000;");

        painelBusca.getChildren().addAll(
                new Label("Filtrar por:"),
                searchNumeroNotaField,
                searchMarcaField,
                searchDataArquivamentoPicker,
                btnBuscar,
                btnLimparBusca
        );

        // Tabela de notas arquivadas
        tabelaNotasArquivadas = new TableView<>();
        tabelaNotasArquivadas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ViewUtils.aplicarEstiloPadrao(tabelaNotasArquivadas);
        // Coluna Número da Nota
        TableColumn<NotaFiscalArquivadaDAO, String> colNumeroNota =
                new TableColumn<>("Número Nota Fiscal");
        colNumeroNota.setCellValueFactory(new PropertyValueFactory<>("numeroNota"));
        colNumeroNota.setPrefWidth(180);

        // Coluna Qtd. Faturas
        TableColumn<NotaFiscalArquivadaDAO, Integer> colQtdFaturas =
                new TableColumn<>("Qtd. Faturas");
        colQtdFaturas.setCellValueFactory(new PropertyValueFactory<>("quantidadeFaturas"));
        colQtdFaturas.setPrefWidth(100);

        // Coluna Marca com cor dinâmica
        TableColumn<NotaFiscalArquivadaDAO, String> colMarca =
                new TableColumn<>("Marca");
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colMarca.setPrefWidth(150);
        colMarca.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String marca, boolean empty) {
                super.updateItem(marca, empty);
                if (empty || marca == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(marca);
                    String cor = getTableView()
                            .getItems()
                            .get(getIndex())
                            .getMarcaColor();
                    setTextFill(Color.web(cor));
                    setStyle(
                            "-fx-background-color: transparent; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-alignment: CENTER-LEFT;"
                    );
                }
            }
        });

        // Coluna Data de Arquivamento
        TableColumn<NotaFiscalArquivadaDAO, LocalDate> colDataArquivamento =
                new TableColumn<>("Data de Arquivamento");
        colDataArquivamento.setCellValueFactory(new PropertyValueFactory<>("dataArquivamento"));
        colDataArquivamento.setPrefWidth(140);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colDataArquivamento.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate data, boolean empty) {
                super.updateItem(data, empty);
                setText((empty || data == null) ? "" : data.format(fmt));
            }
        });

        // Adiciona colunas e popula a tabela
        tabelaNotasArquivadas.getColumns().setAll(
                colNumeroNota,
                colQtdFaturas,
                colMarca,
                colDataArquivamento
        );
        try {
            tabelaNotasArquivadas.setItems(
                    FXCollections.observableArrayList(
                            new NotaFiscalDAO().listarNotasFiscaisArquivadasComContagem((Map<String,Object>) null)
                    )
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Monta o layout
        containerPrincipal.getChildren().addAll(
                titulo,
                painelBusca,
                tabelaNotasArquivadas
        );
        root.setCenter(containerPrincipal);
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
}