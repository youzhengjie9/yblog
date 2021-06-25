package com.boot.pojo;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "标签实体类",description = "封装标签名称和使用的数量")
public class tag {

    private int id;
    private String tagName;
    private int tagCount;

    public tag() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getTagCount() {
        return tagCount;
    }

    public void setTagCount(int tagCount) {
        this.tagCount = tagCount;
    }

    @Override
    public String toString() {
        return "tag{" +
                "id=" + id +
                ", tagName='" + tagName + '\'' +
                ", tagCount=" + tagCount +
                '}';
    }
}
