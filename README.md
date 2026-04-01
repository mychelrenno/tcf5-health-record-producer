# tcf5-health-record-producer

Serviço responsável por receber prontuários eletrônicos em qualquer formato JSON enviado por unidades do SUS e publicá-los de forma assíncrona no Apache Kafka para posterior transformação em padrão HL7 JSON.

Este é um dos microsserviços do projeto **Consolidação de Prontuários SUS** desenvolvido para o Hackathon Fase 5 - Pós-Tech Arquitetura e Desenvolvimento Java (FIAP / POS TECH).

## 🎯 Objetivo

Permitir que **qualquer unidade de saúde** (UBS, hospital, posto avançado, etc.) envie um prontuário no formato JSON que possuir, sem a necessidade de seguir um padrão rígido na origem.  
O serviço publica o JSON cru no Kafka, onde outro serviço (`tcf5-health-record-transformer`) fará a transformação para o padrão HL7 JSON e persistirá no banco de dados centralizado.

## 🏗️ Arquitetura

- **Arquitetura Hexagonal** (Ports and Adapters)
- Separação clara entre domínio, aplicação e infraestrutura
- Comunicação assíncrona via **Apache Kafka**
- API REST simples para integração com o API Gateway

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.3+**
- **Spring for Apache Kafka**
- **Lombok**
- **Maven**
- **Docker** + **Docker Compose**
- **Apache Kafka** + **Zookeeper**
- **Postgres** (utilizado pelos demais serviços)

## Estrutura do Projeto (Hexagonal)

src/main/java/com/tcf5/healthrecord/producer
├── domain
│ └── model
│ └── ProntuarioRaw.java
├── application
│ ├── port
│ │ ├── input
│ │ └── output
│ └── usecase
│ └── CadastrarProntuarioUseCase.java
├── infrastructure
│ ├── adapter
│ │ ├── kafka
│ │ └── web
│ └── config
└── HealthRecordProducerApplication.java

## Endpoints

`POST /v1/prontuarios`

- Parâmetros de Query:

* `patientId` → CPF ou identificador único do paciente (usado como chave de partição)
* `unidadeOrigem` → Código da unidade de saúde que está enviando o prontuário

- Body: Qualquer JSON válido (prontuário cru)

## Fluxo Completo do Sistema

1. Unidade do SUS → envia JSON cru para o Producer
2. Producer → publica no tópico prontuarios-raw
3. Consumer (tcf5-health-record-transformer) → consome, transforma com Jolt para HL7 JSON
4. HL7 JSON → salvo no PostgreSQL
5. Qualquer outra unidade → consulta via tcf5-health-record-provider

## Como Executar

### 1. Subir as dependências (Kafka + Postgres)

Na raiz do repositório (ou na pasta `infra`):

```bash
docker-compose up -d
```

### 2. Executar o producer

```bash
# Via Maven
./mvnw spring-boot:run

# Ou após build
java -jar target/tcf5-health-record-producer-0.0.1-SNAPSHOT.jar
```

### 3. Exemplo de requisição (cURL):

```bash
curl -X POST http://localhost:8080/v1/prontuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Oliveira",
    "cpf": "98765432100",
    "dataNascimento": "1985-03-15",
    "pressaoArterial": "120/80",
    "temperatura": 36.5,
    "queixa": "Dor de cabeça persistente",
    "medicamentos": ["Paracetamol 750mg"]
  }' \
  -G \
  --data-urlencode "patientId=98765432100" \
  --data-urlencode "unidadeOrigem=UBS-VILA_PROGRESSO"
```
