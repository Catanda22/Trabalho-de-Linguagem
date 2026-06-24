package dao;

import config.Conexao;
import model.Medico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO — acesso a dados da entidade Medico.
 */
public class MedicoDAO {

    public void inserir(Medico m) throws SQLException {
        String sql = "INSERT INTO medicos (usuario_id,nome,crm,especialidade,telefone,email) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(ps, m);
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) m.setId(gk.getInt(1));
            }
        }
    }

    public void atualizar(Medico m) throws SQLException {
        String sql = "UPDATE medicos SET usuario_id=?,nome=?,crm=?,especialidade=?,telefone=?,email=?,ativo=? WHERE id=?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            setParams(ps, m);
            ps.setBoolean(7, m.isAtivo());
            ps.setInt(8, m.getId());
            ps.executeUpdate();
        }
    }

    public void inativar(int id) throws SQLException {
        String sql = "UPDATE medicos SET ativo=0 WHERE id=?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Optional<Medico> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM medicos WHERE id=?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public List<Medico> listarAtivos() throws SQLException {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT * FROM medicos WHERE ativo=1 ORDER BY nome";
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Medico> listarTodos() throws SQLException {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT * FROM medicos ORDER BY nome";
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Medico> buscarPorNomeOuEspecialidade(String filtro) throws SQLException {
        List<Medico> lista = new ArrayList<>();
        String like = "%" + filtro + "%";
        String sql = "SELECT * FROM medicos WHERE ativo=1 AND (nome LIKE ? OR especialidade LIKE ?) ORDER BY nome";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ── helpers ────────────────────────────────
    private void setParams(PreparedStatement ps, Medico m) throws SQLException {
        if (m.getUsuarioId() != null) ps.setInt(1, m.getUsuarioId());
        else ps.setNull(1, Types.INTEGER);
        ps.setString(2, m.getNome());
        ps.setString(3, m.getCrm());
        ps.setString(4, m.getEspecialidade());
        ps.setString(5, m.getTelefone());
        ps.setString(6, m.getEmail());
    }

    private Medico mapear(ResultSet rs) throws SQLException {
        Medico m = new Medico();
        m.setId(rs.getInt("id"));
        int uid = rs.getInt("usuario_id");
        m.setUsuarioId(rs.wasNull() ? null : uid);
        m.setNome(rs.getString("nome"));
        m.setCrm(rs.getString("crm"));
        m.setEspecialidade(rs.getString("especialidade"));
        m.setTelefone(rs.getString("telefone"));
        m.setEmail(rs.getString("email"));
        m.setAtivo(rs.getBoolean("ativo"));
        Timestamp ts = rs.getTimestamp("criado_em");
        if (ts != null) m.setCriadoEm(ts.toLocalDateTime());
        return m;
    }
}
