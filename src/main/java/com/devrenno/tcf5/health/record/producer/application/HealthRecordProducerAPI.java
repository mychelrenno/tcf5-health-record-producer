package com.devrenno.tcf5.health.record.producer.application;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Health Record Producer", description = "Endpoints para cadastro de prontuários do SUS")
@RequestMapping("/health-record-producer/v1")
public interface HealthRecordProducerAPI {
    @Operation(
            summary = "Cadastrar prontuário de qualquer unidade SUS",
            description = "Recebe um JSON arbitrário enviado por qualquer unidade de saúde do SUS, " +
                    "enriquece com metadados e publica de forma assíncrona no tópico 'atendimentos-brutos'."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Prontuário aceito para processamento assíncrono via Kafka/Redpanda"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida: patientId, unidadeOrigem ou JSON vazio",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no processamento do prontuário",
                    content = @Content)
    })
    @PostMapping("/publish")
    ResponseEntity<Void> healthRecordPublish(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Qualquer JSON válido representando o prontuário da unidade de saúde",
                    required = true,
                    content = @Content(mediaType = "application/json")
            )
            String jsonRaw,

            @Parameter(
                    description = "CPF ou identificador único do paciente",
                    required = true,
                    example = "12345678901",
                    in = ParameterIn.HEADER
            )
            @RequestHeader String patientId,

            @Parameter(
                    description = "Código da unidade SUS que está enviando o prontuário",
                    required = true,
                    example = "UBS-CENTRO",
                    in = ParameterIn.HEADER
            )
             @RequestHeader String unitOrigin
    );
}
