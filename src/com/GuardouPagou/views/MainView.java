package com.GuardouPagou.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.Objects;

public class MainView {
    private BorderPane root;
    private Button btnListarFaturas, btnListarMarcas, btnArquivadas;
    private Button btnNovaFatura, btnNovaMarca, btnSalvarEmail;
    private Label labelText;

    public MainView() {
        criarUI();
    }

    public BorderPane getRoot() {
        return this.root;
    }

    public void setConteudoPrincipal(Node novoConteudo) {
        root.setCenter(novoConteudo);
    }

    private void criarUI() {
        root = new BorderPane();
        root.getStyleClass().add("main-root");

        root.setLeft(criarMenuLateral());

        labelText = new Label("Bem-vindo ao Guardou-Pagou");
        labelText.getStyleClass().add("h5");
        labelText.setTextFill(Color.web("#323437"));

        StackPane painelCentral = new StackPane(labelText);
        painelCentral.setStyle("-fx-background-color: #BDBDBD;");
        root.setCenter(painelCentral);
    }

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

        btnListarFaturas = criarBotao("Listar Faturas", "/icons/list.png", "botao-listagem");
        btnListarMarcas = criarBotao("Listar Marcas", "/icons/list.png", "botao-listagem");
        btnArquivadas   = criarBotao("Arquivadas", "/icons/archive.png", "botao-listagem");
        btnNovaFatura   = criarBotao("Cadastrar Faturas", "/icons/note-plus.png", "botao-cadastro");
        btnNovaMarca    = criarBotao("Cadastrar Marca", "/icons/note-plus.png", "botao-cadastro");

        VBox secaoListagens = new VBox(criarTitulo("Principais Listagens"), btnListarFaturas, btnListarMarcas, btnArquivadas);
        secaoListagens.getStyleClass().add("menu-section");

        VBox secaoCadastros = new VBox(criarTitulo("Novos Cadastros"), btnNovaFatura, btnNovaMarca);
        secaoCadastros.getStyleClass().add("menu-section");

        btnSalvarEmail = criarBotao("E-mails de Alerta", "/icons/campaing.png", "botao-listagem");
        btnSalvarEmail.setPrefWidth(220);
        VBox secaoOutros = new VBox(criarTitulo("Outros"), btnSalvarEmail);
        secaoOutros.getStyleClass().add("menu-section");

        menuLateral.getChildren().addAll(criarLogo(), criarSeparadorLogo(), secaoListagens, secaoCadastros, secaoOutros, criarEspacoFlexivel());

        for (Button b : List.of(btnListarFaturas, btnListarMarcas, btnArquivadas, btnNovaFatura, btnNovaMarca, btnSalvarEmail)) {
            b.setFocusTraversable(false);
        }
        return menuLateral;
    }

    public Button getBtnListarFaturas() { return btnListarFaturas; }
    public Button getBtnListarMarcas() { return btnListarMarcas; }
    public Button getBtnArquivadas() { return btnArquivadas; }
    public Button getBtnNovaFatura() { return btnNovaFatura; }
    public Button getBtnNovaMarca() { return btnNovaMarca; }
    public Button getBtnSalvarEmail() { return btnSalvarEmail; }

    public void mostrarTelaInicial() {
        StackPane painelCentral = new StackPane(labelText);
        painelCentral.setStyle("-fx-background-color: #BDBDBD;");
        root.setCenter(painelCentral);
    }

    public Label getConteudoLabel() { return labelText; }
}
