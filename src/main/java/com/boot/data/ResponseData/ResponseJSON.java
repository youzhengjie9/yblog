package com.boot.data.ResponseData;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel("响应JSON，向前端传入数据封装到此类中")
public class ResponseJSON implements Serializable {

    private String data; //数据
    private boolean dt; //布尔
    private int result; //结果

    public ResponseJSON() {
    }

    public boolean isDt() {
        return dt;
    }

    public void setDt(boolean dt) {
        this.dt = dt;
    }

    public ResponseJSON(String data, int result) {
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
        return "ResponseJSON{" +
                "data='" + data + '\'' +
                ", dt=" + dt +
                ", result=" + result +
                '}';
    }
}
