import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;
import javafx.scene.control.TableCell;

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
        
        
        
        // Criar componentes
        btnListarFaturas = criarBotao("Listar Faturas", "#C88200");
        btnListarMarcas = criarBotao("Listar Marcas", "#C88200");
        btnArquivadas = criarBotao("Arquivadas", "#C88200");
        btnNovaFatura = criarBotao("Cadastrar nova fatura", "#f0a818");
        btnNovaMarca = criarBotao("Cadastrar nova marca", "#f0a818");
        
        conteudoLabel = new Label("Bem-vindo ao GuardouPagou");
        conteudoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        conteudoLabel.setTextFill(Color.web("#000000"));
        
        // Adicionar componentes ao layout
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
        btn.setStyle("-fx-background-color: " + cor + "; " +
                    "-fx-text-fill: #000000; " +
                    "-fx-font-weight: bold;");
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
        emailPanel.setStyle("-fx-background-color: #3d4043; " +
                          "-fx-border-color: #C88200; " +
                          "-fx-border-radius: 5;");
        
        Label emailTitle = new Label("Alertas por E-mail");
        emailTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        emailTitle.setTextFill(Color.web("#f0a818"));
        
        emailField = new TextField();
        emailField.setPromptText("Digite o e-mail para alertas");
        emailField.setPrefWidth(200);
        emailField.setStyle("-fx-prompt-text-fill: #BDBDBD;");
        
        btnSalvarEmail = new Button("Salvar E-mail");
        btnSalvarEmail.setStyle("-fx-background-color: #C88200; " +
                              "-fx-text-fill: #000000; " +
                              "-fx-font-weight: bold;");
        
        emailPanel.getChildren().addAll(emailTitle, emailField, btnSalvarEmail);
        return emailPanel;
    }

    public void mostrarListaMarcas(ObservableList<Marca> marcas) {
        // Criação da tabela
        TableView<Marca> tabela = new TableView<>();
        tabela.setStyle("-fx-background-color: #323437;");
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Coluna ID
        TableColumn<Marca, Integer> colunaId = new TableColumn<>("ID");
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaId.setStyle("-fx-text-fill: #BDBDBD;");
        colunaId.setPrefWidth(80);

        // Coluna Nome (com cor dinâmica)
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
                    setStyle("-fx-text-fill: " + marca.getCor() + "; " +
                            "-fx-font-weight: bold;");
                }
            }
        });
        colunaNome.setPrefWidth(200);

        // Coluna Cor (visualização)
        TableColumn<Marca, String> colunaCor = new TableColumn<>("Cor");
        colunaCor.setCellValueFactory(new PropertyValueFactory<>("cor"));
        colunaCor.setCellFactory(column -> new TableCell<Marca, String>() {
            @Override
            protected void updateItem(String cor, boolean empty) {
                super.updateItem(cor, empty);
                if (empty || cor == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(cor);
                    setStyle("-fx-background-color: " + cor + "; " +
                            "-fx-text-fill: black; " +
                            "-fx-font-weight: bold;");
                }
            }
    });
        colunaCor.setPrefWidth(150);

        // Adiciona colunas à tabela
        tabela.getColumns().addAll(colunaId, colunaNome, colunaCor);
        tabela.setItems(marcas);

        // Container principal
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: #BDBDBD;");

        // Título
        Label titulo = new Label("LISTAGEM DE MARCAS");
        titulo.setStyle("-fx-text-fill: #F0A818; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Barra de ferramentas
        HBox toolbar = new HBox(10);
        Button btnAtualizar = new Button("Atualizar");
        btnAtualizar.setStyle("-fx-background-color: #C88200; -fx-text-fill: #000000;");

        Button btnNovaMarca = new Button("Nova Marca");
        btnNovaMarca.setStyle("-fx-background-color: #F0A818; -fx-text-fill: #000000;");

        toolbar.getChildren().addAll(btnAtualizar, btnNovaMarca);
        toolbar.setAlignment(Pos.CENTER_RIGHT);

        // Adiciona componentes ao container
        container.getChildren().addAll(titulo, toolbar, tabela);

        // Define como conteúdo central
        root.setCenter(container);

        // Configura ações dos botões
        btnAtualizar.setOnAction(e -> atualizarListaMarcas());
        btnNovaMarca.setOnAction(e -> mostrarFormularioMarca());
    }
    
    private void atualizarListaMarcas() {
        try {
            ObservableList<Marca> marcas = new MarcaDAO().listarMarcas();
            mostrarListaMarcas(marcas);
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Mostrar mensagem de erro
        }
    }

    private void mostrarFormularioMarca() {
        // Implemente a exibição do formulário de cadastro aqui
        // Ou chame o método existente que mostra o formulário
    }

    private <T> TableColumn<Marca, T> criarColuna(String titulo, String propriedade, double largura) {
        TableColumn<Marca, T> coluna = new TableColumn<>(titulo);
        coluna.setCellValueFactory(new PropertyValueFactory<>(propriedade));
        coluna.setPrefWidth(largura);
        coluna.setStyle("-fx-text-fill: #BDBDBD; -fx-alignment: CENTER;");
        return coluna;
    }
    
    // Getters para todos os componentes necessários
    public Button getBtnListarFaturas() { return btnListarFaturas; }
    public Button getBtnListarMarcas() { return btnListarMarcas; }
    public Button getBtnArquivadas() { return btnArquivadas; }
    public Button getBtnNovaFatura() { return btnNovaFatura; }
    public Button getBtnNovaMarca() { return btnNovaMarca; }
    public Button getBtnSalvarEmail() { return btnSalvarEmail; }
    public TextField getEmailField() { return emailField; }
    public Label getConteudoLabel() { return conteudoLabel; }
}