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
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.function.UnaryOperator;
import javafx.util.StringConverter;

public class NotaFaturaController {

    private final NotaFaturaView view;
    private final NotaFiscalDAO notaFiscalDAO;
    private final FaturaDAO faturaDAO;
    private final MarcaDAO marcaDAO;
    private int contadorFaturas = 1;
    private final List<HBox> faturas = new ArrayList<>();

    public NotaFaturaController(NotaFaturaView view) {
        this.view = view;
        this.notaFiscalDAO = new NotaFiscalDAO();
        this.faturaDAO = new FaturaDAO();
        this.marcaDAO = new MarcaDAO();
        configurarEventos();
        carregarMarcas();
    }

    private void configurarEventos() {
        view.getNumeroNotaField().textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                view.getNumeroNotaField().setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        // Configurar botão Adicionar Nova Fatura
        view.getAdicionarFaturaButton().setPrefWidth(200);
        view.getAdicionarFaturaButton().setStyle(
                "-fx-background-color: #f0a818; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-family: Arial; "
                + "-fx-font-weight: bold; "
                + "-fx-font-size: 14px; "
                + "-fx-background-radius: 5;"
        );
        view.getAdicionarFaturaButton().setOnMouseEntered(e -> view.getAdicionarFaturaButton().setStyle(
                "-fx-background-color: #FFC107; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-family: Arial; "
                + "-fx-font-weight: bold; "
                + "-fx-font-size: 14px; "
                + "-fx-background-radius: 5;"
        ));
        view.getAdicionarFaturaButton().setOnMouseExited(e -> view.getAdicionarFaturaButton().setStyle(
                "-fx-background-color: #f0a818; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-family: Arial; "
                + "-fx-font-weight: bold; "
                + "-fx-font-size: 14px; "
                + "-fx-background-radius: 5;"
        ));
        view.getAdicionarFaturaButton().focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                view.getAdicionarFaturaButton().setStyle(
                        "-fx-background-color: #f0a818; "
                        + "-fx-text-fill: #FFFFFF; "
                        + "-fx-font-family: Arial; "
                        + "-fx-font-weight: bold; "
                        + "-fx-font-size: 14px; "
                        + "-fx-border-color: #F0A818; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5;"
                );
            } else {
                view.getAdicionarFaturaButton().setStyle(
                        "-fx-background-color: #f0a818; "
                        + "-fx-text-fill: #FFFFFF; "
                        + "-fx-font-family: Arial; "
                        + "-fx-font-weight: bold; "
                        + "-fx-font-size: 14px; "
                        + "-fx-background-radius: 5;"
                );
            }
        });
        view.getAdicionarFaturaButton().setOnAction(e -> adicionarCamposFatura());
        view.getSalvarButton().setOnAction(e -> salvarNotaFiscal());
    }

    private void carregarMarcas() {
        try {
            List<String> nomesMarcas = marcaDAO.listarMarcas()
                    .stream()
                    .map(Marca::getNome)
                    .collect(Collectors.toList());
            ComboBox<String> marcaComboBox = view.getMarcaComboBox();
            marcaComboBox.setItems(FXCollections.observableArrayList(nomesMarcas));

            // Configurar cellFactory para estilizar itens da lista suspensa
            marcaComboBox.setCellFactory(listView -> new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        setStyle(
                            "-fx-text-fill: #FFFFFF; "
                            + "-fx-background-color: #2A2A2A; "
                            + "-fx-font-family: Arial; "
                            + "-fx-font-size: 14px;"
                        );
                        // Destacar item ao passar o mouse
                        setOnMouseEntered(e -> setStyle(
                            "-fx-text-fill: #F0A818; "
                            + "-fx-background-color: #3A3A3A; "
                            + "-fx-font-family: Arial; "
                            + "-fx-font-size: 14px;"
                        ));
                        setOnMouseExited(e -> setStyle(
                            "-fx-text-fill: #FFFFFF; "
                            + "-fx-background-color: #2A2A2A; "
                            + "-fx-font-family: Arial; "
                            + "-fx-font-size: 14px;"
                        ));
                    }
                }
            });

            // Configurar buttonCell para estilizar o item selecionado
            marcaComboBox.setButtonCell(new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        setStyle(
                            "-fx-text-fill: #FFFFFF; "
                            + "-fx-background-color: #2A2A2A; "
                            + "-fx-font-family: Arial; "
                            + "-fx-font-size: 14px;"
                        );
                    }
                }
            });

            // Garantir estilo consistente do ComboBox
            marcaComboBox.setStyle(
                "-fx-background-color: #2A2A2A; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-size: 14px; "
                + "-fx-border-color: #4A4A4A; "
                + "-fx-border-width: 1; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5;"
            );
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar marcas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void adicionarCamposFatura() {
        HBox faturaBox = new HBox(15);
        faturaBox.setAlignment(Pos.CENTER_LEFT);

        // Número da Fatura
        VBox numeroFaturaBox = new VBox(5);
        Label numeroFaturaLabel = new Label("Número da Fatura " + contadorFaturas + ":");
        numeroFaturaLabel.setStyle(
                "-fx-font-family: Arial; "
                + "-fx-font-size: 16px; "
                + "-fx-text-fill: #BDBDBD;"
        );
        TextField numeroFaturaField = new TextField(String.valueOf(contadorFaturas));
        numeroFaturaField.setEditable(false);
        numeroFaturaField.setStyle(
                "-fx-background-color: #2A2A2A; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-size: 14px; "
                + "-fx-border-color: #4A4A4A; "
                + "-fx-border-width: 1; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5;"
        );
        numeroFaturaBox.getChildren().addAll(numeroFaturaLabel, numeroFaturaField);
        numeroFaturaBox.setPrefWidth(150);

        // Vencimento
        VBox vencimentoBox = new VBox(5);
        Label vencimentoLabel = new Label("Vencimento*:");
        vencimentoLabel.setStyle(
                "-fx-font-family: Arial; "
                + "-fx-font-size: 16px; "
                + "-fx-text-fill: #BDBDBD;"
        );
        DatePicker vencimentoPicker = new DatePicker();
        // Configurar formato de data brasileiro (dd-MM-yy)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy", new Locale("pt", "BR"));
        vencimentoPicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                try {
                    return LocalDate.parse(string, formatter);
                } catch (Exception e) {
                    return null;
                }
            }
        });
        vencimentoPicker.setStyle(
                "-fx-background-color: #2A2A2A; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-size: 14px; "
                + "-fx-border-color: #4A4A4A; "
                + "-fx-border-width: 1; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5;"
        );
        vencimentoPicker.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                vencimentoPicker.setStyle(
                        "-fx-background-color: #2A2A2A; "
                        + "-fx-text-fill: #FFFFFF; "
                        + "-fx-font-size: 14px; "
                        + "-fx-border-color: #F0A818; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5;"
                );
            } else {
                vencimentoPicker.setStyle(
                        "-fx-background-color: #2A2A2A; "
                        + "-fx-text-fill: #FFFFFF; "
                        + "-fx-font-size: 14px; "
                        + "-fx-border-color: #4A4A4A; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5;"
                );
            }
        });
        vencimentoBox.getChildren().addAll(vencimentoLabel, vencimentoPicker);
        vencimentoBox.setPrefWidth(150);

        // Valor
        VBox valorBox = new VBox(5);
        Label valorLabel = new Label("Valor*:");
        valorLabel.setStyle(
                "-fx-font-family: Arial; "
                + "-fx-font-size: 16px; "
                + "-fx-text-fill: #BDBDBD;"
        );
        TextField valorField = new TextField();
        valorField.setPromptText("Ex.: 150,50");
        valorField.setStyle(
                "-fx-background-color: #2A2A2A; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-size: 14px; "
                + "-fx-border-color: #4A4A4A; "
                + "-fx-border-width: 1; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5; "
                + "-fx-prompt-text-fill: #BDBDBD;"
        );
        valorField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                valorField.setStyle(
                        "-fx-background-color: #2A2A2A; "
                        + "-fx-text-fill: #FFFFFF; "
                        + "-fx-font-size: 14px; "
                        + "-fx-border-color: #F0A818; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5; "
                        + "-fx-prompt-text-fill: #BDBDBD;"
                );
            } else {
                valorField.setStyle(
                        "-fx-background-color: #2A2A2A; "
                        + "-fx-text-fill: #FFFFFF; "
                        + "-fx-font-size: 14px; "
                        + "-fx-border-color: #4A4A4A; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5; "
                        + "-fx-prompt-text-fill: #BDBDBD;"
                );
            }
        });

        // Adicionar TextFormatter para aceitar números decimais com vírgula
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(,\\d{0,2})?")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        valorField.setTextFormatter(textFormatter);

        valorBox.getChildren().addAll(valorLabel, valorField);
        valorBox.setPrefWidth(150);

        // Botão Remover
        VBox removerBox = new VBox(5);
        Label placeholderLabel = new Label("");
        placeholderLabel.setStyle("-fx-font-size: 16px;");
        Button removerButton = new Button("Remover");
        removerButton.setStyle(
                "-fx-background-color: #F44336; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-family: Arial; "
                + "-fx-font-weight: bold; "
                + "-fx-font-size: 14px; "
                + "-fx-background-radius: 5;"
        );
        removerButton.setOnMouseEntered(e -> removerButton.setStyle(
                "-fx-background-color: #D32F2F; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-family: Arial; "
                + "-fx-font-weight: bold; "
                + "-fx-font-size: 14px; "
                + "-fx-background-radius: 5;"
        ));
        removerButton.setOnMouseExited(e -> removerButton.setStyle(
                "-fx-background-color: #F44336; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-family: Arial; "
                + "-fx-font-weight: bold; "
                + "-fx-font-size: 14px; "
                + "-fx-background-radius: 5;"
        ));
        removerButton.setPrefWidth(100);
        removerButton.setOnAction(e -> {
            view.getFaturasContainer().getChildren().remove(faturaBox);
            faturas.remove(faturaBox);
        });
        removerBox.getChildren().addAll(placeholderLabel, removerButton);
        removerBox.setPrefWidth(100);

        faturaBox.getChildren().addAll(numeroFaturaBox, vencimentoBox, valorBox, removerBox);
        view.getFaturasContainer().getChildren().add(faturaBox);
        faturas.add(faturaBox);
        contadorFaturas++;
    }

    private void salvarNotaFiscal() {
        if (!validarDados()) {
            return;
        }

        try (var conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            String numeroNota = view.getNumeroNotaField().getText().trim();
            LocalDate dataEmissao = view.getDataEmissaoPicker().getValue();
            String marca = view.getMarcaComboBox().getValue();

            List<Fatura> listaFaturas = new ArrayList<>();
            for (HBox faturaBox : faturas) {
                TextField numeroFaturaField = (TextField) ((VBox) faturaBox.getChildren().get(0)).getChildren().get(1);
                DatePicker vencimentoPicker = (DatePicker) ((VBox) faturaBox.getChildren().get(1)).getChildren().get(1);
                TextField valorField = (TextField) ((VBox) faturaBox.getChildren().get(2)).getChildren().get(1);

                int numeroFatura = Integer.parseInt(numeroFaturaField.getText());
                LocalDate vencimento = vencimentoPicker.getValue();
                String valorTexto = valorField.getText().trim();
                double valor = converterValor(valorTexto);

                Fatura fatura = new Fatura(numeroFatura, vencimento, valor, "Não Emitida");
                listaFaturas.add(fatura);
            }

            NotaFiscal notaFiscal = new NotaFiscal(numeroNota, dataEmissao, marca, listaFaturas);
            int notaFiscalId = notaFiscalDAO.inserirNotaFiscal(notaFiscal);

            faturaDAO.inserirFaturas(listaFaturas, notaFiscalId);

            conn.commit();
            mostrarAlerta("Nota fiscal e faturas cadastradas com sucesso!", Alert.AlertType.INFORMATION);
            limparFormulario();
        } catch (SQLException e) {
            mostrarAlerta("Erro ao salvar nota fiscal: " + e.getMessage(), Alert.AlertType.ERROR);
            try (var conn = DatabaseConnection.getConnection()) {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private double converterValor(String valorTexto) throws NumberFormatException {
        if (valorTexto.isEmpty()) {
            throw new NumberFormatException("Valor não pode estar vazio");
        }
        String valorFormatado = valorTexto.replace(",", ".");
        return Double.parseDouble(valorFormatado);
    }

    private boolean validarDados() {
        StringBuilder erros = new StringBuilder();
        boolean valido = true;

        String numeroNota = view.getNumeroNotaField().getText().trim();
        LocalDate dataEmissao = view.getDataEmissaoPicker().getValue();
        String marca = view.getMarcaComboBox().getValue();

        if (numeroNota.isEmpty()) {
            erros.append("• Número da nota é obrigatório\n");
            destacarErro(view.getNumeroNotaField());
            valido = false;
        }
        if (dataEmissao == null) {
            erros.append("• Data de emissão é obrigatória\n");
            destacarErro(view.getDataEmissaoPicker());
            valido = false;
        }
        if (marca == null) {
            erros.append("• Marca é obrigatória\n");
            destacarErro(view.getMarcaComboBox());
            valido = false;
        }

        for (HBox faturaBox : faturas) {
            TextField numeroFaturaField = (TextField) ((VBox) faturaBox.getChildren().get(0)).getChildren().get(1);
            DatePicker vencimentoPicker = (DatePicker) ((VBox) faturaBox.getChildren().get(1)).getChildren().get(1);
            TextField valorField = (TextField) ((VBox) faturaBox.getChildren().get(2)).getChildren().get(1);

            if (vencimentoPicker.getValue() == null) {
                erros.append("• Vencimento da fatura ").append(numeroFaturaField.getText()).append(" é obrigatório\n");
                destacarErro(vencimentoPicker);
                valido = false;
            } else if (dataEmissao != null && vencimentoPicker.getValue().isBefore(dataEmissao)) {
                erros.append("• Vencimento da fatura ").append(numeroFaturaField.getText()).append(" deve ser após a data de emissão\n");
                destacarErro(vencimentoPicker);
                valido = false;
            }
            if (valorField.getText().trim().isEmpty()) {
                erros.append("• Valor da fatura ").append(numeroFaturaField.getText()).append(" é obrigatório\n");
                destacarErro(valorField);
                valido = false;
            } else {
                try {
                    converterValor(valorField.getText().trim());
                } catch (NumberFormatException e) {
                    erros.append("• Valor da fatura ").append(numeroFaturaField.getText()).append(" deve ser um número válido (ex.: 123,45)\n");
                    destacarErro(valorField);
                    valido = false;
                }
            }
        }

        if (!valido) {
            mostrarAlerta("Erros no formulário:\n" + erros.toString(), Alert.AlertType.ERROR);
        }
        return valido;
    }

    private void destacarErro(Control control) {
        control.setStyle(control.getStyle() + "-fx-border-color: #FF0000; -fx-border-width: 1.5;");
    }

    private void resetarEstilosErro() {
        view.getNumeroNotaField().setStyle(
                "-fx-background-color: #2A2A2A; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-size: 14px; "
                + "-fx-border-color: #4A4A4A; "
                + "-fx-border-width: 1; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5; "
                + "-fx-prompt-text-fill: #BDBDBD;"
        );
        view.getDataEmissaoPicker().setStyle(
                "-fx-background-color: #2A2A2A; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-size: 14px; "
                + "-fx-border-color: #4A4A4A; "
                + "-fx-border-width: 1; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5;"
        );
        view.getMarcaComboBox().setStyle(
                "-fx-background-color: #2A2A2A; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-size: 14px; "
                + "-fx-border-color: #4A4A4A; "
                + "-fx-border-width: 1; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5;"
        );

        for (var node : view.getFaturasContainer().getChildren()) {
            if (node instanceof HBox faturaBox) {
                TextField numeroFaturaField = (TextField) ((VBox) faturaBox.getChildren().get(0)).getChildren().get(1);
                DatePicker vencimentoPicker = (DatePicker) ((VBox) faturaBox.getChildren().get(1)).getChildren().get(1);
                TextField valorField = (TextField) ((VBox) faturaBox.getChildren().get(2)).getChildren().get(1);

                numeroFaturaField.setStyle(
                        "-fx-background-color: #2A2A2A; "
                        + "-fx-text-fill: #FFFFFF; "
                        + "-fx-font-size: 14px; "
                        + "-fx-border-color: #4A4A4A; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5;"
                );
                vencimentoPicker.setStyle(
                        "-fx-background-color: #2A2A2A; "
                        + "-fx-text-fill: #FFFFFF; "
                        + "-fx-font-size: 14px; "
                        + "-fx-border-color: #4A4A4A; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5;"
                );
                valorField.setStyle(
                        "-fx-background-color: #2A2A2A; "
                        + "-fx-text-fill: #FFFFFF; "
                        + "-fx-font-size: 14px; "
                        + "-fx-border-color: #4A4A4A; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5; "
                        + "-fx-prompt-text-fill: #BDBDBD;"
                );
            }
        }
    }

    private void mostrarAlerta(String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(tipo == Alert.AlertType.ERROR ? "Erro" : "Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void limparFormulario() {
        view.getNumeroNotaField().clear();
        view.getDataEmissaoPicker().setValue(null);
        view.getMarcaComboBox().setValue(null);
        view.getFaturasContainer().getChildren().clear();
        faturas.clear();
        contadorFaturas = 1;
        resetarEstilosErro();
    }
}
