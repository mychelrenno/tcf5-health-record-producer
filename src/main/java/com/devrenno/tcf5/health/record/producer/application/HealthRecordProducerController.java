package com.devrenno.tcf5.health.record.producer.application;

import com.devrenno.tcf5.health.record.producer.application.port.input.HealthRecordProducerPortInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HealthRecordProducerController implements HealthRecordProducerAPI {

    private final HealthRecordProducerPortInput healthRecordProducerPortInput;

    @Override
    public ResponseEntity<Void> healthRecordPublish(String jsonRaw, String authorization) {

        log.info("Recebido prontuário: {}", jsonRaw);

        healthRecordProducerPortInput.publishHealthRecordRaw(jsonRaw, authorization);

       // 202 Accepted = processamento assíncrono (ideal para Kafka)
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
