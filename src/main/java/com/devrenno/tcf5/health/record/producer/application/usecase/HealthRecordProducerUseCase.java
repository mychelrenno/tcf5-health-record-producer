package com.devrenno.tcf5.health.record.producer.application.usecase;

import com.devrenno.tcf5.health.record.producer.application.port.input.HealthRecordProducerPortInput;
import com.devrenno.tcf5.health.record.producer.application.port.output.HealthRecordProducerPortOutput;
import com.devrenno.tcf5.health.record.producer.domain.exception.BusinessException;
import com.devrenno.tcf5.health.record.producer.domain.exception.UnauthorizedException;
import com.devrenno.tcf5.health.record.producer.domain.model.HealthRecordInputDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.devrenno.tcf5.health.record.producer.application.usecase.mapper.InputsToHealthRecordRaw.mapInputsToHealthRecordInputDto;


@Slf4j
@RequiredArgsConstructor
@Service
public class HealthRecordProducerUseCase implements HealthRecordProducerPortInput {

    private final HealthRecordProducerPortOutput eventPortOutput;

    @Override
    public void publishHealthRecordRaw(String jsonRaw, String authorization) {

        try {

            HealthRecordInputDto healthRecordInputDtoEvent = mapInputsToHealthRecordInputDto(jsonRaw, authorization);
            log.info("Inputs mapeados com sucesso para processamento assíncrono. ClientId: {}", healthRecordInputDtoEvent.getClientId());

            // Chama o UseCase (que vai para o Producer Kafka)
            eventPortOutput.publishHealthRecordRaw(healthRecordInputDtoEvent);

            log.info("Prontuário aceito para processamento assíncrono. ClientId: {}", healthRecordInputDtoEvent.getClientId());


        } catch (IllegalArgumentException e) {
            throw new BusinessException("Erro de validação ao processar prontuário: " + jsonRaw, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(e.getMessage());
        } catch (Exception e) {
            throw new BusinessException("Erro inesperado ao processar prontuário: " + jsonRaw, e.getMessage());
        }
    }


}
