create database clinica_newHorizonte;
use clinica_newHorizonte;

CREATE TABLE usuario(
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil ENUM('ADMINISTRADOR',
                'MEDICO',
                'RECEPCIONISTA') NOT NULL
);

CREATE TABLE paciente (
    id_paciente INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    bi VARCHAR(14) UNIQUE NOT NULL,
    sexo VARCHAR(1),
    data_nascimento DATE NOT NULL,
    endereco VARCHAR(200),
    telefone VARCHAR(20),
    historico_medico TEXT,
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE medico (
    id_medico INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    crm VARCHAR(20) UNIQUE NOT NULL,
    especialidade VARCHAR(100),
    telefone VARCHAR(20),
    email VARCHAR(100)
);

CREATE TABLE consulta (
    id_consulta INT AUTO_INCREMENT PRIMARY KEY,

    id_paciente INT NOT NULL,
    id_medico INT NOT NULL,

    data_consulta DATE NOT NULL,
    hora_consulta TIME NOT NULL,

    status ENUM(
        'AGENDADA',
        'CONFIRMADA',
        'REALIZADA',
        'CANCELADA'
    ) DEFAULT 'AGENDADA',

    observacao TEXT,

    CONSTRAINT fk_consulta_paciente
        FOREIGN KEY(id_paciente)
        REFERENCES paciente(id_paciente),

    CONSTRAINT fk_consulta_medico
        FOREIGN KEY(id_medico)
        REFERENCES medico(id_medico)
);

CREATE TABLE prontuario (
    id_prontuario INT AUTO_INCREMENT PRIMARY KEY,
    id_consulta INT NOT NULL,
    id_paciente INT NOT NULL,
    diagnostico TEXT NOT NULL,
    prescricao TEXT,
    observacoes TEXT,
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_prontuario_consulta
        FOREIGN KEY(id_consulta)
        REFERENCES consulta(id_consulta),

    CONSTRAINT fk_prontuario_paciente
        FOREIGN KEY(id_paciente)
        REFERENCES paciente(id_paciente)
);

INSERT INTO usuario (nome, login, senha, perfil) VALUES
('Administrador', 'admin', 'admin', 'ADMINISTRADOR'),
('Recepcionista', 'recepcao', '1234', 'RECEPCIONISTA'),
('Medico', 'medico', '1234', 'MEDICO');
