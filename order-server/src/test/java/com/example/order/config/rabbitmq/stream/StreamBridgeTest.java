package com.example.order.config.rabbitmq.stream;



import com.example.order.config.rebbitmq.stream.StreamBridgeProduce;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
//@Import(AlarmConsumerPush.class) // 注册测试用的消费者监听器
//@DirtiesContext
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class StreamBridgeTest {
    @Autowired
    private StreamBridgeProduce alarmProducer;

    @Autowired
    StreamBridgeProduce streamBridgeProduce;
    public static CountDownLatch latch = new CountDownLatch(1); // 静态 latch 供消费端调用

    @Test
    void testAlarmDetectAndResolveMessageFlow() throws InterruptedException {
        // 准备消息体

        streamBridgeProduce.messageDetect("消息测试");
        // 等待最多 10 秒
        boolean received = latch.await(10, TimeUnit.SECONDS);


        System.out.println("执行完成");
        // 等待消息被消费

    }
}
