/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Ferraz Caianda
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Medico;

public class MedicoDAO {

    private final Connection con;

    public MedicoDAO(Connection con) {
        this.con = con;
    }

    public void salvar(Medico m) throws Exception {

        String sql = "INSERT INTO medico(nome, crm, especialidade, telefone, email) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        stmt.setString(1, m.getNome());
        stmt.setString(2, m.getCrm());
        stmt.setString(3, m.getEspecialidade());
        stmt.setString(4, m.getTelefone());
        stmt.setString(5, m.getEmail());

        stmt.executeUpdate();

        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                m.setIdMedico(rs.getInt(1));
            }
        }

        stmt.close();
    }

    public void atualizar(Medico m) throws Exception {
        String sql = "UPDATE medico SET nome = ?, crm = ?, especialidade = ?, telefone = ?, email = ? WHERE id_medico = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, m.getNome());
            stmt.setString(2, m.getCrm());
            stmt.setString(3, m.getEspecialidade());
            stmt.setString(4, m.getTelefone());
            stmt.setString(5, m.getEmail());
            stmt.setInt(6, m.getIdMedico());
            stmt.executeUpdate();
        }
    }

    public void deletar(int idMedico) throws Exception {
        String sql = "DELETE FROM medico WHERE id_medico = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idMedico);
            stmt.executeUpdate();
        }
    }

    public List<Medico> pesquisar(String termo) throws Exception {
        String sql = "SELECT * FROM medico WHERE nome LIKE ? OR crm LIKE ? OR especialidade LIKE ? ORDER BY nome";
        List<Medico> medicos = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            String filtro = "%" + (termo == null ? "" : termo.trim()) + "%";
            stmt.setString(1, filtro);
            stmt.setString(2, filtro);
            stmt.setString(3, filtro);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    medicos.add(mapearMedico(rs));
                }
            }
        }

        return medicos;
    }

    public Medico buscarPorId(int idMedico) throws Exception {
        String sql = "SELECT * FROM medico WHERE id_medico = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idMedico);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapearMedico(rs) : null;
            }
        }
    }

    private Medico mapearMedico(ResultSet rs) throws Exception {
        Medico m = new Medico();
        m.setIdMedico(rs.getInt("id_medico"));
        m.setNome(rs.getString("nome"));
        m.setCrm(rs.getString("crm"));
        m.setEspecialidade(rs.getString("especialidade"));
        m.setTelefone(rs.getString("telefone"));
        m.setEmail(rs.getString("email"));
        return m;
    }
}

