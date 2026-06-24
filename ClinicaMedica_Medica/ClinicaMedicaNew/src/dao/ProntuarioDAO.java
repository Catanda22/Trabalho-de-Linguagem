package dao;

import config.Conexao;
import model.Prontuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO — acesso a dados da entidade Prontuario.
 */
public class ProntuarioDAO {

    private static final String SELECT_COMPLETO =
    "SELECT pr.*, p.nome AS nom_pac, m.nome AS nom_med, c.data_hora " +
    "FROM prontuarios pr " +
    "JOIN consultas c ON c.id = pr.consulta_id " +
    "JOIN pacientes p ON p.id = c.paciente_id " +
    "JOIN medicos m ON m.id = c.medico_id";

    public void inserir(Prontuario p) throws SQLException {
        String sql = "INSERT INTO prontuarios (consulta_id,diagnostico,prescricao,observacoes) VALUES (?,?,?,?)";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getConsultaId());
            ps.setString(2, p.getDiagnostico());
            ps.setString(3, p.getPrescricao());
            ps.setString(4, p.getObservacoes());
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) p.setId(gk.getInt(1));
            }
        }
    }

    public void atualizar(Prontuario p) throws SQLException {
        String sql = "UPDATE prontuarios SET diagnostico=?,prescricao=?,observacoes=? WHERE id=?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setString(1, p.getDiagnostico());
            ps.setString(2, p.getPrescricao());
            ps.setString(3, p.getObservacoes());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        }
    }

    public Optional<Prontuario> buscarPorConsulta(int consultaId) throws SQLException {
        String sql = SELECT_COMPLETO + " WHERE pr.consulta_id=?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, consultaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public List<Prontuario> listarPorPaciente(int pacienteId) throws SQLException {
        List<Prontuario> lista = new ArrayList<>();
        String sql = SELECT_COMPLETO + " WHERE c.paciente_id=? ORDER BY c.data_hora DESC";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, pacienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Prontuario> listarTodos() throws SQLException {
        List<Prontuario> lista = new ArrayList<>();
        String sql = SELECT_COMPLETO + " ORDER BY c.data_hora DESC";
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    // ── mapeamento ─────────────────────────────
    private Prontuario mapear(ResultSet rs) throws SQLException {
        Prontuario p = new Prontuario();
        p.setId(rs.getInt("id"));
        p.setConsultaId(rs.getInt("consulta_id"));
        p.setDiagnostico(rs.getString("diagnostico"));
        p.setPrescricao(rs.getString("prescricao"));
        p.setObservacoes(rs.getString("observacoes"));
        Timestamp ts = rs.getTimestamp("criado_em");
        if (ts != null) p.setCriadoEm(ts.toLocalDateTime());
        p.setNomePaciente(rs.getString("nom_pac"));
        p.setNomeMedico(rs.getString("nom_med"));
        p.setDataConsulta(rs.getTimestamp("data_hora").toLocalDateTime());
        return p;
    }
}
