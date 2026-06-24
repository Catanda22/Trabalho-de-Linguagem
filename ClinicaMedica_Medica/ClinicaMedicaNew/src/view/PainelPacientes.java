package view;

import controller.PacienteController;
import model.Paciente;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Painel de gestão de pacientes — design renovado.
 */
public class PainelPacientes extends JPanel {

    private final PacienteController ctrl = new PacienteController();
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JTextField txtBusca;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PainelPacientes() {
        setLayout(new BorderLayout());
        setBackground(DS.BG_BASE);
        setBorder(DS.panelBorder());
        construirUI();
        carregarDados("");
    }

    private void construirUI() {
        // Cabeçalho
        JPanel header = DS.pageHeader("25A3", "Gestão de Pacientes", "Cadastro e histórico de pacientes");
        add(header, BorderLayout.NORTH);

        // Barra de ações
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);
        bar.setBorder(DS.emptyBorder(0, 0, 14, 0));

        // Busca
        JPanel searchArea = new JPanel(new BorderLayout(6, 0));
        searchArea.setOpaque(false);
        txtBusca = new JTextField();
        txtBusca.setPreferredSize(new Dimension(250, 36));
        DS.styleField(txtBusca);
        txtBusca.setToolTipText("Buscar por nome ou BI");
        txtBusca.addActionListener(e -> carregarDados(txtBusca.getText()));
        JButton btnBuscar = DS.btnPrimary("Buscar");
        btnBuscar.addActionListener(e -> carregarDados(txtBusca.getText()));
        searchArea.add(txtBusca, BorderLayout.CENTER);
        searchArea.add(btnBuscar, BorderLayout.EAST);

        // Botões de ação
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton btnNovo     = DS.btnSuccess("Novo Paciente");
        JButton btnEditar   = DS.btnGhost("Editar");
        JButton btnInativar = DS.btnDanger("Inativar");
        btnNovo.addActionListener(e     -> abrirFormulario(null));
        btnEditar.addActionListener(e   -> editarSelecionado());
        btnInativar.addActionListener(e -> inativarSelecionado());
        actions.add(btnNovo);
        actions.add(btnEditar);
        actions.add(btnInativar);

        bar.add(searchArea, BorderLayout.WEST);
        bar.add(actions, BorderLayout.EAST);

        // Tabela
        String[] cols = {"ID","Nome","BI","Nascimento","Sexo","Telefone","Cidade","Ativo"};
        modeloTabela = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modeloTabela);
        estilizarTabela(tabela);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);
        tabela.getColumnModel().getColumn(4).setMaxWidth(55);
        tabela.getColumnModel().getColumn(7).setMaxWidth(60);

        JScrollPane scroll = DS.cardScroll(tabela);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);
        center.add(bar, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private void carregarDados(String filtro) {
        modeloTabela.setRowCount(0);
        try {
            List<Paciente> lista = ctrl.buscar(filtro);
            for (Paciente p : lista)
                modeloTabela.addRow(new Object[]{
                    p.getId(), p.getNome(), p.getBi(),
                    p.getDataNascimento().format(FMT),
                    String.valueOf(p.getSexo()), p.getTelefone(),
                    p.getCidade(), p.isAtivo() ? "Sim" : "Não"
                });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirFormulario(Paciente paciente) {
        FormularioPaciente form = new FormularioPaciente((JFrame) SwingUtilities.getWindowAncestor(this), paciente);
        form.setVisible(true);
        if (form.isSalvo()) carregarDados(txtBusca.getText());
    }

    private void editarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) { avisar("Selecione um paciente."); return; }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        try { ctrl.buscarPorId(id).ifPresent(this::abrirFormulario); }
        catch (Exception e) { erro(e.getMessage()); }
    }

    private void inativarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) { avisar("Selecione um paciente."); return; }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        String nome = (String) modeloTabela.getValueAt(linha, 1);
        int conf = JOptionPane.showConfirmDialog(this,
            "Inativar o paciente \"" + nome + "\"?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try { ctrl.inativar(id); carregarDados(txtBusca.getText()); }
            catch (Exception e) { erro(e.getMessage()); }
        }
    }

    // ── Utilitários compartilhados (mantidos para compatibilidade) ────────────
    static void estilizarTabela(JTable t) { DS.styleTable(t); }

    static JButton btnVerde(String t)    { return DS.btnSuccess(t); }
    static JButton btnAzul(String t)     { return DS.btnPrimary(t); }
    static JButton btnCinza(String t)    { return DS.btnGhost(t); }
    static JButton btnVermelho(String t) { return DS.btnDanger(t); }

    private void avisar(String msg) { JOptionPane.showMessageDialog(this, msg, "Aviso",  JOptionPane.WARNING_MESSAGE); }
    private void erro(String msg)   { JOptionPane.showMessageDialog(this, msg, "Erro",   JOptionPane.ERROR_MESSAGE); }
}
