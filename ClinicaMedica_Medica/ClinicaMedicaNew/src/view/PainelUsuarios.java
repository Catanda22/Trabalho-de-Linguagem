package view;

import dao.UsuarioDAO;
import model.Perfil;
import model.Usuario;
import util.Seguranca;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PainelUsuarios extends JPanel {

    private final UsuarioDAO dao = new UsuarioDAO();
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public PainelUsuarios() {
        setLayout(new BorderLayout());
        setBackground(DS.BG_BASE);
        setBorder(DS.panelBorder());
        construirUI();
        carregarDados();
    }

    private void construirUI() {
        add(DS.pageHeader("25A3", "Gestão de Usuários", "Contas de acesso e perfis"), BorderLayout.NORTH);

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bar.setOpaque(false);
        bar.setBorder(DS.emptyBorder(0, 0, 14, 0));

        JButton btnNovo     = DS.btnSuccess("Novo Usuário");
        JButton btnAtivar   = DS.btnGhost("Ativar/Inativar");
        JButton btnRecarr   = DS.btnGhost("Recarregar");

        btnNovo.addActionListener(e   -> novoUsuario());
        btnAtivar.addActionListener(e -> toggleAtivo());
        btnRecarr.addActionListener(e -> carregarDados());

        bar.add(btnNovo); bar.add(btnAtivar); bar.add(btnRecarr);

        String[] cols = {"ID","Nome","Login","Perfil","Ativo"};
        modeloTabela = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modeloTabela);
        DS.styleTable(tabela);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);
        tabela.getColumnModel().getColumn(4).setMaxWidth(60);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);
        center.add(bar, BorderLayout.NORTH);
        center.add(DS.cardScroll(tabela), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private void carregarDados() {
        modeloTabela.setRowCount(0);
        try {
            for (Usuario u : dao.listarTodos())
                modeloTabela.addRow(new Object[]{
                    u.getId(), u.getNome(), u.getLogin(),
                    u.getPerfil().getDescricao(), u.isAtivo() ? "Sim" : "Não"
                });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void novoUsuario() {
        JTextField txtNome  = new JTextField();
        JTextField txtLogin = new JTextField();
        JPasswordField txtSenha = new JPasswordField();
        JComboBox<Perfil> cbPerfil = new JComboBox<>(Perfil.values());

        DS.styleField(txtNome); DS.styleField(txtLogin); DS.styleField(txtSenha); DS.styleCombo(cbPerfil);

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 10));
        form.setBorder(DS.emptyBorder(10, 10, 10, 10));
        form.add(DS.label("Nome *:"));   form.add(txtNome);
        form.add(DS.label("Login *:"));  form.add(txtLogin);
        form.add(DS.label("Senha *:"));  form.add(txtSenha);
        form.add(DS.label("Perfil *:")); form.add(cbPerfil);

        int res = JOptionPane.showConfirmDialog(this, form, "Novo Usuário",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String nome  = txtNome.getText().trim();
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();
        if (nome.isEmpty() || login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario u = new Usuario();
        u.setNome(nome); u.setLogin(login);
        u.setSenhaHash(Seguranca.hashSenha(senha));
        u.setPerfil((Perfil) cbPerfil.getSelectedItem());
        u.setAtivo(true);

        try { dao.inserir(u); carregarDados(); }
        catch (SQLException e) { JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); }
    }

    private void toggleAtivo() {
        int l = tabela.getSelectedRow();
        if (l < 0) { JOptionPane.showMessageDialog(this, "Selecione um usuário."); return; }
        int id = (int) modeloTabela.getValueAt(l, 0);
        String ativo = (String) modeloTabela.getValueAt(l, 4);
        try {
            for (Usuario u : dao.listarTodos()) {
                if (u.getId() == id) {
                    u.setAtivo(!"Sim".equals(ativo));
                    dao.atualizar(u); break;
                }
            }
            carregarDados();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
