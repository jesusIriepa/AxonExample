package com.example.axon.infrastructure.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfiguration {

    @Bean
    public Queue declareQueue() {
        return new Queue("test-queue", true, false, false);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("test_direct_exchange", true, false);
    }

    @Bean
    public Binding declareBindings() {
        return BindingBuilder
            .bind(declareQueue())
            .to(fanoutExchange());
    }
}
