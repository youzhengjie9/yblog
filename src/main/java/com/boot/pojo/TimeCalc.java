package com.boot.pojo;

import io.swagger.annotations.ApiModel;

/**
 * @author 游政杰
 */
@ApiModel("封装接口耗时")
public class TimeCalc {

    private int id;
    private String uri; //接口uri
    private String calc; //访问接口耗时
    private String time; //访问接口的时间

    public int getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCalc() {
        return calc;
    }

    public void setCalc(String calc) {
        this.calc = calc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "TimeCalc{" +
                "id=" + id +
                ", uri='" + uri + '\'' +
                ", calc='" + calc + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
