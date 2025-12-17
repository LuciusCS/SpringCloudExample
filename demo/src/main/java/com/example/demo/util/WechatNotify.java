package com.example.demo.util;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WechatNotify {
    private String outTradeNo;      // 商户订单号
    private String transactionId;   // 微信支付订单号
    private BigDecimal totalAmount;     // 支付金额（以分为单位）
    private String sign;            // 签名
    private String resultCode;      // 结果代码（SUCCESS / FAIL）

    // 解析微信支付回调消息
    public static WechatNotify parse(String notifyBody) {
        // 假设你已经有一个XML解析工具将其转换为对象
        // 这里我给你简单的解析代码，你需要自己实现 XML -> Object 的转换
        // 可用第三方库：Jackson, FastXML等
        // 这里只是伪代码示例，具体实现要根据微信返回的 XML 格式来写

        // 例如：你可以使用 org.springframework.util.xml.XmlPullParser 或 Jackson 来解析
        WechatNotify notify = new WechatNotify();
        // 解析逻辑填充到 notify 对象中
        // notify.setOutTradeNo(...);
        // notify.setTransactionId(...);
        // ...
        return notify;
    }

    // 校验签名
    public boolean isSuccess() {
        // 这里用微信支付的签名验签逻辑来验证回调的合法性
        return "SUCCESS".equals(this.resultCode); // 示例
    }
}
