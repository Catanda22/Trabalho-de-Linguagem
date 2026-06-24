package model;

import java.time.LocalDateTime;

/**
 * Entidade Médico.
 */
public class Medico {

    private int id;
    private Integer usuarioId;   // pode ser null
    private String nome;
    private String crm;
    private String especialidade;
    private String telefone;
    private String email;
    private boolean ativo;
    private LocalDateTime criadoEm;

    public Medico() {}

    // ── Getters / Setters ──────────────────────
    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }

    public Integer getUsuarioId()               { return usuarioId; }
    public void setUsuarioId(Integer uid)       { this.usuarioId = uid; }

    public String getNome()                     { return nome; }
    public void setNome(String nome)            { this.nome = nome; }

    public String getCrm()                      { return crm; }
    public void setCrm(String crm)              { this.crm = crm; }

    public String getEspecialidade()            { return especialidade; }
    public void setEspecialidade(String e)      { this.especialidade = e; }

    public String getTelefone()                 { return telefone; }
    public void setTelefone(String t)           { this.telefone = t; }

    public String getEmail()                    { return email; }
    public void setEmail(String e)              { this.email = e; }

    public boolean isAtivo()                    { return ativo; }
    public void setAtivo(boolean ativo)         { this.ativo = ativo; }

    public LocalDateTime getCriadoEm()          { return criadoEm; }
    public void setCriadoEm(LocalDateTime t)    { this.criadoEm = t; }

    @Override
    public String toString() { return nome + " — " + especialidade + " (" + crm + ")"; }
}
