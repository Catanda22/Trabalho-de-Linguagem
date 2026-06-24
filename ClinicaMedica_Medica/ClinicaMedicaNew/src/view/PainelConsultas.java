package view;

import controller.ConsultaController;
import model.Consulta;
import model.StatusConsulta;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PainelConsultas extends JPanel {

    private final ConsultaController ctrl = new ConsultaController();
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public PainelConsultas() {
        setLayout(new BorderLayout());
        setBackground(DS.BG_BASE);
        setBorder(DS.panelBorder());
        construirUI();
        carregarDados();
    }

    private void construirUI() {
        add(DS.pageHeader("25A3", "Gestão de Agendamentos", "Consultas e acompanhamento de status"), BorderLayout.NORTH);

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bar.setOpaque(false);
        bar.setBorder(DS.emptyBorder(0, 0, 14, 0));

        JButton btnNovo      = DS.btnSuccess("Agendar");
        JButton btnEditar    = DS.btnGhost("Editar");
        JButton btnConfirmar = DS.btnPrimary("Confirmar");
        JButton btnRealizada = DS.btnPurple("Realizada");
        JButton btnCancelar  = DS.btnDanger("Cancelar");
        JButton btnRecarr    = DS.btnGhost("Actualizar");

        btnNovo.addActionListener(e      -> abrirFormulario(null));
        btnEditar.addActionListener(e    -> editarSelecionado());
        btnConfirmar.addActionListener(e -> mudarStatus(StatusConsulta.CONFIRMADA, "Confirmar esta consulta?"));
        btnRealizada.addActionListener(e -> mudarStatus(StatusConsulta.REALIZADA,  "Marcar como realizada?"));
        btnCancelar.addActionListener(e  -> mudarStatus(StatusConsulta.CANCELADA,  "Cancelar esta consulta?"));
        btnRecarr.addActionListener(e    -> carregarDados());

        bar.add(btnNovo); bar.add(btnEditar); bar.add(btnConfirmar);
        bar.add(btnRealizada); bar.add(btnCancelar); bar.add(btnRecarr);

        String[] cols = {"ID","Paciente","Médico","Especialidade","Data/Hora","Status","Observação"};
        modeloTabela = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modeloTabela);
        DS.styleTable(tabela);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);
        tabela.getColumnModel().getColumn(5).setMaxWidth(100);

        // Renderer de status com badge colorido
        tabela.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                setFont(new Font(DS.FONT, Font.BOLD, 11));
                if (!sel) {
                    String s = v != null ? v.toString() : "";
                    switch (s) {
                        case "Agendada":  setBackground(DS.STATUS_AGENDADA);  setForeground(new Color(0x92400E)); break;
                        case "Confirmada":setBackground(DS.STATUS_CONFIRMADA);setForeground(DS.BLUE);             break;
                        case "Realizada": setBackground(DS.STATUS_REALIZADA); setForeground(DS.TEAL);             break;
                        case "Cancelada": setBackground(DS.STATUS_CANCELADA); setForeground(DS.DANGER);           break;
                        default:          setBackground(DS.BG_CARD);          setForeground(DS.TEXT);             break;
                    }
                }
                return this;
            }
        });

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);
        center.add(bar, BorderLayout.NORTH);
        center.add(DS.cardScroll(tabela), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    void carregarDados() {
        modeloTabela.setRowCount(0);
        try {
            for (Consulta c : ctrl.listarTodas())
                modeloTabela.addRow(new Object[]{
                    c.getId(), c.getNomePaciente(), c.getNomeMedico(),
                    c.getEspecialidade(), c.getDataHora().format(FMT),
                    c.getStatus().getDescricao(), c.getObservacao()
                });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirFormulario(Consulta consulta) {
        FormularioConsulta form = new FormularioConsulta((JFrame) SwingUtilities.getWindowAncestor(this), consulta);
        form.setVisible(true);
        if (form.isSalvo()) carregarDados();
    }

    private void editarSelecionado() {
        int l = tabela.getSelectedRow();
        if (l < 0) { avisar("Selecione uma consulta."); return; }
        try { ctrl.buscarPorId((int) modeloTabela.getValueAt(l, 0)).ifPresent(this::abrirFormulario); }
        catch (Exception e) { erro(e.getMessage()); }
    }

    private void mudarStatus(StatusConsulta novo, String msg) {
        int l = tabela.getSelectedRow();
        if (l < 0) { avisar("Selecione uma consulta."); return; }
        if (JOptionPane.showConfirmDialog(this, msg, "Confirmar", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        int id = (int) modeloTabela.getValueAt(l, 0);
        try {
            switch (novo) {
                case CONFIRMADA: ctrl.confirmar(id);      break;
                case REALIZADA:  ctrl.marcarRealizada(id);break;
                case CANCELADA:  ctrl.cancelar(id);       break;
                default: break;
            }
            carregarDados();
        } catch (Exception e) { erro(e.getMessage()); }
    }

    private void avisar(String m) { JOptionPane.showMessageDialog(this, m, "Aviso", JOptionPane.WARNING_MESSAGE); }
    private void erro(String m)   { JOptionPane.showMessageDialog(this, m, "Erro",  JOptionPane.ERROR_MESSAGE); }
}
