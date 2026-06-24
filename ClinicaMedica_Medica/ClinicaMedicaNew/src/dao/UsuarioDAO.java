package dao;

import config.Conexao;
import model.Perfil;
import model.Usuario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO — acesso a dados da entidade Usuario.
 */
public class UsuarioDAO {

    /** Busca usuário por login e verifica o hash da senha. */
    public Optional<Usuario> autenticar(String login, String senhaHash) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE login = ? AND senha_hash = ? AND ativo = 1";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, senhaHash);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nome";
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public void inserir(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, login, senha_hash, perfil) VALUES (?,?,?,?)";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getLogin());
            ps.setString(3, u.getSenhaHash());
            ps.setString(4, u.getPerfil().name());
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) u.setId(gk.getInt(1));
            }
        }
    }

    public void atualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuarios SET nome=?, login=?, perfil=?, ativo=? WHERE id=?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getLogin());
            ps.setString(3, u.getPerfil().name());
            ps.setBoolean(4, u.isAtivo());
            ps.setInt(5, u.getId());
            ps.executeUpdate();
        }
    }

    // ── mapeamento ─────────────────────────────
    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNome(rs.getString("nome"));
        u.setLogin(rs.getString("login"));
        u.setSenhaHash(rs.getString("senha_hash"));
        u.setPerfil(Perfil.valueOf(rs.getString("perfil")));
        u.setAtivo(rs.getBoolean("ativo"));
        Timestamp ts = rs.getTimestamp("criado_em");
        if (ts != null) u.setCriadoEm(ts.toLocalDateTime());
        return u;
    }
}
