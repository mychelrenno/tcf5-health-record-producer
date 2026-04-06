package com.devrenno.tcf5.health.record.producer.application.port.input;

public interface HealthRecordProducerPortInput {

    void publishHealthRecordRaw(String jsonRaw, String patientId, String unitOrigin);

}
