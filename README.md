# ConnectFood – Core Service (TC-2)

## 📌 Visão Geral

O Core Service é um serviço backend desenvolvido como parte do Tech Challenge 2 (TC-2) da FIAP Pós-Tech, responsável por
gerenciar funcionalidades centrais do domínio da aplicação ConnectFood.

O projeto foi desenvolvido seguindo rigorosamente os princípios de Clean Architecture, garantindo:

- Separação clara de responsabilidades
- Facilidade de testes
- Manutenibilidade
- Independência de frameworks no domínio

---

## 🧱 Arquitetura

O projeto segue Clean Architecture, com dependências sempre apontando para o núcleo do domínio.

```
┌──────────────────────────────┐
│ Entrypoint                   │
│ Controllers / REST / DTOs    │
└──────────────▲───────────────┘
               │
┌──────────────┴───────────────┐
│ Application                  │
│ Use Cases / Orquestração     │
└──────────────▲───────────────┘
               │
┌──────────────┴───────────────┐
│ Domain                       │
│ Entidades / Regras / Ports   │
└──────────────▲───────────────┘
               │
┌──────────────┴───────────────┐
│ Infrastructure               │
│ JPA / Banco / Implementações │
└──────────────────────────────┘
```

---

## 🛠️ Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Maven
- Spring Data JPA
- Flyway
- JUnit 5
- Mockito
- JaCoCo
- Docker / Docker Compose
- Swagger (SpringDoc OpenAPI)
- Postman

---

## 🧪 Execução dos Testes

### 🔹 Testes Unitários

Os testes unitários cobrem principalmente:

- Casos de uso (Application)
- Regras de negócio (Domain)

### 🔹 Testes de Integração

Os testes de integração validam:

- Controllers REST
- Integração com banco de dados
- Camada de infraestrutura

Para executar testes unitários + integração:

```shell
mvn clean verify
```

Os testes de integração utilizam o contexto real da aplicação.

--- 

## 📊 Relatório de Cobertura (JaCoCo)

Após a execução dos testes, o JaCoCo gera automaticamente o relatório de cobertura.

### Acessar relatório

Abra o arquivo abaixo no navegador:

```shell
target/site/jacoco/index.html
```

O relatório apresenta:
- Cobertura por pacote
- Cobertura por classe
- Cobertura por método e linha

---
## 🐳 Subindo o Ambiente com Docker

O projeto possui configuração para execução via Docker Compose.

Pré-requisitos
- Docker
- Docker Compose

### Subir o ambiente

```shell
docker-compose up --build -d
```

Isso irá:
- Subir o banco de dados
- Subir a aplicação Core Service
- Executar as migrations automaticamente

### Parar o ambiente

```shell
docker-compose down
```

## 📖 Documentação da API (Swagger)

Com a aplicação em execução:

- Swagger UI:

```shell
http://localhost:9090/swagger-ui.html
```

- OpenAPI JSON:

```shell
http://localhost:9090/v3/api-docs
```
--- 
## 📬 Testando a API com Postman
### Importar Collection
1. Abrir o Postman
2. Clicar em Import
3. Selecionar o arquivo da collection disponível no repositório
4. Importar também o Environment, se disponível

### Variáveis de Ambiente

Certifique-se de que o environment da Collection Postman possui:

- `base_url` = `http://localhost:9090`

### Executar os testes
- Utilize as requisições da collection
- Os endpoints seguem exatamente o contrato documentado no Swagger

--- 
## 🎓 Contexto Acadêmico

Projeto desenvolvido para:
- FIAP Pós-Tech
- Tech Challenge 2 (TC-2)

Com foco em:
- Arquitetura limpa
- Qualidade de código
- Testabilidade
- Boas práticas de engenharia de software

## 👥 Autores

| Nome                  |
|-----------------------|
| Lucas Santos Mumbarra |
| Suelen Peres          |
| Beatriz Ribeiro       | 
| Pilar Calderón        | 
| Caio Teles            |

---

## 🔗 Links úteis

- Swagger UI → http://localhost:9090/swagger-ui.html
- API Docs → http://localhost:9090/v3/api-docs
- Postman Collection → `/docs/postman`
- Banco (Docker) → `localhost:5432` (connect/food)
