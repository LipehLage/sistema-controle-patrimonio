# Frontend - Sistema de Controle de Patrimônio

Esta é a interface de usuário (UI) construída com Vue.js para o Sistema de Controle de Patrimônio.

## Tecnologias

* Vue.js 3
* Vue Router 4
* Vuetify 3
* Keycloak-JS (para integração com Keycloak)
* Node.js / npm

## Como Executar

### Via Docker (Recomendado)

Consulte o `README.md` principal na raiz do projeto para instruções de como usar o Docker Compose.

### Localmente (Para Desenvolvimento)

1.  Certifique-se de que a API backend esteja rodando.
2.  Instale as dependências:
    ```bash
    npm install
    ```
3.  Inicie o servidor de desenvolvimento:
    ```bash
    npm run serve
    ```
4.  Acesse a aplicação em `http://localhost:8082` (ou na porta indicada no terminal).

### Build para Produção

Para criar os arquivos estáticos otimizados para produção:
```bash
npm run build
```