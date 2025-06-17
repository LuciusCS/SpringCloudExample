package com.example.order.component;

import com.example.order.controller.OrderController;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderCleanupJob {

    private static final Logger logger = LoggerFactory.getLogger(OrderCleanupJob.class);

    @XxlJob("cleanExpiredOrders")
    public void cleanExpiredOrders() {
        // 假设我们有一个订单服务，它能清理掉过期的订单
//        orderService.cleanupExpiredOrders();
        //只会显示再项目日志中
        logger.debug("开发环境清理过期订单"); // 开发环境可见
        logger.info("生产环境清理过期订单"); // 生产环境可见
        logger.error("生产环境清理过期订单异常"); // 错误日志
        //只会显示在xxl-job的日志中
        XxlJobHelper.log("无参数执行一个定时/指定任务");
    }
}
