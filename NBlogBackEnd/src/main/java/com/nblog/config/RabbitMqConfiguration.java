package com.nblog.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.nblog.constant.MQPrefixConst.*;

/**
 *
 * @author liulin
 * RabbitMQ 配置
 *
 */
@Configuration
public class RabbitMqConfiguration {
    /**
     * 处理文章消息的 队列
     * @return
     */
    @Bean
    public Queue ESQueue() {
        return new Queue(ES_QUEUE, true);
    }

    /**
     * 处理文章消息的 交换机，采用 fanout 模式
     * @return
     */
    @Bean
    public DirectExchange ESExchange() {
        return new DirectExchange(ES_EXCHANGE, true, false);
    }

    /**
     * 绑定队列和交换机
     * @return
     */
    @Bean
    public Binding bindingESDirect() {
        return BindingBuilder.bind(ESQueue()).to(ESExchange()).with(ES_ROUTING_KEY);
    }

    /**
     * 处理邮件消息的 队列
     * @return
     */
    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    /**
     * 处理邮件消息的 交换机，采用 fanout 模式
     * @return
     */
    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EMAIL_EXCHANGE, true, false);
    }

    /**
     * 绑定队列和交换机
     * @return
     */
    @Bean
    public Binding bindingEmailDirect() {
        return BindingBuilder.bind(emailQueue()).to(emailExchange()).with(EMAIL_ROUTING_KEY);
    }
}
