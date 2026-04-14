package com.devrenno.tcf5.health.record.producer.application.usecase.mapper;

import com.devrenno.tcf5.health.record.producer.domain.exception.UnauthorizedException;
import com.devrenno.tcf5.health.record.producer.domain.model.HealthRecordInputDto;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class InputsToHealthRecordRaw {

        public static HealthRecordInputDto mapInputsToHealthRecordInputDto(String jsonRaw, String authorization) throws UnauthorizedException {
            log.info("Iniciando processamento do prontuário: {}", jsonRaw);


            String token = authorization.trim();
            if (token.isEmpty()) {
                throw new UnauthorizedException("Requisição não autorizada: authorization está vazio");
            } else if (token.toLowerCase().startsWith("bearer ")) {
                token = token.substring(7).trim();
            } else {
                throw new UnauthorizedException("Requisição não autorizada: authorization inválido");
            }


            if (jsonRaw == null || jsonRaw.trim().isEmpty()) {
                throw new IllegalArgumentException("Requisição inválida: corpo do JSON está vazio");
            }

            return HealthRecordInputDto.builder()
                    .clientId(token)
                    .jsonRaw(jsonRaw.trim())
                    .build();
        }
}
