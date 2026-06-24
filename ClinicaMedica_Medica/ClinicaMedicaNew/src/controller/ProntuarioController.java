package controller;

import dao.ProntuarioDAO;
import model.Prontuario;
import util.Validacao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Controller — regras de negócio para Prontuários.
 */
public class ProntuarioController {

    private final ProntuarioDAO dao = new ProntuarioDAO();

    public void salvar(Prontuario p) throws SQLException {
        if (!Validacao.preenchido(p.getDiagnostico()))
            throw new IllegalArgumentException("Diagnóstico é obrigatório.");
        Optional<Prontuario> existente = dao.buscarPorConsulta(p.getConsultaId());
        if (existente.isPresent()) {
            p.setId(existente.get().getId());
            dao.atualizar(p);
        } else {
            dao.inserir(p);
        }
    }

    public Optional<Prontuario> buscarPorConsulta(int consultaId) throws SQLException {
        return dao.buscarPorConsulta(consultaId);
    }

    public List<Prontuario> listarPorPaciente(int pacienteId) throws SQLException {
        return dao.listarPorPaciente(pacienteId);
    }

    public List<Prontuario> listarTodos() throws SQLException {
        return dao.listarTodos();
    }
}
