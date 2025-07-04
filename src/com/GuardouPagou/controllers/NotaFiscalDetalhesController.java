package com.GuardouPagou.controllers;

import com.GuardouPagou.models.Fatura;
import com.GuardouPagou.models.NotaFiscal;
import com.GuardouPagou.views.NotaFiscalDetalhesView;
import com.GuardouPagou.dao.NotaFiscalDAO;
import com.GuardouPagou.dao.MarcaDAO;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class NotaFiscalDetalhesController {

    private final NotaFiscalDetalhesView view;
    private final Stage stage;
    private NotaFiscal nota;
    private String numeroNotaOriginal;
    private boolean editMode = false;
    private java.util.List<javafx.scene.control.DatePicker> dpVencimentos;
    private java.util.List<javafx.scene.control.TextField> tfValores;
    private java.util.List<javafx.scene.control.ComboBox<String>> cbStatus;
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
        view.getBtnEditar().setOnAction(e -> alternarEdicaoOuSalvar());
    }

    public void preencherDados(NotaFiscal nota) {
        this.nota = nota;
        this.numeroNotaOriginal = nota.getNumeroNota();
        view.getNumeroNotaField().setText(nota.getNumeroNota());
        view.getDataEmissaoPicker().setValue(nota.getDataEmissao());

        try {
            var marcas = new MarcaDAO().listarMarcas().stream()
                    .map(com.GuardouPagou.models.Marca::getNome)
                    .toList();
            view.getMarcaComboBox().setItems(FXCollections.observableArrayList(marcas));
        } catch (Exception ex) {
            view.getMarcaComboBox().setItems(FXCollections.observableArrayList(nota.getMarca()));
        }
        view.getMarcaComboBox().getSelectionModel().select(nota.getMarca());

        mostrarCamposSomenteLeitura();
    }

    private void alternarEdicaoOuSalvar() {
        if (!editMode) {
            habilitarEdicao(true);
            trocarParaSalvar();
            editMode = true;
        } else {
            salvarAlteracoes();
            habilitarEdicao(false);
            trocarParaEditar();
            editMode = false;
        }
    }

    private void habilitarEdicao(boolean enable) {
        view.getNumeroNotaField().setEditable(enable);
        view.getDataEmissaoPicker().setDisable(!enable);
        view.getMarcaComboBox().setDisable(!enable);

        if (enable) {
            mostrarCamposEdicao();
        } else {
            mostrarCamposSomenteLeitura();
        }
    }

    private void trocarParaSalvar() {
        view.getBtnEditar().setText("Salvar");
        javafx.scene.image.ImageView icon = new javafx.scene.image.ImageView(new javafx.scene.image.Image(
                java.util.Objects.requireNonNull(getClass().getResourceAsStream("/icons/save.png"))
        ));
        icon.setFitHeight(20); icon.setFitWidth(20);
        view.getBtnEditar().setGraphic(icon);
    }

    private void trocarParaEditar() {
        view.getBtnEditar().setText("Editar");
        javafx.scene.image.ImageView icon = new javafx.scene.image.ImageView(new javafx.scene.image.Image(
                java.util.Objects.requireNonNull(getClass().getResourceAsStream("/icons/edit.png"))
        ));
        icon.setFitHeight(20); icon.setFitWidth(20);
        view.getBtnEditar().setGraphic(icon);
    }

    private void salvarAlteracoes() {
        NotaFiscal nova = new NotaFiscal();
        nova.setNumeroNota(view.getNumeroNotaField().getText());
        nova.setDataEmissao(view.getDataEmissaoPicker().getValue());
        nova.setMarca(view.getMarcaComboBox().getValue());
        try {
            new NotaFiscalDAO().atualizarNotaFiscal(numeroNotaOriginal, nova);

            // Atualiza cada fatura conforme os controles de edição
            if (dpVencimentos != null && cbStatus != null && tfValores != null) {
                for (int i = 0; i < nota.getFaturas().size(); i++) {
                    Fatura f = nota.getFaturas().get(i);
                    f.setVencimento(dpVencimentos.get(i).getValue());
                    String txt = tfValores.get(i).getText();
                    String clean = txt.replaceAll("[^\\d,]", "").replace(",", ".");
                    f.setValor(clean.isEmpty() ? 0.0 : Double.parseDouble(clean));
                    f.setStatus(cbStatus.get(i).getValue());
                    new com.GuardouPagou.dao.FaturaDAO().atualizarFatura(f);
                }
            }

            // Atualiza objeto existente para manter lista de faturas em memória
            nota.setNumeroNota(nova.getNumeroNota());
            nota.setDataEmissao(nova.getDataEmissao());
            nota.setMarca(nova.getMarca());
            numeroNotaOriginal = nova.getNumeroNota();
            mostrarCamposSomenteLeitura();
        } catch (Exception ex) {
            javafx.scene.control.Alert a = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR,
                    "Erro ao salvar: " + ex.getMessage());
            a.showAndWait();
        }
    }

    private void mostrarCamposSomenteLeitura() {
        view.getVencimentosColumn().getChildren().clear();
        view.getValoresColumn().getChildren().clear();
        view.getStatusColumn().getChildren().clear();

        int index = 1;
        for (Fatura f : nota.getFaturas()) {
            Label lblSubtituloVenc = new Label("Vencimento Fatura " + index + ":");
            lblSubtituloVenc.getStyleClass().add("field-subtitle");
            javafx.scene.control.DatePicker dp = new javafx.scene.control.DatePicker(f.getVencimento());
            dp.setDisable(true);
            dp.getStyleClass().addAll("read-only-field", "cursor-unavailable");
            VBox vencimentoBox = new VBox(5, lblSubtituloVenc, dp);
            vencimentoBox.getStyleClass().add("pill-field");
            vencimentoBox.setPadding(new Insets(4, 10, 4, 10));
            view.getVencimentosColumn().getChildren().add(vencimentoBox);

            Label lblSubtituloValor = new Label("Valor Fatura " + index + ":");
            lblSubtituloValor.getStyleClass().add("field-subtitle");
            TextField tf = new TextField(CURRENCY_FMT.format(f.getValor()));
            tf.setEditable(false);
            tf.getStyleClass().addAll("read-only-field", "cursor-unavailable");
            VBox valorBox = new VBox(5, lblSubtituloValor, tf);
            valorBox.getStyleClass().add("pill-field");
            valorBox.setPadding(new Insets(4, 10, 4, 10));
            view.getValoresColumn().getChildren().add(valorBox);

            Label lblSubtituloStatus = new Label("Status Fatura " + index + ":");
            lblSubtituloStatus.getStyleClass().add("field-subtitle");
            javafx.scene.control.ComboBox<String> cb = new javafx.scene.control.ComboBox<>();
            cb.getItems().addAll("Não Emitida", "Emitida");
            cb.setValue(f.getStatus());
            cb.setDisable(true);
            cb.getStyleClass().add("cursor-unavailable");
            VBox statusBox = new VBox(5, lblSubtituloStatus, cb);
            statusBox.getStyleClass().add("pill-field");
            statusBox.setPadding(new Insets(4, 10, 4, 10));
            view.getStatusColumn().getChildren().add(statusBox);

            index++;
        }
    }

    private void mostrarCamposEdicao() {
        dpVencimentos = new java.util.ArrayList<>();
        tfValores = new java.util.ArrayList<>();
        cbStatus = new java.util.ArrayList<>();
        view.getVencimentosColumn().getChildren().clear();
        view.getValoresColumn().getChildren().clear();
        view.getStatusColumn().getChildren().clear();

        int idx = 1;
        for (Fatura f : nota.getFaturas()) {
            Label lblVen = new Label("Vencimento Fatura " + idx + ":");
            lblVen.getStyleClass().add("field-subtitle");
            javafx.scene.control.DatePicker dp = new javafx.scene.control.DatePicker(f.getVencimento());
            VBox boxVen = new VBox(5, lblVen, dp);
            boxVen.getStyleClass().add("pill-field");
            boxVen.setPadding(new Insets(4, 10, 4, 10));
            view.getVencimentosColumn().getChildren().add(boxVen);
            dpVencimentos.add(dp);

            Label lblVal = new Label("Valor Fatura " + idx + ":");
            lblVal.getStyleClass().add("field-subtitle");
            TextField tf = criarValorField(f.getValor());
            VBox boxVal = new VBox(5, lblVal, tf);
            boxVal.getStyleClass().add("pill-field");
            boxVal.setPadding(new Insets(4, 10, 4, 10));
            view.getValoresColumn().getChildren().add(boxVal);
            tfValores.add(tf);

            Label lblSt = new Label("Status Fatura " + idx + ":");
            lblSt.getStyleClass().add("field-subtitle");
            javafx.scene.control.ComboBox<String> cb = new javafx.scene.control.ComboBox<>();
            cb.getItems().addAll("Não Emitida", "Emitida");
            cb.setValue(f.getStatus());
            VBox boxSt = new VBox(5, lblSt, cb);
            boxSt.getStyleClass().add("pill-field");
            boxSt.setPadding(new Insets(4, 10, 4, 10));
            view.getStatusColumn().getChildren().add(boxSt);
            cbStatus.add(cb);

            idx++;
        }
    }

    private TextField criarValorField(double valorInicial) {
        TextField tf = new TextField(CURRENCY_FMT.format(valorInicial));
        tf.setPrefWidth(150);
        final boolean[] updating = { false };

        tf.textProperty().addListener((obs, oldText, newText) -> {
            if (updating[0]) return;
            updating[0] = true;

            String digits = newText.replaceAll("\\D", "");
            long cents = digits.isEmpty() ? 0L : Long.parseLong(digits);
            BigDecimal value = BigDecimal.valueOf(cents, 2);

            String formatted = CURRENCY_FMT.format(value);
            tf.setText(formatted);
            tf.positionCaret(formatted.length());

            updating[0] = false;
        });

        return tf;
    }
}
