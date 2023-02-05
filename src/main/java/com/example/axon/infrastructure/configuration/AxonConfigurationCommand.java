package com.example.axon.infrastructure.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("command")
@EntityScan(basePackages = {
    "org.axonframework.eventsourcing"} )
@Configuration
public class AxonConfigurationCommand {

}
