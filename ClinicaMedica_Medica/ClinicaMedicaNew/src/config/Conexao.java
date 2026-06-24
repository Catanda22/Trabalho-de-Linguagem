package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gerencia a conexão com o banco de dados MySQL via JDBC.
 * Padrão Singleton para reutilização da conexão.
 */
public class Conexao {

    private static final String URL = "jdbc:mysql://localhost:3306/clinica_medica?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo&useUnicode=true&characterEncoding=UTF-8";
    private static final String USUARIO = "root";
    private static final String SENHA   = "";  // altere conforme seu ambiente

    private static Connection instancia;

    private Conexao() {}

    /**
     * Retorna (ou cria) a conexão singleton.
     */
    public static Connection getConexao() throws SQLException {
        if (instancia == null || instancia.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instancia = DriverManager.getConnection(URL, USUARIO, SENHA);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL não encontrado. Verifique o mysql-connector-java no classpath.", e);
            }
        }
        return instancia;
    }

    /** Fecha a conexão se estiver aberta. */
    public static void fechar() {
        try {
            if (instancia != null && !instancia.isClosed()) {
                instancia.close();
            }
        } catch (SQLException ignored) {}
    }
}
