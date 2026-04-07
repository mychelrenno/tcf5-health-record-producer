package com.devrenno.tcf5.health.record.producer.application.port.output;

import com.devrenno.tcf5.health.record.producer.domain.model.HealthRecordInputDto;

public interface HealthRecordProducerPortOutput {

    void publishHealthRecordRaw(HealthRecordInputDto healthRecordInputDto);

}
