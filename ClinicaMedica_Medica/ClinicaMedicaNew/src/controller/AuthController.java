package controller;

import dao.UsuarioDAO;
import model.Perfil;
import model.Usuario;
import util.Seguranca;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Gerencia autenticação e sessão ativa do sistema.
 */
public class AuthController {

    private static Usuario usuarioLogado;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /** Tenta autenticar e armazena o usuário na sessão. */
    public boolean login(String login, String senha) throws SQLException {
        String hash = Seguranca.hashSenha(senha);
        Optional<Usuario> opt = usuarioDAO.autenticar(login, hash);
        opt.ifPresent(u -> usuarioLogado = u);
        return opt.isPresent();
    }

    public void logout() {
        usuarioLogado = null;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static boolean temPerfil(Perfil... perfis) {
        if (usuarioLogado == null) return false;
        for (Perfil p : perfis) {
            if (usuarioLogado.getPerfil() == p) return true;
        }
        return false;
    }
}
