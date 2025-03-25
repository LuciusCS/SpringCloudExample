package com.example.seckillserver.controller;


import com.example.seckillserver.api.dto.SeckillDTO;
import com.example.seckillserver.api.dto.SeckillOrderDTO;
import com.example.seckillserver.common.exception.BusinessException;
import com.example.seckillserver.constants.SessionConstants;
import com.example.seckillserver.result.RestOut;
import com.example.seckillserver.service.RedisSeckillServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api/seckill/redis/")
@Tag(name = "秒杀练习  RedisLock 版本")
public class SeckillByRedisLockController {
    /**
     * 秒杀服务实现 Bean
     */
    @Resource
    RedisSeckillServiceImpl redisSeckillServiceImpl;


    /**
     * 获取秒杀的令牌
     */
    @Operation(summary = "排队获取秒杀令牌")
    @RequestMapping(value = "/token/{exposedKey}/v1", method = RequestMethod.GET)
    RestOut<String> getSeckillToken(
            @PathVariable String exposedKey, HttpServletRequest request) {
        /// 从Header中获取userid
        String userIdentifier = request.getHeader(SessionConstants.USER_IDENTIFIER);
        if (null == userIdentifier) {
            throw BusinessException.builder().errMsg("用户id不能为空").build();

        }

        String result = redisSeckillServiceImpl.getSeckillToken(
                exposedKey,
                userIdentifier);
        return RestOut.success(result).setRespMsg("这是获取的结果");

    }

    /**
     * 执行秒杀的操作
     *
     * @return
     */
    @Operation(summary = "秒杀")
    @PostMapping("/do/v1")
    RestOut<SeckillOrderDTO> executeSeckill(@RequestBody SeckillDTO dto) {
        SeckillOrderDTO orderDTO = redisSeckillServiceImpl.executeSeckill(dto);
        return RestOut.success(orderDTO).setRespMsg("秒杀成功");
    }


}
