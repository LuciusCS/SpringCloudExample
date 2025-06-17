package com.example.order.config.xxljob;
import com.example.order.config.rebbitmq.MqConsumer;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.UUID;
/**
 * @author 为每个执行器增加traceId
 * 可以通过Spring Aop拦截@XxlJob注解，去处理一些通用业务逻辑。
 * 例如追加TraceId 进行日志定位
 */
@Slf4j
@Order(1)
@Aspect
@Component
public class XxlJobAspect{

    private static final Logger logger = LoggerFactory.getLogger(XxlJobAspect.class);

    @Pointcut("@annotation(com.xxl.job.core.handler.annotation.XxlJob)")
    public void pointCut() {
    }

    @Around("pointCut() && @annotation(xxlJob)")
    public Object doAround(ProceedingJoinPoint point, XxlJob xxlJob) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);

        String jobName = xxlJob.value();
        StopWatch sw = new StopWatch();
        sw.start();
        logger.info("定时任务[{}]开始，开始时间：{}，输入参数：{}", jobName, LocalDateTime.now(), XxlJobHelper.getJobParam());
        Object proceed;
        try {
            proceed = point.proceed();
        } catch (Throwable e) {
            logger.warn("定时任务[{}]执行失败", jobName, e);
            failure(e, traceId);
            return null;
        }
        sw.stop();
        logger.info("定时任务[{}]结束！执行时间：{} ms", jobName, sw.getTotalTimeMillis());
//        log.info("定时任务[{}]结束！执行时间：{} ms", jobName, sw.getTotalTimeMillis());
        success(traceId);
        return proceed;
    }

    private void failure(Throwable e, String traceId) {
        //将异常信息输出到xxl-job日志中
        XxlJobHelper.handleFail("traceId=" + traceId + ",<br>exception=" + getStackTrace(e));
        MDC.remove("traceId");
    }

    private void success(String traceId) {
        XxlJobHelper.handleSuccess("traceId=" + traceId);
        MDC.remove("traceId");
    }

    /**
     * 该方法来捕获异常的堆栈跟踪信息，并将其转换为字符串
     * @param e 异常信息
     * @return 堆栈跟踪字符串
     */
    private String getStackTrace(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
