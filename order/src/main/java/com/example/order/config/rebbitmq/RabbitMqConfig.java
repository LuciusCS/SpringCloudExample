package com.example.order.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String queue1 = "demo_queue_1";
    public static final String queue2 = "demo_queue_2";

    public static final String exchange = "demo_exchange";

    public static final String queue_key_1 = "demo_queue_key_1";
    public static final String queue_key_2 = "demo_queue_key_2";

    //队列
    @Bean
    public Queue demoQueue1(){
        return new Queue(queue1);
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
}
