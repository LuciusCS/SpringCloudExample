package com.example.client;


import com.alibaba.fastjson.JSONObject;
import com.example.client.response.SendResponse;
import jakarta.annotation.Resource;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


///用于表示网络请求client
@Component
public class HttpClientService {


    @Resource
    private CloseableHttpClient closeableHttpClient;


    ///这一个需要在哪个文件里进行配置
    @Resource
    private RequestConfig config;

    /**
     * 不带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
     */
    public String doGet(String url) throws Exception {

        //声明http get请求
        HttpGet httpGet = new HttpGet(url);
        ///用于表示装载配置信息
        httpGet.setConfig(config);

        ///发起请求
        CloseableHttpResponse response = this.closeableHttpClient.execute(httpGet);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        }


        return null;
    }

    /**
     * 带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
     */
    public String doGet(String url, Map<String, Object> map) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);


        if (map != null) {
            ///遍历map
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }


        return this.doGet(uriBuilder.build().toString());

    }

    /**
     * 带参数的post请求
     */
    public SendResponse doPost(String url, Map<String, Object> map) throws IOException {

        ///声明httpPost请求
        HttpPost httpPost = new HttpPost(url);
        ///加入配置信息
        httpPost.setConfig(config);

        if (map != null) {
            List<NameValuePair> list = new ArrayList<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }

            ///构造form表单
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");
            ///把表单放在post里
            httpPost.setEntity(urlEncodedFormEntity);
        }

        ///发起请求
        CloseableHttpResponse response = this.closeableHttpClient.execute(httpPost);

        return new SendResponse(String.valueOf(response.getStatusLine().getStatusCode()), EntityUtils.toString(response.getEntity(), "UTF-8"));


    }

    ///不带参数的post请求
    public SendResponse doPost(String url) throws IOException {
        return  this.doPost(url,null);
    }

    /**
     * 发送post json请求
     *
     * @param url
     * @param json
     * @return
     */
    public JSONObject doPostJSON(String url,String device,String authToken,JSONObject json){
        HttpPost postMethod=new HttpPost(url);
        JSONObject response=null;
        try{
            if(device.equals("xiaomi")){
                postMethod.addHeader("Authorization","key="+authToken);
            }else {
                postMethod.addHeader("authToken",authToken);
            }

            postMethod.addHeader("Content-type","application/json; charset=utf-8");
            postMethod.setEntity(new StringEntity(json.toJSONString(), StandardCharsets.UTF_8));

            HttpResponse httpResponse=this.closeableHttpClient.execute(postMethod);

            if(httpResponse.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                    String result=EntityUtils.toString(httpResponse.getEntity());
                    response=JSONObject.parseObject(result);
            }


        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return  response;
    }


    public JSONObject postJSON(String format,String accessToken,String toJSONString){
        HttpPost postMethod=new HttpPost(format);
//        HttpPost postMethod = new HttpPost(format);
        JSONObject response = null;
        try {
            postMethod.addHeader("Authorization", "Bearer " + accessToken);
            postMethod.addHeader("Content-type", "application/json; charset=utf-8");
            postMethod.setEntity(new StringEntity(toJSONString, StandardCharsets.UTF_8));
            HttpResponse res = this.closeableHttpClient.execute(postMethod);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                response = JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;

    }

}
