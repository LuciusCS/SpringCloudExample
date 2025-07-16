package com.example.order.feign;

import com.example.order.config.feign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "seckill-server", configuration = FeignConfiguration. class, fallback = SeckillFeignClientFallback.class)
public interface SeckillFeignClient {

    /**
     * @SentinelResource 注解：指明这个 Feign 方法将由 Sentinel 保护，value 是资源名，fallback 是指定的回调方法（熔断时调用）
     * @SentinelResource 是用于方法层面限流/熔断的注解，但只支持：
     * 本地方法调用    暂时不这么用了
     * Spring Bean 的方法（要能被 AOP 托管）
     * @return
     */
//    @SentinelResource(value = "getTestHello", fallback = "fallbackGetTestHello")  // Sentinel 熔断资源
    @RequestMapping(method = RequestMethod.GET, value = "/api/hello")
    String getTestHello();



    // Fallback 方法, 也可以加参数，跟 getTestHello参数一致
    default String fallbackGetTestHello(Throwable t) {
        return "fallbackGetTestHello";
    }
}
