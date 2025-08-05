package com.example.order.config.rebbitmq.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class StreamBridgeProduce {

    @Autowired
    private StreamBridge streamBridge;

    public void messageDetect(String info){
        streamBridge.send("messageDetect-out-0",info);
    }
}
