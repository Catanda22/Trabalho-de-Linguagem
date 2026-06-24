/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.Connection;

/**
 *
 * @author Ferraz Caianda
 */
public class TesteConexao {
     public static void main(String[] args) {

        Connection con =
                Conexao.conectar();

        if(con != null){

            System.out.println(
                    "Conectado com sucesso!"
            );

        }else{

            System.out.println(
                    "Falha na conexão!"
            );
        }
    }
    
}
