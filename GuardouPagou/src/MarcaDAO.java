import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class MarcaDAO {
    public boolean existeMarca(String nome) throws SQLException {
        String sql = "SELECT COUNT(*) FROM marcas WHERE nome = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public int inserirMarca(String nome, String descricao, String cor) throws SQLException {
        String sql = "INSERT INTO marcas (nome, descricao, cor) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setString(3, cor);
            return stmt.executeUpdate();
        }
    }

    public ObservableList<Marca> listarMarcas() throws SQLException {
        ObservableList<Marca> marcas = FXCollections.observableArrayList();
        String sql = "SELECT * FROM marcas";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Marca marca = new Marca();
                marca.setId(rs.getInt("id"));
                marca.setNome(rs.getString("nome"));
                marca.setDescricao(rs.getString("descricao"));
                marca.setCor(rs.getString("cor"));
                marcas.add(marca);
            }
        }
        return marcas;
    }
}