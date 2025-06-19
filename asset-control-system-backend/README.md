# Backend - Sistema de Controle de Patrimônio

Esta é a API RESTful construída com Java e Spring Boot para o Sistema de Controle de Patrimônio. Ela gerencia os dados de equipamentos, usuários e movimentações, além de prover segurança com Keycloak.

## Tecnologias

* Java 17
* Spring Boot 3
* Spring Data JPA (Hibernate)
* Spring Security (OAuth2 Resource Server)
* PostgreSQL
* Apache PDFBox (para geração de PDF)
* Maven

## Configuração

As principais configurações da aplicação estão no arquivo `src/main/resources/application.properties`.

* `spring.datasource.url`: URL de conexão com o banco de dados.
* `spring.security.oauth2.resourceserver.jwt.issuer-uri`: URL do emissor de tokens do Keycloak.

Quando executado via Docker Compose, estas propriedades são sobrescritas pelas variáveis de ambiente definidas no `docker-compose.yml`.

## Testando a API com Postman

A API é protegida e requer um Token de Acesso (Bearer Token) para a maioria das requisições.

### 1. Obtendo um Token de Acesso

Faça uma requisição `POST` para o endpoint de token do Keycloak com os seguintes dados:

* **URL:** `http://localhost:8080/realms/asset-control-realm/protocol/openid-connect/token`
* **Body (x-www-form-urlencoded):**
    * `grant_type`: `password`
    * `client_id`: `asset-control-backend`
    * `client_secret`: (O secret do cliente `asset-control-backend` no Keycloak)
    * `username`: `adminuser` (para perfil de Admin) ou `normaluser` (para perfil de Usuário)
    * `password`: (A senha correspondente ao usuário)

A resposta conterá um `access_token`. Copie este valor.

### 2. Fazendo Chamadas para a API

Em cada requisição para a nossa API, vá para a aba **Authorization**, selecione o tipo **Bearer Token** e cole o `access_token` obtido.

## Exemplos de Endpoints da API

A URL base para a API é `http://localhost:8081`.

### Equipamentos (`/api/v1/equipments`)

* `GET /`: Lista todos os equipamentos. (Requer `ROLE_USER` ou `ROLE_ADMIN`)
* `GET /{id}`: Busca um equipamento por ID. (Requer `ROLE_USER` ou `ROLE_ADMIN`)
* `POST /`: Cria um novo equipamento. (Requer `ROLE_ADMIN`)
* `PUT /{id}`: Atualiza um equipamento existente. (Requer `ROLE_ADMIN`)
* `DELETE /{id}`: Deleta um equipamento. (Requer `ROLE_ADMIN`)
* `GET /available`: Relatório de equipamentos disponíveis. (Requer `ROLE_USER` ou `ROLE_ADMIN`)
* `GET /awaiting-repair`: Relatório de equipamentos aguardando reparo. (Requer `ROLE_USER` ou `ROLE_ADMIN`)
* `GET /user/{userId}`: Relatório de equipamentos alocados a um usuário específico. (Requer `ROLE_USER` ou `ROLE_ADMIN`)

### Usuários (`/api/v1/users`)

* `GET /`: Lista todos os usuários da aplicação. (Requer `ROLE_USER` ou `ROLE_ADMIN`)
* `GET /{id}`: Busca um usuário por ID. (Requer `ROLE_USER` ou `ROLE_ADMIN`)
* `POST /`: Cria um novo usuário. (Requer `ROLE_ADMIN`)
* `PUT /{id}`: Atualiza um usuário existente. (Requer `ROLE_ADMIN`)
* `DELETE /{id}`: Deleta um usuário. (Requer `ROLE_ADMIN`)

### Movimentações (`/api/v1/movements`)

* `GET /`: Lista todas as movimentações. (Requer `ROLE_USER` ou `ROLE_ADMIN`)
* `POST /register`: Registra uma nova movimentação. (Requer `ROLE_USER` ou `ROLE_ADMIN`)
* `GET /{id}/term`: Gera e baixa o termo de responsabilidade em PDF para uma movimentação específica. (Requer `ROLE_USER` ou `ROLE_ADMIN`)

## Como Executar

### Via Docker (Recomendado)

Consulte o `README.md` principal na raiz do projeto para instruções de como usar o Docker Compose.

### Localmente (Para Desenvolvimento)

1.  Certifique-se de que uma instância do PostgreSQL e do Keycloak estejam acessíveis (podem ser as do Docker Compose).
2.  Atualize o `application.properties` para apontar para `localhost` (ex: `jdbc:postgresql://localhost:5432/asset_control`).
3.  Execute a classe principal `AssetControlSystemBackendApplication.java` a partir da sua IDE (IntelliJ IDEA).