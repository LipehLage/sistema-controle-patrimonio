# Sistema de Controle de Patrimônio de TI

## 📝 Descrição

Este projeto é um sistema web completo (SaaS - Software as a Service) para gerenciar o controle de entrada e saída de equipamentos de TI. Ele permite o cadastro de usuários, equipamentos, o registro de movimentações e a geração de termos de responsabilidade.

---

## ✨ Funcionalidades Principais

* **Gerenciamento de Equipamentos:** CRUD completo (Criar, Ler, Atualizar, Deletar) para os ativos de TI.
* **Gerenciamento de Usuários:** CRUD completo para os funcionários/usuários da aplicação.
* **Controle de Movimentações:** Registro de entrada (devolução) e saída (alocação, reparo) de equipamentos, vinculando um equipamento a um usuário.
* **Geração de Termos:** Emissão de um Termo de Responsabilidade em PDF para cada movimentação.
* **Relatórios:** Visualização de equipamentos por status (Disponível, Em Reparo) e por usuário alocado.
* **Autenticação e Autorização:** Sistema de segurança robusto com Single Sign-On (SSO) via Keycloak, com controle de acesso baseado em perfis (Admin vs. Usuário).

---

## 🛠️ Arquitetura e Tecnologias

O sistema é construído sobre uma arquitetura de microsserviços e contêineres, utilizando as seguintes tecnologias:

* **Backend:** Java 17, Spring Boot 3
* **Frontend:** Vue.js 3, Vuetify 3, Vue Router
* **Banco de Dados:** PostgreSQL
* **Autenticação/Autorização:** Keycloak
* **Containerização:** Docker & Docker Compose

---

## 🚀 Como Executar o Projeto Completo

Com o Docker e o Docker Compose instalados, o ambiente completo pode ser iniciado com apenas alguns comandos.

### Pré-requisitos

* [Docker](https://www.docker.com/products/docker-desktop/)
* [Docker Compose](https://docs.docker.com/compose/install/) (geralmente já vem com o Docker Desktop)
* [Maven](https://maven.apache.org/download.cgi) (para construir o backend a primeira vez)
* [Node.js e npm](https://nodejs.org/en/) (para construir o frontend a primeira vez)

### Passos para a Primeira Execução

1.  **Construir a Imagem Docker do Backend:**
    ```bash
    cd asset-control-system-backend
    mvn clean package -DskipTests
    docker build -t asset-control-backend:latest .
    cd ..
    ```

2.  **Construir a Imagem Docker do Frontend:**
    ```bash
    cd asset-control-system-frontend
    docker build -t asset-control-frontend:latest .
    cd ..
    ```

3.  **Iniciar Todos os Serviços com Docker Compose:**
    * Certifique-se de que você está na pasta raiz onde o arquivo `docker-compose.yml` se encontra.
    ```bash
    docker-compose up -d
    ```
    O `-d` executa os contêineres em segundo plano. Para ver os logs, você pode usar `docker-compose logs -f`.

4.  **Parar Todos os Serviços:**
    ```bash
    docker-compose down
    ```

---

## 🔗 URLs de Acesso e Credenciais

Após iniciar o ambiente com `docker-compose up`, os serviços estarão acessíveis nas seguintes URLs:

* **Aplicação Frontend:** [http://localhost:8082](http://localhost:8082)
* **Documentação da API (Swagger UI):** [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
* **Console de Administração do Keycloak:** [http://localhost:8080](http://localhost:8080)
* **API Backend (Base URL):** `http://localhost:8081`

### Credenciais Padrão

* **Keycloak (Admin Console):**
    * **Usuário:** `admin`
    * **Senha:** `admin`
* **Usuário Administrador da Aplicação (para login no frontend):**
    * **Usuário:** `adminuser`
    * **Senha:** `admin123` (ou a que você definiu)
* **Usuário Comum da Aplicação (para login no frontend):**
    * **Usuário:** `normaluser`
    * **Senha:** `password123` (ou a que você definiu)

---

## 📂 Estrutura do Projeto

* **/asset-control-system-backend**: Contém o código-fonte da API backend em Java/Spring Boot.
* **/asset-control-system-frontend**: Contém o código-fonte da interface de usuário em Vue.js.
* **docker-compose.yml**: Arquivo de orquestração para todos os serviços.