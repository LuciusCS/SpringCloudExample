package com.example.order.config.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqConfig.class);

    public static final String queue1 = "demo_queue_1";

    /// 用于消息重试失败，消息保存的队列
    public static final String queue2 = "demo_queue_2";
    /// 用于死信队列
    public static final String dead_queue = "dead_queue";

    public static final String exchange = "demo_exchange";
    public static final String dead_exchange = "dead_exchange";

    public static final String queue_key_1 = "demo_queue_key_1";
    public static final String queue_key_2 = "demo_queue_key_2";
    public static final String dead_queue_key_1 = "dead_queue_key_1";

    //队列
    @Bean
    public Queue demoQueue1(){
//        return new Queue(queue1);

        return QueueBuilder.durable(queue1)
//                .withArgument("x-message-ttl", 6000)  // 设置消息过期时间 6秒
//                .withArgument("x-dead-letter-exchange", dead_exchange)  // 设置死信交换机
//                .withArgument("x-dead-letter-routing-key", dead_queue_key_1)  // 设置死信路由键
                .ttl(6000) // 更清晰的TTL设置方式。在队列维度上设置过期时间，超过这个时间没有消费的消息会被丢弃，有死信队列就丢到死信队列
                .deadLetterExchange(dead_exchange)
                .deadLetterRoutingKey(dead_queue_key_1)
                .build();
    }
    //队列
    @Bean
    public Queue demoQueue2(){
        return new Queue(queue2);
    }
    //直连交换机
    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(exchange, false, false);
    }
    //绑定 key，关联交换机和队列
    @Bean
    public Binding binding(Queue demoQueue1, DirectExchange exchange){
        return BindingBuilder.bind(demoQueue1).to(exchange).with(queue_key_1);
    }
    @Bean
    public Binding binding1(Queue demoQueue2, DirectExchange exchange){
        return BindingBuilder.bind(demoQueue2).to(exchange).with(queue_key_2);
    }

    @Bean
    public MessageConverter jsonToMapMessageConverter() {
        DefaultClassMapper defaultClassMapper = new DefaultClassMapper();
        defaultClassMapper.setTrustedPackages("com.example.order.bean"); // trusted packages
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        jackson2JsonMessageConverter.setClassMapper(defaultClassMapper);
        return jackson2JsonMessageConverter;
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rt = new RabbitTemplate(connectionFactory);
        rt.setMandatory(true);

        //确认回调函数ConfirmCallback是消息转发到交换机就会回调的函数，转发是否成功都会被调用
        rt.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                if(b){
                    logger.info("交换机接收消息编号：{}，消息：{} 成功！", s, correlationData);
                }else{
                    logger.info("交换机接收消息失败：{} 失败！原因：{}", correlationData, s);
                }
            }
        });
        //退回回调函数ReturnsCallback是消息转发到队列失败回调的函数，成功则不会执行
        rt.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage rs) {
//                logger.info("消息id：{}", rs.getMessage().getMessageProperties().getHeader("spring_returned_message_correlation").toString());
//                logger.info("交换机：{}", rs.getExchange());
//                logger.info("应答码：{}", rs.getReplyCode());
//                logger.info("应答文本：{}", rs.getReplyText());
//                logger.info("绑定键：{}", rs.getRoutingKey());
//                logger.info("returnsCallback 返回消息：{}", rs.getMessage());
            }
        });
        return rt;
    }


    // 重试失败的消息处理  使用消费者进行异常模拟
    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
        logger.info("重试也失败的消息！");
        /// 将失败的消息添加到其他队列
        return new RepublishMessageRecoverer(rabbitTemplate, RabbitMqConfig.exchange, RabbitMqConfig.queue_key_2);
    }


    //定义死信队列
    @Bean
    public Queue deadQueue(){
        return new Queue(dead_queue);
    }
    //定义死信交换机
    @Bean
    public DirectExchange deadExchange(){
        return new DirectExchange(dead_exchange);
    }
    //绑定
    @Bean
    public Binding deadBinding(Queue deadQueue, DirectExchange deadExchange){
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(dead_queue_key_1);
    }

    ///


}
