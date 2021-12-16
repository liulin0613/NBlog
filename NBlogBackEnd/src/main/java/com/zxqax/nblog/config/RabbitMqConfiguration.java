package com.zxqax.nblog.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.zxqax.nblog.constant.MQPrefixConst.*;


@Configuration
public class RabbitMqConfiguration {
    @Bean
    public Queue ESQueue() {
        return new Queue(ES_QUEUE, true);
    }

    @Bean
    public FanoutExchange ESExchange() {
        return new FanoutExchange(ES_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingESDirect() {
        return BindingBuilder.bind(ESQueue()).to(ESExchange());
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public FanoutExchange emailExchange() {
        return new FanoutExchange(EMAIL_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingEmailDirect() {
        return BindingBuilder.bind(emailQueue()).to(emailExchange());
    }
}
