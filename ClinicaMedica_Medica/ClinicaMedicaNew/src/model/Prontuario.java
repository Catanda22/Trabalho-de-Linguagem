package model;

import java.time.LocalDateTime;

/**
 * Entidade Prontuário — registro clínico de uma consulta realizada.
 */
public class Prontuario {

    private int id;
    private int consultaId;
    private String diagnostico;
    private String prescricao;
    private String observacoes;
    private LocalDateTime criadoEm;

    // campos desnormalizados para exibição
    private String nomePaciente;
    private String nomeMedico;
    private LocalDateTime dataConsulta;

    public Prontuario() {}

    // ── Getters / Setters ──────────────────────
    public int getId()                              { return id; }
    public void setId(int id)                       { this.id = id; }

    public int getConsultaId()                      { return consultaId; }
    public void setConsultaId(int cid)              { this.consultaId = cid; }

    public String getDiagnostico()                  { return diagnostico; }
    public void setDiagnostico(String d)            { this.diagnostico = d; }

    public String getPrescricao()                   { return prescricao; }
    public void setPrescricao(String p)             { this.prescricao = p; }

    public String getObservacoes()                  { return observacoes; }
    public void setObservacoes(String o)            { this.observacoes = o; }

    public LocalDateTime getCriadoEm()              { return criadoEm; }
    public void setCriadoEm(LocalDateTime t)        { this.criadoEm = t; }

    public String getNomePaciente()                 { return nomePaciente; }
    public void setNomePaciente(String n)           { this.nomePaciente = n; }

    public String getNomeMedico()                   { return nomeMedico; }
    public void setNomeMedico(String n)             { this.nomeMedico = n; }

    public LocalDateTime getDataConsulta()          { return dataConsulta; }
    public void setDataConsulta(LocalDateTime d)    { this.dataConsulta = d; }
}
