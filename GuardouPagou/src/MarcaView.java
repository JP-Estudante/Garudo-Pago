import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.*;

public class MarcaView {
    private BorderPane root;
    private TextField nomeField;
    private TextArea descricaoArea;
    private ColorPicker corPicker;
    private Button gravarButton;
    private Label mensagemLabel;

    public MarcaView() {
        criarUI();
    }

    private void criarUI() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #BDBDBD; -fx-padding: 20;");

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(20));
        formulario.setStyle("-fx-background-color: #323437; -fx-border-color: #C88200; -fx-border-width: 2;");

        // Título
        Label titulo = new Label("CADASTRO DE MARCA");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web("#F0A818"));
        GridPane.setColumnSpan(titulo, 2);
        formulario.add(titulo, 0, 0);

        // Campo Nome (obrigatório)
        Label nomeLabel = new Label("Nome da Marca*:");
        nomeLabel.setTextFill(Color.web("#BDBDBD"));
        nomeField = new TextField();
        nomeField.setStyle("-fx-background-color: #000000; -fx-text-fill: #BDBDBD;");
        formulario.addRow(1, nomeLabel, nomeField);

        // Campo Descrição (opcional)
        Label descricaoLabel = new Label("Descrição:");
        descricaoLabel.setTextFill(Color.web("#BDBDBD"));
        descricaoArea = new TextArea();
        descricaoArea.setStyle("-fx-background-color: #000000; -fx-text-fill: #BDBDBD;");
        descricaoArea.setPrefRowCount(3);
        GridPane.setColumnSpan(descricaoArea, 2);
        formulario.addRow(2, descricaoLabel);
        formulario.addRow(3, descricaoArea);

        // Seletor de Cor (obrigatório)
        Label corLabel = new Label("Cor da Marca*:");
        corLabel.setTextFill(Color.web("#BDBDBD"));
        corPicker = new ColorPicker(Color.web("#F0A818")); // Cor padrão
        corPicker.setStyle("-fx-background-color: #000000;");
        formulario.addRow(4, corLabel, corPicker);

        // Botão Gravar
        gravarButton = new Button("GRAVAR");
        gravarButton.setStyle("-fx-background-color: #F0A818; -fx-text-fill: #000000; -fx-font-weight: bold;");
        gravarButton.setMaxWidth(Double.MAX_VALUE);
        GridPane.setColumnSpan(gravarButton, 2);
        formulario.addRow(5, gravarButton);

        // Mensagem de status
        mensagemLabel = new Label();
        mensagemLabel.setTextFill(Color.web("#F0A818"));
        GridPane.setColumnSpan(mensagemLabel, 2);
        formulario.addRow(6, mensagemLabel);

        root.setCenter(formulario);
    }

    // Getters
    public BorderPane getRoot() { return root; }
    public TextField getNomeField() { return nomeField; }
    public TextArea getDescricaoArea() { return descricaoArea; }
    public ColorPicker getCorPicker() { return corPicker; }
    public Button getGravarButton() { return gravarButton; }
    public Label getMensagemLabel() { return mensagemLabel; }
}