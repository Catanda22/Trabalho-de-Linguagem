/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Usuario;

public class UsuarioDAO {
    private final Connection con;

    public UsuarioDAO(Connection con) {
        this.con = con;
    }

    public void salvar(Usuario u) throws Exception {
        String sql = "INSERT INTO usuario (nome, login, senha, perfil) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getUsername());
            stmt.setString(3, u.getSenha());
            stmt.setString(4, normalizarPerfil(u.getNivelAcesso()));
            stmt.executeUpdate();
        }
    }

    public Usuario LoginView(String username, String senha) throws Exception {
        String sql = "SELECT * FROM usuario WHERE login = ? AND senha = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }

        return null;
    }

    public String autenticar(String usuario, String senha) {
        try {
            Usuario u = LoginView(usuario, senha);
            return u == null ? null : u.getNivelAcesso();
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao autenticar utilizador: " + e.getMessage(), e);
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws Exception {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNome(rs.getString("nome"));
        u.setUsername(rs.getString("login"));
        u.setSenha(rs.getString("senha"));
        u.setNivelAcesso(rs.getString("perfil"));
        return u;
    }

    private String normalizarPerfil(String perfil) {
        if (perfil == null) {
            return "RECEPCIONISTA";
        }

        String valor = perfil.trim().toUpperCase();
        switch (valor) {
            case "ADMINISTRADOR":
            case "ADMIN":
                return "ADMINISTRADOR";
            case "MEDICO":
            case "MÉDICO":
                return "MEDICO";
            case "RECEPÇÃO":
            case "RECEPCAO":
            case "RECEPCIONISTA":
                return "RECEPCIONISTA";
            default:
                return valor;
        }
    }
}
