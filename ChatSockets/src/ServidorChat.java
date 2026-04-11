
import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorChat {

    public static Set<ClientHandler> clientes = new HashSet<>();

    public static void main(String[] args) {
        int porta = 11000;

//criacao do servidor TCP
        try {
            ServerSocket servidor = new ServerSocket(porta);

            // a funcao contiua executando e clientes poddem conectar-se

            while (true) {
                Socket socket = servidor.accept();
                //criacao do handler

                ClientHandler handler = new ClientHandler(socket);
                clientes.add(handler);

                //iniciar thread

                new Thread(handler).start();
            }
            // tratar erros

        } catch (IOException e) {
            System.out.println("failure" + e.getMessage());

        }
    }
// transmitir mensagem
    public static void broadcast(String sms, ClientHandler processar) {
        for (ClientHandler cliente : clientes) {
            if (cliente != processar) {
                cliente.enviar(sms);
            }
        }
    }
}
