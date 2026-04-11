# Aplicação de Chat Simples com Sockets (Redes) em Java
## Descricão
• Tema: Comunicação em rede e programação concorrente.
• Objetivo: Desenvolver uma aplicação de chat simples (CLI) onde múltiplos clientes
podem conectar-se a um servidor e trocar mensagens. O servidor deve ser capaz de
gerenciar múltiplos clientes simultaneamente.
# Metodologia:
  Criamos uma classe ServidorChat que executa conexões de clientes na porta 11.000 utilizando
  localhost para clientes que estão na mesma máquina e IP para máquinas diferentes;

 Para cada cliente que se conecta, o servidor deve cria uma nova thread;
  (ClientHandler) para lidar com a comunicação entre clientes;
  A classe ClientHandler  lê as mensagens dos clientes e retransmiti-las para
  todos os outros clientes conectados;

 Criou-se classes ClienteChat(1,2,3) que se conectam ao servidor, enviam mensagens e
  exibem as mensagens recebidas;
  Utilizou-se  as bibliotecas java.net, java.util e java.io;
  Garantiu-se o tratamento de IOExceptions.
