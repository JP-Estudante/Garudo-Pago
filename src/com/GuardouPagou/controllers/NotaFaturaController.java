package com.GuardouPagou.controllers;

import com.GuardouPagou.dao.FaturaDAO;
import com.GuardouPagou.dao.MarcaDAO;
import com.GuardouPagou.dao.NotaFiscalDAO;
import com.GuardouPagou.models.DatabaseConnection;
import com.GuardouPagou.models.Fatura;
import com.GuardouPagou.models.NotaFiscal;
import com.GuardouPagou.views.NotaFaturaView;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NotaFaturaController {

    private final NotaFaturaView view;
    private final NotaFiscalDAO notaFiscalDAO;
    private final FaturaDAO faturaDAO;
    private final MarcaDAO marcaDAO;

    // Para armazenar dinamicamente os campos de vencimento e valor
    private final List<DatePicker> vencimentoPickers = new ArrayList<>();
    private final List<TextField> valorFields      = new ArrayList<>();

    // Formatação de data
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("pt", "BR"));

    public NotaFaturaController(NotaFaturaView view) {
        this.view            = view;
        this.notaFiscalDAO   = new NotaFiscalDAO();
        this.faturaDAO       = new FaturaDAO();
        this.marcaDAO        = new MarcaDAO();

        configurarEventos();
        carregarMarcas();
        inicializarFaturas(view.getSpinnerFaturas().getValue());
    }

    private void configurarEventos() {
        // Só dígitos no nº da nota
        view.getNumeroNotaField().textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                view.getNumeroNotaField().setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        // Quando mudar o nº de faturas, recria os campos
        view.getSpinnerFaturas().valueProperty().addListener((obs, oldVal, newVal) -> {
            inicializarFaturas(newVal);
        });

        // Cancelar limpa o formulário
        view.getBtnLimpar().setOnAction(e -> limparFormulario());

        // Gravar tenta salvar
        view.getBtnGravar().setOnAction(e -> salvarNotaFiscal());
    }

    private void carregarMarcas() {
        try {
            List<String> nomes = marcaDAO.listarMarcas()
                                        .stream()
                                        .map(m -> m.getNome())
                                        .toList();
            ComboBox<String> cb = view.getMarcaComboBox();
            cb.setItems(FXCollections.observableArrayList(nomes));
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar marcas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void inicializarFaturas(int quantidade) {
        // limpa tudo
        vencimentoPickers.clear();
        valorFields.clear();
        view.getVencimentosColumn().getChildren().clear();
        view.getValoresColumn().getChildren().clear();

        for (int i = 1; i <= quantidade; i++) {
            // --- Vencimento ---
            VBox vbV = new VBox(5);
            vbV.setStyle(
                "-fx-background-color: #BDBDBD;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 10;"
            );
            Label lblV = new Label("Vencimento Fatura " + i + ":");
            lblV.setTextFill(Color.web("#323437"));
            lblV.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

            DatePicker dp = new DatePicker();
            dp.setPromptText("DD/MM/AAAA");
            dp.setConverter(new javafx.util.StringConverter<LocalDate>() {
                @Override
                public String toString(LocalDate date) {
                    return date == null ? "" : formatter.format(date);
                }
                @Override
                public LocalDate fromString(String str) {
                    try { return str==null||str.isBlank() ? null : LocalDate.parse(str, formatter); }
                    catch (Exception ex) { return null; }
                }
            });
            dp.setStyle("-fx-background-color: transparent; -fx-text-fill: #323437; -fx-prompt-text-fill: #323437; -fx-border-width:0;");
            dp.setPrefWidth(150);

            vencimentoPickers.add(dp);
            vbV.getChildren().addAll(lblV, dp);
            view.getVencimentosColumn().getChildren().add(vbV);

            // --- Valor ---
            VBox vbVal = new VBox(5);
            vbVal.setStyle(
                "-fx-background-color: #BDBDBD;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 10;"
            );
            Label lblVal = new Label("Valor Fatura " + i + ":");
            lblVal.setTextFill(Color.web("#323437"));
            lblVal.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

            TextField tf = new TextField();
            tf.setPromptText("R$");
            tf.setStyle("-fx-background-color: transparent; -fx-text-fill: #323437; -fx-prompt-text-fill: #323437; -fx-border-width:0;");
            tf.setPrefWidth(150);
            // somente números e até 2 decimais
            tf.setTextFormatter(new TextFormatter<>(change -> {
                String s = change.getControlNewText();
                return s.matches("\\d*(,\\d{0,2})?") ? change : null;
            }));

            valorFields.add(tf);
            vbVal.getChildren().addAll(lblVal, tf);
            view.getValoresColumn().getChildren().add(vbVal);
        }
    }

    private void salvarNotaFiscal() {
        // valida campos principais
        String numNota = view.getNumeroNotaField().getText().trim();
        LocalDate data   = view.getDataEmissaoPicker().getValue();
        String marca     = view.getMarcaComboBox().getValue();

        if (numNota.isEmpty() || data==null || marca==null || vencimentoPickers.isEmpty()) {
            mostrarAlerta("Erro ao cadastrar, verifique os campos!", Alert.AlertType.ERROR);
            return;
        }

        // valida cada fatura
        for (int i = 0; i < vencimentoPickers.size(); i++) {
            LocalDate venc = vencimentoPickers.get(i).getValue();
            String  valTxt = valorFields.get(i).getText().trim();
            if (venc==null || venc.isBefore(data) || valTxt.isEmpty()) {
                mostrarAlerta("Erro ao cadastrar, verifique os campos!", Alert.AlertType.ERROR);
                return;
            }
        }

        // persiste em transação
        try (var conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            NotaFiscal nf = new NotaFiscal(numNota, data, marca, new ArrayList<>());
            int nfId = notaFiscalDAO.inserirNotaFiscal(nf);

            // cria lista de Fatura e insere
            var lista = new ArrayList<Fatura>();
            for (int i = 0; i < vencimentoPickers.size(); i++) {
                double valor = Double.parseDouble(
                    valorFields.get(i).getText().replace(",", ".")
                );
                lista.add(new Fatura(i+1, vencimentoPickers.get(i).getValue(), valor, "Não Emitida"));
            }
            faturaDAO.inserirFaturas(lista, nfId);

            conn.commit();
            mostrarAlerta("Faturas cadastradas com sucesso!", Alert.AlertType.INFORMATION);
            limparFormulario();

        } catch (Exception e) {
            mostrarAlerta("Erro ao salvar nota fiscal: " + e.getMessage(), Alert.AlertType.ERROR);
            try { DatabaseConnection.getConnection().rollback(); }
            catch (SQLException ex) { ex.printStackTrace(); }
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
        Alert a = new Alert(tipo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
