package com.boot.pojo;

import java.io.Serializable;

public class userDetail implements Serializable {

    private int id; //id
    private String name; //用户名
    private String blogName;//博客名称
    private String job; //工作
    private String detail; //详情
    private String github; //GitHub地址
    private String weibo; //微博地址


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    @Override
    public String toString() {
        return "userDetail{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", blogName='" + blogName + '\'' +
                ", job='" + job + '\'' +
                ", detail='" + detail + '\'' +
                ", github='" + github + '\'' +
                ", weibo='" + weibo + '\'' +
                '}';
    }
}
