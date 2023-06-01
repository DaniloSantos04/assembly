# Gerenciador de Sessões de Votação Cooperativa

Este projeto desenvolve uma solução de back-end baseada em REST API para gerenciar sessões de votação em uma cooperativa. Para facilitar o processo de tomada de decisão dos associados, a solução oferece as seguintes funcionalidades:

* Cadastro de novas pautas: Permite o registro de novas pautas, fornecendo um título e descrição para cada uma delas.
* Abertura de sessões de votação: Permite iniciar uma sessão de votação para uma pauta específica. O tempo de duração da sessão pode ser informado na chamada de abertura ou, caso não seja fornecido, o valor padrão será de 1 minuto.
* Recebimento de votos dos associados: Os associados podem enviar seus votos ('Sim' ou 'Não') para uma determinada pauta. Cada associado é identificado por um id único e só pode votar uma vez em cada pauta.
* Contabilização dos votos e divulgação do resultado: Após o encerramento da sessão de votação, a solução realiza a contagem dos votos recebidos e fornece o resultado da votação para a pauta em questão.

Essa API fornece os endpoints necessários para executar as operações acima para gerenciar com eficiência uma sessão de votação cooperativa.

## Instalação

Para configurar e executar a solução, siga as etapas abaixo:
1. Clone o projeto do GitHub: git clone 
`https://github.com/DaniloSantos04/assembly.git`
2. Abra o projeto na sua IDE de preferência.
3. Certifique-se de ter o SDK do Java 11 e o Docker instalados na sua máquina.
4. Navegue até a pasta assembly dentro do projeto clonado.
5. Execute o comando para iniciar os serviços do banco de dados MongoDB e do RabbitMQ usando o arquivo docker-compose.yaml: `docker-compose up -d`

Isso irá baixar as imagens necessárias e iniciar as instâncias dos serviços.
6. Após os serviços estarem em execução, execute o aplicativo AssemblyApplication. Isso pode ser feito através da IDE ou usando o seguinte comando na raiz do projeto: `mvn spring-boot:run
   `

# Acessos

* Swagger: `http://localhost:8080/api/swagger-ui.html#`
* MongoDb: `mongodb://localhost:27017`
* RabbitMQ: 
```sh
host: http://localhost:15672
Username: guest
Password: guest
```

# Detalhes de negócio

* Só pode existir uma pauta por sessão, mas pode-se criar varias sessões para a mesma pauta.
* Só pode criar uma sessão para pauta existente.
* Mesmo que se crie/atualize uma sessao com status ativa, mas com data/hora fim anterior a atual, esta sessão é automaticamente encerrada.
* O resultado  só pode ser exibido após o encerramento do horário da sessão.
* A exibição do resultado é feita a partir do id da sessão.


# Utilização dos Endpoints
* Para poder cadastrar um voto é necessário seguir essa ordem: Cadastrar uma Pauta, criar uma sessão ativa e por fim cadastrar o voto. Segue os endpoints que devem ser chamados:

1. Cadastrar Pauta: 
```sh
curl --location 'http://localhost:8080/api/v1/agenda' \
--header 'Content-Type: application/json' \
--data '{
  "description": "description",
  "title": "title"
}'
```

2. Abrir uma Sessão:
```sh
curl --location 'http://localhost:8080/api/v1/voting-session' \
--header 'Content-Type: application/json' \
--data '{
  "active": true,
  "dateStart": "2023-06-01T11:25",
  "idAgenda": 1
}'
```

3. Votar:
```sh
curl --location 'http://localhost:8080/api/v1/vote' \
--header 'Content-Type: application/json' \
--data '{
  "idAssociate":3,
  "idVotingSession": 1,
  "vote": "NAO"
}'
```

4. Visualizar Resultado:
```sh
curl --location 'http://localhost:8080/api/v1/voting-session/1/result'
```

* Endpoints relacionados à Pauta: `http://localhost:8080/api/swagger-ui.html#/agenda-controller`
* Endpoints relacionados à Sessão: `http://localhost:8080/api/swagger-ui.html#/voting-session-controller`
* Endpoints relacionados ao Votação: `http://localhost:8080/api/swagger-ui.html#/vote-controller`

# Detalhes de implementação
* O projeto foi desenvolvido em Java 11 com framework Spring.
* Foi feito tratamento de possíveis bugs.
* Foi feito tratamento de erros e exceções.
* Foi colocado logs.
* Criado versionamento de API.
* Criado testes automatizados.
* Foi utilizado o Swagger para documentação da aplicação.

**Banco de dados:**
* Foi utilizado o banco de dados MongoDB. As informações de configurações encontram-se no arquivo application.yml.

**Mensageria:**

* Foi utilizado o RabbitMQ para mensageria.
* As configurações estão no arquivo application.yml.

**Observações:**
* Não consegui implementar a camada de Logs em tempo. Mas dando uma visão de implementação, eu utilizaria a anotação @Slf4j para logar o passo a passo do código
* Não consegui implementar a camada de testes com JUnit e Jacoco em tempo. Mas dando uma visão de implementação, eu faria os asserts utilizando as mensagens das exceções lançadas

**Agendamento:**
* Para garantir o fechamento das sessões de votação foi utilizado o `@Scheduled` configurado para o intervalo de 1 minuto, onde ele verifica as sessões que estão sendo encerradas naquela hora.
* Após o encerramento faz a publicação do resultado na fila do RabbitMq.
