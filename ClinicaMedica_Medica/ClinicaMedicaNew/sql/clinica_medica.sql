-- ============================================================
--  SISTEMA DE GESTÃO DE CLÍNICA MÉDICA
--  Script SQL — DDL + DML (dados de exemplo)
--  Banco: MySQL 8+
-- ============================================================

CREATE DATABASE IF NOT EXISTS clinica_medica
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE clinica_medica;

-- ──────────────────────────────────────────────
--  TABELA: usuarios
-- ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS usuarios (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nome        VARCHAR(120)        NOT NULL,
    login       VARCHAR(60)         NOT NULL UNIQUE,
    senha_hash  VARCHAR(64)         NOT NULL,   -- SHA-256 hex
    perfil      ENUM('ADMIN','MEDICO','RECEPCIONISTA') NOT NULL,
    ativo       TINYINT(1)          NOT NULL DEFAULT 1,
    criado_em   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ──────────────────────────────────────────────
--  TABELA: pacientes
-- ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS pacientes (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    nome            VARCHAR(120)    NOT NULL,
    cpf             CHAR(11)        NOT NULL UNIQUE,
    data_nascimento DATE            NOT NULL,
    sexo            ENUM('M','F','O') NOT NULL DEFAULT 'O',
    telefone        VARCHAR(20),
    email           VARCHAR(120),
    logradouro      VARCHAR(150),
    numero          VARCHAR(10),
    bairro          VARCHAR(80),
    cidade          VARCHAR(80),
    historico       TEXT,
    ativo           TINYINT(1)      NOT NULL DEFAULT 1,
    criado_em       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ──────────────────────────────────────────────
--  TABELA: medicos
-- ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS medicos (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id     INT             NULL,
    nome           VARCHAR(120)    NOT NULL,
    crm            VARCHAR(20)     NOT NULL UNIQUE,
    especialidade  VARCHAR(80)     NOT NULL,
    telefone       VARCHAR(20),
    email          VARCHAR(120),
    ativo          TINYINT(1)      NOT NULL DEFAULT 1,
    criado_em      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ──────────────────────────────────────────────
--  TABELA: consultas
-- ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS consultas (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id    INT             NOT NULL,
    medico_id      INT             NOT NULL,
    data_hora      DATETIME        NOT NULL,
    status         ENUM('AGENDADA','CONFIRMADA','REALIZADA','CANCELADA')
                                   NOT NULL DEFAULT 'AGENDADA',
    observacao     VARCHAR(300),
    criado_em      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    FOREIGN KEY (medico_id)   REFERENCES medicos(id),
    UNIQUE KEY uk_medico_horario (medico_id, data_hora)
) ENGINE=InnoDB;

-- ──────────────────────────────────────────────
--  TABELA: prontuarios
-- ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS prontuarios (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    consulta_id     INT             NOT NULL UNIQUE,
    diagnostico     TEXT            NOT NULL,
    prescricao      TEXT,
    observacoes     TEXT,
    criado_em       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (consulta_id) REFERENCES consultas(id)
) ENGINE=InnoDB;

-- ══════════════════════════════════════════════
--  DML — DADOS DE EXEMPLO
-- ══════════════════════════════════════════════

-- Usuários  (senha "admin123" → sha256; "medico123"; "recep123")
INSERT INTO usuarios (nome, login, senha_hash, perfil) VALUES
 ('Administrador',  'admin',  '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN'),
 ('Dr. Carlos Mota','carlos', 'c6f30d03de73a27765e3e9e27e1be6ae4ca9c8ba6e29a22a5f6dc81e5f2e1ea9', 'MEDICO'),
 ('Ana Recepção',   'ana',    '16d51fbe18499cbee8cd7af6ab23aea6d1e5bc3d1ffa46b5ca4e3ba5e0d50937', 'RECEPCIONISTA');

-- Médicos
INSERT INTO medicos (usuario_id, nome, crm, especialidade, telefone, email) VALUES
 (2, 'Dr. Carlos Mota',    'CRM/SP-12345', 'Clínica Geral',  '(11)99001-0001', 'carlos@clinica.com'),
 (NULL,'Dra. Fernanda Lima','CRM/SP-67890', 'Cardiologia',    '(11)99001-0002', 'fernanda@clinica.com'),
 (NULL,'Dr. Paulo Souza',   'CRM/SP-11223', 'Ortopedia',      '(11)99001-0003', 'paulo@clinica.com'),
 (NULL,'Dra. Mariana Costa','CRM/SP-44556', 'Pediatria',      '(11)99001-0004', 'mariana@clinica.com');

-- Pacientes
INSERT INTO pacientes (nome, cpf, data_nascimento, sexo, telefone, email, logradouro, numero, bairro, cidade, historico) VALUES
 ('João Silva',        '12345678901','1985-03-15','M','(11)98000-0001','joao@email.com',   'Rua das Flores','10','Centro',    'São Paulo','Hipertensão controlada'),
 ('Maria Oliveira',    '23456789012','1990-07-22','F','(11)98000-0002','maria@email.com',  'Av. Paulista',  '200','Bela Vista','São Paulo','Diabetes tipo 2'),
 ('Pedro Santos',      '34567890123','1978-11-30','M','(11)98000-0003','pedro@email.com',  'Rua Augusta',   '55','Consolação','São Paulo','Sem histórico'),
 ('Ana Lima',          '45678901234','2005-01-10','F','(11)98000-0004','ana.l@email.com',  'Rua 7 de Abril','80','República', 'São Paulo','Asma leve'),
 ('Carlos Pereira',    '56789012345','1965-09-05','M','(11)98000-0005','carlos.p@email.com','Rua Barão',    '300','Liberdade', 'São Paulo','Colesterol alto'),
 ('Lucia Fernandes',   '67890123456','1992-04-18','F','(11)98000-0006','lucia@email.com',  'Av. Brasil',    '15','Moema',     'São Paulo','Sem histórico'),
 ('Roberto Alves',     '78901234567','1955-12-01','M','(11)98000-0007','roberto@email.com','Rua XV',        '5', 'Jardins',   'São Paulo','Cardiopatia leve');

-- Consultas
INSERT INTO consultas (paciente_id, medico_id, data_hora, status, observacao) VALUES
 (1, 1, '2026-06-10 08:00:00', 'REALIZADA',  'Consulta de rotina'),
 (2, 2, '2026-06-10 09:00:00', 'REALIZADA',  'Retorno cardiologia'),
 (3, 3, '2026-06-11 10:00:00', 'CONFIRMADA', 'Dor no joelho'),
 (4, 4, '2026-06-12 14:00:00', 'AGENDADA',   'Controle pediátrico'),
 (5, 1, '2026-06-13 08:30:00', 'AGENDADA',   NULL),
 (6, 2, '2026-06-14 11:00:00', 'AGENDADA',   NULL),
 (7, 2, '2026-06-10 10:00:00', 'CANCELADA',  'Paciente desmarcou'),
 (1, 3, '2026-06-15 09:00:00', 'AGENDADA',   'Dor nas costas');

-- Prontuários (das consultas realizadas)
INSERT INTO prontuarios (consulta_id, diagnostico, prescricao, observacoes) VALUES
 (1, 'Hipertensão estável', 'Losartana 50mg — 1x ao dia', 'Pressão 130/80. Manter dieta.'),
 (2, 'Arritmia leve identificada', 'Atenolol 25mg — 1x ao dia. Retorno em 30 dias.', 'ECG realizado. Solicitar Holter.');
