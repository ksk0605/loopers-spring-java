package com.loopers.infrastructure.payment.pgsimulator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class PgSimulatorConfig {

    @Bean
    ErrorDecoder errorDecoder() {
        return new PgSimulatorErrorDecoder(new ObjectMapper());
    }
}
