package com.example.order.config.rebbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MqConsumer {
    private static final Logger logger = LoggerFactory.getLogger(DeadLetterConsumer.class);

    @RabbitListener(queues = RabbitMqConfig.queue1)
    public void c1(Message message) {

        logger.info( "消费者消费信息：");
        logger.info(new String(message.getBody()));

        //模拟异常 ，测试 RabbitMQ的重试机制，是可以正常使用的
//        throw new RuntimeException();
    }
}
