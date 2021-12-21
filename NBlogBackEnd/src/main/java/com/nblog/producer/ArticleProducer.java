package com.nblog.producer;

import com.alibaba.fastjson.JSON;
import com.nblog.entity.Article;
import com.nblog.service.RedisService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.UUID;

import static com.nblog.constant.MQPrefixConst.ES_EXCHANGE;
import static com.nblog.constant.MQPrefixConst.ES_ROUTING_KEY;
import static com.nblog.constant.RedisPrefixConst.ARTICLE_MESSAGE;
import static com.nblog.constant.RedisPrefixConst.CONSUMER_ARTICLE_MESSAGE;

/**
 * @author liulin
 * 防止生产端丢消息
 *
 * ================================================
 *
 * RabbitMQ 保证消息可靠性全链路：
 *
 *      -- 生产端发送消息到 rabbitMQ     --> 采用 confirm 消息确认机制
 *                                     -->  消息补偿机制：消息入库 （ redis 存储 ）
 *
 *      -- RabbitMQ 自身问题：队列，交换机，消息持久化
 *
 *      -- RabbitMQ 发送消息到消费端    --> 自动 ack 机制改为手动 ack/nack
 *
 *      -- 重发机制防止重复消费（幂等性）         --> 采用 UUID 作为消息的唯一标识，被消费的消息从集合中删除
 *
 * ================================================
 *
 */

@Slf4j
@Component
@ConfigurationProperties(prefix = "spring.rabbitmq.message")
@Data
public class ArticleProducer implements RabbitTemplate.ConfirmCallback , RabbitTemplate.ReturnsCallback {
    private RabbitTemplate rabbitTemplate;
    private RedisService redisService;
    private Boolean retry;

    @Autowired
    public ArticleProducer(RabbitTemplate rabbitTemplate,RedisService redisService) {
        this.rabbitTemplate = rabbitTemplate;
        this.redisService = redisService;
        rabbitTemplate.setConfirmCallback(this);
    }

    // 发送消息
    public void sendMessage(Article article) {
        // 获取唯一消息的 UUID
        String uuid = UUID.randomUUID().toString();
        // UUID 发送给 MQ
        CorrelationData correlationData = new CorrelationData(uuid);
        // 将 UUID 以及对应的消息存入 Redis
        redisService.hSet(ARTICLE_MESSAGE,uuid,JSON.toJSONString(article));
        redisService.sAdd(CONSUMER_ARTICLE_MESSAGE,uuid);

        //消息发送失败返回到队列中, yml需要配置 publisher-returns: true
        rabbitTemplate.setMandatory(true);
        //消息消费者确认收到消息后，手动ack回执
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
        // 发送消息
        this.rabbitTemplate.convertAndSend(ES_EXCHANGE, ES_ROUTING_KEY, JSON.toJSONString(article), correlationData);
        // log
        log.info("【生产者】 ：" + uuid + " 已发送");
    }


    /**
     * 生产端可靠传递，confirm 消息确认机制
     * @param correlationData uuid
     * @param ack 回执
     * @param cause 原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String uuid = correlationData.getId();
        // 消息发送成功
        if(ack){
            log.info("【MQ】 ：" + uuid + " 已接收");
        }else { // 消息发送失败
            log.info("【MQ】 ：" + uuid + " 未接收，原因为"+ cause);

            if(retry){
                // 重新发送消息
                log.info("【生产者】 ：" + uuid + " 尝试重新发送");
                this.rabbitTemplate.convertAndSend(ES_EXCHANGE, "", redisService.hGet(ARTICLE_MESSAGE,uuid), correlationData);
            }
        }
    }

    /**
     * 消息路由失败时返回消息而不是丢弃
     * @param returned Message
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info(returned.toString());
    }
}
