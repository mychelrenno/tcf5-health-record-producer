package com.devrenno.tcf5.health.record.producer.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TCF5 Health Record Producer API")
                        .description("API responsável por receber prontuários de qualquer unidade do SUS " +
                                "e publicar de forma assíncrona no Redpanda/Kafka para consolidação.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe Hackathon Fase 5")
                                .email("seuemail@fiap.com.br")
                        )
                );
    }
}
