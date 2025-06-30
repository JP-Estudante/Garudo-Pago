package com.GuardouPagou.controllers;

import com.GuardouPagou.models.Fatura;
import com.GuardouPagou.models.NotaFiscal;
import com.GuardouPagou.views.NotaFiscalDetalhesView;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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
            Label lv = new Label(DATE_FMT.format(f.getVencimento()));
            lv.getStyleClass().add("field-subtitle");
            view.getVencimentosColumn().getChildren().add(lv);

            Label val = new Label(CURRENCY_FMT.format(f.getValor()));
            val.getStyleClass().add("field-subtitle");
            view.getValoresColumn().getChildren().add(val);

            Label st = new Label(f.getStatus());
            st.getStyleClass().add("field-subtitle");
            view.getStatusColumn().getChildren().add(st);
            index++;
        }
    }
}
