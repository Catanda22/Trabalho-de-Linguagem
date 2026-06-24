# Analise do Projeto - Sistema de Gestao de Clinica Medica

## Enunciado

O trabalho pede uma aplicacao Java desktop para gestao de clinica medica, usando:

- Java 8 ou superior.
- Interface grafica em Swing ou JavaFX.
- MySQL com JDBC.
- Arquitetura MVC.
- Padrao DAO para persistencia.
- CRUD de pacientes e medicos.
- Agendamento de consultas com controlo de status e conflito de horario.
- Prontuario eletronico por consulta.
- Login com perfis: administrador, medico e recepcionista.
- Relatorios basicos.

## Estado encontrado

O projeto extraido de `CLINICA.zip` ja tinha uma boa base visual em Swing/NetBeans:

- Telas de login, menu, paciente, medico, consulta, agendamento, prontuario e relatorio.
- Pacotes `model`, `dao`, `controller` e `view`.
- Script MySQL inicial em `mysql/clinica_new_horizonte.sql`.
- Relatorio em Word.

Tambem havia inconsistencias importantes:

- A URL JDBC tinha um espaco em `3306/ clinica_newHorizonte`.
- O SQL usava coluna `login`, mas `UsuarioDAO` procurava `Nomeusuario`.
- A tela de funcionarios enviava perfis como `Recepcao`, `Administrador` e `Medico`, mas o banco espera valores em maiusculas.
- `PacienteDao` usava colunas que nao existiam ou com nomes diferentes.
- `Consulta`, `Prontuario`, `ConsultaDAO` e `ProntuarioDAO` estavam vazios.
- A mensagem de login invalido aparecia mesmo apos login correto.
- O projeto NetBeans estava configurado como JavaFX, embora as telas sejam Swing.

## Avanco realizado

- Corrigida a conexao JDBC e permitida configuracao por propriedades Java:
  - `clinica.db.url`
  - `clinica.db.user`
  - `clinica.db.password`
- Corrigido `UsuarioDAO` para usar `login` e `PreparedStatement`.
- Normalizados perfis de acesso para `ADMINISTRADOR`, `MEDICO` e `RECEPCIONISTA`.
- Completado o modelo de paciente com `bi` e `dataNascimento`.
- Refeito `PacienteDao` com salvar, atualizar, inativar e pesquisar.
- Adicionado ID e CRUD basico para medico.
- Criados modelos completos de consulta e prontuario.
- Criados DAOs de consulta e prontuario.
- Consulta agora valida conflito de horario para o mesmo medico.
- Atualizado o SQL com coluna `sexo`, tabela `prontuario` e usuarios iniciais.
- Corrigido o fluxo de login na tela `LogoView`.
- Ajustada a tela `PacienteView` para enviar data de nascimento ao modelo.
- Ajustado `nbproject/project.properties` para abrir como projeto Swing com main `view.LogoView`.

## Como testar

1. Criar o banco executando `mysql/clinica_new_horizonte.sql`.
2. Adicionar o MySQL Connector/J em `libs/mysql-connector-j-9.7.0.jar`.
3. Abrir o projeto no NetBeans.
4. Executar a classe principal `view.LogoView`.
5. Testar login com:
   - Administrador: `admin` / `admin`
   - Recepcao: `recepcao` / `1234`
   - Medico: `medico` / `1234`

## Proximas etapas recomendadas

- Ligar as tabelas das telas aos metodos `pesquisar`.
- Implementar salvamento real na tela de agendamento usando `ConsultaDAO`.
- Implementar tela de prontuario usando `ProntuarioDAO`.
- Criar relatorios de pacientes ativos e consultas por medico/periodo.
- Melhorar validacoes de campos obrigatorios nas telas.
- Atualizar o relatorio tecnico com MER, arquitetura MVC/DAO e manual de uso.
