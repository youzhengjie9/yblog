package com.boot.pojo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel(value = "封装查询出来的每一天的访客的数量")
public class OnedayVisitor implements Serializable {

    private String day; //日期
    private int count; //这一天（day）对应的访客数量

    public OnedayVisitor() {
    }

    public OnedayVisitor(String day, int count) {
        this.day = day;
        this.count = count;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "onedayVisitor{" +
                "day='" + day + '\'' +
                ", count=" + count +
                '}';
    }
}
