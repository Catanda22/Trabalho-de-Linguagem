package controller;

import dao.ConsultaDAO;
import model.Consulta;
import model.StatusConsulta;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controller — regras de negócio para Consultas.
 */
public class ConsultaController {

    private final ConsultaDAO dao = new ConsultaDAO();

    public void agendar(Consulta c) throws SQLException {
        validar(c);
        if (dao.existeConflito(c.getMedicoId(), c.getDataHora(), 0))
            throw new IllegalArgumentException("O médico já possui consulta neste horário.");
        dao.inserir(c);
    }

    public void atualizar(Consulta c) throws SQLException {
        validar(c);
        if (dao.existeConflito(c.getMedicoId(), c.getDataHora(), c.getId()))
            throw new IllegalArgumentException("Conflito de horário com outra consulta do médico.");
        dao.atualizar(c);
    }

    public void cancelar(int id) throws SQLException {
        dao.atualizarStatus(id, StatusConsulta.CANCELADA);
    }

    public void confirmar(int id) throws SQLException {
        dao.atualizarStatus(id, StatusConsulta.CONFIRMADA);
    }

    public void marcarRealizada(int id) throws SQLException {
        dao.atualizarStatus(id, StatusConsulta.REALIZADA);
    }

    public Optional<Consulta> buscarPorId(int id) throws SQLException {
        return dao.buscarPorId(id);
    }

    public List<Consulta> listarTodas() throws SQLException {
        return dao.listarTodas();
    }

    public List<Consulta> listarPorPaciente(int pacienteId) throws SQLException {
        return dao.listarPorPaciente(pacienteId);
    }

    public List<Consulta> listarPorMedico(int medicoId) throws SQLException {
        return dao.listarPorMedico(medicoId);
    }

    public List<Consulta> listarPorPeriodo(LocalDate inicio, LocalDate fim) throws SQLException {
        return dao.listarPorPeriodo(inicio, fim);
    }

    public List<Consulta> listarPorMedicoEPeriodo(int medicoId, LocalDate inicio, LocalDate fim) throws SQLException {
        return dao.listarPorMedicoEPeriodo(medicoId, inicio, fim);
    }

    private void validar(Consulta c) {
        if (c.getPacienteId() <= 0)
            throw new IllegalArgumentException("Selecione um paciente.");
        if (c.getMedicoId() <= 0)
            throw new IllegalArgumentException("Selecione um médico.");
        if (c.getDataHora() == null)
            throw new IllegalArgumentException("Data/hora é obrigatória.");
        if (c.getDataHora().isBefore(LocalDateTime.now().minusMinutes(1)))
            throw new IllegalArgumentException("Não é possível agendar para data/hora passada.");
    }
}
