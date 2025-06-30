package com.GuardouPagou.controllers;

import com.GuardouPagou.models.Fatura;
import com.GuardouPagou.models.NotaFiscal;
import com.GuardouPagou.views.NotaFiscalDetalhesView;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class NotaFiscalDetalhesController {

    private final NotaFiscalDetalhesView view;
    private final Stage stage;
    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy", PT_BR);
    private static final NumberFormat CURRENCY_FMT = NumberFormat.getCurrencyInstance(PT_BR);

    public NotaFiscalDetalhesController(NotaFiscalDetalhesView view, Stage stage) {
        this.view = view;
        this.stage = stage;
        configurarBotoes();
    }

    private void configurarBotoes() {
        view.getBtnVoltar().setOnAction(e -> stage.close());
        view.getBtnEditar().setOnAction(e -> {
            // placeholder para implementação futura
        });
    }

    public void preencherDados(NotaFiscal nota) {
        view.getNumeroNotaField().setText(nota.getNumeroNota());
        view.getDataEmissaoPicker().setValue(nota.getDataEmissao());
        view.getMarcaComboBox().setItems(FXCollections.observableArrayList(nota.getMarca()));
        view.getMarcaComboBox().getSelectionModel().selectFirst();

        view.getVencimentosColumn().getChildren().clear();
        view.getValoresColumn().getChildren().clear();
        view.getStatusColumn().getChildren().clear();

        int index = 1;
        for (Fatura f : nota.getFaturas()) {
            // --- Vencimento ---
            Label lblSubtituloVenc = new Label("Vencimento Fatura " + index + ":");
            lblSubtituloVenc.getStyleClass().add("field-subtitle");
            Label lv = new Label(DATE_FMT.format(f.getVencimento()));
            VBox vencimentoBox = new VBox(5, lblSubtituloVenc, lv); // VBox com subtítulo e dado
            vencimentoBox.getStyleClass().add("pill-field");
            vencimentoBox.setPadding(new Insets(4, 10, 4, 10));
            view.getVencimentosColumn().getChildren().add(vencimentoBox);

            // --- Valor ---
            Label lblSubtituloValor = new Label("Valor Fatura " + index + ":");
            lblSubtituloValor.getStyleClass().add("field-subtitle");
            Label val = new Label(CURRENCY_FMT.format(f.getValor()));
            VBox valorBox = new VBox(5, lblSubtituloValor, val);
            valorBox.getStyleClass().add("pill-field");
            valorBox.setPadding(new Insets(4, 10, 4, 10));
            view.getValoresColumn().getChildren().add(valorBox);

            // --- Status ---
            Label lblSubtituloStatus = new Label("Status Fatura " + index + ":");
            lblSubtituloStatus.getStyleClass().add("field-subtitle");
            Label st = new Label(f.getStatus());
            VBox statusBox = new VBox(5, lblSubtituloStatus, st);
            statusBox.getStyleClass().add("pill-field");
            statusBox.setPadding(new Insets(4, 10, 4, 10));
            view.getStatusColumn().getChildren().add(statusBox);

            index++;
        }
    }
}
