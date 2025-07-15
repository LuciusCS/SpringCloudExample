//package com.example.order.config.feign;
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
//import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
//import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
//import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.env.Environment;
//
//@Configuration
//public class LoadBalancerConfiguration {
//
//    @Bean
//    public ReactorLoadBalancer<ServiceInstance> roundRobinLoadBalancer(
//            Environment environment,
//            LoadBalancerClientFactory loadBalancerClientFactory) {
//
//        String serviceId = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
//        return new RoundRobinLoadBalancer(
//                loadBalancerClientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class),
//                serviceId);
//    }
//}
