package com.GuardouPagou.controllers;

import com.GuardouPagou.dao.FaturaDAO;
import com.GuardouPagou.dao.MarcaDAO;
import com.GuardouPagou.dao.NotaFiscalDAO;
import com.GuardouPagou.models.DatabaseConnection;
import com.GuardouPagou.models.Fatura;
import com.GuardouPagou.models.Marca;
import com.GuardouPagou.models.NotaFiscal;
import com.GuardouPagou.views.NotaFaturaView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotaFaturaController {

    private static final Logger LOGGER = Logger.getLogger(NotaFaturaController.class.getName());

    private final NotaFaturaView view;
    private final NotaFiscalDAO   notaFiscalDAO;
    private final FaturaDAO       faturaDAO;
    private final MarcaDAO        marcaDAO;

    // utilizar Locale.forLanguageTag para evitar construtor deprecated
    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy", PT_BR);
    private static final NumberFormat CURRENCY_FORMAT =
            NumberFormat.getCurrencyInstance(PT_BR);

    private final List<DatePicker> vencimentoPickers = new ArrayList<>();
    private final List<TextField>  valorFields        = new ArrayList<>();

    public NotaFaturaController(NotaFaturaView view) {
        this.view         = view;
        this.notaFiscalDAO = new NotaFiscalDAO();
        this.faturaDAO     = new FaturaDAO();
        this.marcaDAO      = new MarcaDAO();

        configurarEventos();
        carregarMarcas();
        inicializarFaturas(view.getSpinnerFaturas().getValue());
    }

    private void configurarEventos() {
        // só dígitos no n.º da nota, renomeando parâmetros não usados
        view.getNumeroNotaField().textProperty()
                .addListener((ignoredObs, ignoredOld, newVal) -> {
                    if (!newVal.matches("\\d*")) {
                        view.getNumeroNotaField()
                                .setText(newVal.replaceAll("\\D", ""));
                    }
                });

        // recria campos ao mudar nº de faturas
        view.getSpinnerFaturas().valueProperty()
                .addListener((ignoredObs, ignoredOld, newVal) ->
                        inicializarFaturas(newVal)
                );

        // extrair método para onAction evita warning de 'e' não usado
        view.getBtnLimpar().setOnAction(this::onLimpar);
        view.getBtnGravar().setOnAction(this::onGravar);
    }

    private void onLimpar(ActionEvent ignored) {
        limparFormulario();
    }

    private void onGravar(ActionEvent ignored) {
        salvarNotaFiscal();
    }

    private void carregarMarcas() {
        try {
            // method reference em vez de lambda em map(...)
            var nomes = marcaDAO.listarMarcas().stream()
                    .map(Marca::getNome)
                    .toList();

            view.getMarcaComboBox()
                    .setItems(FXCollections.observableArrayList(nomes));
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar marcas", ex);
            mostrarAlerta(
                    "Erro ao carregar marcas: " + ex.getMessage(),
                    Alert.AlertType.ERROR
            );
        }
    }

    private void inicializarFaturas(int quantidade) {
        vencimentoPickers.clear();
        valorFields.clear();
        view.getVencimentosColumn().getChildren().clear();
        view.getValoresColumn().getChildren().clear();

        for (int i = 1; i <= quantidade; i++) {
            // extrai criação de DatePicker para método
            DatePicker dp = criarDatePicker();
            adicionarVencimentoField(i, dp);
            vencimentoPickers.add(dp);

            // extrai criação de TextField para método
            TextField tf = criarValorField();
            adicionarValorField(i, tf);
            valorFields.add(tf);
        }
    }

    private DatePicker criarDatePicker() {
        DatePicker dp = new DatePicker();
        dp.setPromptText("DD/MM/AAAA");
        dp.setPrefWidth(150);

        // usar diamond <> para StringConverter
        dp.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date == null ? "" : FORMATTER.format(date);
            }
            @Override
            public LocalDate fromString(String text) {
                try {
                    return (text == null || text.isBlank())
                            ? null
                            : LocalDate.parse(text, FORMATTER);
                } catch (Exception ex) {
                    return null;
                }
            }
        });
        return dp;
    }

    private void adicionarVencimentoField(int index, DatePicker dp) {
        VBox vb = new VBox(5);
        vb.getStyleClass().add("pill-field");
        Label lbl = new Label("Vencimento Fatura " + index + ":");
        lbl.getStyleClass().add("field-subtitle");
        vb.getChildren().addAll(lbl, dp);
        view.getVencimentosColumn().getChildren().add(vb);
    }

    private TextField criarValorField() {
        TextField tf = new TextField(CURRENCY_FORMAT.format(BigDecimal.ZERO));
        tf.setPrefWidth(150);
        final boolean[] updating = { false };

        tf.textProperty().addListener((ignoredObs, ignoredOld, newText) -> {
            if (updating[0]) return;
            updating[0] = true;

            String digits = newText.replaceAll("\\D", "");
            long cents = digits.isEmpty() ? 0L : Long.parseLong(digits);
            BigDecimal value = BigDecimal.valueOf(cents, 2);

            String formatted = CURRENCY_FORMAT.format(value);
            tf.setText(formatted);
            tf.positionCaret(formatted.length());

            updating[0] = false;
        });
        return tf;
    }

    private void adicionarValorField(int index, TextField tf) {
        VBox vb = new VBox(5);
        vb.getStyleClass().add("pill-field");
        Label lbl = new Label("Valor Fatura " + index + ":");
        lbl.getStyleClass().add("field-subtitle");
        vb.getChildren().addAll(lbl, tf);
        view.getValoresColumn().getChildren().add(vb);
    }

    private void salvarNotaFiscal() {
        String numNota = view.getNumeroNotaField().getText().trim();
        LocalDate data  = view.getDataEmissaoPicker().getValue();
        String marca    = view.getMarcaComboBox().getValue();

        if (numNota.isEmpty() || data == null || marca == null) {
            mostrarAlerta(
                    "Erro ao cadastrar, verifique os campos!",
                    Alert.AlertType.ERROR
            );
            return;
        }

        for (int i = 0; i < vencimentoPickers.size(); i++) {
            LocalDate venc = vencimentoPickers.get(i).getValue();
            String valTxt  = valorFields.get(i).getText().trim();
            if (venc == null || venc.isBefore(data) || valTxt.isEmpty()) {
                mostrarAlerta(
                        "Erro ao cadastrar, verifique os campos!",
                        Alert.AlertType.ERROR
                );
                return;
            }
        }

        try (var conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            NotaFiscal nf = new NotaFiscal(numNota, data, marca, new ArrayList<>());
            int nfId = notaFiscalDAO.inserirNotaFiscal(nf);

            List<Fatura> lista = new ArrayList<>();
            for (int i = 0; i < vencimentoPickers.size(); i++) {
                LocalDate venc = vencimentoPickers.get(i).getValue();
                String text = valorFields.get(i).getText();
                String clean = text.replaceAll("[^\\d,]", "").replace(",", ".");
                double valor = Double.parseDouble(clean);

                lista.add(new Fatura(
                        i + 1,
                        venc,
                        valor,
                        "Não Emitida"
                ));
            }

            faturaDAO.inserirFaturas(lista, nfId);
            conn.commit();

            mostrarAlerta(
                    "Faturas cadastradas com sucesso!",
                    Alert.AlertType.INFORMATION
            );
            limparFormulario();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar nota fiscal", e);
            try (var conn = DatabaseConnection.getConnection()) {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Erro no rollback", ex);
            }
            mostrarAlerta(
                    "Erro ao salvar nota fiscal: " + e.getMessage(),
                    Alert.AlertType.ERROR
            );
        }
    }

    private void limparFormulario() {
        view.getNumeroNotaField().clear();
        view.getDataEmissaoPicker().setValue(null);
        view.getMarcaComboBox().setValue(null);
        view.getSpinnerFaturas().getValueFactory().setValue(1);

        vencimentoPickers.clear();
        valorFields.clear();
        view.getVencimentosColumn().getChildren().clear();
        view.getValoresColumn().getChildren().clear();
        inicializarFaturas(1);
    }

    private void mostrarAlerta(String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
