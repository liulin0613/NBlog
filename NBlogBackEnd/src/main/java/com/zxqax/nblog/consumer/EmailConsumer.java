package com.zxqax.nblog.consumer;

import com.zxqax.nblog.utils.EmailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.zxqax.nblog.constant.MQPrefixConst.EMAIL_QUEUE;

@Component
@RabbitListener(queues = EMAIL_QUEUE)
@Slf4j
public class EmailConsumer {

    @RabbitHandler
    public void process(Map<String,String> data){
        EmailUtils.sendEmail(data.get("email"),data.get("code"));
        log.info("消费了消息：" + data.toString());
    }
}
