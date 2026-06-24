package view;

import controller.ProntuarioController;
import model.Prontuario;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PainelProntuarios extends JPanel {

    private final ProntuarioController ctrl = new ProntuarioController();
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public PainelProntuarios() {
        setLayout(new BorderLayout());
        setBackground(DS.BG_BASE);
        setBorder(DS.panelBorder());
        construirUI();
        carregarDados();
    }

    private void construirUI() {
        add(DS.pageHeader("25A3", "Prontuários Eletrônicos", "Diagnósticos e prescrições"), BorderLayout.NORTH);

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bar.setOpaque(false);
        bar.setBorder(DS.emptyBorder(0, 0, 14, 0));

        JButton btnNovo       = DS.btnSuccess("Novo Prontuário");
        JButton btnVisualizar = DS.btnPrimary("Visualizar");
        JButton btnEditar     = DS.btnGhost("Editar");
        JButton btnRecarr     = DS.btnGhost("Actualizar");

        btnNovo.addActionListener(e       -> abrirFormulario(null));
        btnVisualizar.addActionListener(e -> visualizarSelecionado());
        btnEditar.addActionListener(e     -> editarSelecionado());
        btnRecarr.addActionListener(e     -> carregarDados());

        bar.add(btnNovo); bar.add(btnVisualizar); bar.add(btnEditar); bar.add(btnRecarr);

        String[] cols = {"ID","Paciente","Médico","Data Consulta","Diagnóstico (resumo)"};
        modeloTabela = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modeloTabela);
        DS.styleTable(tabela);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);
        center.add(bar, BorderLayout.NORTH);
        center.add(DS.cardScroll(tabela), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private void carregarDados() {
        modeloTabela.setRowCount(0);
        try {
            for (Prontuario p : ctrl.listarTodos()) {
                String diag = p.getDiagnostico() != null && p.getDiagnostico().length() > 60
                    ? p.getDiagnostico().substring(0, 60) + "…" : p.getDiagnostico();
                modeloTabela.addRow(new Object[]{
                    p.getId(), p.getNomePaciente(), p.getNomeMedico(),
                    p.getDataConsulta().format(FMT), diag
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirFormulario(Prontuario p) {
        FormularioProntuario form = new FormularioProntuario((JFrame) SwingUtilities.getWindowAncestor(this), p);
        form.setVisible(true);
        if (form.isSalvo()) carregarDados();
    }

    private void editarSelecionado() {
        int l = tabela.getSelectedRow();
        if (l < 0) { avisar("Selecione um prontuário."); return; }
        try {
            List<Prontuario> lista = ctrl.listarTodos();
            if (l < lista.size()) abrirFormulario(lista.get(l));
        } catch (Exception e) { erro(e.getMessage()); }
    }

    private void visualizarSelecionado() {
        int l = tabela.getSelectedRow();
        if (l < 0) { avisar("Selecione um prontuário."); return; }
        try {
            List<Prontuario> lista = ctrl.listarTodos();
            if (l >= lista.size()) return;
            Prontuario p = lista.get(l);

            // Diálogo de visualização com layout cuidadoso
            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            content.setBackground(DS.BG_CARD);
            content.setBorder(DS.emptyBorder(16, 20, 16, 20));

            content.add(metaRow("Paciente", p.getNomePaciente()));
            content.add(metaRow("Médico",   p.getNomeMedico()));
            content.add(metaRow("Data",     p.getDataConsulta().format(FMT)));
            content.add(Box.createVerticalStrut(14));
            content.add(section("DIAGNÓSTICO", p.getDiagnostico()));
            content.add(Box.createVerticalStrut(10));
            content.add(section("PRESCRIÇÃO", p.getPrescricao() != null ? p.getPrescricao() : "—"));
            content.add(Box.createVerticalStrut(10));
            content.add(section("OBSERVAÇÕES", p.getObservacoes() != null ? p.getObservacoes() : "—"));

            JScrollPane sp = new JScrollPane(content);
            sp.setPreferredSize(new Dimension(520, 420));
            sp.setBorder(null);

            JOptionPane.showMessageDialog(this, sp,
                "Prontuário — " + p.getNomePaciente(), JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) { erro(e.getMessage()); }
    }

    private JPanel metaRow(String label, String value) {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setOpaque(false);
        p.setBorder(DS.emptyBorder(3, 0, 3, 0));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        JLabel lbl = DS.labelMuted(label + ":");
        lbl.setPreferredSize(new Dimension(70, 20));
        JLabel val = DS.label(value);
        p.add(lbl, BorderLayout.WEST);
        p.add(val, BorderLayout.CENTER);
        return p;
    }

    private JPanel section(String title, String text) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel hdr = new JLabel(title);
        hdr.setFont(new Font(DS.FONT, Font.BOLD, 10));
        hdr.setForeground(DS.MUTED);
        hdr.setBorder(DS.emptyBorder(0, 0, 4, 0));

        JTextArea ta = new JTextArea(text);
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFont(DS.fontBody());
        ta.setForeground(DS.TEXT);
        ta.setBackground(DS.BG_BASE);
        ta.setBorder(DS.emptyBorder(8, 10, 8, 10));
        ta.setRows(3);

        p.add(hdr, BorderLayout.NORTH);
        p.add(ta, BorderLayout.CENTER);
        return p;
    }

    private void avisar(String m) { JOptionPane.showMessageDialog(this, m, "Aviso", JOptionPane.WARNING_MESSAGE); }
    private void erro(String m)   { JOptionPane.showMessageDialog(this, m, "Erro",  JOptionPane.ERROR_MESSAGE); }
}
