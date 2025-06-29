package com.GuardouPagou.controllers;

import com.GuardouPagou.dao.MarcaDAO;
import com.GuardouPagou.views.MarcaView;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MarcaController {

    private static final Logger LOGGER = Logger.getLogger(MarcaController.class.getName());

    private final MarcaView view;
    private final MarcaDAO  marcaDAO;

    public MarcaController(MarcaView view) {
        this.view    = view;
        this.marcaDAO = new MarcaDAO();
        init();
    }

    private void init() {
        // substituir printStackTrace() por logger
        try {
            view.setNextId(marcaDAO.getNextId());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao obter próximo ID da marca", e);
            mostrarErro("Erro ao iniciar cadastro: " + e.getMessage());
        }

        // method references em vez de lambdas com parâmetro não usado
        view.getSalvarButton().setOnAction(this::handleSalvarMarca);
        view.getLimparButton().setOnAction(this::handleLimparFormulario);

        // renomear parâmetros não usados para “ignored…”
        view.getDescricaoArea().textProperty()
                .addListener(this::onDescricaoChange);

        // remover toString() desnecessário e renomear params
        view.getCorPicker().valueProperty()
                .addListener(this::onCorPickerChange);
    }

    private void handleSalvarMarca(ActionEvent ignored) {
        salvarMarca();
    }

    private void handleLimparFormulario(ActionEvent ignored) {
        limparFormulario();
    }

    private void onDescricaoChange(ObservableValue<? extends String> ignoredObs,
                                   String ignoredOld, String newText) {
        int length = newText.length();
        if (length > 500) {
            view.getDescricaoArea().setText(newText.substring(0, 500));
            length = 500;
        }
        view.getDescCounterLabel().setText(length + "/500");
    }

    private void onCorPickerChange(ObservableValue<? extends Color> ignoredObs,
                                   Color ignoredOld, Color newVal) {
        // toString() implícito, não mais newVal.toString()
        System.out.println("ColorPicker mudou: " + newVal);
    }

    private void salvarMarca() {
        try {
            if (!validarDados()) return;

            String nome      = view.getNomeField().getText().trim();
            String descricao = view.getDescricaoArea().getText().trim();
            String cor       = formatarCor(view.getCorPicker().getValue());

            marcaDAO.inserirMarca(nome, descricao, cor);
            mostrarSucesso();   // método sem parâmetro para eliminar warning de valor constante

            Stage stage = (Stage) view.getRoot().getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar marca", e);
            mostrarErro("Erro ao salvar marca: " + e.getMessage());
        }
    }

    private String formatarCor(Color color) {
        if (color == null) return "";
        int r = (int) Math.round(color.getRed()   * 255);
        int g = (int) Math.round(color.getGreen() * 255);
        int b = (int) Math.round(color.getBlue()  * 255);
        return String.format("#%02X%02X%02X", r, g, b);
    }

    private boolean validarDados() {
        resetarEstilosErro();
        StringBuilder erros = new StringBuilder();

        if (view.getNomeField().getText().trim().isEmpty()) {
            erros.append("• Nome da marca é obrigatório\n");
            destacarErro(view.getNomeField());
        }
        if (view.getCorPicker().getValue() == null) {
            erros.append("• Cor é obrigatória\n");
            destacarErro(view.getCorPicker());
        }
        if (!erros.isEmpty()) {
            mostrarErro("Corrija os seguintes erros:\n\n" + erros);
            return false;
        }
        return true;
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
        view.getNomeField().setStyle(null);
        view.getDescricaoArea().setStyle(null);
        view.getCorPicker().setStyle(null);
    }

    private void limparFormulario() {
        view.getNomeField().clear();
        view.getDescricaoArea().clear();
        view.getCorPicker().setValue(Color.WHITE);
        resetarEstilosErro();
        view.getRoot().requestFocus();
    }

    // agora sem parâmetro, pois o texto é sempre o mesmo
    private void mostrarSucesso() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("Marca cadastrada com sucesso!");
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
