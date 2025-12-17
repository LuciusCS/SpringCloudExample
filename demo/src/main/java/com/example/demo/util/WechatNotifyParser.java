package com.example.demo.util;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

public class WechatNotifyParser {
    public static WechatNotify parse(String xml) {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.example.demo");  // 根据你自己的包名调整

        // 使用 StreamSource 将字符串转换为 XML Source
        StreamSource source = new StreamSource(new StringReader(xml));

        WechatNotify notify = (WechatNotify) marshaller.unmarshal(source);
        return notify;
    }
}
