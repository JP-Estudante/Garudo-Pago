package com.GuardouPagou.views;

import com.GuardouPagou.dao.FaturaDAO;
import com.GuardouPagou.dao.MarcaDAO;
import com.GuardouPagou.dao.NotaFiscalDAO;
import com.GuardouPagou.models.Fatura;
import com.GuardouPagou.models.Marca;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FaturaView {
    private static final Logger LOGGER = Logger.getLogger(FaturaView.class.getName());
    private static final double MARCA_FONT_SIZE = 22;

    private VBox root;
    private MenuButton btnFiltrar;
    private RadioMenuItem miFiltrarPeriodo;
    private RadioMenuItem miFiltrarMarca;
    private DatePicker dpDataInicio, dpDataFim;
    private ComboBox<Marca> cbFiltroMarca;
    private Popup filtroPopup;
    private LocalDate periodoFilterStart;
    private LocalDate periodoFilterEnd;
    private final Set<Marca> marcaFilters = new HashSet<>();
    private HBox filterTokens;
    private TableView<Fatura> tabelaFaturas;
    private Button btnDetalhes;
    private Consumer<Fatura> notaDoubleClickHandler;
    private Runnable arquivadasNavigateAction;

    public FaturaView(ObservableList<Fatura> faturas) {
        criarUI(faturas);
    }

    public VBox getRoot() { return root; }
    public TableView<Fatura> getTabelaFaturas() { return tabelaFaturas; }
    public Button getBtnDetalhes() { return btnDetalhes; }

    public void setNotaDoubleClickHandler(Consumer<Fatura> handler) {
        this.notaDoubleClickHandler = handler;
    }

    public void setArquivadasNavigateAction(Runnable action) {
        this.arquivadasNavigateAction = action;
    }

    public void recarregarListaFaturas() {
        atualizarListaFaturas();
    }

    private void criarUI(ObservableList<Fatura> faturas) {
        root = new VBox(18);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #BDBDBD;");

        Label titulo = new Label("Listagem de Faturas");
        titulo.getStyleClass().add("h2");
        titulo.setTextFill(Color.web("#181848"));

        filterTokens = new HBox(8);
        filterTokens.setAlignment(Pos.CENTER_LEFT);
        filterTokens.setPadding(new Insets(0, 0, 0, 10));

        ToggleGroup filtroToggleGroup = new ToggleGroup();
        miFiltrarPeriodo = new RadioMenuItem("Filtrar por Período");
        miFiltrarMarca = new RadioMenuItem("Filtrar por Marca");
        miFiltrarPeriodo.setToggleGroup(filtroToggleGroup);
        miFiltrarMarca.setToggleGroup(filtroToggleGroup);

        ImageView filterIcon = new ImageView(
                new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/icons/filter_list.png"))
                )
        );
        filterIcon.setFitHeight(22);
        filterIcon.setPreserveRatio(true);
        btnFiltrar = new MenuButton("Filtrar", filterIcon, miFiltrarPeriodo, miFiltrarMarca);
        btnFiltrar.getStyleClass().addAll("menu-button", "botao-listagem", "btn-filtrar");
        btnFiltrar.setContentDisplay(ContentDisplay.LEFT);
        btnFiltrar.setGraphicTextGap(10);

        filtroPopup = new Popup();
        filtroPopup.setAutoHide(true);
        filtroPopup.setHideOnEscape(true);
        filtroPopup.setOnShowing(evt -> btnFiltrar.getStyleClass().add("filter-open"));
        filtroPopup.setOnHiding(evt -> btnFiltrar.getStyleClass().remove("filter-open"));

        dpDataInicio = new DatePicker();
        dpDataInicio.setPromptText("Início do Período");
        dpDataInicio.setPrefWidth(150);
        Label lblDataInicio = new Label("Data Inicial");
        lblDataInicio.getStyleClass().add("field-subtitle");
        VBox dataInicioBox = new VBox(6, lblDataInicio, dpDataInicio);
        dataInicioBox.getStyleClass().add("pill-field");

        dpDataFim = new DatePicker();
        dpDataFim.setPromptText("Fim do Período");
        dpDataFim.setPrefWidth(150);
        dpDataFim.setOnAction(e -> { aplicarFiltroPeriodo(); filtroPopup.hide(); });
        Label lblDataFim = new Label("Data Final");
        lblDataFim.getStyleClass().add("field-subtitle");
        VBox dataFimBox = new VBox(6, lblDataFim, dpDataFim);
        dataFimBox.getStyleClass().add("pill-field");

        Button btnAplicar = new Button();
        btnAplicar.getStyleClass().addAll("modal-button", "btn-aplicar");
        ImageView checkIcon = new ImageView(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/icons/check_colored.png"))
        ));
        checkIcon.setPreserveRatio(true);
        btnAplicar.setGraphic(checkIcon);
        btnAplicar.setOnAction(e -> { aplicarFiltroPeriodo(); filtroPopup.hide(); });

        Button btnCancelar = new Button();
        btnCancelar.getStyleClass().addAll("modal-button", "btn-cancelar");
        ImageView cancelIcon = new ImageView(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/icons/cancel_colored.png"))
        ));
        cancelIcon.setPreserveRatio(true);
        btnCancelar.setGraphic(cancelIcon);
        btnCancelar.setOnAction(e -> filtroPopup.hide());

        HBox btnBox = new HBox(10, btnAplicar, btnCancelar);
        btnBox.setAlignment(Pos.CENTER_LEFT);

        VBox periodContent = new VBox(10, dataInicioBox, dataFimBox, btnBox);
        periodContent.getStyleClass().addAll("painel-filtros","painel-filtros-canto-quadrado");
        periodContent.setPadding(new Insets(15));

        Label lblFiltrarMarca = new Label("Filtrar por Marca:");
        lblFiltrarMarca.getStyleClass().add("field-subtitle");
        cbFiltroMarca = new ComboBox<>();
        cbFiltroMarca.setPromptText("Selecione uma marca");
        cbFiltroMarca.setPrefWidth(200);
        try { cbFiltroMarca.setItems(new MarcaDAO().listarMarcas()); }
        catch (SQLException ex) { LOGGER.log(Level.SEVERE, "Erro ao carregar marcas", ex); }
        cbFiltroMarca.setOnAction(e -> { aplicarFiltroMarca(); filtroPopup.hide(); });
        VBox marcaBox = new VBox(6, lblFiltrarMarca, cbFiltroMarca);
        marcaBox.getStyleClass().add("pill-field");

        VBox marcaContent = new VBox(marcaBox);
        marcaContent.getStyleClass().addAll("painel-filtros","painel-filtros-canto-quadrado");
        marcaContent.setPadding(new Insets(15));

        miFiltrarPeriodo.setOnAction(e -> {
            filtroPopup.getContent().setAll(periodContent);
            Bounds b = btnFiltrar.localToScreen(btnFiltrar.getBoundsInLocal());
            filtroPopup.show(btnFiltrar.getScene().getWindow(), b.getMinX(), b.getMaxY());
        });
        miFiltrarMarca.setOnAction(e -> {
            filtroPopup.getContent().setAll(marcaContent);
            Bounds b = btnFiltrar.localToScreen(btnFiltrar.getBoundsInLocal());
            filtroPopup.show(btnFiltrar.getScene().getWindow(), b.getMinX(), b.getMaxY());
        });

        btnDetalhes = new Button("Detalhes");
        btnDetalhes.getStyleClass().addAll("menu-button","botao-listagem");
        btnDetalhes.setDisable(true);

        Region espacador = new Region();
        espacador.setMinWidth(0);
        HBox.setHgrow(espacador, Priority.ALWAYS);

        HBox toolbar = new HBox(12, filterTokens, btnFiltrar, espacador, btnDetalhes);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox header = new HBox(20, titulo, headerSpacer, toolbar);
        header.setAlignment(Pos.CENTER_LEFT);

        this.tabelaFaturas = criarTabelaFaturas(faturas);
        this.tabelaFaturas.setRowFactory(tv -> {
            TableRow<Fatura> row = new TableRow<>();
            row.setOnMouseClicked(ev -> {
                if (ev.getClickCount() == 2 && !row.isEmpty() && notaDoubleClickHandler != null) {
                    notaDoubleClickHandler.accept(row.getItem());
                }
            });
            return row;
        });
        VBox.setVgrow(this.tabelaFaturas, Priority.ALWAYS);

        root.getChildren().addAll(header, this.tabelaFaturas);
    }

    private TableView<Fatura> criarTabelaFaturas(ObservableList<Fatura> faturas) {
        TableView<Fatura> tabela = new TableView<>();
        ViewUtils.aplicarEstiloPadrao(tabela);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Fatura, String> colunaNumeroNota = criarColunaNumeroNotaFatura();
        TableColumn<Fatura, Integer> colunaOrdem = criarColunaOrdemFatura();
        TableColumn<Fatura, LocalDate> colunaVencimento = criarColunaVencimentoFatura();
        TableColumn<Fatura, String> colunaMarca = criarColunaMarcaFatura();
        TableColumn<Fatura, String> colunaStatus = criarColunaStatusFatura();

        tabela.getColumns().setAll(List.of(colunaNumeroNota, colunaOrdem, colunaVencimento, colunaMarca, colunaStatus));
        tabela.setItems(faturas);
        return tabela;
    }

    private TableColumn<Fatura, String> criarColunaNumeroNotaFatura() {
        TableColumn<Fatura, String> coluna = new TableColumn<>("NÚMERO DA NOTA");
        coluna.setCellValueFactory(new PropertyValueFactory<>("numeroNota"));
        coluna.setPrefWidth(150);
        coluna.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String numeroNota, boolean empty) {
                super.updateItem(numeroNota, empty);
                if (empty || numeroNota == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(numeroNota);
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-alignment: CENTER-LEFT;");
                }
            }
        });
        return coluna;
    }

    private TableColumn<Fatura, Integer> criarColunaOrdemFatura() {
        TableColumn<Fatura, Integer> coluna = new TableColumn<>("ORDEM DA FATURA");
        coluna.setCellValueFactory(new PropertyValueFactory<>("numeroFatura"));
        coluna.setPrefWidth(120);
        coluna.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer ordem, boolean empty) {
                super.updateItem(ordem, empty);
                if (empty || ordem == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(ordem.toString());
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-alignment: CENTER-LEFT;");
                }
            }
        });
        return coluna;
    }

    private TableColumn<Fatura, LocalDate> criarColunaVencimentoFatura() {
        TableColumn<Fatura, LocalDate> coluna = new TableColumn<>("VENCIMENTO");
        coluna.setCellValueFactory(new PropertyValueFactory<>("vencimento"));
        coluna.setPrefWidth(120);
        coluna.setCellFactory(col -> new TableCell<>() {
            private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            @Override
            protected void updateItem(LocalDate venc, boolean empty) {
                super.updateItem(venc, empty);
                setText((empty || venc == null) ? null : venc.format(fmt));
                setTextFill(Color.WHITE);
                setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-alignment: CENTER-LEFT;");
            }
        });
        return coluna;
    }

    private TableColumn<Fatura, String> criarColunaMarcaFatura() {
        TableColumn<Fatura, String> coluna = new TableColumn<>("MARCA");
        coluna.setCellValueFactory(new PropertyValueFactory<>("marca"));
        coluna.setPrefWidth(150);
        coluna.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String marca, boolean empty) {
                super.updateItem(marca, empty);
                if (empty || marca == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                    return;
                }
                String cor = getTableView().getItems().get(getIndex()).getMarcaColor();
                if (cor != null && cor.matches("#[0-9A-Fa-f]{6}")) {
                    Text txtNode = new Text(marca);
                    txtNode.setFill(Color.web(cor));
                    txtNode.setStroke(Color.BLACK);
                    txtNode.setStrokeWidth(1);
                    txtNode.setFont(Font.font(getFont().getFamily(), FontWeight.BOLD, MARCA_FONT_SIZE));
                    setGraphic(txtNode);
                    setText(null);
                    setStyle("-fx-alignment: CENTER_LEFT; -fx-background-color: transparent;");
                } else {
                    setGraphic(null);
                    setText(marca);
                    setTextFill(Color.BLACK);
                    setFont(Font.font(getFont().getFamily(), FontWeight.BOLD, MARCA_FONT_SIZE));
                    setStyle("-fx-font-weight: bold; -fx-alignment: CENTER_LEFT; -fx-background-color: transparent;");
                }
            }
        });
        return coluna;
    }

    private TableColumn<Fatura, String> criarColunaStatusFatura() {
        TableColumn<Fatura, String> coluna = new TableColumn<>("STATUS");
        coluna.setCellValueFactory(new PropertyValueFactory<>("status"));
        coluna.setMinWidth(180);
        coluna.setPrefWidth(180);
        coluna.setMaxWidth(180);
        coluna.setCellFactory(col -> new TableCell<>() {
            private final MenuItem miNaoEmitida = new MenuItem("Não Emitida");
            private final MenuItem miEmitida   = new MenuItem("Emitida");
            private final MenuButton menu      = new MenuButton();
            {
                menu.getItems().addAll(miNaoEmitida, miEmitida);
                menu.getStyleClass().add("fatura-status-menu");
                setAlignment(Pos.CENTER_LEFT);
                ContextMenu ctx = menu.getContextMenu();
                if (ctx != null) {
                    ctx.getStyleClass().add("fatura-status-popup");
                    ctx.prefWidthProperty().bind(menu.widthProperty());
                }
                menu.skinProperty().addListener((obs, oldSkin, newSkin) -> {
                    Node arrow = menu.lookup(".arrow-button");
                    if (arrow != null) {
                        arrow.setVisible(false);
                        arrow.setManaged(false);
                    }
                });
                menu.setOnMouseClicked(e -> {
                    if (!menu.isDisabled()) menu.show();
                });
                miEmitida.setOnAction(e -> {
                    Fatura f = getTableView().getItems().get(getIndex());
                    marcarFaturaComoEmitida(f);
                });
            }
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                } else {
                    menu.setText(status);
                    menu.setDisable("Emitida".equalsIgnoreCase(status) || "Vencida".equalsIgnoreCase(status));
                    setGraphic(menu);
                }
            }
        });
        return coluna;
    }

    private void atualizarListaFaturas() {
        atualizarListaComFiltros();
    }

    private void mostrarListaFaturas(ObservableList<Fatura> faturas) {
        if (this.tabelaFaturas != null) {
            this.tabelaFaturas.setItems(faturas);
        }
    }

    private void marcarFaturaComoEmitida(Fatura fatura) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Marcar Fatura como Emitida");
        alert.setContentText("Tem certeza que deseja marcar a fatura Nº " + fatura.getNumeroFatura() + " da Nota " + fatura.getNumeroNota() + " como emitida?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean faturaMarcada = new FaturaDAO().marcarFaturaIndividualComoEmitida(fatura.getId());
                if (faturaMarcada) {
                    boolean todasEmitidas = new FaturaDAO().todasFaturasDaNotaEmitidas(fatura.getNotaFiscalId());
                    if (todasEmitidas) {
                        boolean notaFiscalArquivada = new NotaFiscalDAO().marcarComoArquivada(
                                fatura.getNotaFiscalId(),
                                LocalDate.now().plusDays(3)
                        );
                        if (notaFiscalArquivada) {
                            atualizarListaFaturas();
                            if (arquivadasNavigateAction != null) arquivadasNavigateAction.run();
                        } else {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Erro");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Erro ao arquivar a Nota Fiscal após todas as faturas serem emitidas.");
                            errorAlert.showAndWait();
                        }
                    } else {
                        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                        infoAlert.setTitle("Fatura Emitida");
                        infoAlert.setHeaderText(null);
                        infoAlert.setContentText("Fatura Nº " + fatura.getNumeroFatura() + " marcada como emitida. Aguardando outras faturas da Nota " + fatura.getNumeroNota() + " para arquivamento.");
                        infoAlert.showAndWait();
                        atualizarListaFaturas();
                    }
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erro");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Não foi possível marcar a fatura como emitida.");
                    errorAlert.showAndWait();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erro ao processar emissão da fatura.", e);
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erro");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Erro ao processar emissão da fatura: " + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Erro ao filtrar faturas");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void aplicarFiltroPeriodo() {
        LocalDate inicio = dpDataInicio.getValue();
        LocalDate fim    = dpDataFim.getValue();
        if (inicio != null && fim != null) {
            periodoFilterStart = inicio;
            periodoFilterEnd   = fim;
            atualizarListaComFiltros();
            filterTokens.getChildren().removeIf(n -> "periodo".equals(n.getUserData()));
            String txt = inicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + " – "
                    + fim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Button periodoBtn = new Button(txt);
            periodoBtn.setUserData("periodo");
            periodoBtn.getStyleClass().add("filter-token");
            ImageView cancelIcon = new ImageView(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/cancel.png")))
            );
            cancelIcon.setFitHeight(20);
            cancelIcon.setPreserveRatio(true);
            periodoBtn.setGraphic(cancelIcon);
            periodoBtn.setContentDisplay(ContentDisplay.RIGHT);
            periodoBtn.setOnAction(ev -> {
                periodoFilterStart = periodoFilterEnd = null;
                filterTokens.getChildren().remove(periodoBtn);
                atualizarListaComFiltros();
            });
            filterTokens.getChildren().add(periodoBtn);
        }
        filtroPopup.hide();
    }

    private void aplicarFiltroMarca() {
        Marca sel = cbFiltroMarca.getValue();
        if (sel != null && marcaFilters.add(sel)) {
            atualizarListaComFiltros();
            Button marcaBtn = new Button(sel.getNome());
            marcaBtn.setUserData(sel);
            marcaBtn.getStyleClass().add("filter-token");
            ImageView cancelIcon = new ImageView(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/cancel.png")))
            );
            cancelIcon.setFitHeight(20);
            cancelIcon.setPreserveRatio(true);
            marcaBtn.setGraphic(cancelIcon);
            marcaBtn.setContentDisplay(ContentDisplay.RIGHT);
            marcaBtn.setOnAction(ev -> {
                marcaFilters.remove(sel);
                filterTokens.getChildren().remove(marcaBtn);
                atualizarListaComFiltros();
            });
            filterTokens.getChildren().add(marcaBtn);
        }
        filtroPopup.hide();
    }

    private void atualizarListaComFiltros() {
        try {
            ObservableList<Fatura> resultado;
            boolean filtraPeriodo = periodoFilterStart != null && periodoFilterEnd != null;
            boolean filtraMarca   = !marcaFilters.isEmpty();
            if (filtraPeriodo && filtraMarca) {
                List<String> nomes = marcaFilters.stream()
                        .map(Marca::getNome)
                        .toList();
                resultado = new FaturaDAO().listarFaturasPorPeriodoEMarcas(
                        periodoFilterStart,
                        periodoFilterEnd,
                        nomes
                );
            } else if (filtraPeriodo) {
                resultado = new FaturaDAO().listarFaturasPorPeriodo(
                        periodoFilterStart,
                        periodoFilterEnd
                );
            } else if (filtraMarca) {
                List<String> nomes = marcaFilters.stream()
                        .map(Marca::getNome)
                        .toList();
                resultado = new FaturaDAO().listarFaturasPorMarcas(nomes);
            } else {
                resultado = new FaturaDAO().listarFaturas(false);
            }
            mostrarListaFaturas(resultado);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao aplicar filtros de faturas", ex);
            showAlert(ex.getMessage());
        }
    }
}
