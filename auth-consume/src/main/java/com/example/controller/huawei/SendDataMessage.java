///*
// * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
// */
//package com.example.controller.huawei;
//
//import com.huawei.push.exception.HuaweiMesssagingException;
//import com.huawei.push.message.AndroidConfig;
//import com.huawei.push.message.Message;
//import com.huawei.push.messaging.HuaweiApp;
//import com.huawei.push.messaging.HuaweiMessaging;
//import com.huawei.push.model.Urgency;
//import com.huawei.push.reponse.SendResponse;
//import com.huawei.push.util.InitAppUtils;
//
//public class SendDataMessage {
//    /**
//     * send data message
//     *
////     * @throws HuaweiMesssagingException
//     */
//    public void sendTransparent() throws HuaweiMesssagingException {
//        HuaweiApp app = InitAppUtils.initializeApp();
//        HuaweiMessaging huaweiMessaging = HuaweiMessaging.getInstance(app);
//
//        AndroidConfig androidConfig = AndroidConfig.builder().setCollapseKey(-1)
//                .setUrgency(Urgency.HIGH.getValue())
//                .setTtl("10000s")
//                .setBiTag("the_sample_bi_tag_for_receipt_service")
//                .build();
//
//        String token = "IQAAAACy06bsAAAAHXD-2hML-zvhxr6Wg7kSLyYkWvx8n99tvfPLK1_boN4fjlgguH3it1nX_zm0fF3X03MCI7pUoDeAOfMG2Kl1sS8N00p2gw5mcA";
//
//        Message message = Message.builder()
//                .setData("{'key1':'value1', 'key2':'value2'}")
//                .setAndroidConfig(androidConfig)
//                .addToken(token)
//                .build();
//
//        SendResponse response = huaweiMessaging.sendMessage(message);
//    }
//
//    public static void main(String[] args) {
//        SendDataMessage sendDataMessage=new SendDataMessage();
//        try {
//            sendDataMessage.sendTransparent();
//
//        }catch (Exception e){
//            System.out.println(e.toString());
//        }
//    }
//}
