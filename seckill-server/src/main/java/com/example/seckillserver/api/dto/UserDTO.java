package com.example.seckillserver.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description="用户")
public class UserDTO implements Serializable
{


    //用户ID
    @Schema(description ="用户ID")
    private Long userId;
    //用户名
    @Schema(description ="用户名")
    private String username;
    //用户登录密码
    @Schema(description ="密码")
    private String password;
    //用户姓名
    @Schema(description ="昵称")
    private String nickname;
    //用户token
    @Schema(description ="令牌")
    private String token;


    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
    private Date createTime;

    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
    private Date updateTime;


    private String headImgUrl;
    private String mobile;
    private Integer sex;
    private Boolean enabled;
    private String type;
    private String openId;
    private boolean isDel;


}
