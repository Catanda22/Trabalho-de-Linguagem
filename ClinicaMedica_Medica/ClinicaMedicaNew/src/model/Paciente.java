package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade Paciente – dados cadastrais completos.
 */
public class Paciente {

    private int           id;
    private String        nome;
    private String        bi;               // Bilhete de Identidade angolano
    private LocalDate     dataNascimento;
    private char          sexo;             // 'M', 'F', 'O'
    private String        telefone;
    private String        email;
    private String        bairro;
    private String        cidade;
    private String        historico;
    private boolean       ativo;
    private LocalDateTime criadoEm;

    public Paciente() {}

    // ── Getters / Setters ──────────────────────────────────────────────────

    public int getId()                         { return id; }
    public void setId(int id)                  { this.id = id; }

    public String getNome()                    { return nome; }
    public void setNome(String nome)           { this.nome = nome; }

    /** BI angolano – armazenado sem espaços extras, em maiúsculas. */
    public String getBi()                      { return bi; }
    public void setBi(String bi)               { this.bi = bi == null ? null : bi.trim().toUpperCase(); }

    public LocalDate getDataNascimento()       { return dataNascimento; }
    public void setDataNascimento(LocalDate d) { this.dataNascimento = d; }

    public char getSexo()                      { return sexo; }
    public void setSexo(char sexo)             { this.sexo = sexo; }

    public String getTelefone()                { return telefone; }
    public void setTelefone(String t)          { this.telefone = t; }

    public String getEmail()                   { return email; }
    public void setEmail(String e)             { this.email = e; }

    public String getBairro()                  { return bairro; }
    public void setBairro(String b)            { this.bairro = b; }

    public String getCidade()                  { return cidade; }
    public void setCidade(String c)            { this.cidade = c; }

    public String getHistorico()               { return historico; }
    public void setHistorico(String h)         { this.historico = h; }

    public boolean isAtivo()                   { return ativo; }
    public void setAtivo(boolean ativo)        { this.ativo = ativo; }

    public LocalDateTime getCriadoEm()         { return criadoEm; }
    public void setCriadoEm(LocalDateTime t)   { this.criadoEm = t; }

    @Override
    public String toString() {
        return nome + " (BI: " + bi + ")";
    }
}

