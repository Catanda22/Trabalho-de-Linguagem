package model;

import java.time.LocalDateTime;

/**
 * Entidade Consulta — agendamento entre paciente e médico.
 */
public class Consulta {

    private int id;
    private int pacienteId;
    private int medicoId;
    private LocalDateTime dataHora;
    private StatusConsulta status;
    private String observacao;
    private LocalDateTime criadoEm;

    // campos desnormalizados para exibição (join)
    private String nomePaciente;
    private String nomeMedico;
    private String especialidade;

    public Consulta() {}

    // ── Getters / Setters ──────────────────────
    public int getId()                              { return id; }
    public void setId(int id)                       { this.id = id; }

    public int getPacienteId()                      { return pacienteId; }
    public void setPacienteId(int pid)              { this.pacienteId = pid; }

    public int getMedicoId()                        { return medicoId; }
    public void setMedicoId(int mid)                { this.medicoId = mid; }

    public LocalDateTime getDataHora()              { return dataHora; }
    public void setDataHora(LocalDateTime dh)       { this.dataHora = dh; }

    public StatusConsulta getStatus()               { return status; }
    public void setStatus(StatusConsulta s)         { this.status = s; }

    public String getObservacao()                   { return observacao; }
    public void setObservacao(String o)             { this.observacao = o; }

    public LocalDateTime getCriadoEm()              { return criadoEm; }
    public void setCriadoEm(LocalDateTime t)        { this.criadoEm = t; }

    public String getNomePaciente()                 { return nomePaciente; }
    public void setNomePaciente(String n)           { this.nomePaciente = n; }

    public String getNomeMedico()                   { return nomeMedico; }
    public void setNomeMedico(String n)             { this.nomeMedico = n; }

    public String getEspecialidade()                { return especialidade; }
    public void setEspecialidade(String e)          { this.especialidade = e; }

    @Override
    public String toString() {
        return "[" + status + "] " + nomePaciente + " c/ " + nomeMedico + " em " + dataHora;
    }
}
