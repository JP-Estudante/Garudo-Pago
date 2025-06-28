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

    public MainView() {
        criarUI();
        // carrega faturas não arquivadas
        try {
            ObservableList<Fatura> faturas = new FaturaDAO().listarFaturas(false);
            mostrarListaFaturas(faturas);
        } catch (SQLException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Erro ao carregar faturas: " + ex.getMessage())
                    .showAndWait();
            root.setCenter(labelText);
        }
    }

    public BorderPane getRoot() {
        return this.root;
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

        // → Lateral
        root.setLeft(criarMenuLateral());

        // −−− monta o filtro −−−
        // 1) ToggleGroup + RadioMenuItems
        filtroToggleGroup = new ToggleGroup();
        miFiltrarPeriodo = new RadioMenuItem("Filtrar por Período");
        miFiltrarMarca   = new RadioMenuItem("Filtrar por Marca");
        miFiltrarPeriodo.setToggleGroup(filtroToggleGroup);
        miFiltrarMarca  .setToggleGroup(filtroToggleGroup);

        // 2) Ícone e MenuButton
        ImageView filterIcon = new ImageView(
                getClass().getResource("/icons/ajust.png").toExternalForm()
        );
        filterIcon.setFitHeight(16);
        filterIcon.setPreserveRatio(true);

        btnFiltrar = new MenuButton("Filtrar", filterIcon,
                miFiltrarPeriodo,
                miFiltrarMarca);
        btnFiltrar.getStyleClass().addAll("menu-button","botao-listagem");
        btnFiltrar.setContentDisplay(ContentDisplay.LEFT);
        btnFiltrar.setGraphicTextGap(10);
        HBox.setHgrow(btnFiltrar, Priority.ALWAYS);
        btnFiltrar.setMaxWidth(Double.MAX_VALUE);

        // 3) DatePickers
        dpDataInicio = new DatePicker();
        dpDataInicio.setPromptText("Início do Período");
        dpDataInicio.setPrefWidth(150);

        dpDataFim = new DatePicker();
        dpDataFim.setPromptText("Fim do Período");
        dpDataFim.setPrefWidth(150);

        // 4) Botões Aplicar / Remover
        btnAplicarFiltro = new Button("Aplicar Filtro");
        btnAplicarFiltro.getStyleClass().addAll("menu-button","botao-listagem");
        btnAplicarFiltro.setOnAction(e -> aplicarFiltroPeriodo());

        btnRemoverFiltro = new Button("Remover Filtro");
        btnRemoverFiltro.getStyleClass().addAll("menu-button","botao-footer");
        btnRemoverFiltro.setOnAction(e -> {
            dpDataInicio.setValue(null);
            dpDataFim.setValue(null);
            mostrarTodasFaturas();
            filtroContainer.setVisible(false);
            filtroContainer.setManaged(false);
        });

        // 5) Container de período
        filtroContainer = new VBox(6,
                new HBox(6, new Label("De:"), dpDataInicio,
                        new Label("Até:"), dpDataFim),
                new HBox(10, btnAplicarFiltro, btnRemoverFiltro)
        );
        filtroContainer.setPadding(new Insets(8));
        filtroContainer.setVisible(false);
        filtroContainer.setManaged(false);

        // 6) Combo de Marca
        cbFiltroMarca = new ComboBox<>();
        cbFiltroMarca.setPromptText("Selecione a Marca");
        cbFiltroMarca.setPrefWidth(200);
        cbFiltroMarca.setVisible(false);
        cbFiltroMarca.setManaged(false);
        try {
            cbFiltroMarca.setItems(new MarcaDAO().listarMarcas());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // 7) Listeners para mostrar/esconder cada filtro
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

        // 8) Botão Atualizar
        Button btnAtualizar = new Button("Atualizar");
        btnAtualizar.getStyleClass().addAll("menu-button","botao-listagem");
        btnAtualizar.setOnAction(e -> atualizarListaFaturas());

        // 9) Toolbar
        HBox toolbar = new HBox(12, btnFiltrar, btnAtualizar);
        toolbar.setAlignment(Pos.CENTER_RIGHT);

        // 10) Título & Container principal
        Label titulo = new Label("LISTAGEM DE FATURAS");
        titulo.getStyleClass().add("h5");
        titulo.setTextFill(Color.web("#F0A818"));

        VBox container = new VBox(18,
                titulo,
                toolbar,
                filtroContainer,
                cbFiltroMarca,
                new Separator()
        );
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: #BDBDBD;");

        root.setCenter(container);
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

        // — defina aqui suas colunas exatamente como antes —
        // colunaId, colunaNumeroNota, colunaOrdem, colunaVencimento, colunaMarca, colunaStatus, colunaAcoes

        tabela.getColumns().setAll(colunaId, colunaNumeroNota, colunaOrdem,
                colunaVencimento, colunaMarca,
                colunaStatus, colunaAcoes);
        tabela.setItems(faturas);
        return tabela;
    }


    // MÉTODO mostrarListaMarcas - CORRIGIDO E ORIGINAL
    public void mostrarListaMarcas(ObservableList<Marca> marcas) {
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
        root.setCenter(container);
    }

    // MÉTODO mostrarListaFaturas - ADICIONADO E CORRIGIDO
    public void mostrarListaFaturas(ObservableList<Fatura> faturas) {
        // 1) Cria e configura a tabela
        TableView<Fatura> tabelaFaturas = new TableView<>();
        ViewUtils.aplicarEstiloPadrao(tabelaFaturas);
        tabelaFaturas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Coluna ID
        TableColumn<Fatura, Integer> colunaId = new TableColumn<>("ID");
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaId.setPrefWidth(80);
        colunaId.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer id, boolean empty) {
                super.updateItem(id, empty);
                if (empty || id == null) {
                    setText(null); setStyle("");
                } else {
                    setText(id.toString());
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-alignment: CENTER-LEFT;");
                }
            }
        });

        // Coluna Número da Nota
        TableColumn<Fatura, String> colunaNumeroNota = new TableColumn<>("NÚMERO DA NOTA");
        colunaNumeroNota.setCellValueFactory(new PropertyValueFactory<>("numeroNota"));
        colunaNumeroNota.setPrefWidth(150);
        colunaNumeroNota.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String numeroNota, boolean empty) {
                super.updateItem(numeroNota, empty);
                if (empty || numeroNota == null) {
                    setText(null); setStyle("");
                } else {
                    setText(numeroNota);
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-alignment: CENTER-LEFT;");
                }
            }
        });

        // Coluna Ordem da Fatura
        TableColumn<Fatura, Integer> colunaOrdem = new TableColumn<>("ORDEM DA FATURA");
        colunaOrdem.setCellValueFactory(new PropertyValueFactory<>("numeroFatura"));
        colunaOrdem.setPrefWidth(120);
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

        // Coluna Vencimento
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        TableColumn<Fatura, LocalDate> colunaVencimento = new TableColumn<>("VENCIMENTO");
        colunaVencimento.setCellValueFactory(new PropertyValueFactory<>("vencimento"));
        colunaVencimento.setPrefWidth(120);
        colunaVencimento.setCellFactory(col -> new TableCell<>() {
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

        // Coluna Marca
        TableColumn<Fatura, String> colunaMarca = new TableColumn<>("MARCA");
        colunaMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colunaMarca.setPrefWidth(150);
        colunaMarca.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String marca, boolean empty) {
                super.updateItem(marca, empty);
                if (empty || marca == null) {
                    setText(null); setStyle("");
                } else {
                    setText(marca);
                    String cor = getTableView().getItems().get(getIndex()).getMarcaColor();
                    setTextFill(Color.web(cor));
                    setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-alignment: CENTER-LEFT;");
                }
            }
        });

        // Coluna Status
        TableColumn<Fatura, String> colunaStatus = new TableColumn<>("STATUS");
        colunaStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colunaStatus.setPrefWidth(120);
        colunaStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null); setStyle("");
                } else {
                    setText(status);
                    setTextFill("Vencida".equalsIgnoreCase(status) ? Color.web("#f0a818") : Color.WHITE);
                    setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-alignment: CENTER-LEFT;");
                }
            }
        });

        // Coluna Ações
        TableColumn<Fatura, Void> colunaAcoes = new TableColumn<>("Ações");
        colunaAcoes.setPrefWidth(100);
        colunaAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnEmitida = new Button("Emitida");
            {
                btnEmitida.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5px;");
                btnEmitida.setOnAction(evt -> marcarFaturaComoEmitida(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Fatura f = getTableView().getItems().get(getIndex());
                    setGraphic(("Emitida".equalsIgnoreCase(f.getStatus()) || "Vencida".equalsIgnoreCase(f.getStatus()))
                            ? null
                            : new HBox(btnEmitida));
                }
            }
        });

        // Adiciona colunas e dados
        tabelaFaturas.getColumns().setAll(
                colunaId, colunaNumeroNota, colunaOrdem,
                colunaVencimento, colunaMarca,
                colunaStatus, colunaAcoes
        );
        tabelaFaturas.setItems(faturas);

        // 2) Limpa a área central a partir do Separator (índice 4)
        VBox container = (VBox) root.getCenter();
        if (container.getChildren().size() > 4) {
            container.getChildren().subList(4, container.getChildren().size()).clear();
        }

        // 3) Insere a tabela
        container.getChildren().add(tabelaFaturas);
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
            mostrarListaMarcas(marcas);
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
        LocalDate inicio = dpDataInicio.getValue();
        LocalDate fim    = dpDataFim.getValue();

        if (inicio == null || fim == null) {
            showAlert(Alert.AlertType.WARNING, "Atenção",
                    "Selecione data de início e data de término.");
            return;
        }
        if (inicio.isAfter(fim)) {
            showAlert(Alert.AlertType.ERROR, "Data inválida",
                    "Data de início não pode ser posterior à data de término.");
            return;
        }

        try {
            ObservableList<Fatura> lista = new FaturaDAO()
                    .listarFaturasPorPeriodo(inicio, fim);
            mostrarListaFaturas(lista);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro",
                    "Não foi possível filtrar: " + ex.getMessage());
        }
    }

    private void aplicarFiltroMarca() {
        Marca m = cbFiltroMarca.getValue();
        if (m == null) {
            showAlert(Alert.AlertType.WARNING, "Atenção",
                    "Selecione uma marca para filtrar.");
            return;
        }
        try {
            ObservableList<Fatura> lista = new FaturaDAO()
                    .listarFaturasPorMarca(m.getNome());
            mostrarListaFaturas(lista);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro",
                    "Não foi possível filtrar por marca: " + ex.getMessage());
        }
    }

    private void mostrarTodasFaturas() {
        try {
            ObservableList<Fatura> todas = new FaturaDAO().listarFaturas(false);
            mostrarListaFaturas(todas);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro",
                    "Não foi possível recarregar faturas: " + ex.getMessage());
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
