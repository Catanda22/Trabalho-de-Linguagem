package dao;

import config.Conexao;
import model.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO — acesso a dados da entidade Paciente.
 */
public class PacienteDAO {

    public void inserir(Paciente p) throws SQLException {
        String sql =
            "INSERT INTO pacientes " +
            "(nome, bi, data_nascimento, sexo, telefone, email, bairro, cidade, historico) " +
            "VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            definirParametros(ps, p);
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) p.setId(gk.getInt(1));
            }
        }
    }

    public void atualizar(Paciente p) throws SQLException {
        String sql =
            "UPDATE pacientes SET " +
            "nome=?, bi=?, data_nascimento=?, sexo=?, telefone=?, email=?, " +
            "bairro=?, cidade=?, historico=? " +
            "WHERE id=?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            definirParametros(ps, p);
            ps.setInt(10, p.getId());
            ps.executeUpdate();
        }
    }

    public void inativar(int id) throws SQLException {
        String sql = "UPDATE pacientes SET ativo = 0 WHERE id = ?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Optional<Paciente> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public List<Paciente> buscarPorNomeOuBi(String filtro) throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        String like   = "%" + filtro + "%";
        String biLike = "%" + filtro.trim().toUpperCase() + "%";
        String sql = "SELECT * FROM pacientes WHERE ativo=1 AND (nome LIKE ? OR bi LIKE ?) ORDER BY nome";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, biLike);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Paciente> listarAtivos() throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacientes WHERE ativo=1 ORDER BY nome";
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Paciente> listarTodos() throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacientes ORDER BY nome";
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    // ── helpers ────────────────────────────────────────────────────────────

    private void definirParametros(PreparedStatement ps, Paciente p) throws SQLException {
        ps.setString(1, p.getNome());
        ps.setString(2, p.getBi());
        ps.setDate(3,   Date.valueOf(p.getDataNascimento()));
        ps.setString(4, String.valueOf(p.getSexo()));
        ps.setString(5, p.getTelefone());
        ps.setString(6, p.getEmail());
        ps.setString(7, p.getBairro());
        ps.setString(8, p.getCidade());
        ps.setString(9, p.getHistorico());
    }

    private Paciente mapear(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setBi(rs.getString("bi"));
        p.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        String sexo = rs.getString("sexo");
        p.setSexo(sexo != null && !sexo.isEmpty() ? sexo.charAt(0) : 'O');
        p.setTelefone(rs.getString("telefone"));
        p.setEmail(rs.getString("email"));
        p.setBairro(rs.getString("bairro"));
        p.setCidade(rs.getString("cidade"));
        p.setHistorico(rs.getString("historico"));
        p.setAtivo(rs.getBoolean("ativo"));
        Timestamp ts = rs.getTimestamp("criado_em");
        if (ts != null) p.setCriadoEm(ts.toLocalDateTime());
        return p;
    }
}