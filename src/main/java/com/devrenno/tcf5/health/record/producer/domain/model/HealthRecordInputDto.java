package com.devrenno.tcf5.health.record.producer.domain.model;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthRecordInputDto {

    private String clientId;       // Identificador único

    private String jsonRaw;         // JSON original enviado pela unidade (qualquer formato)

}
