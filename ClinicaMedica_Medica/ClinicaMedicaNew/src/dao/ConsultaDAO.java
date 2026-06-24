package dao;

import config.Conexao;
import model.Consulta;
import model.StatusConsulta;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO — acesso a dados da entidade Consulta.
 */
public class ConsultaDAO {

    private static final String SELECT_COMPLETO =
    "SELECT c.*, p.nome AS nom_pac, m.nome AS nom_med, m.especialidade " +
    "FROM consultas c " +
    "JOIN pacientes p ON p.id = c.paciente_id " +
    "JOIN medicos m ON m.id = c.medico_id";

    public void inserir(Consulta c) throws SQLException {
        String sql = "INSERT INTO consultas (paciente_id,medico_id,data_hora,status,observacao) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(ps, c);
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) c.setId(gk.getInt(1));
            }
        }
    }

    public void atualizar(Consulta c) throws SQLException {
        String sql = "UPDATE consultas SET paciente_id=?,medico_id=?,data_hora=?,status=?,observacao=? WHERE id=?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            setParams(ps, c);
            ps.setInt(6, c.getId());
            ps.executeUpdate();
        }
    }

    public void atualizarStatus(int id, StatusConsulta status) throws SQLException {
        String sql = "UPDATE consultas SET status=? WHERE id=?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public Optional<Consulta> buscarPorId(int id) throws SQLException {
        String sql = SELECT_COMPLETO + " WHERE c.id=?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    /** Verifica conflito de horário para o médico (ignora id passado, para edição). */
    public boolean existeConflito(int medicoId, LocalDateTime dataHora, int ignorarId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM consultas WHERE medico_id=? AND data_hora=? AND status<>'CANCELADA' AND id<>?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, medicoId);
            ps.setTimestamp(2, Timestamp.valueOf(dataHora));
            ps.setInt(3, ignorarId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public List<Consulta> listarTodas() throws SQLException {
        return executarLista(SELECT_COMPLETO + " ORDER BY c.data_hora DESC");
    }

    public List<Consulta> listarPorPaciente(int pacienteId) throws SQLException {
        String sql = SELECT_COMPLETO + " WHERE c.paciente_id=? ORDER BY c.data_hora DESC";
        return executarListaParam(sql, pacienteId);
    }

    public List<Consulta> listarPorMedico(int medicoId) throws SQLException {
        String sql = SELECT_COMPLETO + " WHERE c.medico_id=? ORDER BY c.data_hora DESC";
        return executarListaParam(sql, medicoId);
    }

    public List<Consulta> listarPorPeriodo(LocalDate inicio, LocalDate fim) throws SQLException {
        List<Consulta> lista = new ArrayList<>();
        String sql = SELECT_COMPLETO + " WHERE DATE(c.data_hora) BETWEEN ? AND ? ORDER BY c.data_hora";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fim));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Consulta> listarPorMedicoEPeriodo(int medicoId, LocalDate inicio, LocalDate fim) throws SQLException {
        List<Consulta> lista = new ArrayList<>();
        String sql = SELECT_COMPLETO + " WHERE c.medico_id=? AND DATE(c.data_hora) BETWEEN ? AND ? ORDER BY c.data_hora";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, medicoId);
            ps.setDate(2, Date.valueOf(inicio));
            ps.setDate(3, Date.valueOf(fim));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ── helpers ────────────────────────────────
    private void setParams(PreparedStatement ps, Consulta c) throws SQLException {
        ps.setInt(1, c.getPacienteId());
        ps.setInt(2, c.getMedicoId());
        ps.setTimestamp(3, Timestamp.valueOf(c.getDataHora()));
        ps.setString(4, c.getStatus().name());
        ps.setString(5, c.getObservacao());
    }

    private List<Consulta> executarLista(String sql) throws SQLException {
        List<Consulta> lista = new ArrayList<>();
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private List<Consulta> executarListaParam(String sql, int param) throws SQLException {
        List<Consulta> lista = new ArrayList<>();
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    private Consulta mapear(ResultSet rs) throws SQLException {
        Consulta c = new Consulta();
        c.setId(rs.getInt("id"));
        c.setPacienteId(rs.getInt("paciente_id"));
        c.setMedicoId(rs.getInt("medico_id"));
        c.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        c.setStatus(StatusConsulta.valueOf(rs.getString("status")));
        c.setObservacao(rs.getString("observacao"));
        Timestamp ts = rs.getTimestamp("criado_em");
        if (ts != null) c.setCriadoEm(ts.toLocalDateTime());
        c.setNomePaciente(rs.getString("nom_pac"));
        c.setNomeMedico(rs.getString("nom_med"));
        c.setEspecialidade(rs.getString("especialidade"));
        return c;
    }
}
