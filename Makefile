
.PHONY: build build-backend build-frontend up down logs clean help

default: help

build: build-backend build-frontend

# Constrói a imagem Docker do backend
build-backend:
	@echo "--- Construindo imagem do Backend... ---"
	@(cd asset-control-system-backend && mvn clean package -DskipTests && docker build -t asset-control-backend:latest .)
	@echo "--- Imagem do Backend construída com sucesso! ---"

# Constrói a imagem Docker do frontend
build-frontend:
	@echo "--- Construindo imagem do Frontend... ---"
	@(cd asset-control-system-frontend && docker build -t asset-control-frontend:latest .)
	@echo "--- Imagem do Frontend construída com sucesso! ---"

# Sobe todo o ambiente com Docker Compose
# Primeiro garante que as imagens estão construídas
up: build
	@echo "--- Iniciando todos os serviços com Docker Compose... ---"
	docker-compose up -d
	@echo "--- Ambiente iniciado! Aplicação disponível em http://localhost:8082 ---"

# Para todo o ambiente do Docker Compose
down:
	@echo "--- Parando todos os serviços... ---"
	docker-compose down
	@echo "--- Ambiente parado. ---"

# Mostra os logs de todos os serviços em tempo real
logs:
	docker-compose logs -f

# Limpa os contêineres e redes do Docker Compose (não apaga os volumes de dados)
clean:
	docker-compose down --remove-orphans

# Mostra uma lista de todos os comandos disponíveis
help:
	@echo "Comandos disponíveis:"
	@echo "  make build          - Constrói as imagens Docker do backend e do frontend."
	@echo "  make build-backend  - Constrói apenas a imagem do backend."
	@echo "  make build-frontend - Constrói apenas a imagem do frontend."
	@echo "  make up             - Constrói as imagens (se necessário) e inicia todo o ambiente."
	@echo "  make down           - Para todo o ambiente."
	@echo "  make logs           - Mostra os logs dos contêineres."
	@echo "  make clean          - Para e remove os contêineres."
	@echo "  make help           - Mostra esta mensagem de ajuda."