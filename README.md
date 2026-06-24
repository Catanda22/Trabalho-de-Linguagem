# 🏥 Sistema de Gestão de Clínica Médica

**Universidade Kimpa Vita — Engenharia Informática, 2º Ano**
**Docente:** Moyo Kanivengidio | **Disciplina:** Linguagem de Programação VI

---

## Visão Geral

Sistema desktop desenvolvido em **Java + JavaFX + MySQL**, seguindo a arquitetura **MVC + DAO**, para gestão completa de uma clínica médica.

### Funcionalidades
- ✅ Login com perfis (Administrador, Médico, Recepcionista)
- ✅ Dashboard com estatísticas em tempo real
- ✅ Gestão de Pacientes (CRUD + busca por nome/CPF)
- ✅ Gestão de Médicos (CRUD + busca)
- ✅ Agendamentos com controle de status e verificação de conflito de horário
- ✅ Prontuários eletrônicos por consulta
- ✅ Relatórios gerenciais por período e por médico
- ✅ Gestão de Usuários (somente Administrador)

---

## Arquitetura do Projeto

```
clinica/
├── sql/
│   └── clinica_medica.sql          ← DDL + DML (dados de exemplo)
│
└── src/
    ├── Main.java                   ← Ponto de entrada
    │
    ├── config/
    │   └── Conexao.java            ← Singleton de conexão JDBC
    │
    ├── model/                      ← Entidades do domínio
    │   ├── Usuario.java
    │   ├── Paciente.java
    │   ├── Medico.java
    │   ├── Consulta.java
    │   ├── Prontuario.java
    │   ├── Perfil.java             ← Enum de perfis
    │   └── StatusConsulta.java     ← Enum de status
    │
    ├── dao/                        ← Acesso ao banco (JDBC)
    │   ├── UsuarioDAO.java
    │   ├── PacienteDAO.java
    │   ├── MedicoDAO.java
    │   ├── ConsultaDAO.java
    │   └── ProntuarioDAO.java
    │
    ├── controller/                 ← Regras de negócio
    │   ├── AuthController.java
    │   ├── PacienteController.java
    │   ├── MedicoController.java
    │   ├── ConsultaController.java
    │   └── ProntuarioController.java
    │
    ├── view/                       ← Interface gráfica (Swing)
    │   ├── TelaLogin.java
    │   ├── TelaPrincipal.java
    │   ├── PainelDashboard.java
    │   ├── PainelPacientes.java
    │   ├── FormularioPaciente.java
    │   ├── PainelMedicos.java
    │   ├── FormularioMedico.java
    │   ├── PainelConsultas.java
    │   ├── FormularioConsulta.java
    │   ├── PainelProntuarios.java
    │   ├── FormularioProntuario.java
    │   ├── PainelRelatorios.java
    │   └── PainelUsuarios.java
    │
    └── util/
        ├── Seguranca.java          ← Hash SHA-256
        └── Validacao.java          ← Validação de CPF, e-mail etc.
```

---

## Pré-requisitos

| Ferramenta | Versão mínima |
|---|---|
| Java JDK | 11+ |
| MySQL Server | 8.0+ |
| mysql-connector-java | 8.0+ |
| IDE (IntelliJ / Eclipse / NetBeans) | Qualquer versão recente |

---

## Instalação Passo a Passo

### 1. Configurar o Banco de Dados

```sql
-- No MySQL Workbench ou terminal MySQL:
source caminho/para/clinica/sql/clinica_medica.sql
```

Ou copie e execute o conteúdo do arquivo `sql/clinica_medica.sql` diretamente.

### 2. Ajustar a Conexão

Edite o arquivo `src/config/Conexao.java` com suas credenciais MySQL:

```java
private static final String URL     = "jdbc:mysql://localhost:3306/clinica_medica?...";
private static final String USUARIO = "root";      // seu usuário MySQL
private static final String SENHA   = "root";      // sua senha MySQL
```

### 3. Adicionar o Driver JDBC

Baixe o `mysql-connector-java-x.x.x.jar` em:
https://dev.mysql.com/downloads/connector/j/

Adicione ao **classpath** do projeto na sua IDE.

### 4. Compilar e Executar

**Via IntelliJ IDEA:**
1. Abrir o projeto → `File > Open` → selecionar a pasta `clinica/src`
2. Adicionar o jar do MySQL em `File > Project Structure > Libraries`
3. Executar a classe `Main.java`

**Via linha de comando:**
```bash
# Compilar (ajuste o caminho do jar)
javac -cp ".;mysql-connector-java.jar" -d out src/**/*.java src/*.java

# Executar
java -cp "out;mysql-connector-java.jar" Main
```

---

## Credenciais de Acesso (dados de exemplo)

| Usuário | Senha | Perfil |
|---|---|---|
| `admin` | `admin123` | Administrador |
| `carlos` | `medico123` | Médico |
| `ana` | `recep123` | Recepcionista |

---

## Diagrama MER (resumido)

```
usuarios (id, nome, login, senha_hash, perfil, ativo)
    │
medicos (id, usuario_id FK, nome, crm, especialidade, telefone, email, ativo)
    │
    └─── consultas (id, paciente_id FK, medico_id FK, data_hora, status, observacao)
              │
pacientes ────┘
(id, nome, cpf, data_nascimento, sexo, telefone, email, endereco..., historico, ativo)
              │
              └─── prontuarios (id, consulta_id FK, diagnostico, prescricao, observacoes)
```

---

## 📝 Padrões Aplicados

| Padrão | Onde |
|---|---|
| **MVC** | `model/` + `view/` + `controller/` |
| **DAO** | `dao/` — isola todo código JDBC |
| **Singleton** | `config/Conexao.java` |
| **PreparedStatement** | Todos os DAOs — previne SQL Injection |
| **SwingWorker** | `TelaLogin` — login assíncrono |
| **IntersectionObserver** | — |


## 🛠️ Tecnologias

- **Java 11+** com Streams, Lambdas, Records e Text Blocks
- **Java Swing** — interface gráfica desktop
- **MySQL 8** — banco de dados relacional
- **JDBC** — conexão com banco sem ORM
- **SHA-256** — hash de senhas (sem bibliotecas externas)


## menbross
* Anacleta Cazebeca
* Catanda Fernando
* Eulália Marquez
* Pedro Vieira
* Ferraz de Asunção

