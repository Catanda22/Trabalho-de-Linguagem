/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
public class Conexao {

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = System.getProperty(
                    "clinica.db.url",
                    "jdbc:mysql://localhost:3306/clinica_newHorizonte?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
            );
            String user = System.getProperty("clinica.db.user", "root");
            String password = System.getProperty("clinica.db.password", "");

            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }

    static Connection conectar() {
        return getConnection();
    }
}
