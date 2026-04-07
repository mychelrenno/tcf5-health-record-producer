package com.devrenno.tcf5.health.record.producer.infrastructure.event;

import com.devrenno.tcf5.health.record.producer.application.port.output.HealthRecordProducerPortOutput;
import com.devrenno.tcf5.health.record.producer.domain.exception.BusinessException;
import com.devrenno.tcf5.health.record.producer.domain.model.HealthRecordInputDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;



@Component
@Slf4j
@RequiredArgsConstructor
public class HealthRecordKafkaProducer implements HealthRecordProducerPortOutput {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    private final ObjectMapper objectMapper = objectMapper();

    @Value("${spring.kafka.topic.health-record-raw}")
    private String topic;


    public void publishHealthRecordRaw(HealthRecordInputDto raw) {
        try {


            log.info("Iniciando envio ao Tópico. PatientId: {}", raw.getPatientId());

            ObjectNode enriched = objectMapper.readValue(raw.getJsonRaw(), ObjectNode.class);

            // 2. Adiciona metadados no corpo (padrão simples "HL7 FHIR-like")
            enriched.put("receivedAt", raw.getReceivedAt());

            String payload = objectMapper.writeValueAsString(enriched);

            ProducerRecord<String, String> record = new ProducerRecord<>(
                    topic,
                    raw.getPatientId(),   // chave de partição (garante ordem por paciente)
                    payload
            );

            // Headers leves (apenas para auditoria, não são usados para transformação)
            record.headers().add("patientId", raw.getPatientId().getBytes());
            record.headers().add("unitOrigin", raw.getUnitOrigin().getBytes());

            kafkaTemplate.send(record).whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Falha ao publicar prontuário. PatientId: {}", raw.getPatientId(), ex);
                } else {
                    log.info("Prontuário enriquecido publicado com sucesso. PatientId: {} | Offset: {}",
                            raw.getPatientId(), result.getRecordMetadata().offset());
                }
            });

        } catch (Exception e) {
            log.error("Erro ao enriquecer e publicar prontuário. PatientId: {}", raw.getPatientId(), e);
            throw new BusinessException("Falha ao processar prontuário para Kafka", e.getMessage());
        }
    }
}
