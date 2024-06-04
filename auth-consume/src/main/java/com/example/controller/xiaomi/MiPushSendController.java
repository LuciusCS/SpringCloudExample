package com.example.controller.xiaomi;


import com.example.entity.dto.EntityResponseBody;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

///用于小米推送调用
@RestController
@RequestMapping("xiaomi/push")
public class MiPushSendController {

    static  String APP_SECRET_KEY="";

    static  String MY_PACKAGE_NAME="";


    ///用于表示发送数据
    @PostMapping("postMessage")
    private ResponseEntity<Object> sendMessage() throws Exception {

        Constants.useOfficial();
        String regId="";
        Sender sender = new Sender(APP_SECRET_KEY);
        String messagePayload = "This is a message";
        String title = "notification title";
        String description = "notification description";
        Message message = new Message.Builder()
                .title(title)
                .description(description).payload(messagePayload)
                .restrictedPackageName(MY_PACKAGE_NAME)
                .notifyType(1)     // 使用默认提示音提示
                .build();
        Result result = sender.send(message, regId, 3);

        return EntityResponseBody.generateResponse("管理员获取角色列表" , HttpStatus.OK,result);

//        Log.v("Server response: ", "MessageId: " + result.getMessageId()
//                + " ErrorCode: " + result.getErrorCode().toString()
//                + " Reason: " + result.getReason());
    }
}
