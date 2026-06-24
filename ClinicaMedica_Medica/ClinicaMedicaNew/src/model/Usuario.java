package model;

import java.time.LocalDateTime;

/**
 * Representa um usuário do sistema (login/acesso).
 */
public class Usuario {

    private int id;
    private String nome;
    private String login;
    private String senhaHash;   // SHA-256 hexadecimal
    private Perfil perfil;
    private boolean ativo;
    private LocalDateTime criadoEm;

    public Usuario() {}

    public Usuario(int id, String nome, String login, String senhaHash, Perfil perfil, boolean ativo) {
        this.id        = id;
        this.nome      = nome;
        this.login     = login;
        this.senhaHash = senhaHash;
        this.perfil    = perfil;
        this.ativo     = ativo;
    }

    // ── Getters / Setters ──────────────────────
    public int getId()                      { return id; }
    public void setId(int id)               { this.id = id; }

    public String getNome()                 { return nome; }
    public void setNome(String nome)        { this.nome = nome; }

    public String getLogin()                { return login; }
    public void setLogin(String login)      { this.login = login; }

    public String getSenhaHash()            { return senhaHash; }
    public void setSenhaHash(String h)      { this.senhaHash = h; }

    public Perfil getPerfil()               { return perfil; }
    public void setPerfil(Perfil perfil)    { this.perfil = perfil; }

    public boolean isAtivo()                { return ativo; }
    public void setAtivo(boolean ativo)     { this.ativo = ativo; }

    public LocalDateTime getCriadoEm()      { return criadoEm; }
    public void setCriadoEm(LocalDateTime t){ this.criadoEm = t; }

    @Override
    public String toString() { return nome + " [" + perfil + "]"; }
}
