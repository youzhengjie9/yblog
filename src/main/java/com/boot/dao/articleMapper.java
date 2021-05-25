package com.boot.dao;

import com.boot.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface articleMapper {

    public List<Article> selectAllArticle();

    //只查询article，不查询Comment
    public Article selectArticleByArticleIdNoComment(@Param("id") Integer id);

    //排行榜
    public List<Article> selectAllArticleOrderByDesc();

    public List<Article> selectArticleAndComment();

    public List<Article> selectArticleOrderCreateDate();

    public int selectArticleCount();

    public List<Article> selectAllArticleByCreated();

    public int changeArticle(Article article);

    public int addArticle(Article article);

    public int deleteArticleByArticleId(@Param("id") Integer id);

    //当用户点击文章时，文章点击数加1
    public void updateHits(@Param("article_id") Integer article_id);

    public List<Article> selectCategoriesAndTags();

    public List<Article> selectTagsByArticle();

}
