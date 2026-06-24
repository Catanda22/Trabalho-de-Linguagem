import view.TelaLogin;

import javax.swing.*;

/**
 * Ponto de entrada do Sistema de Gestão de Clínica Médica.
 *
 * Universidade Kimpa Vita — Eng. Informática 2º Ano
 * Docente: Moyo Kanivengidio
 */
public class Main {
    public static void main(String[] args) {
        // Usar Look and Feel do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Iniciar na Event Dispatch Thread (EDT) do Swing
        SwingUtilities.invokeLater(() -> {
            TelaLogin login = new TelaLogin();
            login.setVisible(true);
        });
    }
}
