package com.example.order.config.feign;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import java.io.IOException;
public class CustomLoggingInterceptor  implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        // 打印请求信息
        okhttp3.Request request = chain.request();
        System.out.println("OkHttp Request: " + request.url());
        System.out.println("Request Headers: " + request.headers());
        // 检查请求是否显式设置了长连接（通过 "Connection" 头）
        String requestConnection = request.header("Connection");
        if ("keep-alive".equalsIgnoreCase(requestConnection)) {
            System.out.println("Request is using a keep-alive connection.");
        } else {
            System.out.println("Request is not using a keep-alive connection.");
        }
        // 获取响应
        Response response = chain.proceed(request);

        // 打印响应信息
        System.out.println("OkHttp Response Code: " + response.code());
        System.out.println("Response Headers: " + response.headers());
        // 检查响应是否显式设置了长连接（通过 "Connection" 头）
        String responseConnection = response.header("Connection");
        if ("keep-alive".equalsIgnoreCase(responseConnection)) {
            System.out.println("Response is using a keep-alive connection.");
        } else {
            System.out.println("Response is not using a keep-alive connection.");
        }

        return response;
    }
}