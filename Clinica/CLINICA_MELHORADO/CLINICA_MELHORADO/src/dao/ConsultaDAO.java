package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Consulta;

public class ConsultaDAO {

    private final Connection con;

    public ConsultaDAO(Connection con) {
        this.con = con;
    }

    public void salvar(Consulta consulta) throws Exception {
        if (existeConflitoHorario(consulta)) {
            throw new IllegalArgumentException("Ja existe consulta para este medico no mesmo horario.");
        }

        String sql = "INSERT INTO consulta(id_paciente, id_medico, data_consulta, hora_consulta, status, observacao) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherStatement(stmt, consulta);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    consulta.setIdConsulta(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Consulta consulta) throws Exception {
        if (existeConflitoHorario(consulta)) {
            throw new IllegalArgumentException("Ja existe consulta para este medico no mesmo horario.");
        }

        String sql = "UPDATE consulta SET id_paciente = ?, id_medico = ?, data_consulta = ?, hora_consulta = ?, "
                + "status = ?, observacao = ? WHERE id_consulta = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            preencherStatement(stmt, consulta);
            stmt.setInt(7, consulta.getIdConsulta());
            stmt.executeUpdate();
        }
    }

    public void cancelar(int idConsulta) throws Exception {
        String sql = "UPDATE consulta SET status = 'CANCELADA' WHERE id_consulta = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idConsulta);
            stmt.executeUpdate();
        }
    }

    public List<Consulta> listarPorPaciente(int idPaciente) throws Exception {
        String sql = "SELECT * FROM consulta WHERE id_paciente = ? ORDER BY data_consulta DESC, hora_consulta DESC";
        List<Consulta> consultas = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idPaciente);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultas.add(mapearConsulta(rs));
                }
            }
        }

        return consultas;
    }

    public List<Consulta> listarTodas() throws Exception {
        String sql = "SELECT * FROM consulta ORDER BY data_consulta DESC, hora_consulta DESC";
        List<Consulta> consultas = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                consultas.add(mapearConsulta(rs));
            }
        }

        return consultas;
    }

    public List<Consulta> listarPorMedicoPeriodo(int idMedico, java.sql.Date inicio, java.sql.Date fim) throws Exception {
        String sql = "SELECT * FROM consulta WHERE id_medico = ? AND data_consulta BETWEEN ? AND ? "
                + "ORDER BY data_consulta, hora_consulta";
        List<Consulta> consultas = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idMedico);
            stmt.setDate(2, inicio);
            stmt.setDate(3, fim);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultas.add(mapearConsulta(rs));
                }
            }
        }

        return consultas;
    }

    private boolean existeConflitoHorario(Consulta consulta) throws Exception {
        String sql = "SELECT COUNT(*) FROM consulta WHERE id_medico = ? AND data_consulta = ? "
                + "AND hora_consulta = ? AND status <> 'CANCELADA' AND id_consulta <> ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, consulta.getIdMedico());
            stmt.setDate(2, consulta.getDataConsulta());
            stmt.setTime(3, consulta.getHoraConsulta());
            stmt.setInt(4, consulta.getIdConsulta());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private void preencherStatement(PreparedStatement stmt, Consulta consulta) throws Exception {
        stmt.setInt(1, consulta.getIdPaciente());
        stmt.setInt(2, consulta.getIdMedico());
        stmt.setDate(3, consulta.getDataConsulta());
        stmt.setTime(4, consulta.getHoraConsulta());
        stmt.setString(5, normalizarStatus(consulta.getStatus()));
        stmt.setString(6, consulta.getObservacao());
    }

    private Consulta mapearConsulta(ResultSet rs) throws Exception {
        Consulta c = new Consulta();
        c.setIdConsulta(rs.getInt("id_consulta"));
        c.setIdPaciente(rs.getInt("id_paciente"));
        c.setIdMedico(rs.getInt("id_medico"));
        c.setDataConsulta(rs.getDate("data_consulta"));
        c.setHoraConsulta(rs.getTime("hora_consulta"));
        c.setStatus(rs.getString("status"));
        c.setObservacao(rs.getString("observacao"));
        return c;
    }

    private String normalizarStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return "AGENDADA";
        }

        String valor = status.trim().toUpperCase();
        switch (valor) {
            case "AGENDADA":
            case "CONFIRMADA":
            case "REALIZADA":
            case "CANCELADA":
                return valor;
            default:
                return "AGENDADA";
        }
    }
}
