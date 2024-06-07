package com.example.client.request;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wq
 * @version 1.0
 * @date 2021/03/04 17:02
 */
@Data
public class PushRequest implements Serializable {

    /**
     * 推送用户ID
     */
    private Integer userId;

    /**
     * 推送指定用户集合
     */
    private List<Integer> userIds;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 推送消息
     */
    private String content;

    /**
     * registerId
     */
    private String registerId;

    /**
     * 图片
     */
    private String images;

    /**
     * 自定义字段
     */
    private JSONObject extras;


}
