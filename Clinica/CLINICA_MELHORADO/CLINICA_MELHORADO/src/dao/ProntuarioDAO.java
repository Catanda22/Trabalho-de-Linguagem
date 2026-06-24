package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Prontuario;

public class ProntuarioDAO {

    private final Connection con;

    public ProntuarioDAO(Connection con) {
        this.con = con;
    }

    public void salvar(Prontuario prontuario) throws Exception {
        String sql = "INSERT INTO prontuario(id_consulta, id_paciente, diagnostico, prescricao, observacoes) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, prontuario.getIdConsulta());
            stmt.setInt(2, prontuario.getIdPaciente());
            stmt.setString(3, prontuario.getDiagnostico());
            stmt.setString(4, prontuario.getPrescricao());
            stmt.setString(5, prontuario.getObservacoes());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    prontuario.setIdProntuario(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Prontuario prontuario) throws Exception {
        String sql = "UPDATE prontuario SET diagnostico = ?, prescricao = ?, observacoes = ? WHERE id_prontuario = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, prontuario.getDiagnostico());
            stmt.setString(2, prontuario.getPrescricao());
            stmt.setString(3, prontuario.getObservacoes());
            stmt.setInt(4, prontuario.getIdProntuario());
            stmt.executeUpdate();
        }
    }

    public List<Prontuario> listarPorPaciente(int idPaciente) throws Exception {
        String sql = "SELECT * FROM prontuario WHERE id_paciente = ? ORDER BY data_registro DESC";
        List<Prontuario> prontuarios = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idPaciente);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    prontuarios.add(mapearProntuario(rs));
                }
            }
        }

        return prontuarios;
    }

    private Prontuario mapearProntuario(ResultSet rs) throws Exception {
        Prontuario p = new Prontuario();
        p.setIdProntuario(rs.getInt("id_prontuario"));
        p.setIdConsulta(rs.getInt("id_consulta"));
        p.setIdPaciente(rs.getInt("id_paciente"));
        p.setDiagnostico(rs.getString("diagnostico"));
        p.setPrescricao(rs.getString("prescricao"));
        p.setObservacoes(rs.getString("observacoes"));
        p.setDataRegistro(rs.getTimestamp("data_registro"));
        return p;
    }
}
