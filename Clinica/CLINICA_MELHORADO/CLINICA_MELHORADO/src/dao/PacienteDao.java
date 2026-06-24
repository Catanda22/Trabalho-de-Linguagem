package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.Paciente;

public class PacienteDao {

    public void Salvar(Paciente p) {
        String sql = "INSERT INTO paciente(nome, bi, sexo, data_nascimento, endereco, telefone, historico_medico, ativo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexao.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, valorObrigatorio(p.getBi(), gerarBiTemporario()));
            stmt.setString(3, p.getSexo());
            stmt.setDate(4, dataObrigatoria(p.getDataNascimento()));
            stmt.setString(5, p.getEndereco());
            stmt.setString(6, p.getTelefone());
            stmt.setString(7, p.getHistoricoMedico());
            stmt.setBoolean(8, p.isAtivo());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId_paciente(rs.getInt(1));
                }
            }

            JOptionPane.showMessageDialog(null, "Paciente cadastrado com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }

    public void atualizar(Paciente p) {
        String sql = "UPDATE paciente SET nome = ?, bi = ?, sexo = ?, data_nascimento = ?, telefone = ?, "
                + "endereco = ?, historico_medico = ?, ativo = ? WHERE id_paciente = ?";

        try (Connection con = Conexao.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, valorObrigatorio(p.getBi(), gerarBiTemporario()));
            stmt.setString(3, p.getSexo());
            stmt.setDate(4, dataObrigatoria(p.getDataNascimento()));
            stmt.setString(5, p.getTelefone());
            stmt.setString(6, p.getEndereco());
            stmt.setString(7, p.getHistoricoMedico());
            stmt.setBoolean(8, p.isAtivo());
            stmt.setInt(9, p.getId_paciente());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Paciente atualizado!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar: " + e.getMessage());
        }
    }

    public void deletar(int id_paciente) {
        String sql = "UPDATE paciente SET ativo = FALSE WHERE id_paciente = ?";

        try (Connection con = Conexao.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id_paciente);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Paciente inativado!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar: " + e.getMessage());
        }
    }

    public List<Paciente> pesquisar(String termo) throws Exception {
        String sql = "SELECT * FROM paciente WHERE ativo = TRUE AND (nome LIKE ? OR bi LIKE ?) ORDER BY nome";
        List<Paciente> pacientes = new ArrayList<>();

        try (Connection con = Conexao.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            String filtro = "%" + (termo == null ? "" : termo.trim()) + "%";
            stmt.setString(1, filtro);
            stmt.setString(2, filtro);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pacientes.add(mapearPaciente(rs));
                }
            }
        }

        return pacientes;
    }

    public Paciente buscarPorId(int idPaciente) throws Exception {
        String sql = "SELECT * FROM paciente WHERE id_paciente = ?";

        try (Connection con = Conexao.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idPaciente);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapearPaciente(rs) : null;
            }
        }
    }

    private Paciente mapearPaciente(ResultSet rs) throws Exception {
        Paciente p = new Paciente();
        p.setId_paciente(rs.getInt("id_paciente"));
        p.setNome(rs.getString("nome"));
        p.setBi(rs.getString("bi"));
        p.setSexo(rs.getString("sexo"));
        p.setDataNascimento(rs.getDate("data_nascimento"));
        p.setEndereco(rs.getString("endereco"));
        p.setTelefone(rs.getString("telefone"));
        p.setHistoricoMedico(rs.getString("historico_medico"));
        p.setAtivo(rs.getBoolean("ativo"));
        return p;
    }

    private Date dataObrigatoria(Date data) {
        return data == null ? Date.valueOf("1900-01-01") : data;
    }

    private String valorObrigatorio(String valor, String padrao) {
        return valor == null || valor.trim().isEmpty() ? padrao : valor.trim();
    }

    private String gerarBiTemporario() {
        return "SEM-BI-" + System.currentTimeMillis();
    }
}
