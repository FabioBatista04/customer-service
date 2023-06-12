Aplicação Java - API de Gerenciamento de Clientes e Produtos Favoritos
Este é o README da aplicação Java que implementa uma API de gerenciamento de clientes e produtos favoritos. A API permite a criação, atualização, visualização e remoção de clientes, além de permitir a adição e remoção de produtos na lista de produtos favoritos de um cliente.

Funcionalidades
A API possui as seguintes funcionalidades:

Gerenciamento de Clientes:

Criar um novo cliente fornecendo seu nome e endereço de e-mail.
Atualizar os dados de um cliente existente.
Visualizar os dados de um cliente específico.
Remover um cliente existente.
Produtos Favoritos:

Adicionar um produto à lista de produtos favoritos de um cliente.
Remover um produto da lista de produtos favoritos de um cliente.
Requisitos
A aplicação requer os seguintes requisitos:

Java JDK 17 ou superior instalado.
Gradle 7.6.1 ou superior instalado.
Configuração
Siga as etapas abaixo para configurar e executar a aplicação:

Clone o repositório do projeto:
git clone https://github.com/FabioBatista04/customer-service.git

Acesse o diretório do projeto:
cd customer-service

Build o projeto:
./gradlew clean build

execute a aplicação
./gradlew bootRun

A API estará disponível em http://localhost:8080.

Certifique-se de ter o Java JDK instalado no seu sistema antes de executar esses comandos. 

Lembre-se de que o uso do gradlew pode variar dependendo do sistema operacional. No Windows, você pode precisar usar o comando gradlew.bat em vez de ./gradlew.