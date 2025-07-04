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
import javafx.scene.control.TableRow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.HBox;

public class MainView {
    private static final Logger LOGGER = Logger.getLogger(MainView.class.getName());

    private MenuButton btnFiltrar;
    private RadioMenuItem miFiltrarPeriodo;
    private RadioMenuItem miFiltrarMarca;
    private BorderPane root;
    private Button btnListarFaturas, btnListarMarcas, btnArquivadas;
    private Button btnNovaFatura, btnNovaMarca, btnSalvarEmail;
    private Label labelText; // Alterado de conteudoLabel para labelText para corresponder ao uso em criarUI
    private TextField emailField;
    private DatePicker dpDataInicio, dpDataFim;
    private Button btnAplicarFiltro, btnRemoverFiltro;
    private VBox filtroContainer;
    private ComboBox<Marca> cbFiltroMarca;
    private ObservableList<Marca> cacheMarcas;
    private final Popup filtroPopup = new Popup();
    private LocalDate periodoFilterStart;
    private LocalDate periodoFilterEnd;
    private final Set<Marca> marcaFilters = new HashSet<>();
    private HBox filterTokens;
    private TableView<Fatura> tabelaFaturas;
    private Button btnDetalhes;
    private VBox faturasContainer;
    private java.util.function.Consumer<Fatura> notaDoubleClickHandler;

    public MainView() {
        criarUI();
    }

    public BorderPane getRoot() {
        return this.root;
    }

    // NOVO MÉTODO: Responsável apenas por trocar o conteúdo central
    public void setConteudoPrincipal(Node novoConteudo) {
        root.setCenter(novoConteudo);
    }

    public void setNotaDoubleClickHandler(java.util.function.Consumer<Fatura> handler) {
        this.notaDoubleClickHandler = handler;
    }

    private void atualizarListaFaturas() {
        atualizarListaComFiltros();
    }

    // Novo: torna público para que o controller possa chamar quando
    // a tela de detalhes for fechada e seja necessário recarregar a lista
    public void recarregarListaFaturas() {
        atualizarListaFaturas();
    }

    private void criarUI() {
        root = new BorderPane();
        root.getStyleClass().add("main-root");

        // 1. Cria e posiciona o menu lateral.
        root.setLeft(criarMenuLateral());

        // 2. Cria uma label com a mensagem de boas-vindas.
        labelText = new Label("Bem-vindo ao Guardou-Pagou");
        labelText.getStyleClass().add("h5"); // Estilo de fonte definido no seu CSS.
        labelText.setTextFill(Color.web("#323437")); // Uma cor escura para o texto.

        // 3. Coloca a mensagem em um painel centralizador.
        StackPane painelCentral = new StackPane(labelText);
        painelCentral.setStyle("-fx-background-color: #BDBDBD;"); // Fundo cinza padrão.

        // 4. Define o painel de boas-vindas como o conteúdo inicial.
        root.setCenter(painelCentral);
    }

    @SuppressWarnings("unused")
    private Button criarBotao(String texto, String iconPath, String cssClass) {
        Button btn = new Button(" " + texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.getStyleClass().addAll("menu-button", cssClass);

        if (iconPath != null) {
            try {
                ImageView icon = new ImageView(Objects.requireNonNull(getClass().getResource(iconPath)).toExternalForm());
                icon.setPreserveRatio(true);
                btn.setGraphic(icon);
            } catch (Exception e) {
                System.err.println("Erro ao carregar ícone: " + iconPath);
            }
        }

        btn.setOnMouseClicked(e -> btn.getParent().requestFocus());

        return btn;
    }

    private VBox criarLogo() {
        VBox logoContainer = new VBox();
        logoContainer.setPadding(new Insets(10, 0, 5, 10));
        logoContainer.setSpacing(0);
        logoContainer.setAlignment(Pos.TOP_LEFT);

        try {
            // Apenas a logo
            Image logoImage = new Image(Objects.requireNonNull(getClass().getResource("/icons/G-Clock_home.png")).toExternalForm());
            ImageView logoView = new ImageView(logoImage);
            logoView.setPreserveRatio(true);
            logoView.setSmooth(true);
            logoView.setCache(true);

            logoContainer.getChildren().add(logoView);
        } catch (Exception e) {
            Label fallback = new Label("LOGO");
            fallback.setFont(Font.font("Poppins", FontWeight.BOLD, 24));
            fallback.setTextFill(Color.web("#F0A818"));
            logoContainer.getChildren().add(fallback);
        }

        return logoContainer;
    }

    private Region criarSeparadorLogo() {
        Region linha = new Region();
        linha.setPrefHeight(2);
        linha.setMaxWidth(Double.MAX_VALUE);
        linha.getStyleClass().add("logo-divider");
        return linha;
    }

    private Label criarTitulo(String texto) {
        Label label = new Label(texto);
        label.getStyleClass().add("menu-subtitle-light");
        return label;
    }

    private VBox criarEspacoFlexivel() {
        VBox espaco = new VBox();
        VBox.setVgrow(espaco, Priority.ALWAYS);
        return espaco;
    }

    private VBox criarMenuLateral() {
        VBox menuLateral = new VBox();
        menuLateral.getStyleClass().add("menu-lateral-root");

        // Botões principais
        btnListarFaturas = criarBotao("Listar Faturas", "/icons/list.png", "botao-listagem");
        btnListarMarcas = criarBotao("Listar Marcas", "/icons/list.png", "botao-listagem");
        btnArquivadas = criarBotao("Arquivadas", "/icons/archive.png", "botao-listagem");
        btnNovaFatura = criarBotao("Cadastrar Faturas", "/icons/note-plus.png", "botao-cadastro");
        btnNovaMarca = criarBotao("Cadastrar Marca", "/icons/note-plus.png", "botao-cadastro");

        // Seções
        VBox secaoListagens = new VBox(criarTitulo("Principais Listagens"), btnListarFaturas, btnListarMarcas, btnArquivadas);
        secaoListagens.getStyleClass().add("menu-section");

        VBox secaoCadastros = new VBox(criarTitulo("Novos Cadastros"), btnNovaFatura, btnNovaMarca);
        secaoCadastros.getStyleClass().add("menu-section");

        btnSalvarEmail = criarBotao("E-mails de Alerta", "/icons/campaing.png", "botao-listagem");
        btnSalvarEmail.setPrefWidth(220);
        VBox secaoOutros = new VBox(criarTitulo("Outros"), btnSalvarEmail);
        secaoOutros.getStyleClass().add("menu-section");

        // Monta tudo
        menuLateral.getChildren().addAll(criarLogo(), criarSeparadorLogo(), secaoListagens, secaoCadastros, secaoOutros, criarEspacoFlexivel());

        // desabilita focus traversal
        for (Button b : List.of(btnListarFaturas, btnListarMarcas, btnArquivadas, btnNovaFatura, btnNovaMarca, btnSalvarEmail)) {
            b.setFocusTraversable(false);
        }

        return menuLateral;
    }

    @SuppressWarnings("unused")
    private TableView<Fatura> criarTabelaFaturas(ObservableList<Fatura> faturas) {
        TableView<Fatura> tabela = new TableView<>();
        ViewUtils.aplicarEstiloPadrao(tabela);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // Chama os métodos auxiliares para criar cada coluna
        TableColumn<Fatura, Integer> colunaId = criarColunaIdFatura();
        TableColumn<Fatura, String> colunaNumeroNota = criarColunaNumeroNotaFatura();
        TableColumn<Fatura, Integer> colunaOrdem = criarColunaOrdemFatura();
        TableColumn<Fatura, LocalDate> colunaVencimento = criarColunaVencimentoFatura();
        TableColumn<Fatura, String> colunaMarca = criarColunaMarcaFatura();
        TableColumn<Fatura, String> colunaStatus = criarColunaStatusFatura();

        // Adiciona as colunas e os itens à tabela
        tabela.getColumns().setAll(List.of(colunaId, colunaNumeroNota, colunaOrdem, colunaVencimento, colunaMarca, colunaStatus));
        tabela.setItems(faturas);

        return tabela;
    }

// --- NOVOS MÉTODOS PRIVADOS AUXILIARES PARA CADA COLUNA ---

    @SuppressWarnings("unused")
    private TableColumn<Fatura, Integer> criarColunaIdFatura() {
        TableColumn<Fatura, Integer> coluna = new TableColumn<>("ID");
        coluna.setCellValueFactory(new PropertyValueFactory<>("id"));
        coluna.setPrefWidth(80);
        coluna.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer id, boolean empty) {
                super.updateItem(id, empty);
                if (empty || id == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(id.toString());
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-alignment: CENTER-LEFT;");
                }
            }
        });
        return coluna;
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
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
                    setStyle("");
                } else {
                    setText(marca);
                    String cor = getTableView().getItems().get(getIndex()).getMarcaColor();
                    setTextFill(Color.web(cor));
                    setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-alignment: CENTER-LEFT;");
                }
            }
        });
        return coluna;
    }

    @SuppressWarnings("unused")
    private TableColumn<Fatura, String> criarColunaStatusFatura() {
        TableColumn<Fatura, String> coluna = new TableColumn<>("STATUS");
        coluna.setCellValueFactory(new PropertyValueFactory<>("status"));
        coluna.setMinWidth(180);
        coluna.setPrefWidth(180);
        coluna.setMaxWidth(180);
        coluna.setCellFactory(col -> new TableCell<>() {
            private final MenuItem miNaoEmitida = new MenuItem("Não Emitida");
            private final MenuItem miEmitida = new MenuItem("Emitida");
            private final MenuButton menu = new MenuButton();

            {
                menu.getItems().addAll(miNaoEmitida, miEmitida);
                menu.getStyleClass().add("fatura-status-menu");
                menu.contextMenuProperty().addListener((obs, oldCtx, newCtx) -> {
                    if (newCtx != null) {
                        newCtx.getStyleClass().add("fatura-status-popup");
                        // (aqui fica também o binding de largura que você já tinha)
                    }
                });
                setAlignment(Pos.CENTER_LEFT);
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

    // MÉTODO criarViewMarcas - CORRIGIDO E ORIGINAL
    @SuppressWarnings("unused")
    public Node criarViewMarcas(ObservableList<Marca> marcas) {
        // 1. Cria a tabela
        TableView<Marca> tabela = new TableView<>();
        ViewUtils.aplicarEstiloPadrao(tabela);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // 2. Cria as colunas chamando os novos métodos auxiliares
        TableColumn<Marca, Integer> colunaId = criarColunaIdMarca();
        TableColumn<Marca, String> colunaNome = criarColunaNomeMarca();
        TableColumn<Marca, String> colunaDescricao = criarColunaDescricaoMarca();

        // 3. Monta a tabela
        tabela.getColumns().setAll(List.of(colunaId, colunaNome, colunaDescricao));
        tabela.setItems(marcas);

        // 4. Monta o layout da tela
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: #BDBDBD;");

        Label titulo = new Label("LISTAGEM DE MARCAS");
        titulo.setStyle("-fx-text-fill: #F0A818; -fx-font-size: 18px; -fx-font-weight: bold;");

        HBox toolbar = new HBox(10);
        Button btnAtualizar = new Button("Atualizar");
        btnAtualizar.setStyle("-fx-background-color: #C88200; -fx-text-fill: #000000; -fx-font-weight: bold;");
        btnAtualizar.setOnAction(e -> atualizarListaMarcas());

        toolbar.getChildren().add(btnAtualizar);
        toolbar.setAlignment(Pos.CENTER_RIGHT);

        container.getChildren().addAll(titulo, toolbar, tabela);
        return container;
    }

    @SuppressWarnings("unused")
    private TableColumn<Marca, Integer> criarColunaIdMarca() {
        TableColumn<Marca, Integer> colunaId = new TableColumn<>("ID");
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaId.setPrefWidth(80);
        colunaId.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer id, boolean empty) {
                super.updateItem(id, empty);
                if (empty || id == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(id.toString());
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-alignment: CENTER-LEFT;");
                }
            }
        });
        return colunaId;
    }

    @SuppressWarnings("unused")
    private TableColumn<Marca, String> criarColunaNomeMarca() {
        TableColumn<Marca, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaNome.setPrefWidth(200);
        colunaNome.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String nome, boolean empty) {
                super.updateItem(nome, empty);
                if (empty || nome == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(nome);
                    String cor = getTableView().getItems().get(getIndex()).getCor();
                    if (cor != null && cor.matches("#[0-9A-Fa-f]{6}")) {
                        setTextFill(Color.web(cor));
                    } else {
                        setTextFill(Color.WHITE);
                    }
                    setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-alignment: CENTER-LEFT;");
                }
            }
        });
        return colunaNome;
    }

    @SuppressWarnings("unused")
    private TableColumn<Marca, String> criarColunaDescricaoMarca() {
        TableColumn<Marca, String> colunaDescricao = new TableColumn<>("Descrição");
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaDescricao.setPrefWidth(250);
        colunaDescricao.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String desc, boolean empty) {
                super.updateItem(desc, empty);
                if (empty) {
                    setText(null);
                    setStyle("");
                } else if (desc == null || desc.isBlank()) {
                    setText("Nenhuma descrição adicionada");
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: transparent; -fx-alignment: CENTER-LEFT;");
                } else {
                    setText(desc);
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: transparent; -fx-alignment: CENTER-LEFT;");
                }
            }
        });
        return colunaDescricao;
    }

    // MÉTODO criarViewFaturas - ADICIONADO E CORRIGIDO
    @SuppressWarnings("unused")
    public Node criarViewFaturas(ObservableList<Fatura> faturas) {
        // 1. Container principal
        VBox container = new VBox(18);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: #BDBDBD;");
        this.faturasContainer = container;

        // 2. Título
        Label titulo = new Label("LISTAGEM DE FATURAS");
        titulo.getStyleClass().add("h5");
        titulo.setTextFill(Color.web("#F0A818"));

        // 3. MenuButton Filtrar
        ToggleGroup filtroToggleGroup = new ToggleGroup();
        miFiltrarPeriodo = new RadioMenuItem("Filtrar por Período");
        miFiltrarMarca   = new RadioMenuItem("Filtrar por Marca");
        miFiltrarPeriodo.setToggleGroup(filtroToggleGroup);
        miFiltrarMarca.setToggleGroup(filtroToggleGroup);

        ImageView filterIcon = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/filter_list.png")))
        );
        filterIcon.setFitHeight(22);
        filterIcon.setPreserveRatio(true);
        btnFiltrar = new MenuButton("Filtrar", filterIcon, miFiltrarPeriodo, miFiltrarMarca);
        btnFiltrar.getStyleClass().addAll("menu-button", "botao-listagem", "btn-filtrar");
        btnFiltrar.setContentDisplay(ContentDisplay.LEFT);
        btnFiltrar.setGraphicTextGap(10);

        // ─── Conteúdo PERÍODO para o Popup ───
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
        dpDataFim.setOnAction(e -> {
            aplicarFiltroPeriodo();
            filtroPopup.hide();
        });
        Label lblDataFim = new Label("Data Final");
        lblDataFim.getStyleClass().add("field-subtitle");
        VBox dataFimBox = new VBox(6, lblDataFim, dpDataFim);
        dataFimBox.getStyleClass().add("pill-field");

        // Botões “Aplicar” e “Cancelar” (só ícone)
        Button btnAplicarFiltro = new Button();
        btnAplicarFiltro.getStyleClass().addAll("modal-button", "btn-aplicar");
        ImageView checkIcon = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/check.png")))
        );
        checkIcon.setPreserveRatio(true);
        btnAplicarFiltro.setGraphic(checkIcon);
        btnAplicarFiltro.setFocusTraversable(false);
        btnAplicarFiltro.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        btnAplicarFiltro.setOnAction(e -> {
            aplicarFiltroPeriodo();
            filtroPopup.hide();
        });

        Button btnCancelarFiltro = new Button();
        btnCancelarFiltro.getStyleClass().addAll("modal-button", "btn-cancelar");
        ImageView cancelIcon = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/cancel.png")))
        );
        cancelIcon.setPreserveRatio(true);
        btnCancelarFiltro.setGraphic(cancelIcon);
        btnCancelarFiltro.setFocusTraversable(false);
        btnCancelarFiltro.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        btnCancelarFiltro.setOnAction(e -> filtroPopup.hide());

        // Layout do período: datas empilhadas + botões empilhados, tudo centralizado
        VBox dateVBox   = new VBox(10, dataInicioBox, dataFimBox);
        dateVBox.setAlignment(Pos.CENTER_LEFT);
        VBox buttonVBox = new VBox(10, btnAplicarFiltro, btnCancelarFiltro);
        buttonVBox.setAlignment(Pos.CENTER_LEFT);
        HBox contentHBox = new HBox(20, dateVBox, buttonVBox);
        contentHBox.setAlignment(Pos.CENTER);

        VBox periodContent = new VBox(contentHBox);
        periodContent.getStyleClass().addAll("painel-filtros","painel-filtros-canto-quadrado");
        periodContent.setPadding(new Insets(15));

        // ─── Conteúdo MARCA para o Popup ───
        Label lblFiltrarMarca = new Label("Filtrar por Marca:");
        lblFiltrarMarca.getStyleClass().add("field-subtitle");
        cbFiltroMarca = new ComboBox<>();
        cbFiltroMarca.setPromptText("Selecione uma marca");
        cbFiltroMarca.setPrefWidth(200);
        try {
            cbFiltroMarca.setItems(new MarcaDAO().listarMarcas());
        } catch (SQLException ex) {
            // Substitui printStackTrace pela chamada ao Logger
            LOGGER.log(Level.SEVERE, "Falha ao carregar a lista de marcas para o ComboBox de filtro.", ex);
        }
        cbFiltroMarca.setOnAction(e -> {
            aplicarFiltroMarca();
            filtroPopup.hide();
        });

        // aplica mesmo estilo “pill” dos DatePickers
        VBox marcaBox = new VBox(6, lblFiltrarMarca, cbFiltroMarca);
        marcaBox.getStyleClass().add("pill-field");

        VBox marcaContent = new VBox(marcaBox);
        marcaContent.getStyleClass().addAll("painel-filtros","painel-filtros-canto-quadrado");
        marcaContent.setPadding(new Insets(15));

        // ─── Configura o Popup ───
        filtroPopup.setAutoHide(true);
        filtroPopup.setHideOnEscape(true);
        filtroPopup.setOnShowing(evt ->
                btnFiltrar.getStyleClass().add("filter-open")
        );
        filtroPopup.setOnHiding(evt ->
                btnFiltrar.getStyleClass().remove("filter-open")
        );

        // Container de tokens (filtros ativos)
        filterTokens = new HBox(8);
        filterTokens.setAlignment(Pos.CENTER_LEFT);
        filterTokens.setPadding(new Insets(0,0,0,10));

        // Handlers do MenuButton
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

        // ─── Monta toolbar e tabela ───
        btnDetalhes = new Button("Detalhes");
        btnDetalhes.getStyleClass().addAll("menu-button","botao-listagem");
        btnDetalhes.setDisable(true);

        HBox espacador = new HBox();
        HBox.setHgrow(espacador, Priority.ALWAYS);

        HBox toolbar = new HBox(12, filterTokens, espacador, btnFiltrar, btnDetalhes);
        toolbar.setAlignment(Pos.CENTER_LEFT);

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

        // 6. Adiciona tudo ao container principal
        container.getChildren().addAll(titulo, toolbar, this.tabelaFaturas);
        return container;
    }

    private void mostrarListaFaturas(ObservableList<Fatura> faturas) {
        if (this.tabelaFaturas != null) {
            this.tabelaFaturas.setItems(faturas);
        }
    }

    // MÉTODO marcarFaturaComoEmitida - ADICIONADO
    private void marcarFaturaComoEmitida(Fatura fatura) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Marcar Fatura como Emitida");
        alert.setContentText("Tem certeza que deseja marcar a fatura Nº " + fatura.getNumeroFatura() + " da Nota " + fatura.getNumeroNota() + " como emitida?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // 1. Marcar a fatura individual como 'Emitida' no banco de dados
                // NOTA: 'marcarFaturaIndividualComoEmitida' e 'todasFaturasDaNotaEmitidas'
                // devem existir ou ser implementados em FaturaDAO.java
                boolean faturaMarcada = new FaturaDAO().marcarFaturaIndividualComoEmitida(fatura.getId());

                if (faturaMarcada) {
                    // 2. Verificar se TODAS as faturas desta nota fiscal estão agora 'Emitida'
                    boolean todasEmitidas = new FaturaDAO().todasFaturasDaNotaEmitidas(fatura.getNotaFiscalId());

                    if (todasEmitidas) {
                        // Se todas as faturas da Nota Fiscal estão emitidas, marcar a Nota Fiscal como arquivada
                        // NOTA: 'marcarComoArquivada' deve existir ou ser implementado em NotaFiscalDAO.java
                        boolean notaFiscalArquivada = new NotaFiscalDAO().marcarComoArquivada(
                                fatura.getNotaFiscalId(),
                                LocalDate.now().plusDays(3)
                        );
                        if (notaFiscalArquivada) {
                            // Atualizar a lista e navegar para arquivadas
                            atualizarListaFaturas();
                            if (getBtnArquivadas() != null) {
                                getBtnArquivadas().fire(); // Simula o clique para navegar
                            }
                        } else {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Erro");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Erro ao arquivar a Nota Fiscal após todas as faturas serem emitidas.");
                            errorAlert.showAndWait();
                        }
                    } else {
                        // Se nem todas as faturas da nota foram emitidas, apenas atualiza a lista atual
                        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                        infoAlert.setTitle("Fatura Emitida");
                        infoAlert.setHeaderText(null);
                        infoAlert.setContentText("Fatura Nº " + fatura.getNumeroFatura() + " marcada como emitida. Aguardando outras faturas da Nota " + fatura.getNumeroNota() + " para arquivamento.");
                        infoAlert.showAndWait();
                        atualizarListaFaturas(); // Apenas recarrega a lista para refletir a mudança de status
                    }
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erro");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Não foi possível marcar a fatura como emitida.");
                    errorAlert.showAndWait();
                }
            } catch (SQLException e) {
                // Substitui printStackTrace pela chamada ao Logger
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

    private void atualizarListaMarcas() {
        try {
            ObservableList<Marca> marcas = new MarcaDAO().listarMarcas();
            criarViewMarcas(marcas);
        } catch (SQLException ex) {
            // Substitui printStackTrace pela chamada ao Logger
            LOGGER.log(Level.SEVERE, "Falha ao carregar a lista de marcas do banco de dados.", ex);

            // O restante do código permanece o mesmo
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao carregar marcas: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    @SuppressWarnings("unused")
    private void aplicarFiltroPeriodo() {
        LocalDate inicio = dpDataInicio.getValue();
        LocalDate fim    = dpDataFim.getValue();
        if (inicio != null && fim != null) {
            // 1) guarda estado
            periodoFilterStart = inicio;
            periodoFilterEnd   = fim;

            // 2) aplica filtros utilizando o método auxiliar
            atualizarListaComFiltros();

            // 3) remove token anterior de período (caso exista)
            filterTokens.getChildren().removeIf(n -> "periodo".equals(n.getUserData()));

            // 4) formata texto do token
            String txt = inicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + " – "
                    + fim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            // 5) cria o botão-token
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

            // 6) ao clicar, limpa filtro e remove o token
            periodoBtn.setOnAction(ev -> {
                periodoFilterStart = periodoFilterEnd = null;
                filterTokens.getChildren().remove(periodoBtn);
                atualizarListaComFiltros();
            });

            // 7) adiciona o token à barra
            filterTokens.getChildren().add(periodoBtn);
        }
        // fecha o popup
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

    @SuppressWarnings("unused")
    private void aplicarFiltroMarca() {
        Marca sel = cbFiltroMarca.getValue();
        if (sel != null && marcaFilters.add(sel)) {
            // 1) aplica filtros utilizando o método auxiliar
            atualizarListaComFiltros();

            // 3) cria o botão-token
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

            // 4) ao clicar, remove apenas aquela marca e refaz o filtro
            marcaBtn.setOnAction(ev -> {
                marcaFilters.remove(sel);
                filterTokens.getChildren().remove(marcaBtn);
                atualizarListaComFiltros();
            });

            // 5) adiciona o token à barra
            filterTokens.getChildren().add(marcaBtn);
        }
        filtroPopup.hide();
    }

    public Button getBtnListarFaturas() {
        return btnListarFaturas;
    }

    public Button getBtnListarMarcas() {
        return btnListarMarcas;
    }

    public Button getBtnArquivadas() {
        return btnArquivadas;
    }

    public Button getBtnNovaFatura() {
        return btnNovaFatura;
    }

    public Button getBtnNovaMarca() {
        return btnNovaMarca;
    }

    public Button getBtnSalvarEmail() {
        return btnSalvarEmail;
    }

    public TableView<Fatura> getTabelaFaturas() { return tabelaFaturas; }

    public Button getBtnDetalhes() { return btnDetalhes; }

    public VBox getFaturasViewContainer() { return faturasContainer; }

    public void mostrarTelaInicial() {
        StackPane painelCentral = new StackPane(labelText);
        painelCentral.setStyle("-fx-background-color: #BDBDBD;");
        root.setCenter(painelCentral);
    }

    public Label getConteudoLabel() {
        return labelText; // CORRIGIDO: Retornar labelText, não conteudoLabel
    }
}
