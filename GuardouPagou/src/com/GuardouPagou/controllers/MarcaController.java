package com.GuardouPagou.controllers;

import com.GuardouPagou.models.MarcaView;
import com.GuardouPagou.dao.MarcaDAO;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import java.sql.SQLException;

public class MarcaController {
    private final MarcaView view;
    private final MarcaDAO marcaDAO;

    public MarcaController(MarcaView view) {
        this.view = view;
        this.marcaDAO = new MarcaDAO();
        configurarEventos();
        configurarDepuracaoCor();
    }

    private void configurarEventos() {
        view.getSalvarButton().setOnAction(e -> salvarMarca());
        view.getCancelarButton().setOnAction(e -> limparFormulario());
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

            System.out.println("Salvando marca: nome=" + nome + ", cor bruta=" + (color != null ? color.toString() : "null") + ", cor formatada=" + cor);

            marcaDAO.inserirMarca(nome, descricao, cor);
            mostrarSucesso("Marca cadastrada com sucesso!");
            limparFormulario();
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar marca: " + e.getMessage());
        }
    }

    private String formatarCor(Color color) {
        if (color == null) {
            System.out.println("Cor é null");
            return "";
        }
        int r = (int) Math.round(color.getRed() * 255);
        int g = (int) Math.round(color.getGreen() * 255);
        int b = (int) Math.round(color.getBlue() * 255);
        String formattedColor = String.format("#%02X%02X%02X", r, g, b);
        System.out.println("RGB: R=" + r + ", G=" + g + ", B=" + b + ", Formatada=" + formattedColor);
        return formattedColor;
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
            "-fx-border-color: #FF0000; " +
            "-fx-border-width: 1.5; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5;"
        );
    }

    private void resetarEstilosErro() {
        view.getNomeField().setStyle(
            "-fx-background-color: #2A2A2A; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-border-color: #4A4A4A; " +
            "-fx-border-width: 1; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5;"
        );
        view.getDescricaoArea().setStyle(
            "-fx-control-inner-background: #2A2A2A; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-border-color: #4A4A4A; " +
            "-fx-border-width: 1; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5;"
        );
        view.getCorPicker().setStyle(
            "-fx-background-color: #2A2A2A; " +
            "-fx-border-color: #4A4A4A; " +
            "-fx-border-width: 1; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5;"
        );
    }

    private void limparFormulario() {
        view.getNomeField().clear();
        view.getDescricaoArea().clear();
        view.getCorPicker().setValue(null);
        resetarEstilosErro();
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