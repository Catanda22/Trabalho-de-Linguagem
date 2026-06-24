package view;

import dao.Conexao;
import dao.ConsultaDAO;
import dao.MedicoDAO;
import dao.PacienteDao;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.Consulta;
import model.Medico;
import model.Paciente;

public class Agendar extends JFrame {

    private final JComboBox<Item> pacienteCombo = new JComboBox<>();
    private final JComboBox<Item> medicoCombo = new JComboBox<>();
    private final JComboBox<String> statusCombo = new JComboBox<>(new String[]{"AGENDADA", "CONFIRMADA", "REALIZADA", "CANCELADA"});
    private final JTextField dataField = new JTextField();
    private final JTextField horaField = new JTextField();
    private final JTextArea observacaoArea = new JTextArea(3, 30);
    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new Object[]{"ID", "Paciente", "Medico", "Data", "Hora", "Status"}, 0
    );
    private final JTable tabela = new JTable(modeloTabela);
    private int consultaSelecionada = 0;

    public Agendar() {
        setTitle("Agendamento de Consultas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(980, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(criarFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(criarBotoes(), BorderLayout.SOUTH);
        InterfaceUsuario.aplicarUsuarioLogado(this);

        tabela.getSelectionModel().addListSelectionListener(e -> carregarSelecionado());
        carregarCombos();
        carregarTabela();
    }

    private JPanel criarFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        adicionarCampo(panel, gbc, 0, "Paciente", pacienteCombo);
        adicionarCampo(panel, gbc, 1, "Medico", medicoCombo);
        adicionarCampo(panel, gbc, 2, "Data (yyyy-mm-dd)", dataField);
        adicionarCampo(panel, gbc, 3, "Hora (HH:mm)", horaField);
        adicionarCampo(panel, gbc, 4, "Status", statusCombo);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Observacao"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(new JScrollPane(observacaoArea), gbc);
        gbc.gridwidth = 1;

        return panel;
    }

    private JPanel criarBotoes() {
        JPanel panel = new JPanel();
        JButton salvar = new JButton("Salvar");
        JButton novo = new JButton("Novo");
        JButton cancelar = new JButton("Cancelar consulta");
        JButton voltar = new JButton("Voltar");

        salvar.addActionListener(e -> salvarConsulta());
        novo.addActionListener(e -> limparFormulario());
        cancelar.addActionListener(e -> cancelarConsulta());
        voltar.addActionListener(e -> {
            new MenuPrincipal().setVisible(true);
            dispose();
        });

        panel.add(novo);
        panel.add(salvar);
        panel.add(cancelar);
        panel.add(voltar);
        return panel;
    }

    private void adicionarCampo(JPanel panel, GridBagConstraints gbc, int linha, String label, java.awt.Component component) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(component, gbc);
    }

    private void carregarCombos() {
        pacienteCombo.removeAllItems();
        medicoCombo.removeAllItems();

        try (Connection con = Conexao.getConnection()) {
            for (Paciente p : new PacienteDao().pesquisar("")) {
                pacienteCombo.addItem(new Item(p.getId_paciente(), p.getNome()));
            }
            for (Medico m : new MedicoDAO(con).pesquisar("")) {
                medicoCombo.addItem(new Item(m.getIdMedico(), m.getNome()));
            }
        } catch (Exception e) {
            mostrarErro(e);
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        try (Connection con = Conexao.getConnection()) {
            ConsultaDAO consultaDAO = new ConsultaDAO(con);
            PacienteDao pacienteDAO = new PacienteDao();
            MedicoDAO medicoDAO = new MedicoDAO(con);
            List<Consulta> consultas = consultaDAO.listarTodas();

            for (Consulta c : consultas) {
                Paciente paciente = pacienteDAO.buscarPorId(c.getIdPaciente());
                Medico medico = medicoDAO.buscarPorId(c.getIdMedico());
                modeloTabela.addRow(new Object[]{
                    c.getIdConsulta(),
                    paciente == null ? c.getIdPaciente() : paciente.getNome(),
                    medico == null ? c.getIdMedico() : medico.getNome(),
                    c.getDataConsulta(),
                    c.getHoraConsulta(),
                    c.getStatus()
                });
            }
        } catch (Exception e) {
            mostrarErro(e);
        }
    }

    private void carregarSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            return;
        }

        consultaSelecionada = Integer.parseInt(modeloTabela.getValueAt(row, 0).toString());
        dataField.setText(String.valueOf(modeloTabela.getValueAt(row, 3)));
        horaField.setText(String.valueOf(modeloTabela.getValueAt(row, 4)).substring(0, 5));
        statusCombo.setSelectedItem(String.valueOf(modeloTabela.getValueAt(row, 5)));
    }

    private void salvarConsulta() {
        try (Connection con = Conexao.getConnection()) {
            Item paciente = (Item) pacienteCombo.getSelectedItem();
            Item medico = (Item) medicoCombo.getSelectedItem();
            if (paciente == null || medico == null) {
                JOptionPane.showMessageDialog(this, "Cadastre pelo menos um paciente e um medico.");
                return;
            }

            Consulta consulta = new Consulta();
            consulta.setIdConsulta(consultaSelecionada);
            consulta.setIdPaciente(paciente.id);
            consulta.setIdMedico(medico.id);
            consulta.setDataConsulta(Date.valueOf(dataField.getText().trim()));
            consulta.setHoraConsulta(Time.valueOf(horaField.getText().trim() + ":00"));
            consulta.setStatus(statusCombo.getSelectedItem().toString());
            consulta.setObservacao(observacaoArea.getText());

            ConsultaDAO dao = new ConsultaDAO(con);
            if (consultaSelecionada == 0) {
                dao.salvar(consulta);
            } else {
                dao.atualizar(consulta);
            }

            JOptionPane.showMessageDialog(this, "Consulta gravada com sucesso.");
            limparFormulario();
            carregarTabela();
        } catch (Exception e) {
            mostrarErro(e);
        }
    }

    private void cancelarConsulta() {
        if (consultaSelecionada == 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma consulta.");
            return;
        }

        try (Connection con = Conexao.getConnection()) {
            new ConsultaDAO(con).cancelar(consultaSelecionada);
            limparFormulario();
            carregarTabela();
            JOptionPane.showMessageDialog(this, "Consulta cancelada.");
        } catch (Exception e) {
            mostrarErro(e);
        }
    }

    private void limparFormulario() {
        consultaSelecionada = 0;
        dataField.setText("");
        horaField.setText("");
        observacaoArea.setText("");
        statusCombo.setSelectedIndex(0);
        tabela.clearSelection();
    }

    private void mostrarErro(Exception e) {
        JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new Agendar().setVisible(true));
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
