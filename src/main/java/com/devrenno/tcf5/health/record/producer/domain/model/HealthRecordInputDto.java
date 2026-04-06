package com.devrenno.tcf5.health.record.producer.domain.model;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthRecordInputDto {

    private String patientId;       // CPF ou identificador único

    private String unitOrigin;      // Código da UBS / hospital

    private String jsonRaw;         // JSON original enviado pela unidade (qualquer formato)

    private String receivedAt;      // timestamp de recebimento

}
