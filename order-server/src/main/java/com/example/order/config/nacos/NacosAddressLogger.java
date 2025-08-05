package com.example.order.config.nacos;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.example.order.config.rebbitmq.stream.StreamBridgeConsumer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class NacosAddressLogger {
    private static final Logger log = LoggerFactory.getLogger(NacosAddressLogger.class);

    @Autowired(required = false)
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Bean
    public ApplicationRunner printNacosServerAddress() {
        return args -> {
            if (nacosDiscoveryProperties != null) {
                String serverAddr = nacosDiscoveryProperties.getServerAddr();
                String namespace = nacosDiscoveryProperties.getNamespace();
                String group = nacosDiscoveryProperties.getGroup();
                log.info("✅ Nacos Server Address: {}", serverAddr);
                log.info("✅ Nacos Namespace: {}", namespace);
                log.info("✅ Nacos Group: {}", group);
            } else {
                log.warn("⚠️ 未找到 NacosDiscoveryProperties，可能未启用 Nacos Discovery");
            }
        };
    }
}
