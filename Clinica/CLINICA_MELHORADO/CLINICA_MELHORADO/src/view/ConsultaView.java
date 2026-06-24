package view;

public class ConsultaView extends Agendar {

    public ConsultaView() {
        super();
        setTitle("Consultas");
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new ConsultaView().setVisible(true));
    }
}
