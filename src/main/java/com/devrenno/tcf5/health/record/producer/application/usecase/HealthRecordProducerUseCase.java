package com.devrenno.tcf5.health.record.producer.application.usecase;

import com.devrenno.tcf5.health.record.producer.application.port.input.HealthRecordProducerPortInput;
import com.devrenno.tcf5.health.record.producer.application.port.output.HealthRecordProducerPortOutput;
import com.devrenno.tcf5.health.record.producer.domain.exception.BusinessException;
import com.devrenno.tcf5.health.record.producer.domain.model.HealthRecordInputDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.devrenno.tcf5.health.record.producer.application.usecase.mapper.InputsToHealthRecordRaw.mapInputsToHealthRecordInputDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class HealthRecordProducerUseCase implements HealthRecordProducerPortInput {

    HealthRecordProducerPortOutput eventPortOutput;
    @Override
    public void publishHealthRecordRaw(String jsonRaw, String patientId, String unitOrigin) {


        // Envia para o Adapter do Kafka (processamento assíncrono)
        try {

            HealthRecordInputDto healthRecordInputDtoEvent = mapInputsToHealthRecordInputDto(jsonRaw, patientId, unitOrigin);

            // Chama o UseCase (que vai para o Producer Kafka)
            eventPortOutput.publishHealthRecordRaw(healthRecordInputDtoEvent);

            log.info("Prontuário aceito para processamento assíncrono. PatientId: {}", healthRecordInputDtoEvent.getPatientId());


        } catch (IllegalArgumentException e) {
            throw new BusinessException("Erro de validação ao processar prontuário: {}", e.getMessage());
        } catch (Exception e) {
            throw new BusinessException("Erro inesperado ao processar prontuário do patientId: {}" + patientId, e.getMessage());
        }
    }


}
