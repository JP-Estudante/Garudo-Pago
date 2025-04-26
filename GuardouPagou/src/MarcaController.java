import javafx.scene.paint.Color;
import java.sql.SQLException;

public class MarcaController {
    private final MarcaView view;
    private final MarcaDAO marcaDAO;
    private static final String COR_PADRAO = "#F0A818";

    public MarcaController(MarcaView view) {
        this.view = view;
        this.marcaDAO = new MarcaDAO();
        configurarEventos();
        configurarVisualizacaoCor();
    }

    private void configurarEventos() {
        view.getGravarButton().setOnAction(e -> gravarMarca());
    }

    private void configurarVisualizacaoCor() {
        view.getCorPicker().valueProperty().addListener((obs, oldColor, newColor) -> {
            String corHex = colorToHex(newColor);
            view.getNomeField().setStyle("-fx-text-fill: " + corHex + ";");
        });
    }

    private void gravarMarca() {
        String nome = view.getNomeField().getText().trim();
        String descricao = view.getDescricaoArea().getText().trim();
        Color cor = view.getCorPicker().getValue();
        
        if (!validarDados(nome)) return;

        try {
            String corHex = colorToHex(cor);
            if (marcaDAO.existeMarca(nome)) {
                view.getMensagemLabel().setText("Marca já cadastrada!");
                return;
            }

            int linhasAfetadas = marcaDAO.inserirMarca(nome, 
                                      descricao.isEmpty() ? null : descricao, 
                                      corHex);
            
            if (linhasAfetadas > 0) {
                view.getMensagemLabel().setText("Marca cadastrada com sucesso!");
                limparCampos();
            } else {
                view.getMensagemLabel().setText("Falha ao cadastrar marca.");
            }
        } catch (SQLException ex) {
            view.getMensagemLabel().setText("Erro no banco de dados.");
            ex.printStackTrace();
        }
    }

    private boolean validarDados(String nome) {
        if (nome.isEmpty()) {
            view.getMensagemLabel().setText("Nome da marca é obrigatório!");
            return false;
        }
        if (nome.length() > 100) {
            view.getMensagemLabel().setText("Nome muito longo (máx. 100 caracteres)");
            return false;
        }
        return true;
    }

    private void limparCampos() {
        view.getNomeField().clear();
        view.getDescricaoArea().clear();
        view.getCorPicker().setValue(Color.web(COR_PADRAO));
    }

    private String colorToHex(Color color) {
        return String.format("#%02X%02X%02X",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255));
    }
}