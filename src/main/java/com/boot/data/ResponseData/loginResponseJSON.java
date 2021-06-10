package com.boot.data.ResponseData;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel("登录界面输入框触发ajax异步发送数据，后台封装json串给前端")
public class loginResponseJSON implements Serializable {

    private String data; //数据
    private int result; //结果

    public loginResponseJSON() {
    }

    public loginResponseJSON(String data, int result) {
        this.data = data;
        this.result = result;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "loginResponseJSON{" +
                "data='" + data + '\'' +
                ", result=" + result +
                '}';
    }
}
