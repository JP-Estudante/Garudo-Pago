package com.GuardouPagou.controllers;

import com.GuardouPagou.dao.MarcaDAO;
import com.GuardouPagou.views.MarcaView;
import javafx.scene.control.Control;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import javafx.stage.Stage;

public class MarcaController {

    private final MarcaView view;
    private final MarcaDAO marcaDAO;

    public MarcaController(MarcaView view) {
        this.view = view;
        this.marcaDAO = new MarcaDAO();

        try {
            int nextId = marcaDAO.getNextId();
            view.setNextId(nextId);
        } catch (SQLException e) {
            e.printStackTrace(); // ou mostrarErro…
        }

        configurarEventos();
        configurarDepuracaoCor();

    }

    private void configurarEventos() {
        view.getSalvarButton().setOnAction(e -> salvarMarca());
        view.getLimparButton().setOnAction(e -> limparFormulario());

        // listener de contador de descrição
        view.getDescricaoArea().textProperty().addListener((obs, oldText, newText) -> {
            int length = newText.length();
            if (length > 500) {
                // opcional: corta o excesso
                view.getDescricaoArea().setText(newText.substring(0, 500));
                length = 500;
            }
            view.getDescCounterLabel().setText(length + "/500");
        });
    }

    private void configurarDepuracaoCor() {
        view.getCorPicker().valueProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("ColorPicker mudou: " + (newVal != null ? newVal.toString() : "null"));
        });
    }

    private void salvarMarca() {
        try {
            if (!validarDados()) {
                return;
            }

            String nome = view.getNomeField().getText().trim();
            String descricao = view.getDescricaoArea().getText().trim();
            Color color = view.getCorPicker().getValue();
            String cor = formatarCor(color);

            marcaDAO.inserirMarca(nome, descricao, cor);
            mostrarSucesso("Marca cadastrada com sucesso!");

            // Fecha o modal
            Stage stage = (Stage) view.getRoot().getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            mostrarErro("Erro ao salvar marca: " + e.getMessage());
        }
    }

    private String formatarCor(Color color) {
        if (color == null) {
            return "";
        }
        int r = (int) Math.round(color.getRed() * 255);
        int g = (int) Math.round(color.getGreen() * 255);
        int b = (int) Math.round(color.getBlue() * 255);
        return String.format("#%02X%02X%02X", r, g, b);
    }

    private boolean validarDados() {
        resetarEstilosErro();
        boolean valido = true;
        StringBuilder erros = new StringBuilder();

        if (view.getNomeField().getText().trim().isEmpty()) {
            erros.append("• Nome da marca é obrigatório\n");
            destacarErro(view.getNomeField());
            valido = false;
        }
        if (view.getCorPicker().getValue() == null) {
            erros.append("• Cor é obrigatória\n");
            destacarErro(view.getCorPicker());
            valido = false;
        }

        if (!valido) {
            mostrarErro("Corrija os seguintes erros:\n\n" + erros.toString());
        }
        return valido;
    }

    private void destacarErro(Control control) {
        control.setStyle(
                "-fx-border-color: #FF0000; "
                + "-fx-border-width: 1.5; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5;"
        );
    }

    private void resetarEstilosErro() {
        // Retorna ao estilo inicial (deixa o CSS aplicado)
        view.getNomeField().setStyle(null);
        view.getDescricaoArea().setStyle(null);
        view.getCorPicker().setStyle(null);
    }

    private void limparFormulario() {
        // Limpa os valores
        view.getNomeField().clear();
        view.getDescricaoArea().clear();
        view.getCorPicker().setValue(Color.WHITE);

        // Reseta bordas/vermelho de erro
        resetarEstilosErro();

        // Remove foco dos campos, voltando ao estilo "pill" do CSS
        view.getRoot().requestFocus();
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
