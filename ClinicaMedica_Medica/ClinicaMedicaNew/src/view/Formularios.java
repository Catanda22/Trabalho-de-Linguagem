package view;

import controller.ConsultaController;
import controller.MedicoController;
import controller.PacienteController;
import controller.ProntuarioController;
import model.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// ─────────────────────────────────────────────────────────────────────────────
// Base de diálogo estilizado
// ─────────────────────────────────────────────────────────────────────────────
class BaseDialog extends JDialog {

    protected BaseDialog(JFrame pai, String titulo) {
        super(pai, titulo, true);
        setResizable(false);
    }

    protected JPanel buildRoot() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(DS.BG_CARD);
        setContentPane(root);
        return root;
    }

    protected JPanel dialogHeader(String icon, String titulo) {
        JPanel h = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(DS.NAVY);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        h.setOpaque(false);
        h.setBorder(DS.emptyBorder(16, 20, 16, 20));
        JLabel l = new JLabel(icon + "  " + titulo);
        l.setFont(DS.fontH3());
        l.setForeground(Color.WHITE);
        h.add(l, BorderLayout.WEST);
        return h;
    }

    protected JPanel formPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(DS.BG_CARD);
        p.setBorder(DS.emptyBorder(20, 24, 10, 24));
        return p;
    }

    protected GridBagConstraints gbc(int x, int y, int w) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x; c.gridy = y; c.gridwidth = w;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(5, 6, 5, 6);
        return c;
    }

    protected JPanel footer(JButton... btns) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 14));
        p.setBackground(DS.BG_BASE);
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, DS.BORDER));
        for (JButton b : btns) p.add(b);
        return p;
    }

    protected JPanel fieldGroup(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        p.add(DS.label(label), BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    protected JScrollPane styledScrollArea(JTextArea ta) {
        JScrollPane sp = new JScrollPane(ta);
        sp.setBorder(new DS.RoundedLineBorder(DS.BORDER, 1, 6));
        return sp;
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// FormularioMedico
// ─────────────────────────────────────────────────────────────────────────────
class FormularioMedico extends BaseDialog {

    private final MedicoController ctrl = new MedicoController();
    private Medico medico;
    private boolean salvo = false;

    private JTextField txtNome, txtCrm, txtEspecialidade, txtTelefone, txtEmail;

    FormularioMedico(JFrame pai, Medico m) {
        super(pai, m == null ? "Novo Médico" : "Editar Médico");
        this.medico = m == null ? new Medico() : m;
        setSize(500, 380);
        setLocationRelativeTo(pai);
        construirUI();
        if (m != null) preencherCampos();
    }

    private void construirUI() {
        JPanel root = buildRoot();
        root.add(dialogHeader("", medico.getId() == 0 ? "Novo Médico" : "Editar Médico"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5, 2, 12, 12));
        form.setBackground(DS.BG_CARD);
        form.setBorder(DS.emptyBorder(24, 24, 16, 24));

        txtNome          = styledField();
        txtCrm           = styledField();
        txtEspecialidade = styledField();
        txtTelefone      = styledField();
        txtEmail         = styledField();

        form.add(DS.label("Nome *"));          form.add(txtNome);
        form.add(DS.label("CRM *"));           form.add(txtCrm);
        form.add(DS.label("Especialidade *")); form.add(txtEspecialidade);
        form.add(DS.label("Telefone"));        form.add(txtTelefone);
        form.add(DS.label("E-mail"));          form.add(txtEmail);

        JButton btnSalvar   = DS.btnSuccess("Salvar");
        JButton btnCancelar = DS.btnGhost("Cancelar");
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        root.add(form, BorderLayout.CENTER);
        root.add(footer(btnCancelar, btnSalvar), BorderLayout.SOUTH);
    }

    private JTextField styledField() {
        JTextField f = new JTextField();
        DS.styleField(f);
        return f;
    }

    private void preencherCampos() {
        txtNome.setText(medico.getNome());
        txtCrm.setText(medico.getCrm());
        txtEspecialidade.setText(medico.getEspecialidade());
        txtTelefone.setText(medico.getTelefone());
        txtEmail.setText(medico.getEmail());
    }

    private void salvar() {
        try {
            medico.setNome(txtNome.getText().trim());
            medico.setCrm(txtCrm.getText().trim());
            medico.setEspecialidade(txtEspecialidade.getText().trim());
            medico.setTelefone(txtTelefone.getText().trim());
            medico.setEmail(txtEmail.getText().trim());
            medico.setAtivo(true);
            if (medico.getId() == 0) ctrl.cadastrar(medico);
            else ctrl.atualizar(medico);
            salvo = true;
            JOptionPane.showMessageDialog(this, "Médico salvo com sucesso!");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSalvo() { return salvo; }
}

// ─────────────────────────────────────────────────────────────────────────────
// FormularioPaciente  (BI em vez de CPF · sem logradouro/número)
// ─────────────────────────────────────────────────────────────────────────────
class FormularioPaciente extends BaseDialog {

    private final PacienteController ctrl = new PacienteController();
    private Paciente paciente;
    private boolean salvo = false;

    // Campos de dados pessoais
    private JTextField        txtNome, txtTelefone, txtEmail;
    private JFormattedTextField txtBi, txtNascimento;
    private JComboBox<String> cbSexo;

    // Endereço simplificado (sem logradouro e número)
    private JTextField txtBairro, txtCidade;

    private JTextArea txtHistorico;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Máscara do BI angolano: 9 dígitos + 2 letras + 3 dígitos  →  000000000AA000 */
   private static final String MASCARA_BI = "#########UU###";

    FormularioPaciente(JFrame pai, Paciente p) {
        super(pai, p == null ? "Novo Paciente" : "Editar Paciente");
        this.paciente = p == null ? new Paciente() : p;
        setSize(580, 560);
        setLocationRelativeTo(pai);
        construirUI();
        if (p != null) preencherCampos();
    }

    private void construirUI() {
        JPanel root = buildRoot();
        root.add(dialogHeader("", paciente.getId() == 0 ? "Novo Paciente" : "Editar Paciente"),
                BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(DS.BG_CARD);
        form.setBorder(DS.emptyBorder(20, 24, 10, 24));

        // ── campos ──────────────────────────────────────────────
        txtNome = tf();

        // BI angolano com máscara
        try {
            MaskFormatter mBi = new MaskFormatter(MASCARA_BI);
            mBi.setPlaceholderCharacter('_');
            mBi.setValidCharacters("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
            txtBi = new JFormattedTextField(mBi);
        } catch (ParseException e) {
            txtBi = new JFormattedTextField();
        }
        DS.styleField(txtBi);

        // Data de nascimento
        try {
            MaskFormatter mDt = new MaskFormatter("##/##/####");
            mDt.setPlaceholderCharacter('_');
            txtNascimento = new JFormattedTextField(mDt);
        } catch (ParseException e) {
            txtNascimento = new JFormattedTextField();
        }
        DS.styleField(txtNascimento);

        cbSexo    = new JComboBox<>(new String[]{"M - Masculino", "F - Feminino", "O - Outro"});
        DS.styleCombo(cbSexo);

        txtTelefone = tf();
        txtEmail    = tf();
        txtBairro   = tf();
        txtCidade   = tf();

        txtHistorico = new JTextArea(4, 20);
        DS.styleField(txtHistorico);

        // ── layout ──────────────────────────────────────────────
        int r = 0;
        addRow(form,  r++, "Nome *",              txtNome,       0, 5);
        addRow2(form, r++, "BI *",                txtBi,
                            "Nascimento *",       txtNascimento);
        addRow2(form, r++, "Sexo",                cbSexo,
                            "Telefone",           txtTelefone);
        addRow(form,  r++, "E-mail",              txtEmail,      0, 5);
        addRow2(form, r++, "Bairro",              txtBairro,
                            "Cidade",             txtCidade);
        addRow(form,  r,   "Histórico Médico",
                           new JScrollPane(txtHistorico),        0, 5);

        JButton btnSalvar   = DS.btnSuccess("Salvar");
        JButton btnCancelar = DS.btnGhost("Cancelar");
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        root.add(new JScrollPane(form), BorderLayout.CENTER);
        root.add(footer(btnCancelar, btnSalvar), BorderLayout.SOUTH);
    }

    private JTextField tf() {
        JTextField f = new JTextField();
        DS.styleField(f);
        return f;
    }

    private void addRow(JPanel p, int row, String lbl, JComponent comp, int col, int span) {
        GridBagConstraints c = gbc(col, row, 1);
        c.weightx = 0.3;
        p.add(DS.label(lbl), c);
        c = gbc(col + 1, row, span - 1);
        c.weightx = 0.7;
        p.add(comp, c);
    }

    private void addRow2(JPanel p, int row,
                         String lbl1, JComponent c1,
                         String lbl2, JComponent c2) {
        GridBagConstraints c = gbc(0, row, 1); c.weightx = 0.2; p.add(DS.label(lbl1), c);
        c = gbc(1, row, 2);                    c.weightx = 0.3; p.add(c1, c);
        c = gbc(3, row, 1);                    c.weightx = 0.2; p.add(DS.label(lbl2), c);
        c = gbc(4, row, 2);                    c.weightx = 0.3; p.add(c2, c);
    }

    private void preencherCampos() {
        txtNome.setText(paciente.getNome());
        // usa getBi() caso o model tenha sido renomeado, senão getCpf()
        ((JTextField) txtBi).setText(paciente.getBi());
        txtNascimento.setText(paciente.getDataNascimento().format(FMT));
        String sx = String.valueOf(paciente.getSexo());
        for (int i = 0; i < cbSexo.getItemCount(); i++)
            if (cbSexo.getItemAt(i).startsWith(sx)) { cbSexo.setSelectedIndex(i); break; }
        txtTelefone.setText(paciente.getTelefone());
        txtEmail.setText(paciente.getEmail());
        txtBairro.setText(paciente.getBairro());
        txtCidade.setText(paciente.getCidade());
        txtHistorico.setText(paciente.getHistorico());
    }

    private void salvar() {
        try {
            paciente.setNome(txtNome.getText().trim());
            // usa setBi() caso o model tenha sido renomeado, senão setCpf()
            paciente.setBi(((JTextField) txtBi).getText().trim());
            paciente.setDataNascimento(LocalDate.parse(txtNascimento.getText().trim(), FMT));
            String s = (String) cbSexo.getSelectedItem();
            paciente.setSexo(s != null && !s.isEmpty() ? s.charAt(0) : 'O');
            paciente.setTelefone(txtTelefone.getText().trim());
            paciente.setEmail(txtEmail.getText().trim());
            paciente.setBairro(txtBairro.getText().trim());
            paciente.setCidade(txtCidade.getText().trim());
            paciente.setHistorico(txtHistorico.getText().trim());
            paciente.setAtivo(true);
            if (paciente.getId() == 0) ctrl.cadastrar(paciente);
            else ctrl.atualizar(paciente);
            salvo = true;
            JOptionPane.showMessageDialog(this, "Paciente salvo com sucesso!");
            dispose();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Data de nascimento inválida. Use dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSalvo() { return salvo; }
}

// ─────────────────────────────────────────────────────────────────────────────
// FormularioConsulta
// ─────────────────────────────────────────────────────────────────────────────
class FormularioConsulta extends BaseDialog {

    private final ConsultaController ctrl    = new ConsultaController();
    private final PacienteController ctrlPac = new PacienteController();
    private final MedicoController   ctrlMed = new MedicoController();
    private Consulta consulta;
    private boolean salvo = false;

    private JComboBox<Paciente>       cbPaciente;
    private JComboBox<Medico>         cbMedico;
    private JFormattedTextField       txtDataHora;
    private JComboBox<StatusConsulta> cbStatus;
    private JTextField                txtObservacao;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    FormularioConsulta(JFrame pai, Consulta c) {
        super(pai, c == null ? "Novo Agendamento" : "Editar Agendamento");
        this.consulta = c == null ? new Consulta() : c;
        setSize(520, 400);
        setLocationRelativeTo(pai);
        construirUI();
        carregarCombos();
        if (c != null) preencherCampos();
    }

    private void construirUI() {
        JPanel root = buildRoot();
        root.add(dialogHeader("", consulta.getId() == 0 ? "Novo Agendamento" : "Editar Agendamento"),
                BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(6, 2, 12, 12));
        form.setBackground(DS.BG_CARD);
        form.setBorder(DS.emptyBorder(24, 24, 10, 24));

        cbPaciente = new JComboBox<>(); DS.styleCombo(cbPaciente);
        cbMedico   = new JComboBox<>(); DS.styleCombo(cbMedico);

        try {
            MaskFormatter m = new MaskFormatter("##/##/#### ##:##");
            m.setPlaceholderCharacter('_');
            txtDataHora = new JFormattedTextField(m);
        } catch (ParseException e) {
            txtDataHora = new JFormattedTextField();
        }
        DS.styleField(txtDataHora);

        cbStatus      = new JComboBox<>(StatusConsulta.values()); DS.styleCombo(cbStatus);
        txtObservacao = new JTextField(); DS.styleField(txtObservacao);

        form.add(DS.label("Paciente *"));  form.add(cbPaciente);
        form.add(DS.label("Médico *"));    form.add(cbMedico);
        form.add(DS.label("Data/Hora *")); form.add(txtDataHora);
        form.add(DS.label("Status"));      form.add(cbStatus);
        form.add(DS.label("Observação"));  form.add(txtObservacao);
        form.add(new JLabel());
        form.add(DS.labelMuted("Formato: dd/MM/yyyy HH:mm  –  ex: 15/06/2026 09:30"));

        JButton btnSalvar   = DS.btnSuccess("Salvar");
        JButton btnCancelar = DS.btnGhost("Cancelar");
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        root.add(form, BorderLayout.CENTER);
        root.add(footer(btnCancelar, btnSalvar), BorderLayout.SOUTH);
    }

    private void carregarCombos() {
        try {
            ctrlPac.listarAtivos().forEach(cbPaciente::addItem);
            ctrlMed.listarAtivos().forEach(cbMedico::addItem);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void preencherCampos() {
        for (int i = 0; i < cbPaciente.getItemCount(); i++)
            if (cbPaciente.getItemAt(i).getId() == consulta.getPacienteId()) {
                cbPaciente.setSelectedIndex(i); break;
            }
        for (int i = 0; i < cbMedico.getItemCount(); i++)
            if (cbMedico.getItemAt(i).getId() == consulta.getMedicoId()) {
                cbMedico.setSelectedIndex(i); break;
            }
        txtDataHora.setText(consulta.getDataHora().format(FMT));
        cbStatus.setSelectedItem(consulta.getStatus());
        txtObservacao.setText(consulta.getObservacao());
    }

    private void salvar() {
        try {
            Paciente pac = (Paciente) cbPaciente.getSelectedItem();
            Medico   med = (Medico)   cbMedico.getSelectedItem();
            if (pac == null || med == null)
                throw new IllegalArgumentException("Selecione paciente e médico.");
            LocalDateTime dt = LocalDateTime.parse(txtDataHora.getText().trim(), FMT);
            consulta.setPacienteId(pac.getId());
            consulta.setMedicoId(med.getId());
            consulta.setDataHora(dt);
            consulta.setStatus((StatusConsulta) cbStatus.getSelectedItem());
            consulta.setObservacao(txtObservacao.getText().trim());
            if (consulta.getId() == 0) ctrl.agendar(consulta);
            else ctrl.atualizar(consulta);
            salvo = true;
            JOptionPane.showMessageDialog(this, "Consulta salva com sucesso!");
            dispose();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Data/hora inválida. Use dd/MM/yyyy HH:mm", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSalvo() { return salvo; }
}

// ─────────────────────────────────────────────────────────────────────────────
// FormularioProntuario
// ─────────────────────────────────────────────────────────────────────────────
class FormularioProntuario extends BaseDialog {

    private final ProntuarioController ctrl     = new ProntuarioController();
    private final ConsultaController   ctrlCons = new ConsultaController();
    private Prontuario prontuario;
    private boolean salvo = false;

    private JComboBox<Consulta> cbConsulta;
    private JTextArea txtDiagnostico, txtPrescricao, txtObservacoes;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    FormularioProntuario(JFrame pai, Prontuario p) {
        super(pai, p == null ? "Novo Prontuário" : "Editar Prontuário");
        this.prontuario = p == null ? new Prontuario() : p;
        setSize(580, 520);
        setLocationRelativeTo(pai);
        construirUI();
        carregarConsultas();
        if (p != null) preencherCampos();
    }

    private void construirUI() {
        JPanel root = buildRoot();
        root.add(dialogHeader("", prontuario.getId() == 0 ? "Novo Prontuário" : "Editar Prontuário"),
                BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(DS.BG_CARD);
        form.setBorder(DS.emptyBorder(20, 24, 10, 24));

        cbConsulta     = new JComboBox<>(); DS.styleCombo(cbConsulta);
        txtDiagnostico = new JTextArea(4, 30); DS.styleField(txtDiagnostico);
        txtPrescricao  = new JTextArea(4, 30); DS.styleField(txtPrescricao);
        txtObservacoes = new JTextArea(3, 30); DS.styleField(txtObservacoes);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill   = GridBagConstraints.BOTH;
        gc.weightx = 1.0;

        gc.gridx=0; gc.gridy=0; gc.weighty=0; gc.gridwidth=1; gc.weightx=0.3;
        form.add(DS.label("Consulta (Realizada) *"), gc);
        gc.gridx=1; gc.weightx=0.7;
        form.add(cbConsulta, gc);

        gc.gridx=0; gc.gridy=1; gc.weightx=0.3;
        form.add(DS.label("Diagnóstico *"), gc);
        gc.gridx=1; gc.weighty=0.35; gc.weightx=0.7;
        form.add(styledScrollArea(txtDiagnostico), gc);

        gc.gridx=0; gc.gridy=2; gc.weighty=0; gc.weightx=0.3;
        form.add(DS.label("Prescrição"), gc);
        gc.gridx=1; gc.weighty=0.35; gc.weightx=0.7;
        form.add(styledScrollArea(txtPrescricao), gc);

        gc.gridx=0; gc.gridy=3; gc.weighty=0; gc.weightx=0.3;
        form.add(DS.label("Observações"), gc);
        gc.gridx=1; gc.weighty=0.3; gc.weightx=0.7;
        form.add(styledScrollArea(txtObservacoes), gc);

        JButton btnSalvar   = DS.btnSuccess("Salvar");
        JButton btnCancelar = DS.btnGhost("Cancelar");
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        root.add(form, BorderLayout.CENTER);
        root.add(footer(btnCancelar, btnSalvar), BorderLayout.SOUTH);
    }

    private void carregarConsultas() {
        try {
            ctrlCons.listarTodas().stream()
                    .filter(c -> c.getStatus() == StatusConsulta.REALIZADA)
                    .forEach(cbConsulta::addItem);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar consultas: " + e.getMessage());
        }
    }

    private void preencherCampos() {
        for (int i = 0; i < cbConsulta.getItemCount(); i++)
            if (cbConsulta.getItemAt(i).getId() == prontuario.getConsultaId()) {
                cbConsulta.setSelectedIndex(i); break;
            }
        txtDiagnostico.setText(prontuario.getDiagnostico());
        txtPrescricao.setText(prontuario.getPrescricao());
        txtObservacoes.setText(prontuario.getObservacoes());
    }

    private void salvar() {
        try {
            Consulta c = (Consulta) cbConsulta.getSelectedItem();
            if (c == null) throw new IllegalArgumentException("Selecione uma consulta realizada.");
            prontuario.setConsultaId(c.getId());
            prontuario.setDiagnostico(txtDiagnostico.getText().trim());
            prontuario.setPrescricao(txtPrescricao.getText().trim());
            prontuario.setObservacoes(txtObservacoes.getText().trim());
            ctrl.salvar(prontuario);
            salvo = true;
            JOptionPane.showMessageDialog(this, "Prontuário salvo com sucesso!");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSalvo() { return salvo; }
}