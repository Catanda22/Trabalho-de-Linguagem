package view;

import dao.Conexao;
import dao.ConsultaDAO;
import dao.MedicoDAO;
import dao.PacienteDao;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.Consulta;
import model.Medico;
import model.Paciente;

public class RelatorioView extends JFrame {

    private final JComboBox<Item> medicoCombo = new JComboBox<>();
    private final JTextField inicioField = new JTextField(10);
    private final JTextField fimField = new JTextField(10);
    private final DefaultTableModel modeloTabela = new DefaultTableModel();
    private final JTable tabela = new JTable(modeloTabela);

    public RelatorioView() {
        setTitle("Relatorios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(980, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(criarTopo(), BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        InterfaceUsuario.aplicarUsuarioLogado(this);
        carregarMedicos();
        relatorioPacientes();
    }

    private JPanel criarTopo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton pacientes = new JButton("Pacientes ativos");
        JButton consultas = new JButton("Consultas por medico");
        JButton voltar = new JButton("Voltar");

        pacientes.addActionListener(e -> relatorioPacientes());
        consultas.addActionListener(e -> relatorioConsultas());
        voltar.addActionListener(e -> {
            new MenuPrincipal().setVisible(true);
            dispose();
        });

        inicioField.setText("2026-01-01");
        fimField.setText("2026-12-31");

        panel.add(pacientes);
        panel.add(new JLabel("Medico"));
        panel.add(medicoCombo);
        panel.add(new JLabel("Inicio"));
        panel.add(inicioField);
        panel.add(new JLabel("Fim"));
        panel.add(fimField);
        panel.add(consultas);
        panel.add(voltar);
        return panel;
    }

    private void carregarMedicos() {
        medicoCombo.removeAllItems();
        try (Connection con = Conexao.getConnection()) {
            for (Medico m : new MedicoDAO(con).pesquisar("")) {
                medicoCombo.addItem(new Item(m.getIdMedico(), m.getNome()));
            }
        } catch (Exception e) {
            mostrarErro(e);
        }
    }

    private void relatorioPacientes() {
        modeloTabela.setColumnIdentifiers(new Object[]{"ID", "Nome", "BI", "Sexo", "Telefone", "Endereco"});
        modeloTabela.setRowCount(0);

        try {
            for (Paciente p : new PacienteDao().pesquisar("")) {
                modeloTabela.addRow(new Object[]{
                    p.getId_paciente(),
                    p.getNome(),
                    p.getBi(),
                    p.getSexo(),
                    p.getTelefone(),
                    p.getEndereco()
                });
            }
        } catch (Exception e) {
            mostrarErro(e);
        }
    }

    private void relatorioConsultas() {
        Item medico = (Item) medicoCombo.getSelectedItem();
        if (medico == null) {
            JOptionPane.showMessageDialog(this, "Cadastre pelo menos um medico.");
            return;
        }

        modeloTabela.setColumnIdentifiers(new Object[]{"ID", "Paciente", "Medico", "Data", "Hora", "Status"});
        modeloTabela.setRowCount(0);

        try (Connection con = Conexao.getConnection()) {
            ConsultaDAO consultaDAO = new ConsultaDAO(con);
            PacienteDao pacienteDAO = new PacienteDao();

            for (Consulta c : consultaDAO.listarPorMedicoPeriodo(
                    medico.id,
                    Date.valueOf(inicioField.getText().trim()),
                    Date.valueOf(fimField.getText().trim()))) {
                Paciente paciente = pacienteDAO.buscarPorId(c.getIdPaciente());
                modeloTabela.addRow(new Object[]{
                    c.getIdConsulta(),
                    paciente == null ? c.getIdPaciente() : paciente.getNome(),
                    medico.nome,
                    c.getDataConsulta(),
                    c.getHoraConsulta(),
                    c.getStatus()
                });
            }
        } catch (Exception e) {
            mostrarErro(e);
        }
    }

    private void mostrarErro(Exception e) {
        JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new RelatorioView().setVisible(true));
    }

    private static class Item {
        private final int id;
        private final String nome;

        Item(int id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        @Override
        public String toString() {
            return nome;
        }
    }
}
