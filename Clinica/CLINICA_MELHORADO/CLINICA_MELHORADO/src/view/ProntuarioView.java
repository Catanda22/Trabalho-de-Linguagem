package view;

import dao.Conexao;
import dao.ConsultaDAO;
import dao.PacienteDao;
import dao.ProntuarioDAO;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import model.Consulta;
import model.Paciente;
import model.Prontuario;

public class ProntuarioView extends JFrame {

    private final JComboBox<Item> consultaCombo = new JComboBox<>();
    private final JTextArea diagnosticoArea = new JTextArea(4, 40);
    private final JTextArea prescricaoArea = new JTextArea(4, 40);
    private final JTextArea observacoesArea = new JTextArea(4, 40);
    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new Object[]{"ID", "Consulta", "Paciente", "Diagnostico", "Data"}, 0
    );
    private final JTable tabela = new JTable(modeloTabela);
    private int prontuarioSelecionado = 0;

    public ProntuarioView() {
        setTitle("Prontuarios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(980, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(criarFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(criarBotoes(), BorderLayout.SOUTH);
        InterfaceUsuario.aplicarUsuarioLogado(this);

        carregarConsultas();
        carregarTabela();
    }

    private JPanel criarFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Consulta realizada"), gbc);
        gbc.gridx = 1;
        panel.add(consultaCombo, gbc);

        adicionarArea(panel, gbc, 1, "Diagnostico", diagnosticoArea);
        adicionarArea(panel, gbc, 2, "Prescricao", prescricaoArea);
        adicionarArea(panel, gbc, 3, "Observacoes", observacoesArea);
        return panel;
    }

    private void adicionarArea(JPanel panel, GridBagConstraints gbc, int linha, String label, JTextArea area) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(new JScrollPane(area), gbc);
    }

    private JPanel criarBotoes() {
        JPanel panel = new JPanel();
        JButton salvar = new JButton("Salvar");
        JButton novo = new JButton("Novo");
        JButton voltar = new JButton("Voltar");

        salvar.addActionListener(e -> salvarProntuario());
        novo.addActionListener(e -> limparFormulario());
        voltar.addActionListener(e -> {
            new MenuPrincipal().setVisible(true);
            dispose();
        });

        panel.add(novo);
        panel.add(salvar);
        panel.add(voltar);
        return panel;
    }

    private void carregarConsultas() {
        consultaCombo.removeAllItems();
        try (Connection con = Conexao.getConnection()) {
            PacienteDao pacienteDAO = new PacienteDao();
            for (Consulta consulta : new ConsultaDAO(con).listarTodas()) {
                if (!"REALIZADA".equalsIgnoreCase(consulta.getStatus())) {
                    continue;
                }
                Paciente paciente = pacienteDAO.buscarPorId(consulta.getIdPaciente());
                String nomePaciente = paciente == null ? String.valueOf(consulta.getIdPaciente()) : paciente.getNome();
                consultaCombo.addItem(new Item(consulta.getIdConsulta(), consulta.getIdPaciente(),
                        "Consulta " + consulta.getIdConsulta() + " - " + nomePaciente));
            }
        } catch (Exception e) {
            mostrarErro(e);
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        try (Connection con = Conexao.getConnection()) {
            PacienteDao pacienteDAO = new PacienteDao();
            for (Item item : new ItemIterable(consultaCombo)) {
                for (Prontuario p : new ProntuarioDAO(con).listarPorPaciente(item.idPaciente)) {
                    Paciente paciente = pacienteDAO.buscarPorId(p.getIdPaciente());
                    modeloTabela.addRow(new Object[]{
                        p.getIdProntuario(),
                        p.getIdConsulta(),
                        paciente == null ? p.getIdPaciente() : paciente.getNome(),
                        p.getDiagnostico(),
                        p.getDataRegistro()
                    });
                }
            }
        } catch (Exception e) {
            mostrarErro(e);
        }
    }

    private void salvarProntuario() {
        Item consulta = (Item) consultaCombo.getSelectedItem();
        if (consulta == null) {
            JOptionPane.showMessageDialog(this, "Nao existe consulta realizada para gerar prontuario.");
            return;
        }

        try (Connection con = Conexao.getConnection()) {
            Prontuario p = new Prontuario();
            p.setIdProntuario(prontuarioSelecionado);
            p.setIdConsulta(consulta.id);
            p.setIdPaciente(consulta.idPaciente);
            p.setDiagnostico(diagnosticoArea.getText());
            p.setPrescricao(prescricaoArea.getText());
            p.setObservacoes(observacoesArea.getText());

            ProntuarioDAO dao = new ProntuarioDAO(con);
            if (prontuarioSelecionado == 0) {
                dao.salvar(p);
            } else {
                dao.atualizar(p);
            }

            JOptionPane.showMessageDialog(this, "Prontuario gravado com sucesso.");
            limparFormulario();
            carregarTabela();
        } catch (Exception e) {
            mostrarErro(e);
        }
    }

    private void limparFormulario() {
        prontuarioSelecionado = 0;
        diagnosticoArea.setText("");
        prescricaoArea.setText("");
        observacoesArea.setText("");
    }

    private void mostrarErro(Exception e) {
        JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new ProntuarioView().setVisible(true));
    }

    private static class Item {
        private final int id;
        private final int idPaciente;
        private final String nome;

        Item(int id, int idPaciente, String nome) {
            this.id = id;
            this.idPaciente = idPaciente;
            this.nome = nome;
        }

        @Override
        public String toString() {
            return nome;
        }
    }

    private static class ItemIterable implements Iterable<Item> {
        private final JComboBox<Item> combo;

        ItemIterable(JComboBox<Item> combo) {
            this.combo = combo;
        }

        public java.util.Iterator<Item> iterator() {
            java.util.List<Item> itens = new java.util.ArrayList<>();
            for (int i = 0; i < combo.getItemCount(); i++) {
                itens.add(combo.getItemAt(i));
            }
            return itens.iterator();
        }
    }
}
