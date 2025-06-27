package com.example.order.filter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


import java.io.IOException;
import java.net.InetAddress;

/**
 * 在日志的上下文加入动态的内容，如 IP Host
 */
public class LoggingMDCFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {


            HttpServletRequest httpRequest = (HttpServletRequest) request;

            String clientIP = httpRequest.getHeader("X-Forwarded-For");

            if (clientIP == null || clientIP.isEmpty()) {
                clientIP = request.getRemoteAddr();
            }

            MDC.put("clientIP", clientIP);

            /**
             * 获取主机名，会造成执行时间过长，影响接口请求, 这个地方后面需要修改
             */
//            String hostName = InetAddress.getLocalHost().getHostName();

//            MDC.put("host", hostName);


            chain.doFilter(request, response);
        } finally {
            MDC.clear(); // 防止内存泄露
        }
    }
}
