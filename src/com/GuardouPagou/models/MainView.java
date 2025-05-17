package com.GuardouPagou.models;

import com.GuardouPagou.dao.MarcaDAO;
import com.GuardouPagou.controllers.MarcaController;
import com.GuardouPagou.dao.FaturaDAO;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import java.time.LocalDate;

public class MainView {

    private BorderPane root;
    private Button btnListarFaturas, btnListarMarcas, btnArquivadas;
    private Button btnNovaFatura, btnNovaMarca, btnSalvarEmail;
    private Label conteudoLabel;
    private TextField emailField;

    public BorderPane getRoot() {
        return this.root;
    }

    public MainView() {
        criarUI();
    }

    private void criarUI() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #BDBDBD;");

        VBox menuLateral = new VBox(20);
        menuLateral.setPadding(new Insets(20));
        menuLateral.setStyle("-fx-background-color: #323437; -fx-min-width: 250px;");

        btnListarFaturas = criarBotao("Listar Faturas", "#C88200");
        btnListarMarcas = criarBotao("Listar Marcas", "#C88200");
        btnArquivadas = criarBotao("Arquivadas", "#C88200");
        btnNovaFatura = criarBotao("Cadastrar Nota com Faturas", "#f0a818");
        btnNovaMarca = criarBotao("Cadastrar nova marca", "#f0a818");

        conteudoLabel = new Label("Bem-vindo ao GuardouPagou");
        conteudoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        conteudoLabel.setTextFill(Color.web("#000000"));

        menuLateral.getChildren().addAll(
                criarLogo(),
                criarTitulo("Principais listagens"),
                btnListarFaturas, btnListarMarcas, btnArquivadas,
                criarTitulo("Novos Cadastros"),
                btnNovaFatura, btnNovaMarca,
                criarEspacoFlexivel(),
                criarEmailPanel()
        );

        root.setLeft(menuLateral);
        root.setCenter(conteudoLabel);
    }

    private Button criarBotao(String texto, String cor) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: " + cor + "; "
                + "-fx-text-fill: #000000; "
                + "-fx-font-weight: bold;");
        return btn;
    }

    private VBox criarLogo() {
        VBox logoContainer = new VBox();
        logoContainer.setAlignment(Pos.CENTER);
        logoContainer.setPadding(new Insets(0, 0, 30, 0));
        logoContainer.setMinHeight(150);

        try {
            Image logoImage = new Image("file:logo.png");
            ImageView logoView = new ImageView(logoImage);
            logoView.setFitWidth(180);
            logoView.setPreserveRatio(true);
            logoContainer.getChildren().add(logoView);
        } catch (Exception e) {
            Label logoPlaceholder = new Label("LOGO DA LOJA");
            logoPlaceholder.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            logoPlaceholder.setTextFill(Color.web("#f0a818"));
            logoPlaceholder.setStyle("-fx-border-color: #C88200; -fx-border-width: 2px; -fx-padding: 40px;");
            logoContainer.getChildren().add(logoPlaceholder);
        }
        return logoContainer;
    }

    private Label criarTitulo(String texto) {
        Label label = new Label(texto);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#f0a818"));
        return label;
    }

    private VBox criarEspacoFlexivel() {
        VBox espaco = new VBox();
        VBox.setVgrow(espaco, Priority.ALWAYS);
        return espaco;
    }

    private VBox criarEmailPanel() {
        VBox emailPanel = new VBox(10);
        emailPanel.setPadding(new Insets(15));
        emailPanel.setStyle("-fx-background-color: #3d4043; "
                + "-fx-border-color: #C88200; "
                + "-fx-border-radius: 5;");

        Label emailTitle = new Label("Alertas por E-mail");
        emailTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        emailTitle.setTextFill(Color.web("#f0a818"));

        emailField = new TextField();
        emailField.setPromptText("Digite o e-mail para alertas");
        emailField.setPrefWidth(200);
        emailField.setStyle("-fx-prompt-text-fill: #BDBDBD;");

        btnSalvarEmail = new Button("Salvar E-mail");
        btnSalvarEmail.setStyle("-fx-background-color: #C88200; "
                + "-fx-text-fill: #000000; "
                + "-fx-font-weight: bold;");

        emailPanel.getChildren().addAll(emailTitle, emailField, btnSalvarEmail);
        return emailPanel;
    }

    public void mostrarListaMarcas(ObservableList<Marca> marcas) {
        TableView<Marca> tabela = new TableView<>();
        tabela.setStyle("-fx-background-color: #3d4043; -fx-border-color: #4A4A4A; -fx-border-width: 1; -fx-background-radius: 5; -fx-border-radius: 5;");
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Marca, Integer> colunaId = new TableColumn<>("ID");
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaId.setCellFactory(column -> new TableCell<Marca, Integer>() {
            @Override
            protected void updateItem(Integer id, boolean empty) {
                super.updateItem(id, empty);
                if (empty || id == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(id.toString());
                }
            }
        });
        colunaId.setPrefWidth(80);

        TableColumn<Marca, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaNome.setCellFactory(column -> new TableCell<Marca, String>() {
            @Override
            protected void updateItem(String nome, boolean empty) {
                super.updateItem(nome, empty);
                if (empty || nome == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(nome);

                    Marca marca = getTableView().getItems().get(getIndex());
                    String cor = marca.getCor();

                    if (cor != null && cor.matches("#[0-9A-Fa-f]{6}")) {
                        setStyle("-fx-text-fill: " + cor + "; "
                                + "-fx-font-weight: bold; "
                                + "-fx-border-color: #4A4A4A; "
                                + "-fx-border-width: 0.5; "
                                + "-fx-alignment: CENTER-LEFT;");
                    } else {
                        setStyle("-fx-text-fill: #FFFFFF; "
                                + "-fx-font-weight: bold; "
                                + "-fx-border-color: #4A4A4A; "
                                + "-fx-border-width: 0.5; "
                                + "-fx-alignment: CENTER-LEFT;");
                    }
                }
            }
        });
        colunaNome.setPrefWidth(200);

        TableColumn<Marca, String> colunaDescricao = new TableColumn<>("Descrição");
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaDescricao.setCellFactory(column -> new TableCell<Marca, String>() {
            @Override
            protected void updateItem(String descricao, boolean empty) {
                super.updateItem(descricao, empty);
                if (empty || descricao == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(descricao);
                }
            }
        });
        colunaDescricao.setPrefWidth(250);

        TableColumn<Marca, Void> colunaAcoes = new TableColumn<>("Ações");
        colunaAcoes.setCellFactory(column -> new TableCell<Marca, Void>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");

            {
                btnEditar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5px;");
                btnExcluir.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5px;");

                btnEditar.setOnAction(e -> {
                    Marca marca = getTableView().getItems().get(getIndex());
                    editarMarca(marca);
                });

                btnExcluir.setOnAction(e -> {
                    Marca marca = getTableView().getItems().get(getIndex());
                    excluirMarca(marca);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox botoes = new HBox(5, btnEditar, btnExcluir);
                    botoes.setAlignment(Pos.CENTER);
                    setGraphic(botoes);
                }
            }
        });
        colunaAcoes.setPrefWidth(150);

        tabela.getColumns().addAll(colunaId, colunaNome, colunaDescricao, colunaAcoes);
        tabela.setItems(marcas);

        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: #BDBDBD;");

        Label titulo = new Label("LISTAGEM DE MARCAS");
        titulo.setStyle("-fx-text-fill: #F0A818; -fx-font-size: 18px; -fx-font-weight: bold;");

        HBox toolbar = new HBox(10);
        Button btnAtualizar = new Button("Atualizar");
        btnAtualizar.setStyle("-fx-background-color: #C88200; -fx-text-fill: #000000; -fx-font-weight: bold;");

        Button btnNovaMarca = new Button("Nova Marca");
        btnNovaMarca.setStyle("-fx-background-color: #F0A818; -fx-text-fill: #000000; -fx-font-weight: bold;");

        toolbar.getChildren().addAll(btnAtualizar, btnNovaMarca);
        toolbar.setAlignment(Pos.CENTER_RIGHT);

        container.getChildren().addAll(titulo, toolbar, tabela);
        root.setCenter(container);

        btnAtualizar.setOnAction(e -> atualizarListaMarcas());
        btnNovaMarca.setOnAction(e -> mostrarFormularioMarca());
    }

    public void mostrarListaFaturas(ObservableList<Fatura> faturas) {
        TableView<Fatura> tabela = new TableView<>();
        tabela.setStyle("-fx-background-color: #323437; -fx-table-cell-border-color: #4A4A4A;");

        TableColumn<Fatura, Integer> colunaId = new TableColumn<>("ID");
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaId.setCellFactory(column -> new TableCell<Fatura, Integer>() {
            @Override
            protected void updateItem(Integer id, boolean empty) {
                super.updateItem(id, empty);
                setText(empty || id == null ? null : id.toString());
                setStyle("-fx-text-fill: #000000; -fx-background-color: #FFFFFF;");
            }
        });
        colunaId.setPrefWidth(80);

        TableColumn<Fatura, String> colunaNumeroNota = new TableColumn<>("Número da Nota");
        colunaNumeroNota.setCellValueFactory(new PropertyValueFactory<>("numeroNota"));
        colunaNumeroNota.setCellFactory(column -> new TableCell<Fatura, String>() {
            @Override
            protected void updateItem(String numeroNota, boolean empty) {
                super.updateItem(numeroNota, empty);
                setText(empty || numeroNota == null ? null : numeroNota);
                setStyle("-fx-text-fill: #000000; -fx-background-color: #FFFFFF;");
            }
        });
        colunaNumeroNota.setPrefWidth(150);

        TableColumn<Fatura, LocalDate> colunaVencimento = new TableColumn<>("Vencimento");
        colunaVencimento.setCellValueFactory(new PropertyValueFactory<>("vencimento"));
        colunaVencimento.setCellFactory(column -> new TableCell<Fatura, LocalDate>() {
            @Override
            protected void updateItem(LocalDate vencimento, boolean empty) {
                super.updateItem(vencimento, empty);
                setText(empty || vencimento == null ? null : vencimento.toString());
                setStyle("-fx-text-fill: #000000; -fx-background-color: #FFFFFF;");
            }
        });
        colunaVencimento.setPrefWidth(150);

        TableColumn<Fatura, Double> colunaValor = new TableColumn<>("Valor");
        colunaValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colunaValor.setCellFactory(column -> new TableCell<Fatura, Double>() {
            @Override
            protected void updateItem(Double valor, boolean empty) {
                super.updateItem(valor, empty);
                setText(empty || valor == null ? null : String.format("R$ %.2f", valor));
                setStyle("-fx-text-fill: #000000; -fx-background-color: #FFFFFF;");
            }
        });
        colunaValor.setPrefWidth(120);

        TableColumn<Fatura, String> colunaStatus = new TableColumn<>("Status");
        colunaStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colunaStatus.setCellFactory(column -> new TableCell<Fatura, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                setText(empty || status == null ? null : status);
                if (empty || status == null) {
                    setStyle("-fx-text-fill: #000000; -fx-background-color: #FFFFFF;");
                } else if (status.equals("Vencida")) {
                    setStyle("-fx-text-fill: #FF0000; -fx-font-weight: bold; -fx-background-color: #FFFFFF;");
                } else {
                    setStyle("-fx-text-fill: #000000; -fx-background-color: #FFFFFF;");
                }
            }
        });
        colunaStatus.setPrefWidth(120);

        tabela.getColumns().addAll(colunaId, colunaNumeroNota, colunaVencimento, colunaValor, colunaStatus);
        tabela.setItems(faturas);

        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: #BDBDBD;");

        Label titulo = new Label("LISTAGEM DE FATURAS");
        titulo.setStyle("-fx-text-fill: #F0A818; -fx-font-size: 18px; -fx-font-weight: bold;");

        HBox toolbar = new HBox(10);
        Button btnAtualizar = new Button("Atualizar");
        btnAtualizar.setStyle("-fx-background-color: #C88200; -fx-text-fill: #000000;");

        toolbar.getChildren().add(btnAtualizar);
        toolbar.setAlignment(Pos.CENTER_RIGHT);

        container.getChildren().addAll(titulo, toolbar, tabela);
        root.setCenter(container);

        btnAtualizar.setOnAction(e -> atualizarListaFaturas());
    }

    private void atualizarListaFaturas() {
        try {
            ObservableList<Fatura> faturas = new FaturaDAO().listarFaturas();
            mostrarListaFaturas(faturas);
        } catch (SQLException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao carregar faturas: " + ex.getMessage());
            alert.showAndWait();
        }
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
        return conteudoLabel;
    }
}
