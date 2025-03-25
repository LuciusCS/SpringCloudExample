package com.crazymaker.springcloud.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BusiException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * 默认的错误编码
     */
    private static final int DEFAULT_CODE = -1;


    private static final String DEFAULT_MSG = "业务异常";

    /**
     * 业务错误编码
     */
    @lombok.Builder.Default
    private int errCode = DEFAULT_CODE;
    /**
     * 错误的提示信息
     */
    @lombok.Builder.Default
    private String errMsg = DEFAULT_MSG;

    public BusiException()
    {
        super(DEFAULT_MSG);
    }

    public static BusiException builder()
    {
        return new BusiException();
    }

    public BusiException errMsg(String msg)
    {
        this.errMsg = msg;
        return this;
    }

    public BusiException errCode(int errCode)
    {
        this.errCode = errCode;
        return this;
    }

    public BusiException build()
    {
        return this;
    }
}
