package com.nblog.consumer;

import com.nblog.utils.EmailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.nblog.constant.MQPrefixConst.EMAIL_QUEUE;

/**
 * @author liulin
 * 邮箱消费者
 * 订阅了邮箱队列
 */
@Component
@RabbitListener(queues = EMAIL_QUEUE)
@Slf4j
public class EmailConsumer {
    private EmailUtils emailUtils;

    /**
     * setter 方式注入 , 不允许 final 属性
     * @param emailUtils
     */
    @Autowired
    public void setEmailUtils(EmailUtils emailUtils) {
        this.emailUtils = emailUtils;
    }

    @RabbitHandler
    public void process(Map<String,String> data){
        emailUtils.sendEmail(data.get("email"),data.get("code"));
        log.info("消费了消息：" + data.toString());
    }
}
