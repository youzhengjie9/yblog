package com.boot.pojo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel(value = "归档实体类",description = "封装归档的信息")
public class Archive implements Serializable {
    /**
     * 不建立对应的数据库，因为这个归档每次都是从t_article表中查询
     */

    private String months; //归档年-月
    private String count; //对应归档年-月匹配的文章数量

    public Archive() {
    }

    public Archive(String months, String count) {
        this.months = months;
        this.count = count;
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "archive{" +
                "months='" + months + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
