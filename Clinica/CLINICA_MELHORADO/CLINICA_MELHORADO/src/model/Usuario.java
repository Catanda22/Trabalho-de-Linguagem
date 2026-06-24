/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Ferraz Caianda
 */
public class Usuario {
    private int id_usuario;
    private String nome;
    private String nomeUsuario;
    private String senha;
    private String perfil;

    public int getIdUsuario() {
        return id_usuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.id_usuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsername() {
        return nomeUsuario;
    }
    
     public void setUsername(String username) {
        this.nomeUsuario = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNivelAcesso() {
        return perfil;
    }
    
    public void setNivelAcesso(String nivelAcesso) {
        this.perfil = nivelAcesso;
    }
}
