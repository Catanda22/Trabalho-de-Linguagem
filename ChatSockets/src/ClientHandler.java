
import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String nome;
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
public void run() {

        try {
            //fazer entrada e saida das mensages
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            out = new PrintWriter(socket.getOutputStream(), true);


            // inserir nome do cliente

            nome = in.readLine();
            System.out.println(nome+":online");
            ServidorChat .broadcast(nome+":conectado", this);
            //ler mensagem do cliente
            System.out.println("");
            String mensagem;

        while ((mensagem = in.readLine()) != null) {

                System.out.println(nome+":" + mensagem);

                ServidorChat .broadcast(mensagem, this);
            }
        }
        catch (IOException e) {
            System.out.println("Cliente desconectado!.");
        }
    }

public void enviar(String mensagem) {
        out.println(mensagem);
    }
}
