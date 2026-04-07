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
- **Apache Kafka** + **Redpanda**


## Estrutura do Projeto (Hexagonal)
<img width="536" height="923" alt="image" src="https://github.com/user-attachments/assets/8654a4c6-8ac3-4cdf-9411-51936c7b9505" />



## Endpoints

`POST /health-record-producer/v1/publish`

- Parâmetros de Header:

* `patientId` → CPF ou identificador único do paciente (usado como chave de partição)
* `unitOrigin` → Código da unidade de saúde que está enviando o prontuário

- Body: Qualquer JSON válido (prontuário cru)

## Fluxo Completo do Sistema

1. Unidade do SUS → envia JSON cru para o Producer
2. Producer → publica no tópico prontuarios-raw
3. Consumer (tcf5-health-record-transformer) → consome, transforma com Jolt para HL7 JSON
4. HL7 JSON → salvo no PostgreSQL
5. Qualquer outra unidade → consulta via tcf5-health-record-provider

## Como Executar

### 1. Subir as dependências (Kafka)

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
curl --location 'http://localhost:8082/health-record-producer/v1/publish' \
--header 'accept: */*' \
--header 'unitOrigin: UPA_RECIFE_01' \
--header 'Content-Type: application/json' \
--header 'patientId: 123.456.789-00' \
--data '{
  "hospital_id": "UPA_RECIFE_01",
  "paciente": {
    "cpf": "123.456.789-00",
    "nome": "Thiago Silva"
  },
  "atendimento": {
    "data": "2026-03-26T14:30:00Z",
    "tipo": "Urgencia",
    "queixa": "Cefaleia e tontura",
    "pressao_arterial": "160/100",
    "diagnostico_cid": "I10",
    "condicao": "Hipertensão Essencial"
  }
}'
```
