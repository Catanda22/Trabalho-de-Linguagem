package view;

import controller.ConsultaController;
import controller.MedicoController;
import controller.PacienteController;
import model.Consulta;
import model.Medico;
import model.Paciente;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PainelRelatorios extends JPanel {

    private final ConsultaController ctrlCons = new ConsultaController();
    private final MedicoController   ctrlMed  = new MedicoController();
    private final PacienteController ctrlPac  = new PacienteController();

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JComboBox<String> cbRelatorio;
    private JComboBox<Medico> cbMedico;
    private JTextField txtInicio, txtFim;
    private JLabel lblTotal;

    private static final DateTimeFormatter FMT_DT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FMT_D  = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PainelRelatorios() {
        setLayout(new BorderLayout(0, 0));
        setBackground(DS.BG_BASE);
        setBorder(DS.panelBorder());
        construirUI();
    }

    private void construirUI() {
        add(DS.pageHeader("25A3", "Relatórios Gerenciais", "Análise e exportação de dados"), BorderLayout.NORTH);

        // Painel de filtros
        JPanel filterCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        filterCard.setBackground(DS.BG_CARD);
        filterCard.setBorder(DS.filterBarBorder());

        cbRelatorio = new JComboBox<>(new String[]{
            "Consultas por Período",
            "Consultas por Médico e Período",
            "Pacientes Ativos"
        });
        DS.styleCombo(cbRelatorio);
        cbRelatorio.setPreferredSize(new Dimension(230, 34));

        cbMedico = new JComboBox<>();
        DS.styleCombo(cbMedico);
        cbMedico.setPreferredSize(new Dimension(200, 34));
        try { ctrlMed.listarAtivos().forEach(cbMedico::addItem); } catch (Exception ignored) {}

        txtInicio = new JTextField("01/06/2026", 10);
        txtFim    = new JTextField(LocalDate.now().format(FMT_D), 10);
        DS.styleField(txtInicio);
        DS.styleField(txtFim);
        txtInicio.setPreferredSize(new Dimension(110, 34));
        txtFim.setPreferredSize(new Dimension(110, 34));

        JButton btnGerar = DS.btnPrimary("Gerar Relatório");
        btnGerar.addActionListener(e -> gerarRelatorio());

        filterCard.add(DS.label("Relatório:")); filterCard.add(cbRelatorio);
        filterCard.add(DS.label("Médico:"));    filterCard.add(cbMedico);
        filterCard.add(DS.label("Início:"));    filterCard.add(txtInicio);
        filterCard.add(DS.label("Fim:"));       filterCard.add(txtFim);
        filterCard.add(btnGerar);

        // Rodapé da tabela com total
        lblTotal = DS.labelMuted("Aguardando relatório…");
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.LEFT));
        foot.setOpaque(false);
        foot.add(lblTotal);

        // Tabela
        modeloTabela = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modeloTabela);
        DS.styleTable(tabela);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);
        center.add(filterCard, BorderLayout.NORTH);
        center.add(DS.cardScroll(tabela), BorderLayout.CENTER);
        center.add(foot, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);
    }

    private void gerarRelatorio() {
        modeloTabela.setRowCount(0);
        String tipo = (String) cbRelatorio.getSelectedItem();
        try {
            LocalDate ini = LocalDate.parse(txtInicio.getText().trim(), FMT_D);
            LocalDate fim = LocalDate.parse(txtFim.getText().trim(), FMT_D);

            if ("Pacientes Ativos".equals(tipo)) {
                modeloTabela.setColumnIdentifiers(new String[]{"ID","Nome","CPF","Telefone","Cidade"});
                List<Paciente> lista = ctrlPac.listarAtivos();
                for (Paciente p : lista)
                    modeloTabela.addRow(new Object[]{p.getId(), p.getNome(), p.getBi(), p.getTelefone(), p.getCidade()});
                lblTotal.setText("Total: " + lista.size() + " paciente(s)");
                return;
            }

            List<Consulta> lista;
            if ("Consultas por Médico e Período".equals(tipo)) {
                Medico med = (Medico) cbMedico.getSelectedItem();
                if (med == null) { JOptionPane.showMessageDialog(this, "Selecione um médico.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
                lista = ctrlCons.listarPorMedicoEPeriodo(med.getId(), ini, fim);
            } else {
                lista = ctrlCons.listarPorPeriodo(ini, fim);
            }

            modeloTabela.setColumnIdentifiers(new String[]{"ID","Paciente","Médico","Especialidade","Data/Hora","Status"});
            for (Consulta c : lista)
                modeloTabela.addRow(new Object[]{
                    c.getId(), c.getNomePaciente(), c.getNomeMedico(),
                    c.getEspecialidade(), c.getDataHora().format(FMT_DT),
                    c.getStatus().getDescricao()
                });
            lblTotal.setText("Total: " + lista.size() + " registro(s) encontrado(s)");

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use dd/MM/yyyy", "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
