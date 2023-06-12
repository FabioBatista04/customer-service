# Aplicação Spring Webflux Java - API de Gerenciamento de Clientes e Produtos Favoritos
Este é o README da aplicação Java que implementa uma API de gerenciamento de clientes e produtos favoritos. A API permite a criação, atualização, visualização e remoção de clientes, além de permitir a adição e remoção de produtos na lista de produtos favoritos de um cliente.

## Funcionalidades
A API possui as seguintes funcionalidades:

### Gerenciamento de Clientes:

Criar um novo cliente fornecendo seu nome e endereço de e-mail.
Atualizar os dados de um cliente existente.
Visualizar os dados de um cliente específico.
Remover um cliente existente.
Produtos Favoritos:

Adicionar um produto à lista de produtos favoritos de um cliente.
Remover um produto da lista de produtos favoritos de um cliente.

## Requisitos


- ``Java 17``
- ``Gradle 7.6.1``
- ``Docker``
- ``docker compose``


## Configuração
Siga as etapas abaixo para configurar e executar a aplicação:

Clone o repositório do projeto:

``git clone https://github.com/FabioBatista04/customer-service.git``

Acesse o diretório do projeto:

``cd customer-service``

Build o projeto:

``./gradlew clean build``

Execute o comando docker compose para subir um container mongo e mongo-express

OBS: Dependendo da versão que estiver utilizando pode utilizar ``docker compose``

Caso queira que os logs sejam mostrados (irá consumir seu terminal)

``docker-compose up``

Caso queira execução de forma desanexada do terminal

``docker-compose up -d``


execute a aplicação

``./gradlew bootRun``

A API estará disponível em http://localhost:8080.

Lembre-se de que o uso do gradlew pode variar dependendo do sistema operacional. No Windows, você pode precisar usar o comando gradlew.bat em vez de ./gradlew.

## Endpoints

## Geração de token de autenticação

Login
URL: http://localhost:8080/users/login

### Método: POST

Descrição: Realiza o login do usuário com base nas credenciais fornecidas.

Corpo da Requisição:
```
{
"username": "user",
"password": "1234568"
}
```
Código de Resposta de Sucesso: 201 Created

Corpo da Resposta:
```
{
"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
}
```

## Criação de Usuário

URL: http://localhost:8080/users/create

### Método: POST

Descrição: Cria um novo usuário com base nos dados fornecidos.

Corpo da Requisição:
```
{
    "username": "user",
    "password": "12345678",
    "roles": [
        "ROLE_USER"
    ]
}
```
Código de Resposta de Sucesso: 201 Created
```
{
"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
}
```
## Criação de Cliente
URL: http://localhost:8080/customers

### Método: POST

Descrição: Cria um novo cliente com base nos dados fornecidos.
Corpo da Requisição:

````
{
"name": "user",
"email": "test@test.com.br"
}
````



## Atualização de Cliente

URL: http://localhost:8080/customers/{id}

### Método: PUT

Descrição: Atualiza os dados de um cliente existente.

Parâmetros da URL:

id: ID do cliente a ser atualizado.

Corpo da Requisição:

````
{
"name": "user user",
"email": "testuser@example.com"
}
````
Código de Resposta de Sucesso: 200 OK

## Consulta de Cliente por ID

URL: http://localhost:8080/customers/{id}

### Método: GET

Descrição: Retorna os dados de um cliente com base no ID fornecido.

Parâmetros da URL:

id: ID do cliente a ser consultado.

Código de Resposta de Sucesso: 200 OK

## Consulta de Todos os Clientes

URL: http://localhost:8080/customers

### Método: GET

Descrição: Retorna uma lista paginada de todos os clientes.

Parâmetros de Consulta:

page (opcional): Número da página (padrão: 0).

size (opcional): Tamanho da página (padrão: 20).

Código de Resposta de Sucesso: 200 OK

## Excluir Cliente
### Método HTTP: DELETE
Endpoint: http://localhost:8080/{id}

Descrição: Exclui um cliente com o ID especificado.

Status de Resposta: NO_CONTENT

## Encontrar Produtos Favoritos
## Método HTTP: GET
Endpoint: http://localhost:8080/{id}/favorites

Descrição: Recupera a lista de produtos favoritos para o cliente com o ID especificado.

Status de Resposta: 200 OK

## Adicionar Produto aos Favoritos
### Método HTTP: POST
Endpoint: http://localhost:8080/{id}/favorites/{product_id}

Descrição: Adiciona um produto com o ID especificado à lista de produtos favoritos para o cliente com o ID especificado.

Status de Resposta: 201 CREATED

Parâmetros da Requisição:

id: O ID do cliente.

product_id: O ID do produto a ser adicionado aos favoritos.

Corpo da Resposta: Retorna uma coleção de objetos ProductControllerResponse.

## Excluir Produto Favorito
### Método HTTP: DELETE
Endpoint: http://localhost:8080/{id}/favorites/{product_id}

Descrição: Remove um produto com o ID especificado da lista de produtos favoritos para o cliente com o ID especificado.

Status de Resposta: 204 NO_CONTENT

Parâmetros da Requisição:

id: O ID do cliente.

product_id: O ID do produto a ser removido dos favoritos.