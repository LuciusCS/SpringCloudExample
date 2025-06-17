package com.example.order.config.rebbitmq;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 创建一个消费者，来处理死信队列中的数据
 */
@Component
public class DeadLetterConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MqConsumer.class);

    @RabbitListener(queues = RabbitMqConfig.dead_queue)
    public void consumeDeadLetterMessage(String message) {
        // 打印死信消息
        logger.info("Received dead letter message: " + message);

        // 在这里进行后续处理，比如重试、存入数据库、记录日志等
        handleDeadLetterMessage(message);
    }

    public void handleDeadLetterMessage(String message) {
        // 例如，你可以检查消息是否符合某些条件，并进行后续处理
        // 比如重试、报警、存储到数据库等
        logger.info("Handling dead letter message: " + message);
    }
}
