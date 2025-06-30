package com.GuardouPagou.controllers;

import com.GuardouPagou.models.Fatura;
import com.GuardouPagou.models.Marca;
import com.GuardouPagou.models.NotaFiscal;
import com.GuardouPagou.views.ArquivadasView;
import com.GuardouPagou.views.MainView;
import com.GuardouPagou.views.MarcaView;
import com.GuardouPagou.views.NotaFaturaView;
import com.GuardouPagou.views.NotaFiscalDetalhesView;
import com.GuardouPagou.dao.MarcaDAO;
import com.GuardouPagou.dao.FaturaDAO;
import com.GuardouPagou.dao.NotaFiscalDAO;
import com.GuardouPagou.controllers.NotaFiscalDetalhesController;
import com.GuardouPagou.controllers.NotaFaturaController;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Node;
import java.util.Objects;
import java.sql.SQLException;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import javafx.geometry.Pos;

public class MainController {

    private MainView view;
    private Button botaoSelecionado;

    public MainController(MainView view) {
        this.view = view;
        configurarEventos();
    }

    private void configurarEventos() {
        // Ação para o botão Listar Faturas
        view.getBtnListarFaturas().setOnAction(e -> {
            // 1. Mostra um indicador de "carregando" imediatamente
            ProgressIndicator pi = new ProgressIndicator();
            VBox boxCarregando = new VBox(new Label("Carregando faturas..."), pi);
            boxCarregando.setAlignment(Pos.CENTER);
            boxCarregando.setSpacing(10);
            view.setConteudoPrincipal(boxCarregando);

            // 2. Cria uma Tarefa para rodar a busca no banco em segundo plano
            Task<ObservableList<Fatura>> carregarFaturasTask = new Task<>() {
                @Override
                protected ObservableList<Fatura> call() throws Exception {
                    // Esta é a operação demorada que vai para a outra thread
                    return new FaturaDAO().listarFaturas(false);
                }
            };

            // 3. Define o que fazer QUANDO a tarefa for bem-sucedida
            carregarFaturasTask.setOnSucceeded(event -> {
                ObservableList<Fatura> faturas = carregarFaturasTask.getValue();
                Node viewFaturas = view.criarViewFaturas(faturas);
                view.setConteudoPrincipal(viewFaturas); // Atualiza a tela com o resultado
                view.setNotaDoubleClickHandler(this::abrirDetalhesNotaFiscal);
            });

            // 4. Define o que fazer se a tarefa falhar
            carregarFaturasTask.setOnFailed(event -> {
                view.getConteudoLabel().setText("Erro ao carregar faturas.");
                carregarFaturasTask.getException().printStackTrace();
            });

            // 5. Inicia a tarefa em uma nova thread
            new Thread(carregarFaturasTask).start();
        });
        // Ação para o botão Listar Marcas
        view.getBtnListarMarcas().setOnAction(e -> {
            try {
                // 1. Busca os dados
                ObservableList<Marca> marcas = new MarcaDAO().listarMarcas();

                // 2. Solicita à View que crie o Node (a tela) com os dados
                Node viewMarcas = view.criarViewMarcas(marcas);

                // 3. Define a tela criada como o conteúdo principal
                view.setConteudoPrincipal(viewMarcas);

            } catch (SQLException ex) {
                view.getConteudoLabel().setText("Erro ao carregar marcas.");
                ex.printStackTrace();
            }
        });

        // Ação para o botão Arquivadas
        view.getBtnArquivadas().setOnAction(e -> {
            // 1. Cria a view e o controller da tela de arquivadas
            ArquivadasView arquivadasView = new ArquivadasView();
            new ArquivadasController(arquivadasView);

            // 2. Define a nova tela como conteúdo principal
            view.setConteudoPrincipal(arquivadasView.getRoot());
        });

        // --- O restante do código (modais e outros) permanece o mesmo ---
        // A lógica para abrir janelas modais não precisa mudar.

        view.getBtnNovaFatura().setOnAction(e -> {
            Stage modal = new Stage();
            Window owner = view.getRoot().getScene().getWindow();
            modal.initOwner(owner);
            modal.initModality(Modality.WINDOW_MODAL);
            modal.setTitle("Cadastro de Nota Fiscal");

            modal.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/plus.png"))));

            NotaFaturaView notaView = new NotaFaturaView();
            new NotaFaturaController(notaView);

            Scene cena = new Scene(notaView.getRoot(), 700, 500);
            cena.getStylesheets().addAll(
                    view.getRoot().getScene().getStylesheets()
            );

            cena.setOnKeyPressed(ev -> {
                if (ev.getCode() == KeyCode.ESCAPE) {
                    modal.close();
                }
            });

            modal.setScene(cena);
            modal.setResizable(false);
            modal.showAndWait();
        });

        view.getBtnNovaMarca().setOnAction(e -> {
            Stage modal = new Stage();
            Window owner = view.getRoot().getScene().getWindow();
            modal.initOwner(owner);
            modal.initModality(Modality.WINDOW_MODAL);
            modal.setTitle("Cadastro de Marca");

            modal.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/plus.png"))));

            MarcaView marcaView = new MarcaView();
            new MarcaController(marcaView);

            Scene scene = new Scene(marcaView.getRoot(), 650, 400);
            scene.getStylesheets().addAll(
                    view.getRoot().getScene().getStylesheets()
            );

            scene.setOnKeyPressed(ev -> {
                if (ev.getCode() == KeyCode.ESCAPE) {
                    modal.close();
                }
            });

            modal.setScene(scene);
            modal.setResizable(false);
            modal.showAndWait();
        });

        view.getBtnSalvarEmail().setOnAction(e -> {
            String email = view.getEmailField().getText();
            if (validarEmail(email)) {
                atualizarConteudo("E-mail para alertas salvo: " + email);
            } else {
                atualizarConteudo("E-mail inválido!");
            }
        });
    }

    private void atualizarConteudo(String texto) {
        view.getConteudoLabel().setText(texto);
    }

    private boolean validarEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$");
    }

    private void abrirDetalhesNotaFiscal(Fatura fatura) {
        try {
            NotaFiscal nota = new NotaFiscalDAO().buscarNotaFiscalPorId(fatura.getNotaFiscalId());
            if (nota == null) return;
            nota.setFaturas(new FaturaDAO().listarFaturasDaNota(fatura.getNotaFiscalId()));

            Stage modal = new Stage();
            Window owner = view.getRoot().getScene().getWindow();
            modal.initOwner(owner);
            modal.initModality(Modality.WINDOW_MODAL);
            modal.setTitle("Nota Fiscal " + nota.getNumeroNota());

            // NOVO: Adicionado o ícone da janela, conforme solicitado.
            modal.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/list.png"))));

            NotaFiscalDetalhesView detalhesView = new NotaFiscalDetalhesView();
            NotaFiscalDetalhesController controller = new NotaFiscalDetalhesController(detalhesView, modal);
            controller.preencherDados(nota);

            Scene scene = new Scene(detalhesView.getRoot(), 700, 570);
            scene.getStylesheets().addAll(view.getRoot().getScene().getStylesheets());

            scene.setOnKeyPressed(ev -> {
                if (ev.getCode() == KeyCode.ESCAPE) {
                    modal.close();
                }
            });

            modal.setScene(scene);
            modal.setResizable(false);
            modal.showAndWait();
        } catch (Exception ex) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Erro ao abrir detalhes: " + ex.getMessage());
            a.showAndWait();
        }
    }
}
