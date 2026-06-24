package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.table.JTableHeader;

/**
 * DS — Design System Dark "Obsidian Health"
 *
 * Paleta escura moderna:
 *   BG_DEEP    #0D1117  — fundo mais profundo
 *   BG_BASE    #161B22  — fundo base
 *   BG_CARD    #1C2333  — cards e superfícies
 *   BG_RAISED  #21293A  — elementos elevados
 *   SIDEBAR    #0D1117  — sidebar
 *   ACCENT     #7C3AED  — roxo vibrante (ações primárias)
 *   ACCENT_LT  #8B5CF6  — roxo claro
 *   CYAN       #06B6D4  — ciano (destaque secundário)
 *   CYAN_LT    #22D3EE  — ciano claro
 *   SUCCESS    #10B981  — verde esmeralda
 *   WARN       #F59E0B  — âmbar
 *   DANGER     #EF4444  — vermelho
 *   TEXT       #E2E8F0  — texto principal
 *   MUTED      #64748B  — texto secundário
 *   BORDER     #2D3748  — bordas
 */
public final class DS {

    private DS() {}

    // ── Paleta Dark ──────────────────────────────────────────────────────────
    public static final Color BG_DEEP   = new Color(0x0D1117);
    public static final Color BG_BASE   = new Color(0x161B22);
    public static final Color BG_CARD   = new Color(0x1C2333);
    public static final Color BG_RAISED = new Color(0x21293A);
    public static final Color SIDEBAR   = new Color(0x0D1117);
    public static final Color ACCENT    = new Color(0x7C3AED);
    public static final Color ACCENT_LT = new Color(0x8B5CF6);
    public static final Color ACCENT_DIM= new Color(0x2D1B69);
    public static final Color CYAN      = new Color(0x06B6D4);
    public static final Color CYAN_LT   = new Color(0x22D3EE);
    public static final Color CYAN_DIM  = new Color(0x0C3044);
    public static final Color SUCCESS   = new Color(0x10B981);
    public static final Color SUCCESS_DIM= new Color(0x064E3B);
    public static final Color WARN      = new Color(0xF59E0B);
    public static final Color WARN_DIM  = new Color(0x451A03);
    public static final Color DANGER    = new Color(0xEF4444);
    public static final Color DANGER_DIM= new Color(0x450A0A);
    public static final Color TEXT      = new Color(0xE2E8F0);
    public static final Color TEXT_DIM  = new Color(0x94A3B8);
    public static final Color MUTED     = new Color(0x64748B);
    public static final Color BORDER    = new Color(0x2D3748);
    public static final Color BORDER_LT = new Color(0x374151);

    // Aliases de compatibilidade
    public static final Color NAVY    = SIDEBAR;
    public static final Color NAVY2   = BG_DEEP;
    public static final Color BLUE    = ACCENT;
    public static final Color BLUE_LT = ACCENT_DIM;
    public static final Color TEAL    = CYAN;
    public static final Color TEAL_LT = CYAN_DIM;
    public static final Color CLOUD   = BG_BASE;
    public static final Color SURFACE = BG_CARD;
    public static final Color DANGER_LT = DANGER_DIM;
    public static final Color WARN_LT   = WARN_DIM;
    public static final Color SUCCESS_LT= SUCCESS_DIM;

    // Status consulta
    public static final Color STATUS_AGENDADA   = new Color(0x1E3A5F);
    public static final Color STATUS_CONFIRMADA = new Color(0x1B4332);
    public static final Color STATUS_REALIZADA  = new Color(0x2D1B69);
    public static final Color STATUS_CANCELADA  = new Color(0x450A0A);

    // ── Tipografia ───────────────────────────────────────────────────────────
    public static final String FONT = "Segoe UI";

    public static Font fontH1()   { return new Font(FONT, Font.BOLD,  22); }
    public static Font fontH2()   { return new Font(FONT, Font.BOLD,  18); }
    public static Font fontH3()   { return new Font(FONT, Font.BOLD,  14); }
    public static Font fontBody() { return new Font(FONT, Font.PLAIN, 13); }
    public static Font fontSmall(){ return new Font(FONT, Font.PLAIN, 11); }
    public static Font fontBold() { return new Font(FONT, Font.BOLD,  13); }
    public static Font fontMono() { return new Font("Consolas", Font.PLAIN, 12); }

    // ── Espaçamento ──────────────────────────────────────────────────────────
    public static final int PAD_XS = 6;
    public static final int PAD_S  = 12;
    public static final int PAD_M  = 20;
    public static final int PAD_L  = 28;
    public static final int RADIUS = 10;

    // ── Bordas ────────────────────────────────────────────────────────────────
    public static Border emptyBorder(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }
    public static Border panelBorder() {
        return new EmptyBorder(PAD_M, PAD_L, PAD_M, PAD_L);
    }
    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
            new RoundedLineBorder(BORDER, 1, RADIUS),
            new EmptyBorder(PAD_M, PAD_M, PAD_M, PAD_M)
        );
    }
    public static Border fieldBorder() {
        return BorderFactory.createCompoundBorder(
            new RoundedLineBorder(BORDER, 1, 8),
            new EmptyBorder(6, 12, 6, 12)
        );
    }
    public static Border fieldBorderFocus() {
        return BorderFactory.createCompoundBorder(
            new RoundedLineBorder(ACCENT, 2, 8),
            new EmptyBorder(5, 11, 5, 11)
        );
    }
    public static Border filterBarBorder() {
        return BorderFactory.createCompoundBorder(
            new RoundedLineBorder(BORDER, 1, RADIUS),
            new EmptyBorder(PAD_S, PAD_M, PAD_S, PAD_M)
        );
    }

    // ── Campos de texto ──────────────────────────────────────────────────────
    public static void styleField(JTextField f) {
        f.setFont(fontBody());
        f.setBorder(fieldBorder());
        f.setBackground(BG_RAISED);
        f.setForeground(TEXT);
        f.setCaretColor(ACCENT_LT);
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { f.setBorder(fieldBorderFocus()); f.setBackground(new Color(0x1A1F2E)); }
            public void focusLost(FocusEvent e)   { f.setBorder(fieldBorder()); f.setBackground(BG_RAISED); }
        });
    }
    public static void styleField(JPasswordField f) {
        f.setFont(fontBody());
        f.setBorder(fieldBorder());
        f.setBackground(BG_RAISED);
        f.setForeground(TEXT);
        f.setCaretColor(ACCENT_LT);
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { f.setBorder(fieldBorderFocus()); f.setBackground(new Color(0x1A1F2E)); }
            public void focusLost(FocusEvent e)   { f.setBorder(fieldBorder()); f.setBackground(BG_RAISED); }
        });
    }
    public static void styleField(JTextArea f) {
        f.setFont(fontBody());
        f.setBackground(BG_RAISED);
        f.setForeground(TEXT);
        f.setCaretColor(ACCENT_LT);
        f.setLineWrap(true);
        f.setWrapStyleWord(true);
        f.setBorder(new EmptyBorder(8, 10, 8, 10));
    }
    public static void styleCombo(JComboBox<?> cb) {
        cb.setFont(fontBody());
        cb.setBackground(BG_RAISED);
        cb.setForeground(TEXT);
        cb.setBorder(new EmptyBorder(0, 2, 0, 2));
    }

    // ── Tabela ────────────────────────────────────────────────────────────────
    public static void styleTable(JTable t) {
        t.setFont(fontBody());
        t.setRowHeight(34);
        t.setGridColor(BORDER);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setSelectionBackground(ACCENT_DIM);
        t.setSelectionForeground(TEXT);
        t.setBackground(BG_CARD);
        t.setForeground(TEXT);
        t.setFillsViewportHeight(true);

        JTableHeader header = t.getTableHeader();
        header.setFont(fontBold());
        header.setBackground(BG_RAISED);
        header.setForeground(TEXT_DIM);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER));
        header.setReorderingAllowed(false);

        t.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? BG_CARD : new Color(0x1A2035));
                    setForeground(TEXT);
                }
                return this;
            }
        });
    }

    // ── Rótulos ──────────────────────────────────────────────────────────────
    public static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(fontBold());
        l.setForeground(TEXT);
        return l;
    }
    public static JLabel labelMuted(String text) {
        JLabel l = new JLabel(text);
        l.setFont(fontSmall());
        l.setForeground(MUTED);
        return l;
    }
    public static JLabel heading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(fontH2());
        l.setForeground(TEXT);
        return l;
    }

    // ── Botões ────────────────────────────────────────────────────────────────
    public static JButton btnPrimary(String text) {
        return makeBtn(text, ACCENT, Color.WHITE, ACCENT_LT);
    }
    public static JButton btnSuccess(String text) {
        return makeBtn(text, SUCCESS, Color.WHITE, new Color(0x34D399));
    }
    public static JButton btnDanger(String text) {
        return makeBtn(text, DANGER, Color.WHITE, new Color(0xF87171));
    }
    public static JButton btnSecondary(String text) {
        return makeBtn(text, BG_RAISED, TEXT_DIM, BORDER_LT);
    }
    public static JButton btnGhost(String text) {
        JButton b = makeBtn(text, new Color(0,0,0,0), MUTED, BG_RAISED);
        b.setBorder(new RoundedLineBorder(BORDER, 1, 6));
        return b;
    }
    public static JButton btnWarn(String text) {
        return makeBtn(text, WARN, new Color(0x1C1C1C), new Color(0xFBBF24));
    }
    public static JButton btnPurple(String text) {
        return makeBtn(text, new Color(0x6D28D9), Color.WHITE, ACCENT);
    }
    public static JButton btnCyan(String text) {
        return makeBtn(text, CYAN, new Color(0x0D1117), CYAN_LT);
    }

    private static JButton makeBtn(String text, Color bg, Color fg, Color hover) {
        JButton b = new JButton(text) {
            private boolean hovered = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = hovered ? hover : bg;
                g2.setColor(c);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(fontBold());
        b.setForeground(fg);
        b.setBackground(bg);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMargin(new Insets(8, 18, 8, 18));
        return b;
    }

    // ── ScrollPane ────────────────────────────────────────────────────────────
    public static JScrollPane cleanScroll(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));
        sp.getViewport().setBackground(BG_CARD);
        styleScrollBar(sp);
        return sp;
    }
    public static JScrollPane cardScroll(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(new RoundedLineBorder(BORDER, 1, RADIUS));
        sp.getViewport().setBackground(BG_CARD);
        styleScrollBar(sp);
        return sp;
    }
    private static void styleScrollBar(JScrollPane sp) {
        JScrollBar vsb = sp.getVerticalScrollBar();
        vsb.setBackground(BG_BASE);
        vsb.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            protected void configureScrollBarColors() {
                thumbColor = BORDER_LT;
                trackColor = BG_BASE;
            }
            protected JButton createDecreaseButton(int o) { return zeroBtn(); }
            protected JButton createIncreaseButton(int o) { return zeroBtn(); }
            private JButton zeroBtn() {
                JButton b = new JButton(); b.setPreferredSize(new Dimension(0,0)); return b;
            }
        });
    }

    // ── Separador ─────────────────────────────────────────────────────────────
    public static JSeparator sep() {
        JSeparator s = new JSeparator();
        s.setForeground(BORDER);
        return s;
    }

    // ── Cabeçalho de painel ───────────────────────────────────────────────────
    public static JPanel pageHeader(String icon, String title, String subtitle) {
        JPanel p = new JPanel(new BorderLayout(0, 3)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), RADIUS, RADIUS));
                // linha accent esquerda
                g2.setColor(ACCENT);
                g2.fill(new RoundRectangle2D.Float(0, 0, 4, getHeight(), 4, 4));
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(0, 0, 20, 0),
            new EmptyBorder(14, 18, 14, 18)
        ));

        JLabel ttl = new JLabel(icon + "  " + title);
        ttl.setFont(fontH1());
        ttl.setForeground(TEXT);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(fontSmall());
        sub.setForeground(MUTED);

        p.add(ttl, BorderLayout.CENTER);
        p.add(sub, BorderLayout.SOUTH);
        return p;
    }

    // ── RoundedLineBorder ─────────────────────────────────────────────────────
    public static class RoundedLineBorder extends AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int radius;
        public RoundedLineBorder(Color c, int t, int r) { color = c; thickness = t; radius = r; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.draw(new RoundRectangle2D.Float(x + .5f, y + .5f, w - 1, h - 1, radius, radius));
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(thickness, thickness, thickness, thickness); }
        @Override public Insets getBorderInsets(Component c, Insets i) {
            i.set(thickness, thickness, thickness, thickness); return i;
        }
    }

    // ── Badge de status ───────────────────────────────────────────────────────
    public static JLabel statusBadge(String text, Color bg, Color fg) {
        JLabel l = new JLabel(text, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(new Font(FONT, Font.BOLD, 11));
        l.setForeground(fg);
        l.setBackground(bg);
        l.setOpaque(false);
        l.setBorder(new EmptyBorder(3, 10, 3, 10));
        return l;
    }
}
