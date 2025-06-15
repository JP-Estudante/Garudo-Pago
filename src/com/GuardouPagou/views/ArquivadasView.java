package com.GuardouPagou.views; 

import com.GuardouPagou.dao.NotaFiscalArquivadaDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;

public class ArquivadasView {

    private BorderPane root;
    private TableView<NotaFiscalArquivadaDAO> tabelaNotasArquivadas;
    private TextField searchNumeroNotaField;
    private TextField searchMarcaField;
    private DatePicker searchDataArquivamentoPicker;
    private Button btnBuscar;
    private Button btnLimparBusca; // Opcional

    public ArquivadasView() {
        criarUI();
    }

    private void criarUI() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #BDBDBD; -fx-padding: 20;");

        VBox containerPrincipal = new VBox(20);
        containerPrincipal.setPadding(new Insets(20));
        containerPrincipal.setStyle("-fx-background-color: #323437; -fx-border-color: #C88200; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;");
        containerPrincipal.setAlignment(Pos.TOP_CENTER);

        Label titulo = new Label("NOTAS FISCAIS ARQUIVADAS");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titulo.setStyle("-fx-text-fill: #F0A818;");

        // --- Seção de Busca ---
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

        btnLimparBusca = new Button("Limpar"); // Opcional
        btnLimparBusca.setStyle("-fx-background-color: #C88200; -fx-text-fill: #000000;");


        painelBusca.getChildren().addAll(
                new Label("Filtrar por:"),
                searchNumeroNotaField,
                searchMarcaField,
                searchDataArquivamentoPicker,
                btnBuscar,
                btnLimparBusca
        );

        // --- Tabela de Notas Arquivadas ---
        tabelaNotasArquivadas = new TableView<>();
        tabelaNotasArquivadas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaNotasArquivadas.setStyle("-fx-border-color: #4A4A4A; -fx-border-width: 1; -fx-background-radius: 5; -fx-border-radius: 5;");
        tabelaNotasArquivadas.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        TableColumn<NotaFiscalArquivadaDAO, String> colNumeroNota = new TableColumn<>("Número Nota Fiscal");
        colNumeroNota.setCellValueFactory(new PropertyValueFactory<>("numeroNota"));

        TableColumn<NotaFiscalArquivadaDAO, Integer> colQtdFaturas = new TableColumn<>("Qtd. Faturas");
        colQtdFaturas.setCellValueFactory(new PropertyValueFactory<>("quantidadeFaturas"));

        TableColumn<NotaFiscalArquivadaDAO, String> colMarca = new TableColumn<>("Marca");
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));

        TableColumn<NotaFiscalArquivadaDAO, LocalDate> colDataArquivamento = new TableColumn<>("Data de Arquivamento");
        colDataArquivamento.setCellValueFactory(new PropertyValueFactory<>("dataArquivamento"));

        tabelaNotasArquivadas.getColumns().addAll(colNumeroNota, colQtdFaturas, colMarca, colDataArquivamento);
        // Estilo da tabela pode ser adicionado aqui

        containerPrincipal.getChildren().addAll(titulo, painelBusca, tabelaNotasArquivadas);
        root.setCenter(containerPrincipal);
    }

    // Getters para os componentes que o controller precisará acessar
    public BorderPane getRoot() { return root; }
    public TableView<NotaFiscalArquivadaDAO> getTabelaNotasArquivadas() { return tabelaNotasArquivadas; }
    public TextField getSearchNumeroNotaField() { return searchNumeroNotaField; }
    public TextField getSearchMarcaField() { return searchMarcaField; }
    public DatePicker getSearchDataArquivamentoPicker() { return searchDataArquivamentoPicker; }
    public Button getBtnBuscar() { return btnBuscar; }
    public Button getBtnLimparBusca() { return btnLimparBusca; }
}