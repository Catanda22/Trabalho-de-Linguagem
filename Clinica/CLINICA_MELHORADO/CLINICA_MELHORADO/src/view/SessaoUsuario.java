package view;

import model.Usuario;

public final class SessaoUsuario {
    private static Usuario usuarioLogado;

    private SessaoUsuario() {
    }

    public static void iniciar(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static String getNomeUsuario() {
        if (usuarioLogado == null || usuarioLogado.getNome() == null || usuarioLogado.getNome().trim().isEmpty()) {
            return "Utilizador";
        }
        return usuarioLogado.getNome();
    }

    public static String getPerfilUsuario() {
        if (usuarioLogado == null || usuarioLogado.getNivelAcesso() == null) {
            return "";
        }
        return usuarioLogado.getNivelAcesso();
    }

    public static void encerrar() {
        usuarioLogado = null;
    }
}
