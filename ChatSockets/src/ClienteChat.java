import java.io.*;
import java.net.*;

public class ClienteChat1 {

    //Iniciar  conexao
    public static void main(String[] args) {
        String host = "localhost";
        int porta = 11000;
//conexao com o servidor
        try {
            Socket socket = new Socket(host, porta);
            System.out.println("conectado");

            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //cliente
            System.out.println("Digite o seu nome: ");
            String nome = teclado.readLine();
            out.println(nome);
            // mensagens
            System.out.println("Digite aqui a sua mensagem:");
            new Thread(() -> {
                String msg;
                try {
                    while ((msg = in.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    System.out.println(" Conexão perdida!");
                }
            }).start();

            // Enviar mensagens
            String mensagem;
            while ((mensagem = teclado.readLine()) != null) {
                out.println(mensagem);
            }

        }   catch (IOException e) {
            System.out.println("failure:" + e.getMessage());
        }
    }}

