package com.GuardouPagou.views;

import com.GuardouPagou.controllers.MarcaController;
import com.GuardouPagou.dao.FaturaDAO;
import com.GuardouPagou.dao.MarcaDAO;
import com.GuardouPagou.dao.NotaFiscalDAO;
import com.GuardouPagou.models.Fatura;
import com.GuardouPagou.models.Marca;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ContentDisplay;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class MainView {

    private MenuButton btnFiltrar;
    private RadioMenuItem miFiltrarPeriodo;
    private RadioMenuItem miFiltrarMarca;
    private ToggleGroup filtroToggleGroup;
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

    private void atualizarListaFaturas() {
        if (miFiltrarPeriodo.isSelected()) {
            aplicarFiltroPeriodo();
        } else if (miFiltrarMarca.isSelected()) {
            aplicarFiltroMarca();
        } else {
            mostrarTodasFaturas();
        }
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

    private Button criarBotao(String texto, String iconPath, String cssClass) {
        Button btn = new Button(" " + texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.getStyleClass().addAll("menu-button", cssClass);

        if (iconPath != null) {
            try {
                ImageView icon = new ImageView(getClass().getResource(iconPath).toExternalForm());
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
            Image logoImage = new Image(getClass().getResource("/icons/G-Clock_home.png").toExternalForm());
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

    private VBox criarEspaçoFlexível() {
        VBox espaço = new VBox();
        VBox.setVgrow(espaço, Priority.ALWAYS);
        return espaço;
    }

    private VBox criarMenuLateral() {
        VBox menuLateral = new VBox();
        menuLateral.getStyleClass().add("menu-lateral-root");

        // Botões principais
        btnListarFaturas = criarBotao("Listar Faturas", "/icons/list.png", "botao-listagem");
        btnListarMarcas   = criarBotao("Listar Marcas",   "/icons/list.png", "botao-listagem");
        btnArquivadas     = criarBotao("Arquivadas",      "/icons/archive.png", "botao-listagem");
        btnNovaFatura     = criarBotao("Cadastrar Faturas","/icons/plus.png", "botao-cadastro");
        btnNovaMarca      = criarBotao("Cadastrar Marca", "/icons/plus.png", "botao-cadastro");

        // Seções
        VBox secaoListagens = new VBox(
                criarTitulo("Principais Listagens"),
                btnListarFaturas,
                btnListarMarcas,
                btnArquivadas
        );
        secaoListagens.getStyleClass().add("menu-section");

        VBox secaoCadastros = new VBox(
                criarTitulo("Novos Cadastros"),
                btnNovaFatura,
                btnNovaMarca
        );
        secaoCadastros.getStyleClass().add("menu-section");

        btnSalvarEmail = criarBotao("E-mails de Alerta", "/icons/campaing.png", "botao-listagem");
        btnSalvarEmail.setPrefWidth(220);
        VBox secaoOutros = new VBox(
                criarTitulo("Outros"),
                btnSalvarEmail
        );
        secaoOutros.getStyleClass().add("menu-section");

        // Monta tudo
        menuLateral.getChildren().addAll(
                criarLogo(),
                criarSeparadorLogo(),
                secaoListagens,
                secaoCadastros,
                secaoOutros,
                criarEspaçoFlexível()
        );

        // desabilita focus traversal
        for (Button b : List.of(
                btnListarFaturas, btnListarMarcas, btnArquivadas,
                btnNovaFatura, btnNovaMarca, btnSalvarEmail
        )) {
            b.setFocusTraversable(false);
        }

        return menuLateral;
    }

    private void mostrarTelaListagemFaturas(ObservableList<Fatura> faturas) {
        // 1) monta a tabela
        TableView<Fatura> tabela = criarTabelaFaturas(faturas);

        // 2) monta os filtros / toolbar (se quiser manter)
        HBox toolbar = new HBox(12, btnFiltrar, new Button("Atualizar"));
        toolbar.setAlignment(Pos.CENTER_RIGHT);
        ((Button)toolbar.getChildren().get(1)).setOnAction(e -> atualizarListaFaturas());
        HBox.setHgrow(btnFiltrar, Priority.ALWAYS);
        btnFiltrar.setMaxWidth(Double.MAX_VALUE);

        // 3) título
        Label titulo = new Label("LISTAGEM DE FATURAS");
        titulo.getStyleClass().add("h5");
        titulo.setTextFill(Color.web("#F0A818"));

        // 4) container inteiro (substitui inteiramente o centro)
        VBox tela = new VBox(18, titulo, toolbar, tabela);
        tela.setPadding(new Insets(20));
        tela.setStyle("-fx-background-color: #BDBDBD;");

        // 5) faz a troca “de verdade”
        root.setCenter(tela);
    }

    private TableView<Fatura> criarTabelaFaturas(ObservableList<Fatura> faturas) {
        TableView<Fatura> tabela = new TableView<>();
        ViewUtils.aplicarEstiloPadrao(tabela);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // --- Definição das Colunas ---

        // Coluna ID
        TableColumn<Fatura, Integer> colunaId = new TableColumn<>("ID");
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
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
        colunaId.setPrefWidth(80);

        // Coluna NÚMERO DA NOTA
        TableColumn<Fatura, String> colunaNumeroNota = new TableColumn<>("NÚMERO DA NOTA");
        colunaNumeroNota.setCellValueFactory(new PropertyValueFactory<>("numeroNota"));
        colunaNumeroNota.setCellFactory(col -> new TableCell<>() {
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
        colunaNumeroNota.setPrefWidth(150);

        // Coluna ORDEM DA FATURA
        TableColumn<Fatura, Integer> colunaOrdem = new TableColumn<>("ORDEM DA FATURA");
        colunaOrdem.setCellValueFactory(new PropertyValueFactory<>("numeroFatura"));
        colunaOrdem.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer ordem, boolean empty) {
                super.updateItem(ordem, empty);
                if (empty || ordem == null) {
                    setText(null); setStyle("");
                } else {
                    setText(ordem.toString());
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-alignment: CENTER-LEFT;");
                }
            }
        });
        colunaOrdem.setPrefWidth(120);

        // Coluna VENCIMENTO
        TableColumn<Fatura, LocalDate> colunaVencimento = new TableColumn<>("VENCIMENTO");
        colunaVencimento.setCellValueFactory(new PropertyValueFactory<>("vencimento"));
        colunaVencimento.setCellFactory(col -> new TableCell<>() {
            private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            @Override
            protected void updateItem(LocalDate venc, boolean empty) {
                super.updateItem(venc, empty);
                if (empty || venc == null) {
                    setText(null); setStyle("");
                } else {
                    setText(venc.format(fmt));
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-alignment: CENTER-LEFT;");
                }
            }
        });
        colunaVencimento.setPrefWidth(120);

        // Coluna MARCA
        TableColumn<Fatura, String> colunaMarca = new TableColumn<>("MARCA");
        colunaMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colunaMarca.setCellFactory(col -> new TableCell<>() {
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
        colunaMarca.setPrefWidth(150);

        // Coluna STATUS
        TableColumn<Fatura, String> colunaStatus = new TableColumn<>("STATUS");
        colunaStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colunaStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null); setStyle("");
                } else {
                    setText(status);
                    if ("Vencida".equalsIgnoreCase(status)) {
                        setTextFill(Color.web("#f0a818"));
                    } else {
                        setTextFill(Color.WHITE);
                    }
                    setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-alignment: CENTER-LEFT;");
                }
            }
        });
        colunaStatus.setPrefWidth(120);

        // Coluna AÇÕES
        TableColumn<Fatura, Void> colunaAcoes = new TableColumn<>("Ações");
        colunaAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnEmitida = new Button("Emitida");
            {
                btnEmitida.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5px;");
                btnEmitida.setOnAction(evt -> {
                    Fatura f = getTableView().getItems().get(getIndex());
                    marcarFaturaComoEmitida(f);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Fatura f = getTableView().getItems().get(getIndex());
                    if ("Emitida".equalsIgnoreCase(f.getStatus()) || "Vencida".equalsIgnoreCase(f.getStatus())) {
                        setGraphic(null);
                    } else {
                        HBox box = new HBox(btnEmitida);
                        box.setAlignment(Pos.CENTER);
                        setGraphic(box);
                    }
                }
            }
        });
        colunaAcoes.setPrefWidth(100);

        // Adiciona as colunas e os itens à tabela
        tabela.getColumns().setAll(colunaId, colunaNumeroNota, colunaOrdem,
                colunaVencimento, colunaMarca,
                colunaStatus, colunaAcoes);
        tabela.setItems(faturas);

        return tabela;
    }

    // MÉTODO criarViewMarcas - CORRIGIDO E ORIGINAL
    public Node criarViewMarcas(ObservableList<Marca> marcas) {
        // 1) Cria e estiliza a TableView
        TableView<Marca> tabela = new TableView<>();
        ViewUtils.aplicarEstiloPadrao(tabela);
        tabela.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );

        // 2) Coluna ID
        TableColumn<Marca, Integer> colunaId = new TableColumn<>("ID");
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaId.setPrefWidth(80);
        colunaId.setCellFactory(col -> new TableCell<Marca, Integer>() {
            @Override
            protected void updateItem(Integer id, boolean empty) {
                super.updateItem(id, empty);
                if (empty || id == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(id.toString());
                    setTextFill(Color.WHITE);
                    setStyle(
                            "-fx-background-color: transparent; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-alignment: CENTER-LEFT;"
                    );
                }
            }
        });

        // 3) Coluna Nome (dinâmica conforme a cor cadastrada)
        TableColumn<Marca, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaNome.setPrefWidth(200);
        colunaNome.setCellFactory(col -> new TableCell<Marca, String>() {
            @Override
            protected void updateItem(String nome, boolean empty) {
                super.updateItem(nome, empty);
                if (empty || nome == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(nome);
                    // pega a cor cadastrada na Marca
                    String cor = getTableView()
                            .getItems()
                            .get(getIndex())
                            .getCor(); // ex: "#FF0000"
                    if (cor != null && cor.matches("#[0-9A-Fa-f]{6}")) {
                        setTextFill(Color.web(cor));
                    } else {
                        setTextFill(Color.WHITE);
                    }
                    setStyle(
                            "-fx-background-color: transparent; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-alignment: CENTER-LEFT;"
                    );
                }
            }
        });

        // 4) Coluna Descrição
        TableColumn<Marca, String> colunaDescricao = new TableColumn<>("Descrição");
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaDescricao.setPrefWidth(250);
        colunaDescricao.setCellFactory(col -> new TableCell<Marca, String>() {
            @Override
            protected void updateItem(String desc, boolean empty) {
                super.updateItem(desc, empty);
                if (empty) {
                    setText(null);
                    setStyle("");
                } else if (desc == null || desc.isBlank()) {
                    setText("Nenhuma descrição adicionada");
                    setTextFill(Color.WHITE);
                    setStyle(
                            "-fx-background-color: transparent; " +
                                    "-fx-alignment: CENTER-LEFT;"
                    );
                } else {
                    setText(desc);
                    setTextFill(Color.WHITE);
                    setStyle(
                            "-fx-background-color: transparent; " +
                                    "-fx-alignment: CENTER-LEFT;"
                    );
                }
            }
        });

        // 5) Monta e popula a tabela
        tabela.getColumns().setAll(colunaId, colunaNome, colunaDescricao);
        tabela.setItems(marcas);

        // 6) Coloca no layout
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

    // MÉTODO criarViewFaturas - ADICIONADO E CORRIGIDO
    public Node criarViewFaturas(ObservableList<Fatura> faturas) {
        // 1. CRIA UM CONTAINER NOVO E LIMPO PARA A TELA
        VBox container = new VBox(18);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: #BDBDBD;");

        // 2. CRIA O TÍTULO DA TELA
        Label titulo = new Label("LISTAGEM DE FATURAS");
        titulo.getStyleClass().add("h5");
        titulo.setTextFill(Color.web("#F0A818"));

        // 3. INICIALIZA OS COMPONENTES DE FILTRO (A PARTE QUE FALTAVA)
        // ==========================================================

        // ToggleGroup + RadioMenuItems (são campos da classe, mas precisam ser verificados)
        if (filtroToggleGroup == null) filtroToggleGroup = new ToggleGroup();
        miFiltrarPeriodo = new RadioMenuItem("Filtrar por Período");
        miFiltrarMarca = new RadioMenuItem("Filtrar por Marca");
        miFiltrarPeriodo.setToggleGroup(filtroToggleGroup);
        miFiltrarMarca.setToggleGroup(filtroToggleGroup);

        // Ícone e MenuButton de filtro
        ImageView filterIcon = new ImageView(getClass().getResource("/icons/ajust.png").toExternalForm());
        filterIcon.setFitHeight(16);
        filterIcon.setPreserveRatio(true);

        btnFiltrar = new MenuButton("Filtrar", filterIcon, miFiltrarPeriodo, miFiltrarMarca);
        btnFiltrar.getStyleClass().addAll("menu-button", "botao-listagem");
        btnFiltrar.setContentDisplay(ContentDisplay.LEFT);
        btnFiltrar.setGraphicTextGap(10);
        // As linhas de Hgrow e MaxWidth foram removidas para corrigir o tamanho

        // DatePickers
        dpDataInicio = new DatePicker();
        dpDataInicio.setPromptText("Início do Período");
        dpDataInicio.setPrefWidth(150);

        dpDataFim = new DatePicker();
        dpDataFim.setPromptText("Fim do Período");
        dpDataFim.setPrefWidth(150);

        // Botões Aplicar / Remover
        btnAplicarFiltro = new Button("Aplicar Filtro");
        btnAplicarFiltro.getStyleClass().addAll("menu-button","botao-listagem");
        btnAplicarFiltro.setOnAction(e -> aplicarFiltroPeriodo());
        btnAplicarFiltro.setFocusTraversable(false);

        btnRemoverFiltro = new Button("Remover Filtro");
        btnRemoverFiltro.getStyleClass().addAll("menu-button","botao-footer");
        btnRemoverFiltro.setOnAction(e -> {
            dpDataInicio.setValue(null);
            dpDataFim.setValue(null);
            mostrarTodasFaturas();
            if (filtroContainer != null) {
                filtroContainer.setVisible(false);
                filtroContainer.setManaged(false);
            }
        });

        // Container de período
        filtroContainer = new VBox(6,
                new HBox(6, new Label("De:"), dpDataInicio, new Label("Até:"), dpDataFim),
                new HBox(10, btnAplicarFiltro, btnRemoverFiltro)
        );
        filtroContainer.setPadding(new Insets(8));
        filtroContainer.setVisible(false);
        filtroContainer.setManaged(false);

        // ComboBox de Marca
        cbFiltroMarca = new ComboBox<>();
        cbFiltroMarca.setPromptText("Selecione a Marca");
        cbFiltroMarca.setPrefWidth(200);
        cbFiltroMarca.setVisible(false);
        cbFiltroMarca.setManaged(false);

        try {
            // Se o cache de marcas ainda não foi criado, busca no banco
            if (cacheMarcas == null) {
                System.out.println("Buscando marcas no banco pela primeira vez...");
                cacheMarcas = new MarcaDAO().listarMarcas();
            }
            // Usa a lista do cache
            cbFiltroMarca.setItems(cacheMarcas);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Listener para aplicar o filtro ao selecionar a marca
        cbFiltroMarca.setOnAction(e -> aplicarFiltroMarca());

        // Listeners para mostrar/esconder cada filtro
        miFiltrarPeriodo.setOnAction(e -> {
            filtroContainer.setVisible(true);
            filtroContainer.setManaged(true);
            cbFiltroMarca.setVisible(false);
            cbFiltroMarca.setManaged(false);
        });
        miFiltrarMarca.setOnAction(e -> {
            filtroContainer.setVisible(false);
            filtroContainer.setManaged(false);
            cbFiltroMarca.setVisible(true);
            cbFiltroMarca.setManaged(true);
        });

        // ==========================================================

        // 4. MONTA A BARRA DE FERRAMENTAS
        Button btnAtualizar = new Button("Atualizar");
        btnAtualizar.getStyleClass().addAll("menu-button", "botao-listagem");
        btnAtualizar.setOnAction(e -> atualizarListaFaturas());

        // HBox para alinhar botões à direita
        HBox espacador = new HBox();
        HBox.setHgrow(espacador, Priority.ALWAYS); // Ocupa todo o espaço
        HBox toolbar = new HBox(12, btnFiltrar, espacador, btnAtualizar);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        // 5. CRIA A TABELA DE FATURAS
        TableView<Fatura> tabelaFaturas = criarTabelaFaturas(faturas);
        VBox.setVgrow(tabelaFaturas, Priority.ALWAYS);

        // 6. MONTA A TELA FINAL NO NOVO CONTAINER
        container.getChildren().addAll(
                titulo,
                toolbar,
                filtroContainer,
                cbFiltroMarca,
                tabelaFaturas
        );

        // 7. RETORNA A TELA COMPLETA E PRONTA
        return container;
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
                        boolean notaFiscalArquivada = new NotaFiscalDAO().marcarComoArquivada(fatura.getNotaFiscalId(), LocalDate.now());
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
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erro");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Erro ao processar emissão da fatura: " + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }

    private void showAlert(Alert.AlertType tipo, String titulo, String msg) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void atualizarListaMarcas() {
        try {
            ObservableList<Marca> marcas = new MarcaDAO().listarMarcas();
            criarViewMarcas(marcas);
        } catch (SQLException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao carregar marcas: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void mostrarFormularioMarca() {
        MarcaView marcaView = new MarcaView();
        new MarcaController(marcaView);
        root.setCenter(marcaView.getRoot());
    }

    private void editarMarca(Marca marca) {
        MarcaView marcaView = new MarcaView();
        new MarcaController(marcaView);

        marcaView.getNomeField().setText(marca.getNome());
        marcaView.getDescricaoArea().setText(marca.getDescricao());
        try {
            String cor = marca.getCor() != null && marca.getCor().matches("#[0-9A-Fa-f]{6}") ? marca.getCor() : "#000000";
            marcaView.getCorPicker().setValue(Color.web(cor));
        } catch (IllegalArgumentException e) {
            System.out.println("Cor inválida: " + marca.getCor());
            marcaView.getCorPicker().setValue(Color.BLACK);
        }

        root.setCenter(marcaView.getRoot());
    }

    private void excluirMarca(Marca marca) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Excluir Marca");
        alert.setContentText("Tem certeza que deseja excluir a marca " + marca.getNome() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                new MarcaDAO().excluirMarca(marca.getId());
                atualizarListaMarcas();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erro");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Erro ao excluir marca: " + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }

    private void aplicarFiltroPeriodo() {
        try {
            LocalDate dataInicio = dpDataInicio.getValue();
            LocalDate dataFim = dpDataFim.getValue(); // <-- OBTENDO A DATA DE FIM

            // Validação para garantir que ambas as datas foram selecionadas
            if (dataInicio == null || dataFim == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, selecione a data de INÍCIO e FIM para filtrar por período.");
                alert.showAndWait();
                return;
            }

            // Validação para garantir que a data de início não é posterior à de fim
            if (dataInicio.isAfter(dataFim)) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "A data de início não pode ser posterior à data de fim.");
                alert.showAndWait();
                return;
            }

            // 1. Busca os dados com o filtro de período (agora com os dois argumentos)
            ObservableList<Fatura> faturas = new FaturaDAO().listarFaturasPorPeriodo(dataInicio, dataFim);

            // 2. Cria a view com os dados filtrados
            Node viewFaturas = criarViewFaturas(faturas);

            // 3. Define como conteúdo principal
            setConteudoPrincipal(viewFaturas);

        } catch (SQLException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao filtrar faturas por período: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void aplicarFiltroMarca() {
        try {
            Marca marcaSelecionadaObj = cbFiltroMarca.getValue();
            if (marcaSelecionadaObj == null || marcaSelecionadaObj.getNome().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, selecione uma marca para filtrar.");
                alert.showAndWait();
                mostrarTodasFaturas(); // Opcional: volta para a lista completa se a marca for nula
                return;
            }

            // 1. Busca os dados com o filtro de marca
            String nomeMarca = marcaSelecionadaObj.getNome();
            ObservableList<Fatura> faturas = new FaturaDAO().listarFaturasPorMarca(nomeMarca);

            // 2. Cria a view com os dados filtrados
            Node viewFaturas = criarViewFaturas(faturas);

            // 3. Define como conteúdo principal
            setConteudoPrincipal(viewFaturas);

        } catch (SQLException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao filtrar faturas por marca: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void mostrarTodasFaturas() {
        try {
            // 1. Busca os dados
            ObservableList<Fatura> faturas = new FaturaDAO().listarFaturas(false);

            // 2. Cria a view com base nos dados
            Node viewFaturas = criarViewFaturas(faturas);

            // 3. Define a view como o conteúdo principal, substituindo o antigo
            setConteudoPrincipal(viewFaturas);

        } catch (SQLException ex) {
            ex.printStackTrace();
            // Lógica para tratar o erro
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao carregar todas as faturas: " + ex.getMessage());
            alert.showAndWait();
        }
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

    public TextField getEmailField() {
        return emailField;
    }

    public Label getConteudoLabel() {
        return labelText; // CORRIGIDO: Retornar labelText, não conteudoLabel
    }
}
