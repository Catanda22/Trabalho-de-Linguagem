package view;

import controller.AuthController;
import model.Perfil;
import model.Usuario;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * Janela principal — Dark "Obsidian Health"
 * Sidebar escura profunda + área de conteúdo escura com ícones modernos.
 */
public class TelaPrincipal extends JFrame {

    private JPanel areaPrincipal;
    private JButton itemAtivo;

    // Ícones Unicode modernos para o menu
    private static final String ICO_DASH  = "\u25F0";  // ◰ dashboard
    private static final String ICO_PAC   = "\u25A3";  // ▣ pacientes
    private static final String ICO_MED   = "\u2695";  // ⚕ médicos
    private static final String ICO_CONS  = "\u25A6";  // ▦ consultas
    private static final String ICO_PRONT = "\u2637";  // ☷ prontuários
    private static final String ICO_REL   = "\u25A4";  // ▤ relatórios
    private static final String ICO_USER  = "\u25C6";  // ◆ usuários

    public TelaPrincipal() {
        setTitle("Cl\u00ednica M\u00e9dica \u2014 Sistema de Gest\u00e3o");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 760);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(960, 600));
        construirUI();
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(DS.BG_DEEP);

        // ── Sidebar ──────────────────────────────────────────────────────────
        JPanel sidebar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(DS.SIDEBAR);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // linha direita de borda
                g2.setColor(DS.BORDER);
                g2.fillRect(getWidth()-1, 0, 1, getHeight());
                g2.dispose();
            }
        };
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Logo area
        JPanel logoArea = new JPanel(new BorderLayout(12, 0)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(DS.BG_DEEP);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // linha accent embaixo
                GradientPaint gp = new GradientPaint(0, 0, DS.ACCENT, getWidth(), 0, DS.CYAN);
                g2.setPaint(gp);
                g2.fillRect(0, getHeight()-2, getWidth(), 2);
                g2.dispose();
            }
        };
        logoArea.setOpaque(false);
        logoArea.setMaximumSize(new Dimension(230, 68));
        logoArea.setPreferredSize(new Dimension(230, 68));
        logoArea.setBorder(DS.emptyBorder(0, 18, 0, 18));

        // Ícone logo
        JLabel logoIcon = new JLabel("\u2695");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        logoIcon.setForeground(DS.ACCENT_LT);

        JPanel logoText = new JPanel(new GridLayout(2, 1));
        logoText.setOpaque(false);
        JLabel logoName = new JLabel("Cl\u00ednica M\u00e9dica");
        logoName.setFont(new Font(DS.FONT, Font.BOLD, 13));
        logoName.setForeground(DS.TEXT);
        JLabel logoBadge = new JLabel("Sistema de Gest\u00e3o");
        logoBadge.setFont(new Font(DS.FONT, Font.PLAIN, 10));
        logoBadge.setForeground(DS.MUTED);
        logoText.add(logoName);
        logoText.add(logoBadge);

        logoArea.add(logoIcon, BorderLayout.WEST);
        logoArea.add(logoText, BorderLayout.CENTER);
        sidebar.add(logoArea);
        sidebar.add(Box.createVerticalStrut(12));

        // Seção principal
        sidebar.add(sectionLabel("MENU PRINCIPAL"));

        JButton btnDash  = menuItem(ICO_DASH,  "Painel Principal");
        JButton btnPac   = menuItem(ICO_PAC,   "Pacientes");
        JButton btnMed   = menuItem(ICO_MED,   "M\u00e9dicos");
        JButton btnCons  = menuItem(ICO_CONS,  "Agendamento");
        JButton btnPront = menuItem(ICO_PRONT, "Prontu\u00e1rios");

        btnDash.addActionListener(e  -> navegar(btnDash,  new PainelDashboard()));
        btnPac.addActionListener(e   -> navegar(btnPac,   new PainelPacientes()));
        btnMed.addActionListener(e   -> navegar(btnMed,   new PainelMedicos()));
        btnCons.addActionListener(e  -> navegar(btnCons,  new PainelConsultas()));
        btnPront.addActionListener(e -> navegar(btnPront, new PainelProntuarios()));

        sidebar.add(btnDash);
        sidebar.add(btnPac);
        sidebar.add(btnMed);
        sidebar.add(btnCons);
        sidebar.add(btnPront);

        if (AuthController.temPerfil(Perfil.ADMIN)) {
            sidebar.add(Box.createVerticalStrut(6));
            sidebar.add(sectionLabel("ADMINISTRA\u00c7\u00c3O"));
            JButton btnRel  = menuItem(ICO_REL,  "Relat\u00f3rios");
            JButton btnUser = menuItem(ICO_USER, "Usu\u00e1rios");
            btnRel.addActionListener(e  -> navegar(btnRel,  new PainelRelatorios()));
            btnUser.addActionListener(e -> navegar(btnUser, new PainelUsuarios()));
            sidebar.add(btnRel);
            sidebar.add(btnUser);
        }

        sidebar.add(Box.createVerticalGlue());

        // Rodapé — usuário logado
        Usuario u = AuthController.getUsuarioLogado();
        JPanel userFooter = new JPanel(new BorderLayout(10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(DS.BG_DEEP);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(DS.BORDER);
                g2.fillRect(0, 0, getWidth(), 1);
                g2.dispose();
            }
        };
        userFooter.setOpaque(false);
        userFooter.setMaximumSize(new Dimension(230, 68));
        userFooter.setBorder(DS.emptyBorder(12, 14, 12, 14));

        // Avatar
        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // fundo gradiente
                GradientPaint gp = new GradientPaint(0, 0, DS.ACCENT, getWidth(), getHeight(), DS.CYAN);
                g2.setPaint(gp);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(new Font(DS.FONT, Font.BOLD, 15));
                String ini = u.getNome().isEmpty() ? "U" : String.valueOf(u.getNome().charAt(0)).toUpperCase();
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(ini,
                    (getWidth() - fm.stringWidth(ini)) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(38, 38));

        JPanel userInfo = new JPanel(new GridLayout(2, 1));
        userInfo.setOpaque(false);
        JLabel userName = new JLabel(u.getNome());
        userName.setFont(new Font(DS.FONT, Font.BOLD, 12));
        userName.setForeground(DS.TEXT);
        JLabel userRole = new JLabel(u.getPerfil().getDescricao());
        userRole.setFont(DS.fontSmall());
        userRole.setForeground(DS.MUTED);
        userInfo.add(userName);
        userInfo.add(userRole);

        // Botão sair
        JButton btnSair = new JButton("\u2715") {
            private boolean hov = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hov = true; repaint(); }
                public void mouseExited(MouseEvent e)  { hov = false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hov ? DS.DANGER : new Color(0x2A1A1A));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnSair.setFont(new Font(DS.FONT, Font.BOLD, 13));
        btnSair.setForeground(DS.MUTED);
        btnSair.setOpaque(false);
        btnSair.setContentAreaFilled(false);
        btnSair.setBorderPainted(false);
        btnSair.setFocusPainted(false);
        btnSair.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSair.setPreferredSize(new Dimension(32, 32));
        btnSair.addActionListener(e -> {
            new AuthController().logout();
            dispose();
            new TelaLogin().setVisible(true);
        });

        userFooter.add(avatar, BorderLayout.WEST);
        userFooter.add(userInfo, BorderLayout.CENTER);
        userFooter.add(btnSair, BorderLayout.EAST);
        sidebar.add(userFooter);

        // ── Área principal ────────────────────────────────────────────────────
        areaPrincipal = new JPanel(new BorderLayout());
        areaPrincipal.setBackground(DS.BG_BASE);

        navegar(btnDash, new PainelDashboard());

        add(sidebar, BorderLayout.WEST);
        add(areaPrincipal, BorderLayout.CENTER);
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font(DS.FONT, Font.BOLD, 10));
        l.setForeground(new Color(0x3D4B63));
        l.setBorder(DS.emptyBorder(12, 18, 4, 18));
        l.setMaximumSize(new Dimension(230, 28));
        return l;
    }

    private JButton menuItem(String icon, String label) {
        JButton b = new JButton(icon + "   " + label) {
            private boolean hov = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hov = true; repaint(); }
                public void mouseExited(MouseEvent e)  { hov = false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                boolean ativo = this == itemAtivo;
                if (ativo) {
                    // fundo accent sutil
                    g2.setColor(DS.ACCENT_DIM);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    // barra esquerda gradiente
                    GradientPaint gp = new GradientPaint(0, 0, DS.ACCENT, 0, getHeight(), DS.CYAN);
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, 3, getHeight());
                } else if (hov) {
                    g2.setColor(new Color(0x161B22));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font(DS.FONT, Font.PLAIN, 13));
        b.setForeground(new Color(0x5A7A9A));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(230, 44));
        b.setPreferredSize(new Dimension(230, 44));
        b.setBorder(DS.emptyBorder(0, 24, 0, 18));
        return b;
    }

    private void navegar(JButton btn, JPanel painel) {
        if (itemAtivo != null) {
            itemAtivo.setForeground(new Color(0x5A7A9A));
        }
        itemAtivo = btn;
        btn.setForeground(DS.TEXT);
        btn.repaint();

        areaPrincipal.removeAll();
        areaPrincipal.add(painel, BorderLayout.CENTER);
        areaPrincipal.revalidate();
        areaPrincipal.repaint();
    }

    void mostrar(JPanel painel) {
        areaPrincipal.removeAll();
        areaPrincipal.add(painel, BorderLayout.CENTER);
        areaPrincipal.revalidate();
        areaPrincipal.repaint();
    }
}
