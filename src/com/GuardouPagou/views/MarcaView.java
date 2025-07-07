package com.GuardouPagou.views;

import com.GuardouPagou.models.Marca;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class MarcaView {
    private final VBox root;
    private final TableView<Marca> tabela;

    public MarcaView(ObservableList<Marca> marcas) {
        tabela = new TableView<>();
        ViewUtils.aplicarEstiloPadrao(tabela);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabela.setItems(marcas);

        TableColumn<Marca, Integer> colunaId = criarColunaId();
        TableColumn<Marca, String> colunaNome = criarColunaNome();
        TableColumn<Marca, String> colunaDescricao = criarColunaDescricao();
        tabela.getColumns().setAll(colunaId, colunaNome, colunaDescricao);

        Label titulo = new Label("Listagem de Marcas");
        titulo.getStyleClass().add("h2");
        titulo.setTextFill(Color.web("#181848"));

        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_RIGHT);

        root = new VBox(8, titulo, toolbar, tabela);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #BDBDBD;");
        VBox.setVgrow(tabela, Priority.ALWAYS);
    }

    public VBox getRoot() { return root; }
    public TableView<Marca> getTabela() { return tabela; }

    private TableColumn<Marca, Integer> criarColunaId() {
        TableColumn<Marca, Integer> col = new TableColumn<>("ID");
        col.setCellValueFactory(new PropertyValueFactory<>("id"));
        col.setPrefWidth(80);
        col.setCellFactory(c -> new TableCell<>() {
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
        return col;
    }

    private TableColumn<Marca, String> criarColunaNome() {
        TableColumn<Marca, String> col = new TableColumn<>("Nome");
        col.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col.setPrefWidth(200);
        col.setCellFactory(c -> new TableCell<>() {
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
        return col;
    }

    private TableColumn<Marca, String> criarColunaDescricao() {
        TableColumn<Marca, String> col = new TableColumn<>("Descrição");
        col.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        col.setPrefWidth(250);
        col.setCellFactory(c -> new TableCell<>() {
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
        return col;
    }
}
