# Sistema de Controle de Patrimônio de TI

## 📝 Descrição

Este projeto é um sistema web completo (SaaS - Software as a Service) para gerenciar o controle de entrada e saída de equipamentos de TI. Ele permite o cadastro de usuários, equipamentos, o registro de movimentações e a geração de termos de responsabilidade.

---

## ✨ Funcionalidades Principais

* **Gerenciamento de Equipamentos:** CRUD completo (Criar, Ler, Atualizar, Deletar) para os ativos de TI. 
* [cite_start]**Gerenciamento de Usuários:** CRUD completo para os funcionários/usuários da aplicação. 
* [cite_start]**Controle de Movimentações:** Registro de entrada (devolução) e saída (alocação, reparo) de equipamentos, vinculando um equipamento a um usuário. 
* [cite_start]**Geração de Termos:** Emissão de um Termo de Responsabilidade em PDF para cada movimentação. 

---

## 🛠️ Arquitetura e Tecnologias

O sistema é construído sobre uma arquitetura de microsserviços e contêineres, utilizando as seguintes tecnologias:

* [cite_start]**Backend:** Java (Spring Boot) 
* [cite_start]**Frontend:** Vue.js 
* [cite_start]**Banco de Dados:** PostgreSQL 
* [cite_start]**Autenticação/Autorização:** Keycloak (SSO) 
* **Containerização:** Docker & Docker Compose

---

## 🚀 Como Executar o Projeto Completo

Com o Docker e o Docker Compose instalados, o ambiente completo pode ser iniciado com apenas alguns comandos.

### Pré-requisitos

* [Docker](https://www.docker.com/products/docker-desktop/)
* [Docker Compose](https://docs.docker.com/compose/install/) (geralmente já vem com o Docker Desktop)
* [Maven](https://maven.apache.org/download.cgi) (para construir o backend a primeira vez)
* [Node.js e npm](https://nodejs.org/en/) (para construir o frontend a primeira vez)
* `make` (padrão no macOS e Linux)

### Passos para a Primeira Execução com Makefile (Recomendado)

A forma mais simples de gerenciar o ambiente é usando os comandos `make` a partir da pasta raiz do projeto.

1.  **Construir Imagens e Iniciar o Ambiente:**
    * Este comando único irá construir as imagens do backend e do frontend e depois iniciar todos os contêineres.
    ```bash
    make up
    ```

2.  **Ver os Logs:**
    * Para acompanhar os logs de todos os serviços em tempo real:
    ```bash
    make logs
    ```

3.  **Parar Todos os Serviços:**
    * Para parar todos os contêineres:
    ```bash
    make down
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

## 📂 Estrutura e Documentação Específica

Para detalhes sobre como executar ou testar cada parte do sistema individualmente, consulte os arquivos README dentro de cada pasta.

* **/asset-control-system-backend**: Contém o código-fonte da API backend. O `README.md` interno possui detalhes sobre os endpoints e como testar a API.
* **/asset-control-system-frontend**: Contém o código-fonte da interface de usuário. O `README.md` interno possui detalhes sobre os comandos de desenvolvimento do frontend.
* **docker-compose.yml**: Arquivo de orquestração para todos os serviços.
* **Makefile**: Atalhos para os comandos de build e execução do ambiente.