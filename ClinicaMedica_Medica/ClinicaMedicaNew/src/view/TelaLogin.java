package view;

import controller.AuthController;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * Tela de Login — Dark "Obsidian Health"
 *
 * Layout: painel esquerdo (branding dark gradient) + painel direito (formulário escuro).
 */
public class TelaLogin extends JFrame {

    private JTextField     txtLogin;
    private JPasswordField txtSenha;
    private JButton        btnEntrar;
    private JLabel         lblErro;
    private final AuthController auth = new AuthController();

    public TelaLogin() {
        setTitle("Clínica Médica — Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(880, 540);
        setLocationRelativeTo(null);
        setResizable(false);
        construirUI();
    }

    private void construirUI() {
        JPanel root = new JPanel(new GridLayout(1, 2));
        setContentPane(root);

        // ── Painel esquerdo: branding ─────────────────────────────────────────
        JPanel left = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fundo gradiente profundo
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x0D1117), 0, getHeight(), new Color(0x1A0A3E));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Círculos decorativos translúcidos
                g2.setColor(new Color(0x7C3AED, true));
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.12f));
                g2.fillOval(-60, -60, 280, 280);
                g2.fillOval(getWidth() - 120, getHeight() - 120, 200, 200);

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.07f));
                g2.setColor(new Color(0x06B6D4));
                g2.fillOval(40, getHeight() - 160, 180, 180);

                // Linha accent na base
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                GradientPaint line = new GradientPaint(30, 0, DS.ACCENT, getWidth()-30, 0, DS.CYAN);
                g2.setPaint(line);
                g2.fill(new RoundRectangle2D.Float(30, getHeight() - 5, getWidth() - 60, 4, 4, 4));

                g2.dispose();
            }
        };
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(DS.emptyBorder(60, 48, 60, 48));

        // Ícone com fundo circular
        JPanel iconWrap = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x7C3AED, true));
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconWrap.setOpaque(false);
        iconWrap.setLayout(new GridBagLayout());
        iconWrap.setMaximumSize(new Dimension(90, 90));
        iconWrap.setPreferredSize(new Dimension(90, 90));
        iconWrap.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel iconEmoji = new JLabel("\u2695");  // ⚕ caduceu médico
        iconEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        iconEmoji.setForeground(new Color(0xA78BFA));
        iconWrap.add(iconEmoji);

        JLabel title = new JLabel("<html><div style='text-align:center'>Cl\u00ednica<br>M\u00e9dica</div></html>", SwingConstants.CENTER);
        title.setFont(new Font(DS.FONT, Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Sistema de Gest\u00e3o", SwingConstants.CENTER);
        sub.setFont(DS.fontBody());
        sub.setForeground(new Color(0x8B7EC8));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Linha decorativa
        JPanel linePanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0,0,0,0), getWidth()/2, 0, DS.ACCENT_LT);
                g2.setPaint(gp);
                g2.fillRect(0, 3, getWidth()/2, 2);
                GradientPaint gp2 = new GradientPaint(getWidth()/2, 0, DS.CYAN, getWidth(), 0, new Color(0,0,0,0));
                g2.setPaint(gp2);
                g2.fillRect(getWidth()/2, 3, getWidth()/2, 2);
                g2.dispose();
            }
        };
        linePanel.setOpaque(false);
        linePanel.setMaximumSize(new Dimension(200, 8));
        linePanel.setPreferredSize(new Dimension(200, 8));
        linePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        left.add(Box.createVerticalGlue());
        left.add(iconWrap);
        left.add(Box.createVerticalStrut(20));
        left.add(title);
        left.add(Box.createVerticalStrut(8));
        left.add(sub);
        left.add(Box.createVerticalStrut(20));
        left.add(linePanel);
        left.add(Box.createVerticalGlue());

        JLabel tag = new JLabel("<html><center>Universidade Kimpa Vita<br>Eng. Inform\u00e1tica \u00b7 2\u00ba Ano</center></html>", SwingConstants.CENTER);
        tag.setFont(DS.fontSmall());
        tag.setForeground(new Color(0x4A4070));
        tag.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(tag);

        // ── Painel direito: formulário ────────────────────────────────────────
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(DS.BG_BASE);

        JPanel form = new JPanel();
        form.setBackground(DS.BG_BASE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(360, 700));

        JLabel welcome = new JLabel("Bem-vindo de volta");
        welcome.setFont(DS.fontH2());
        welcome.setForeground(DS.TEXT);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel instruction = new JLabel("Informe suas credenciais para continuar");
        instruction.setFont(DS.fontBody());
        instruction.setForeground(DS.MUTED);
        instruction.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(welcome);
        form.add(Box.createVerticalStrut(5));
        form.add(instruction);
        form.add(Box.createVerticalStrut(32));

        // Campo usuário
        JLabel lblUser = DS.label("\u25A3  Usu\u00e1rio");
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblUser.setForeground(DS.TEXT_DIM);
        form.add(lblUser);
        form.add(Box.createVerticalStrut(6));
        txtLogin = new JTextField();
        txtLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        txtLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        DS.styleField(txtLogin);
        form.add(txtLogin);
        form.add(Box.createVerticalStrut(18));

        // Campo senha
        JLabel lblPass = DS.label("\u25A3  Senha");
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPass.setForeground(DS.TEXT_DIM);
        form.add(lblPass);
        form.add(Box.createVerticalStrut(6));
        txtSenha = new JPasswordField();
        txtSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        txtSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
        DS.styleField(txtSenha);
        form.add(txtSenha);
        form.add(Box.createVerticalStrut(10));

        lblErro = new JLabel(" ");
        lblErro.setFont(DS.fontSmall());
        lblErro.setForeground(DS.DANGER);
        lblErro.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblErro);
        form.add(Box.createVerticalStrut(18));

        btnEntrar = DS.btnPrimary("  Entrar no Sistema");
        btnEntrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnEntrar.setFont(new Font(DS.FONT, Font.BOLD, 14));
        btnEntrar.addActionListener(e -> fazerLogin());
        form.add(btnEntrar);
        form.add(Box.createVerticalStrut(22));

        JLabel hint = new JLabel(" ");
        hint.setFont(DS.fontSmall());
        hint.setForeground(DS.MUTED);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(hint);

        right.add(form);
        getRootPane().setDefaultButton(btnEntrar);

        root.add(left);
        root.add(right);
    }

    private void fazerLogin() {
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());
        if (login.isEmpty() || senha.isEmpty()) {
            lblErro.setText("Preencha login e senha.");
            return;
        }
        lblErro.setText(" ");
        btnEntrar.setEnabled(false);
        btnEntrar.setText("  Aguarde...");

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override protected Boolean doInBackground() throws Exception {
                return auth.login(login, senha);
            }
            @Override protected void done() {
                btnEntrar.setEnabled(true);
                btnEntrar.setText("  Entrar no Sistema");
                try {
                    if (get()) {
                        dispose();
                        new TelaPrincipal().setVisible(true);
                    } else {
                        lblErro.setText("Login ou senha incorretos.");
                        txtSenha.setText("");
                    }
                } catch (Exception ex) {
                    lblErro.setText("Erro: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }
}
