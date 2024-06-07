package com.example.controller.oppo;

import com.alibaba.fastjson.JSONObject;
import com.example.client.HttpClientService;
import com.example.client.request.PushRequest;
import com.example.client.response.SendResponse;
import com.example.controller.push.PushConstant;
import com.example.entity.dto.EntityResponseBody;
import com.oppo.push.server.Notification;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import  com.oppo.push.server.Sender;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("oppo/push")
public class OppoPushSendController {

    ///用于表示http请求
    @Resource
    private HttpClientService httpClientService;

    @Value("${push.oppo.appkey}")
    private String appKey;

    @Value("${push.oppo.masterSecret}")
    private String masterSecret;

    private ResponseEntity<Object> oppoSendMessage() throws Exception {

//        Oppo
        // 使用appKey, masterSecret创建sender对象（每次发送消息都使用这个sender对象）
        Sender sender = Sender.newBuilder()
                .appKey("appKey") // 设置appKey
                .masterSecret("masterSecret") // 设置masterSecret
//                .env(Environment.CHINA_PRODUCTION)  // 中国
                .httpMaxConnection(64) // 设置http最大连接数
                .httpMaxRoute(64)   // 设置最大http路由连接数
                .httpConnectionTimeout(5000) // http连接超时时间
                .httpConnectRequestTimeout(5000) // 等待连接超时时间
                .httpSocketTimeout(5000) // socket超时时间
                .build();

//        sender.


        return EntityResponseBody.generateResponse("管理员获取角色列表" , HttpStatus.OK,"");

    }

    public boolean singlePush(PushRequest req) throws IOException {
        String accessToken=getAccessToken();

        Map<String,Object>map=new HashMap<>();
        map.put("auth_token",accessToken);
        JSONObject jsonData=new JSONObject();
        jsonData.put("target_type",2);
        jsonData.put("target_value",req.getRegisterId());

        ///封装消息内容
        JSONObject notification=new JSONObject();
        notification.put("title", req.getTitle());
        notification.put("style", 1);
        notification.put("click_action_type", 0);
        notification.put("off_line", true);
        notification.put("content", req.getContent());
        jsonData.put("notification", notification);
        map.put("message", jsonData);

        SendResponse sendResponse=httpClientService.doPost(PushConstant.OPPO_SERVER_URL + PushConstant.OPPO_PUSH_URL, map);
        return JSONObject.parseObject(sendResponse.getMsg()).getInteger("code").equals(PushConstant.ZERO);



    }


    /**
     * 这是构造一个通知栏消息体Notification对象的实例方法。
     * 请注意，后续的例子会调用这个getNotification()方法
     * */
    private Notification getNotification() {
        Notification notification = new Notification();

        // 标题，内容是必填项
        notification.setTitle("通知栏消息tile");
        notification.setContent("通知栏内容");

        /*
         * 以下参数非必填项，参考OPPO push服务端api文档进行设置，本示例选取部分参数进行示范
         */
        //通知栏样式 1. 标准样式  2. 长文本样式  3. 大图样式 【非必填，默认1-标准样式】
        notification.setStyle(1);
        // App开发者自定义消息Id，OPPO推送平台根据此ID做去重处理，对于广播推送相同appMessageId只会保存一次，对于单推相同appMessageId只会推送一次
        notification.setAppMessageId(UUID.randomUUID().toString());
        // 应用接收消息到达回执的回调URL，字数限制200以内，中英文均以一个计算
        notification.setCallBackUrl("http://www.test.com");
        // App开发者自定义回执参数，字数限制50以内，中英文均以一个计算
        notification.setCallBackParameter("");
        // 点击动作类型0，启动应用；1，打开应用内页（activity的intent action）；2，打开网页；4，打开应用内页（activity）；【非必填，默认值为0】;5,Intent scheme URL
        notification.setClickActionType(4);
        // 应用内页地址【click_action_type为1或4时必填，长度500】
        notification.setClickActionActivity("com.coloros.push.demo.component.InternalActivity");
        // 网页地址【click_action_type为2必填，长度2000】
        notification.setClickActionUrl("http://www.test.com");
        // 动作参数，打开应用内页或网页时传递给应用或网页【JSON格式，非必填】，字符数不能超过4K，示例：{"key1":"value1","key2":"value2"}
        notification.setActionParameters("{\"key1\":\"value1\",\"key2\":\"value2\"}");
        // 展示类型 (0, “即时”),(1, “定时”)
        notification.setShowTimeType(1);
        // 定时展示开始时间（根据time_zone转换成当地时间），时间的毫秒数
        notification.setShowStartTime(System.currentTimeMillis() + 1000 * 60 * 3);
        // 定时展示结束时间（根据time_zone转换成当地时间），时间的毫秒数
        notification.setShowEndTime(System.currentTimeMillis() + 1000 * 60 * 5);
        // 是否进离线消息,【非必填，默认为True】
        notification.setOffLine(true);
        // 离线消息的存活时间(time_to_live) (单位：秒), 【off_line值为true时，必填，最长3天】
        notification.setOffLineTtl(24 * 3600);
        // 时区，默认值：（GMT+08:00）北京，香港，新加坡
        notification.setTimeZone("GMT+08:00");
        // 0：不限联网方式, 1：仅wifi推送
        notification.setNetworkType(0);
        return notification;
    }

//   用于表示获取accessToken
    public  String getAccessToken() throws IOException {

        Map<String,Object>map=new HashMap<>();
        map.put("app_key",appKey);
        long time=System.currentTimeMillis();
        String encodeData=appKey+time+masterSecret;
        map.put("sign",getSha256(encodeData));
        map.put("timestamp",time);
        SendResponse sendResponse=httpClientService.doPost(PushConstant.OPPO_SERVER_URL+PushConstant.OPPO_TOKEN_URL,map);
        String msg=sendResponse.getMsg();

        if(PushConstant.ZERO.equals(JSONObject.parseObject(msg).getInteger("code"))){
            String data=JSONObject.parseObject(msg).getString("data");
            return JSONObject.parseObject(data).getString("auth_token");
        }

        return null;
    }


    /**
     * 获取sha256
     *
     * @param data data
     * @return String
     */
    public static String getSha256(String data) {
        MessageDigest messageDigest;
        String encodestr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(data.getBytes(StandardCharsets.UTF_8));
            encodestr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodestr;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String temp = null;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                sb.append("0");
            }
            sb.append(temp);
        }
        return sb.toString();
    }


}
