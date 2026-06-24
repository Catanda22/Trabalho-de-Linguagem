package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public final class InterfaceUsuario {
    private InterfaceUsuario() {
    }

    public static void aplicarUsuarioLogado(JFrame frame) {
        JLabel label = new JLabel("Usuario: " + SessaoUsuario.getNomeUsuario(), SwingConstants.RIGHT);
        label.setOpaque(true);
        label.setBackground(new Color(255, 255, 255, 230));
        label.setForeground(new Color(0, 0, 51));
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));

        JLayeredPane layeredPane = frame.getLayeredPane();
        layeredPane.add(label, JLayeredPane.PALETTE_LAYER);

        Runnable reposicionar = () -> {
            int largura = Math.max(220, frame.getWidth());
            label.setBounds(largura - 275, 12, 245, 28);
        };

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                reposicionar.run();
            }
        });

        SwingUtilities.invokeLater(reposicionar);
    }

    public static void prepararBotaoMenu(JButton botao) {
        Color normal = new Color(245, 247, 252);
        Color destaque = new Color(204, 204, 255);
        botao.setBackground(normal);
        botao.setForeground(new Color(0, 0, 51));
        botao.setFocusPainted(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(destaque);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(normal);
            }
        });
    }

    public static JPanel criarCardDashboard(String titulo, String valor, Color cor) {
        JPanel card = new JPanel(null);
        card.setBackground(new Color(255, 255, 255));
        card.setBorder(BorderFactory.createLineBorder(new Color(225, 228, 235)));

        JLabel marcador = new JLabel();
        marcador.setOpaque(true);
        marcador.setBackground(cor);
        card.add(marcador);
        marcador.setBounds(0, 0, 8, 92);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitulo.setForeground(new Color(95, 103, 118));
        card.add(lblTitulo);
        lblTitulo.setBounds(20, 14, 170, 22);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setForeground(new Color(0, 0, 51));
        card.add(lblValor);
        lblValor.setBounds(20, 40, 170, 38);

        return card;
    }
}
