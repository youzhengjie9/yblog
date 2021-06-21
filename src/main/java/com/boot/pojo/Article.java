package com.boot.pojo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 博客文章实体类
 * 1、使用定制的RedisConfig配置类，实现缓存时的JSON序列化机制
 */
@ApiModel(value = "文章实体类", description = "封装文章内容")
public class Article implements Serializable {
    private Integer id;          // 文章id
    private String title;       // 文章标题
    private String content;     // 文章内容
    private Date created;       // 文章创建时间
    private Date modified;      // 文章创建时间
    private String categories; // 文章分类
    private String tags;        // 文章标签
    private Boolean allowComment; // 是否允许评论，默认为true
    private int recommend;        //是否为推荐文章
    private String thumbnail;     // 文章缩略图

    private int likes;            //文章点赞数


    private Statistic statistic;

    private List<Comment> comments;


    public Article() {
    }

    public Article(Integer id, String title,
                   String content, Date created,
                   Date modified, String categories,
                   String tags, Boolean allowComment,
                   String thumbnail, Statistic statistic,
                   int recommend,
                   List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.created = created;
        this.modified = modified;
        this.categories = categories;
        this.tags = tags;
        this.allowComment = allowComment;
        this.recommend=recommend;
        this.thumbnail = thumbnail;
        this.statistic = statistic;
        this.comments = comments;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Boolean getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Boolean allowComment) {
        this.allowComment = allowComment;
    }

    public Statistic getStatistic() {
        return statistic;
    }

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", categories='" + categories + '\'' +
                ", tags='" + tags + '\'' +
                ", allowComment=" + allowComment +
                ", recommend=" + recommend +
                ", thumbnail='" + thumbnail + '\'' +
                ", likes=" + likes +
                ", statistic=" + statistic +
                ", comments=" + comments +
                '}';
    }
}