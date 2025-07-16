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

        // 获取响应
        Response response = chain.proceed(request);

        // 打印响应信息
        System.out.println("OkHttp Response Code: " + response.code());
        System.out.println("Response Headers: " + response.headers());

        return response;
    }
}