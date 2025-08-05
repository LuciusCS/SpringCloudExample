package com.example.order.config.rebbitmq.stream;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

//@Slf4j
@Configuration
public class StreamBridgeConsumer {
    private static final Logger log = LoggerFactory.getLogger(StreamBridgeConsumer.class);

    @Bean
    public Consumer<String> messageDetect(){
       return  payload->{
           log.warn("输出接收到的数据："+payload);
       };
    }

    @Bean
    public Consumer<String> messageDetect2(){
        return  payload->{
            log.warn("输出接收到的数据11："+payload);
        };
    }
}
