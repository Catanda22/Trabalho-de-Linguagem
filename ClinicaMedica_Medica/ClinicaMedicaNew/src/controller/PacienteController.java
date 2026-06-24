package controller;

import dao.PacienteDAO;
import model.Paciente;
import util.Validacao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Controller — regras de negócio para Pacientes.
 */
public class PacienteController {

    private final PacienteDAO dao = new PacienteDAO();

    public void cadastrar(Paciente p) throws SQLException, IllegalArgumentException {
        validar(p);
        dao.inserir(p);
    }

    public void atualizar(Paciente p) throws SQLException, IllegalArgumentException {
        validar(p);
        dao.atualizar(p);
    }

    public void inativar(int id) throws SQLException {
        dao.inativar(id);
    }

    public Optional<Paciente> buscarPorId(int id) throws SQLException {
        return dao.buscarPorId(id);
    }

    public List<Paciente> buscar(String filtro) throws SQLException {
        if (filtro == null || filtro.isBlank()) return dao.listarAtivos();
        return dao.buscarPorNomeOuBi(filtro.trim());
    }

    public List<Paciente> listarAtivos() throws SQLException {
        return dao.listarAtivos();
    }

    public List<Paciente> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    // ── validações ─────────────────────────────
    private void validar(Paciente p) throws IllegalArgumentException {
        if (!Validacao.preenchido(p.getNome()))
            throw new IllegalArgumentException("Nome é obrigatório.");
        if (!Validacao.biValido(p.getBi()))
            throw new IllegalArgumentException("BI inválido: " + p.getBi());
        if (p.getDataNascimento() == null)
            throw new IllegalArgumentException("Data de nascimento é obrigatória.");
        if (!Validacao.emailValido(p.getEmail()))
            throw new IllegalArgumentException("E-mail inválido.");
    }
}
