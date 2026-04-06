package com.devrenno.tcf5.health.record.producer.application.usecase.mapper;

import com.devrenno.tcf5.health.record.producer.domain.model.HealthRecordInputDto;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;


@Slf4j
public class InputsToHealthRecordRaw {

        public static HealthRecordInputDto mapInputsToHealthRecordInputDto(String jsonRaw, String patientId, String unitOrigin) {
            log.info("Iniciando processamento do prontuário. PatientId: {} | Unidade: {} | JsonRaw: {}",
                    patientId, unitOrigin, jsonRaw);

            if (patientId == null || patientId.trim().isEmpty()) {
                throw new IllegalArgumentException("Requisição inválida: patientId é obrigatório");
            }

            if (unitOrigin == null || unitOrigin.trim().isEmpty()) {
                throw new IllegalArgumentException("Requisição inválida: unidadeOrigem é obrigatório");
            }

            if (jsonRaw == null || jsonRaw.trim().isEmpty()) {
                throw new IllegalArgumentException("Requisição inválida: corpo do JSON está vazio");
            }

            return HealthRecordInputDto.builder()
                    .patientId(patientId.trim())
                    .unitOrigin(unitOrigin.trim())
                    .jsonRaw(jsonRaw.trim())
                    .receivedAt(Instant.now().toString())
                    .build();
        }
}
