package com.GuardouPagou.controllers;

import com.GuardouPagou.models.Fatura;
import com.GuardouPagou.models.Marca;
import com.GuardouPagou.dao.FaturaDAO;
import com.GuardouPagou.dao.MarcaDAO;
import com.GuardouPagou.views.MainView;
import com.GuardouPagou.views.ArquivadasView;
import com.GuardouPagou.views.NotaFaturaView;
import com.GuardouPagou.views.MarcaView;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {

    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

    private final MainView view;

    public MainController(MainView view) {
        this.view = view;
        configurarEventos();
    }

    private void configurarEventos() {
        view.getBtnListarFaturas().setOnAction(this::onListarFaturas);
        view.getBtnListarMarcas().setOnAction(this::onListarMarcas);
        view.getBtnArquivadas().setOnAction(this::onAbrirArquivadas);
        view.getBtnNovaFatura().setOnAction(this::onAbrirCadastroNota);
        view.getBtnNovaMarca().setOnAction(this::onAbrirCadastroMarca);
        view.getBtnSalvarEmail().setOnAction(this::onSalvarEmail);
    }

    private void onListarFaturas(ActionEvent ignored) {
        ProgressIndicator pi = new ProgressIndicator();
        VBox boxCarregando = new VBox(new Label("Carregando faturas..."), pi);
        boxCarregando.setAlignment(Pos.CENTER);
        boxCarregando.setSpacing(10);
        view.setConteudoPrincipal(boxCarregando);

        Task<ObservableList<Fatura>> task = new Task<>() {
            @Override
            protected ObservableList<Fatura> call() throws Exception {
                return new FaturaDAO().listarFaturas(false);
            }
        };

        task.setOnSucceeded(ignoredSuccess -> {
            ObservableList<Fatura> faturas = task.getValue();
            Node viewFaturas = view.criarViewFaturas(faturas);
            view.setConteudoPrincipal(viewFaturas);
        });

        task.setOnFailed(ignoredFailure -> {
            LOGGER.log(Level.SEVERE, "Falha ao carregar faturas", task.getException());
            view.getConteudoLabel().setText("Erro ao carregar faturas.");
        });

        new Thread(task).start();
    }

    private void onListarMarcas(ActionEvent ignored) {
        try {
            ObservableList<Marca> marcas = new MarcaDAO().listarMarcas();
            Node viewMarcas = view.criarViewMarcas(marcas);
            view.setConteudoPrincipal(viewMarcas);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar marcas", ex);
            view.getConteudoLabel().setText("Erro ao carregar marcas.");
        }
    }

    private void onAbrirArquivadas(ActionEvent ignored) {
        ArquivadasView arquivadasView = new ArquivadasView();
        new ArquivadasController(arquivadasView);
        view.setConteudoPrincipal(arquivadasView.getRoot());
    }

    private void onAbrirCadastroNota(ActionEvent ignored) {
        Stage modal = new Stage();
        Window owner = view.getRoot().getScene().getWindow();
        modal.initOwner(owner);
        modal.initModality(Modality.WINDOW_MODAL);
        modal.setTitle("Cadastro de Nota Fiscal");
        modal.getIcons().add(new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/icons/plus.png")
        )));

        NotaFaturaView notaView = new NotaFaturaView();
        new NotaFaturaController(notaView);

        Scene cena = new Scene(notaView.getRoot(), 700, 500);
        cena.getStylesheets().addAll(view.getRoot().getScene().getStylesheets());
        cena.setOnKeyPressed(ev -> { if (ev.getCode() == KeyCode.ESCAPE) modal.close(); });

        modal.setScene(cena);
        modal.setResizable(false);
        modal.showAndWait();
    }

    private void onAbrirCadastroMarca(ActionEvent ignored) {
        Stage modal = new Stage();
        Window owner = view.getRoot().getScene().getWindow();
        modal.initOwner(owner);
        modal.initModality(Modality.WINDOW_MODAL);
        modal.setTitle("Cadastro de Marca");
        modal.getIcons().add(new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/icons/plus.png")
        )));

        MarcaView marcaView = new MarcaView();
        new MarcaController(marcaView);

        Scene cena = new Scene(marcaView.getRoot(), 650, 400);
        cena.getStylesheets().addAll(view.getRoot().getScene().getStylesheets());
        cena.setOnKeyPressed(ev -> { if (ev.getCode() == KeyCode.ESCAPE) modal.close(); });

        modal.setScene(cena);
        modal.setResizable(false);
        modal.showAndWait();
    }

    private void onSalvarEmail(ActionEvent ignored) {
        String email = view.getEmailField().getText();
        if (validarEmail(email)) {
            view.getConteudoLabel().setText("E-mail para alertas salvo: " + email);
        } else {
            view.getConteudoLabel().setText("E-mail inválido!");
        }
    }

    private boolean validarEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$");
    }
}
