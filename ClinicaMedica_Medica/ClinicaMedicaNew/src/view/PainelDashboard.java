package view;

import config.Conexao;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;
import java.sql.*;

/**
 * Dashboard — Dark "Obsidian Health"
 * Cards com gradientes de fundo, ícone Unicode moderno e valor destacado.
 */
public class PainelDashboard extends JPanel {

    public PainelDashboard() {
        setLayout(new BorderLayout());
        setBackground(DS.BG_BASE);
        setBorder(DS.panelBorder());
        construirUI();
    }

    private void construirUI() {
        add(DS.pageHeader("\u25F0", "Painel Principal", "Vis\u00e3o geral do sistema em tempo real"), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 16, 16));
        grid.setOpaque(false);

        try (Connection con = Conexao.getConexao()) {
            grid.add(card("\u25A3", "Pacientes Ativos",    contar(con, "SELECT COUNT(*) FROM pacientes WHERE ativo=1"),            DS.CYAN,    DS.CYAN_DIM));
            grid.add(card("\u2695", "M\u00e9dicos Ativos", contar(con, "SELECT COUNT(*) FROM medicos WHERE ativo=1"),              DS.ACCENT,  DS.ACCENT_DIM));
            grid.add(card("\u25A6", "Consultas Hoje",      contar(con, "SELECT COUNT(*) FROM consultas WHERE DATE(data_hora)=CURDATE()"), DS.WARN, DS.WARN_DIM));
            grid.add(card("\u2713", "Realizadas no M\u00eas", contar(con, "SELECT COUNT(*) FROM consultas WHERE status='REALIZADA' AND MONTH(data_hora)=MONTH(CURDATE())"), DS.SUCCESS, DS.SUCCESS_DIM));
            grid.add(card("\u25CE", "Agendadas Pendentes", contar(con, "SELECT COUNT(*) FROM consultas WHERE status='AGENDADA'"),  DS.ACCENT_LT, DS.ACCENT_DIM));
            grid.add(card("\u2637", "Prontu\u00e1rios",    contar(con, "SELECT COUNT(*) FROM prontuarios"),                       DS.DANGER,  DS.DANGER_DIM));
        } catch (SQLException e) {
            grid.add(new JLabel("Erro ao carregar dados: " + e.getMessage()));
        }

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(grid, BorderLayout.NORTH);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.setBorder(DS.emptyBorder(28, 0, 0, 0));
        JLabel info = DS.labelMuted("Universidade Kimpa Vita \u2014 Eng. Inform\u00e1tica 2\u00ba Ano  |  Grupo: 05");
        footer.add(info);
        center.add(footer, BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);
    }

    private JPanel card(String icon, String label, String value, Color accent, Color dim) {
        JPanel card = new JPanel(new BorderLayout(0, 10)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // fundo card
                g2.setColor(DS.BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), DS.RADIUS * 2, DS.RADIUS * 2));
                // gradiente accent suave no canto superior
                GradientPaint gp = new GradientPaint(0, 0, new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 40),
                    getWidth(), getHeight(), new Color(0, 0, 0, 0));
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), DS.RADIUS * 2, DS.RADIUS * 2));
                // borda sutil
                g2.setColor(DS.BORDER);
                g2.setStroke(new BasicStroke(1));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, DS.RADIUS * 2, DS.RADIUS * 2));
                // barra topo accent
                GradientPaint bar = new GradientPaint(0, 0, accent, getWidth()/2f, 0, accent.brighter());
                g2.setPaint(bar);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 4, 4, 4));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(22, 22, 20, 22));

        // Ícone com círculo de fundo
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(dim);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        iconLabel.setForeground(accent);
        iconLabel.setOpaque(false);
        iconLabel.setPreferredSize(new Dimension(56, 56));
        iconLabel.setMinimumSize(new Dimension(56, 56));
        iconLabel.setMaximumSize(new Dimension(56, 56));

        JLabel valLabel = new JLabel(value);
        valLabel.setFont(new Font(DS.FONT, Font.BOLD, 38));
        valLabel.setForeground(accent);

        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(DS.fontSmall());
        titleLabel.setForeground(DS.MUTED);

        JPanel right = new JPanel(new BorderLayout(0, 5));
        right.setOpaque(false);
        right.setBorder(DS.emptyBorder(4, 0, 0, 0));
        right.add(titleLabel, BorderLayout.NORTH);
        right.add(valLabel, BorderLayout.CENTER);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(right, BorderLayout.CENTER);
        return card;
    }

    private String contar(Connection con, String sql) {
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? String.valueOf(rs.getInt(1)) : "0";
        } catch (SQLException e) { return "\u2014"; }
    }
}
