package com.GuardouPagou.controllers;

import com.GuardouPagou.models.Marca;
import com.GuardouPagou.models.Fatura;
import com.GuardouPagou.models.MainView;
import com.GuardouPagou.models.MarcaView;
import com.GuardouPagou.models.NotaFaturaView;
import com.GuardouPagou.dao.MarcaDAO;
import com.GuardouPagou.dao.FaturaDAO;
import com.GuardouPagou.controllers.MarcaController;
import com.GuardouPagou.controllers.NotaFaturaController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList; 
import java.sql.SQLException;             

public class MainController {
    private MainView view;
    private Button botaoSelecionado;
    
    public MainController(MainView view) {
        this.view = view;
        configurarEventos();
    }
    
    private void configurarEventos() {
        view.getBtnListarFaturas().setOnAction(e -> {
            try {
                ObservableList<Fatura> faturas = new FaturaDAO().listarFaturas();
                view.mostrarListaFaturas(faturas);
                destacarBotao(view.getBtnListarFaturas());
            } catch (SQLException ex) {
                view.getConteudoLabel().setText("Erro ao carregar faturas.");
                ex.printStackTrace();
            }
        });
        
        view.getBtnListarMarcas().setOnAction(e -> {
            try {
                ObservableList<Marca> marcas = new MarcaDAO().listarMarcas();
                view.mostrarListaMarcas(marcas);
                destacarBotao(view.getBtnListarMarcas());
            } catch (SQLException ex) {
                view.getConteudoLabel().setText("Erro ao carregar marcas.");
                ex.printStackTrace();
            }
        });
        
        view.getBtnArquivadas().setOnAction(e -> {
            atualizarConteudo("Documentos Arquivados");
            destacarBotao(view.getBtnArquivadas());
        });
        
        view.getBtnNovaFatura().setOnAction(e -> {
            NotaFaturaView notaFaturaView = new NotaFaturaView();
            new NotaFaturaController(notaFaturaView);
            view.getRoot().setCenter(notaFaturaView.getRoot());
            destacarBotao(view.getBtnNovaFatura());
        });
        
        view.getBtnNovaMarca().setOnAction(e -> {
            MarcaView marcaView = new MarcaView();
            new MarcaController(marcaView);
            view.getRoot().setCenter(marcaView.getRoot());
            destacarBotao(view.getBtnNovaMarca());
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
   
    private void destacarBotao(Button botao) {
        // Remove destaque do botão anterior
        if (botaoSelecionado != null) {
            String corOriginal = botaoSelecionado == view.getBtnNovaFatura() || 
                               botaoSelecionado == view.getBtnNovaMarca() ? 
                               "#f0a818" : "#C88200";
            botaoSelecionado.setStyle("-fx-background-color: " + corOriginal + "; " +
                                    "-fx-text-fill: #000000; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-border-width: 0;");
        }
        
        // Aplica destaque ao novo botão
        botao.setStyle("-fx-background-color: #f0a818; " +
                     "-fx-text-fill: #000000; " +
                     "-fx-font-weight: bold; " +
                     "-fx-border-color: #BDBDBD; " +
                     "-fx-border-width: 2px;");
        
        botaoSelecionado = botao;
    }
    
    private void atualizarConteudo(String texto) {
        view.getConteudoLabel().setText(texto);
    }
    
    private boolean validarEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$");
    }
}