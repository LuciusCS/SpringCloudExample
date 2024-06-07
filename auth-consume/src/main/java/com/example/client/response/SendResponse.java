package com.example.client.response;

public class SendResponse {
    private final String code;
    private final String msg;
    private final String requestId;


    public SendResponse(String code, String msg, String requestId) {
        this.code = code;
        this.msg = msg;
        this.requestId = requestId;
    }

    public SendResponse(String statusCode, String msg) {
        this.code = statusCode;
        this.msg = msg;
        requestId = null;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getRequestId() {
        return requestId;
    }

    public static SendResponse fromCode(String coede, String msg, String requestId) {
        return new SendResponse(coede, msg, requestId);
    }
}
