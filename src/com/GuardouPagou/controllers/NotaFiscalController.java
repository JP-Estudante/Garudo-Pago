package com.GuardouPagou.controllers;

import com.GuardouPagou.views.NotaFiscalView;
import com.GuardouPagou.models.Fatura;
import com.GuardouPagou.models.Marca;
import com.GuardouPagou.models.NotaFiscal;
import com.GuardouPagou.dao.FaturaDAO;
import com.GuardouPagou.dao.MarcaDAO;
import com.GuardouPagou.dao.NotaFiscalDAO;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;

public class NotaFiscalController {
    private final NotaFiscalView view;
    private final NotaFiscalDAO notaFiscalDAO;
    private final FaturaDAO faturaDAO;
    private final MarcaDAO marcaDAO;
    private int contadorFaturas = 0;

    public NotaFiscalController(NotaFiscalView view) {
        this.view = view;
        this.notaFiscalDAO = new NotaFiscalDAO();
        this.faturaDAO = new FaturaDAO();
        this.marcaDAO = new MarcaDAO();
        configurarEventos();
        carregarMarcas();
    }

    private void configurarEventos() {
        view.getAdicionarFaturaButton().setOnAction(e -> adicionarCamposFatura());
        view.getSalvarButton().setOnAction(e -> salvarNotaFiscal());
    }

    private void carregarMarcas() {
        try {
            ObservableList<Marca> marcas = marcaDAO.listarMarcas();
            view.getMarcaComboBox().getItems().clear();
            marcas.forEach(marca -> view.getMarcaComboBox().getItems().add(marca.getNome()));
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar marcas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void adicionarCamposFatura() {
        contadorFaturas++;
        HBox faturaBox = new HBox(10);
        faturaBox.setAlignment(Pos.CENTER_LEFT);
        faturaBox.setPadding(new Insets(5));

        VBox numeroFaturaBox = new VBox(5);
        Label numeroFaturaLabel = new Label("Nº da Fatura*:");
        numeroFaturaLabel.setTextFill(Color.web("#BDBDBD"));
        TextField numeroFaturaField = new TextField(String.valueOf(contadorFaturas));
        numeroFaturaField.setStyle(
            "-fx-background-color: #2A2A2A; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-border-color: #4A4A4A; " +
            "-fx-border-width: 1; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5;"
        );
        numeroFaturaField.setPrefWidth(80);
        numeroFaturaField.setEditable(false);
        numeroFaturaBox.getChildren().addAll(numeroFaturaLabel, numeroFaturaField);

        VBox vencimentoBox = new VBox(5);
        Label vencimentoLabel = new Label("Vencimento*:");
        vencimentoLabel.setTextFill(Color.web("#BDBDBD"));
        DatePicker vencimentoPicker = new DatePicker();
        vencimentoPicker.setStyle(
            "-fx-background-color: #2A2A2A; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-border-color: #4A4A4A; " +
            "-fx-border-width: 1; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5;"
        );
        vencimentoPicker.setPrefWidth(150);
        vencimentoPicker.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                vencimentoPicker.setStyle(
                    "-fx-background-color: #2A2A2A; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-border-color: #F0A818; " +
                    "-fx-border-width: 1; " +
                    "-fx-background-radius: 5; " +
                    "-fx-border-radius: 5;"
                );
            } else {
                vencimentoPicker.setStyle(
                    "-fx-background-color: #2A2A2A; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-border-color: #4A4A4A; " +
                    "-fx-border-width: 1; " +
                    "-fx-background-radius: 5; " +
                    "-fx-border-radius: 5;"
                );
            }
        });
        vencimentoBox.getChildren().addAll(vencimentoLabel, vencimentoPicker);

        VBox valorBox = new VBox(5);
        Label valorLabel = new Label("Valor (R$)*:");
        valorLabel.setTextFill(Color.web("#BDBDBD"));
        TextField valorField = new TextField();
        valorField.setPromptText("Digite o valor");
        valorField.setStyle(
            "-fx-background-color: #2A2A2A; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-border-color: #4A4A4A; " +
            "-fx-border-width: 1; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5;"
        );
        valorField.setPrefWidth(120);
        valorField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                valorField.setStyle(
                    "-fx-background-color: #2A2A2A; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-border-color: #F0A818; " +
                    "-fx-border-width: 1; " +
                    "-fx-background-radius: 5; " +
                    "-fx-border-radius: 5;"
                );
            } else {
                valorField.setStyle(
                    "-fx-background-color: #2A2A2A; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-border-color: #4A4A4A; " +
                    "-fx-border-width: 1; " +
                    "-fx-background-radius: 5; " +
                    "-fx-border-radius: 5;"
                );
            }
        });
        valorBox.getChildren().addAll(valorLabel, valorField);

        Button removerButton = new Button("Remover");
        removerButton.setStyle(
            "-fx-background-color: #F44336; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5;"
        );
        removerButton.setOnMouseEntered(e -> removerButton.setStyle(
            "-fx-background-color: #D32F2F; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5;"
        ));
        removerButton.setOnMouseExited(e -> removerButton.setStyle(
            "-fx-background-color: #F44336; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5;"
        ));
        removerButton.setOnAction(e -> {
            view.getFaturasContainer().getChildren().remove(faturaBox);
            atualizarNumerosFatura();
        });

        faturaBox.getChildren().addAll(numeroFaturaBox, vencimentoBox, valorBox, removerButton);
        view.getFaturasContainer().getChildren().add(faturaBox);
    }

    private void atualizarNumerosFatura() {
        contadorFaturas = 0;
        for (var node : view.getFaturasContainer().getChildren()) {
            if (node instanceof HBox faturaBox) {
                contadorFaturas++;
                TextField numeroFaturaField = (TextField) ((VBox) faturaBox.getChildren().get(0)).getChildren().get(1);
                numeroFaturaField.setText(String.valueOf(contadorFaturas));
            }
        }
    }

    private void salvarNotaFiscal() {
        try {
            if (!validarDados()) {
                return;
            }

            NotaFiscal nota = new NotaFiscal();
            nota.setNumeroNota(view.getNumeroNotaField().getText());
            nota.setDataEmissao(view.getDataEmissaoPicker().getValue());
            nota.setMarca(view.getMarcaComboBox().getValue());

            int notaId = notaFiscalDAO.inserirNotaFiscal(nota);

            if (notaId > 0) {
                List<Fatura> faturas = coletarFaturas();
                faturaDAO.inserirFaturas(faturas, notaId);
                mostrarAlerta("Nota Fiscal gravada com sucesso!", Alert.AlertType.INFORMATION);
                limparFormulario();
            }
        } catch (SQLException e) {
            mostrarAlerta("Erro ao salvar nota fiscal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private List<Fatura> coletarFaturas() {
        List<Fatura> faturas = new ArrayList<>();
        for (var node : view.getFaturasContainer().getChildren()) {
            if (node instanceof HBox faturaBox) {
                TextField numeroFaturaField = (TextField) ((VBox) faturaBox.getChildren().get(0)).getChildren().get(1);
                DatePicker vencimentoPicker = (DatePicker) ((VBox) faturaBox.getChildren().get(1)).getChildren().get(1);
                TextField valorField = (TextField) ((VBox) faturaBox.getChildren().get(2)).getChildren().get(1);

                Fatura fatura = new Fatura();
                fatura.setNumeroFatura(Integer.parseInt(numeroFaturaField.getText()));
                fatura.setVencimento(vencimentoPicker.getValue());
                fatura.setValor(Double.parseDouble(valorField.getText()));
                fatura.setStatus("Não Emitida");
                fatura.setNumeroNota(view.getNumeroNotaField().getText());
                faturas.add(fatura);
            }
        }
        return faturas;
    }

    private boolean validarDados() {
        resetarEstilosErro();
        boolean valido = true;
        StringBuilder erros = new StringBuilder();

        if (view.getNumeroNotaField().getText().trim().isEmpty()) {
            erros.append("• Número da nota é obrigatório\n");
            destacarErro(view.getNumeroNotaField());
            valido = false;
        }

        if (view.getDataEmissaoPicker().getValue() == null) {
            erros.append("• Data de emissão é obrigatória\n");
            destacarErro(view.getDataEmissaoPicker());
            valido = false;
        }

        if (view.getMarcaComboBox().getValue() == null) {
            erros.append("• Selecione uma marca\n");
            destacarErro(view.getMarcaComboBox());
            valido = false;
        }

        if (view.getFaturasContainer().getChildren().isEmpty()) {
            erros.append("• Adicione pelo menos uma fatura\n");
            valido = false;
        } else {
            LocalDate dataEmissao = view.getDataEmissaoPicker().getValue();
            for (var node : view.getFaturasContainer().getChildren()) {
                if (node instanceof HBox faturaBox) {
                    TextField numeroFaturaField = (TextField) ((VBox) faturaBox.getChildren().get(0)).getChildren().get(1);
                    DatePicker vencimentoPicker = (DatePicker) ((VBox) faturaBox.getChildren().get(1)).getChildren().get(1);
                    TextField valorField = (TextField) ((VBox) faturaBox.getChildren().get(2)).getChildren().get(1);

                    if (numeroFaturaField.getText().trim().isEmpty()) {
                        erros.append("• Nº da fatura é obrigatório\n");
                        destacarErro(numeroFaturaField);
                        valido = false;
                    }

                    if (vencimentoPicker.getValue() == null) {
                        erros.append("• Vencimento da fatura é obrigatório\n");
                        destacarErro(vencimentoPicker);
                        valido = false;
                    } else if (dataEmissao != null && vencimentoPicker.getValue().isBefore(dataEmissao)) {
                        erros.append("• Fatura não pode vencer antes da emissão\n");
                        destacarErro(vencimentoPicker);
                        valido = false;
                    }

                    try {
                        double valor = Double.parseDouble(valorField.getText());
                        if (valor <= 0) {
                            erros.append("• Valor da fatura deve ser positivo\n");
                            destacarErro(valorField);
                            valido = false;
                        }
                    } catch (NumberFormatException e) {
                        erros.append("• Valor da fatura inválido\n");
                        destacarErro(valorField);
                        valido = false;
                    }
                }
            }
        }

        if (!valido) {
            mostrarAlerta("Corrija os seguintes erros:\n\n" + erros.toString(), Alert.AlertType.ERROR);
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
        view.getNumeroNotaField().setStyle(
            "-fx-background-color: #2A2A2A; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-border-color: #4A4A4A; " +
            "-fx-border-width: 1; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5;"
        );
        view.getDataEmissaoPicker().setStyle(
            "-fx-background-color: #2A2A2A; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-border-color: #4A4A4A; " +
            "-fx-border-width: 1; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5;"
        );
        view.getMarcaComboBox().setStyle(
            "-fx-background-color: #BDBDBD; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-border-color: #4A4A4A; " +
            "-fx-border-width: 1; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5;"
        );
        for (var node : view.getFaturasContainer().getChildren()) {
            if (node instanceof HBox faturaBox) {
                TextField numeroFaturaField = (TextField) ((VBox) faturaBox.getChildren().get(0)).getChildren().get(1);
                DatePicker vencimentoPicker = (DatePicker) ((VBox) faturaBox.getChildren().get(1)).getChildren().get(1);
                TextField valorField = (TextField) ((VBox) faturaBox.getChildren().get(2)).getChildren().get(1);
                numeroFaturaField.setStyle(
                    "-fx-background-color: #2A2A2A; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-border-color: #4A4A4A; " +
                    "-fx-border-width: 1; " +
                    "-fx-background-radius: 5; " +
                    "-fx-border-radius: 5;"
                );
                vencimentoPicker.setStyle(
                    "-fx-background-color: #2A2A2A; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-border-color: #4A4A4A; " +
                    "-fx-border-width: 1; " +
                    "-fx-background-radius: 5; " +
                    "-fx-border-radius: 5;"
                );
                valorField.setStyle(
                    "-fx-background-color: #2A2A2A; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-border-color: #4A4A4A; " +
                    "-fx-border-width: 1; " +
                    "-fx-background-radius: 5; " +
                    "-fx-border-radius: 5;"
                );
            }
        }
    }

    private void limparFormulario() {
        view.getNumeroNotaField().clear();
        view.getDataEmissaoPicker().setValue(null);
        view.getMarcaComboBox().setValue(null);
        view.getFaturasContainer().getChildren().clear();
        contadorFaturas = 0;
        resetarEstilosErro();
    }

    private void mostrarAlerta(String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(tipo == Alert.AlertType.ERROR ? "Erro" : "Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}