package com.example.seckillserver.standard.properties;


import com.example.seckillserver.ratelimit.RedisRateLimitImpl;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/// 这个来说目前没用，因为没有在yml中进行配置
/// 秒杀的限制信息如 LimiterInfo 中的内容
@Data
@ConfigurationProperties(prefix = "application.rate.limit.redis" )
public class RedisRateLimitProperties
{
    private List<RedisRateLimitImpl.LimiterInfo> limiterInfos;



}


/**
 *
 * 对应 @ConfigurationProperties(prefix = "application.rate.limit.redis" )
 * application.yml文件下的配置示例
 *
 * application:
 *   rate:
 *     limit:
 *       redis:
 *         typeInfos:
 *           - key: user_request
 *             type: ip
 *             maxPermits: 100
 *             rate: 10
 *         rateLimitLua: classpath:script/rate_limiter.lua
 *
 *
 */



