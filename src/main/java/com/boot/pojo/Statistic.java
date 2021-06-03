package com.boot.pojo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * 文章相关动态数据统计实体类
 * */
@ApiModel(value = "文章数据统计实体类",description = "封装每个文章的点击量和评论量")
public class Statistic implements Serializable {
    private Integer id;
    private Integer articleId;   // 评论的文章id
    private Integer hits;        // 点击量
    private Integer commentsNum;// 评论总量

    public Statistic() {
    }

    public Statistic(Integer articleId, Integer hits, Integer commentsNum) {
        this.articleId = articleId;
        this.hits = hits;
        this.commentsNum = commentsNum;
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

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Integer getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(Integer commentsNum) {
        this.commentsNum = commentsNum;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", hits=" + hits +
                ", commentsNum=" + commentsNum +
                '}';
    }
}
