package com.boot.pojo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

/**
 * 博客评论实体类
 * 1、使用定制的RedisConfig配置类，实现缓存时的JSON序列化机制
 */
@ApiModel(value = "评论实体类", description = "封装评论信息")
public class Comment implements Serializable {
    private Integer id;         // 评论id
    private Integer articleId; // 评论的文章id
    private String c_content;    // 评论内容
    private Date created;      // 评论日期
    private String author;     // 评论作者名
    private String ip;          // 评论用户登录ip
    private String status;     // 评论状态，默认审核通过approved

    public Comment() {
    }

    public Comment(Integer id, Integer articleId,
                   String c_content, Date created, String author, String ip, String status) {
        this.id = id;
        this.articleId = articleId;
        this.c_content = c_content;
        this.created = created;
        this.author = author;
        this.ip = ip;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public void setC_content(String c_content) {
        this.c_content = c_content;
    }

    public String getC_content() {
        return c_content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", c_content='" + c_content + '\'' +
                ", created=" + created +
                ", author='" + author + '\'' +
                ", ip='" + ip + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
