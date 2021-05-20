package com.boot.data.ResponseData;

/**
 * 前端请求响应的封装类
 *
 */
public class ArticleResponseData<T> {
    private T payload;        //服务器响应数据
    private boolean success; //请求是否成功
    private String msg;       // 错误信息
    private int code = -1;   // 状态码
    private long timestamp; //服务器响应时间

    public ArticleResponseData() {
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    public ArticleResponseData(boolean success) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
    }

    public ArticleResponseData(boolean success, T payload) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.payload = payload;
    }

    public ArticleResponseData(boolean success, T payload, int code) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.payload = payload;
        this.code = code;
    }

    public ArticleResponseData(boolean success, String msg) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.msg = msg;
    }

    public ArticleResponseData(boolean success, String msg, int code) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.msg = msg;
        this.code = code;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static ArticleResponseData ok() {
        return new ArticleResponseData(true);
    }

    public static <T> ArticleResponseData ok(T payload) {
        return new ArticleResponseData(true, payload);
    }

    public static <T> ArticleResponseData ok(int code) {
        return new ArticleResponseData(true, null, code);
    }

    public static <T> ArticleResponseData ok(T payload, int code) {
        return new ArticleResponseData(true, payload, code);
    }

    public static ArticleResponseData fail() {
        return new ArticleResponseData(false);
    }

    public static ArticleResponseData fail(String msg) {
        return new ArticleResponseData(false, msg);
    }

    public static ArticleResponseData fail(int code) {
        return new ArticleResponseData(false, null, code);
    }

    public static ArticleResponseData fail(int code, String msg) {
        return new ArticleResponseData(false, msg, code);
    }

}
