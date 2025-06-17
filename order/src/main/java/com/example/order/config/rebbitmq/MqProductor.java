package com.example.order.config.rebbitmq;


import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqProductor {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void convertAndSend(Object data){

        // 创建一个MessagePostProcessor来设置过期时间
        //给每一条消息设置过期时间，如果同时队列也设置了过期时间，取两个时间较小的值。
        // 有个问题，这个时间判断是在消息即将投递时候才计算的，如果前面消息过期时间很长，可能导致后面的消息不能及时过期。
        MessagePostProcessor  messagePostProcessor=   new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置过期时间
                message.getMessageProperties().setExpiration("200");
                return message;
            }
        };

//        rabbitTemplate.convertAndSend(RabbitMqConfig.exchange, RabbitMqConfig.queue_key_1, data);
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchange, RabbitMqConfig.queue_key_1, data,messagePostProcessor,new CorrelationData());
    }
}
