package view;

import controller.MedicoController;
import model.Medico;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class PainelMedicos extends JPanel {

    private final MedicoController ctrl = new MedicoController();
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JTextField txtBusca;

    public PainelMedicos() {
        setLayout(new BorderLayout());
        setBackground(DS.BG_BASE);
        setBorder(DS.panelBorder());
        construirUI();
        carregarDados("");
    }

    private void construirUI() {
        add(DS.pageHeader("25A3", "Gestão de Médicos", "Cadastro e especialidades médicas"), BorderLayout.NORTH);

        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);
        bar.setBorder(DS.emptyBorder(0, 0, 14, 0));

        JPanel searchArea = new JPanel(new BorderLayout(6, 0));
        searchArea.setOpaque(false);
        txtBusca = new JTextField();
        txtBusca.setPreferredSize(new Dimension(250, 36));
        DS.styleField(txtBusca);
        txtBusca.addActionListener(e -> carregarDados(txtBusca.getText()));
        JButton btnBuscar = DS.btnPrimary("Buscar");
        btnBuscar.addActionListener(e -> carregarDados(txtBusca.getText()));
        searchArea.add(txtBusca, BorderLayout.CENTER);
        searchArea.add(btnBuscar, BorderLayout.EAST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton btnNovo     = DS.btnSuccess("Novo Médico");
        JButton btnEditar   = DS.btnGhost("Editar");
        JButton btnInativar = DS.btnDanger("Inativar");
        btnNovo.addActionListener(e     -> abrirFormulario(null));
        btnEditar.addActionListener(e   -> editarSelecionado());
        btnInativar.addActionListener(e -> inativarSelecionado());
        actions.add(btnNovo); actions.add(btnEditar); actions.add(btnInativar);

        bar.add(searchArea, BorderLayout.WEST);
        bar.add(actions, BorderLayout.EAST);

        String[] cols = {"ID","Nome","CRM","Especialidade","Telefone","E-mail","Ativo"};
        modeloTabela = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modeloTabela);
        DS.styleTable(tabela);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);
        tabela.getColumnModel().getColumn(6).setMaxWidth(60);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);
        center.add(bar, BorderLayout.NORTH);
        center.add(DS.cardScroll(tabela), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private void carregarDados(String filtro) {
        modeloTabela.setRowCount(0);
        try {
            for (Medico m : ctrl.buscar(filtro))
                modeloTabela.addRow(new Object[]{
                    m.getId(), m.getNome(), m.getCrm(), m.getEspecialidade(),
                    m.getTelefone(), m.getEmail(), m.isAtivo() ? "Sim" : "Não"
                });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirFormulario(Medico medico) {
        FormularioMedico form = new FormularioMedico((JFrame) SwingUtilities.getWindowAncestor(this), medico);
        form.setVisible(true);
        if (form.isSalvo()) carregarDados(txtBusca.getText());
    }

    private void editarSelecionado() {
        int l = tabela.getSelectedRow();
        if (l < 0) { JOptionPane.showMessageDialog(this, "Selecione um médico.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        try { ctrl.buscarPorId((int) modeloTabela.getValueAt(l, 0)).ifPresent(this::abrirFormulario); }
        catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); }
    }

    private void inativarSelecionado() {
        int l = tabela.getSelectedRow();
        if (l < 0) { JOptionPane.showMessageDialog(this, "Selecione um médico.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        int id = (int) modeloTabela.getValueAt(l, 0);
        String nome = (String) modeloTabela.getValueAt(l, 1);
        if (JOptionPane.showConfirmDialog(this, "Inativar \"" + nome + "\"?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try { ctrl.inativar(id); carregarDados(txtBusca.getText()); }
            catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); }
        }
    }
}
