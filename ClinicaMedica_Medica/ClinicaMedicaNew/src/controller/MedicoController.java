package controller;

import dao.MedicoDAO;
import model.Medico;
import util.Validacao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Controller — regras de negócio para Médicos.
 */
public class MedicoController {

    private final MedicoDAO dao = new MedicoDAO();

    public void cadastrar(Medico m) throws SQLException {
        validar(m);
        dao.inserir(m);
    }

    public void atualizar(Medico m) throws SQLException {
        validar(m);
        dao.atualizar(m);
    }

    public void inativar(int id) throws SQLException {
        dao.inativar(id);
    }

    public Optional<Medico> buscarPorId(int id) throws SQLException {
        return dao.buscarPorId(id);
    }

    public List<Medico> buscar(String filtro) throws SQLException {
        if (filtro == null || filtro.isBlank()) return dao.listarAtivos();
        return dao.buscarPorNomeOuEspecialidade(filtro.trim());
    }

    public List<Medico> listarAtivos() throws SQLException {
        return dao.listarAtivos();
    }

    public List<Medico> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    private void validar(Medico m) {
        if (!Validacao.preenchido(m.getNome()))
            throw new IllegalArgumentException("Nome é obrigatório.");
        if (!Validacao.preenchido(m.getCrm()))
            throw new IllegalArgumentException("CRM é obrigatório.");
        if (!Validacao.preenchido(m.getEspecialidade()))
            throw new IllegalArgumentException("Especialidade é obrigatória.");
    }
}
