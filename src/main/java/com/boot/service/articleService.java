package com.boot.service;

import com.boot.pojo.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface articleService {

    public List<Article> selectAllArticle();

    public Article selectArticleByArticleIdNoComment(Integer id);

    //排行榜
    public List<Article> selectAllArticleOrderByDesc();

    public List<Article> selectArticleAndComment();

    public List<Article> selectArticleOrderCreateDate();

    public int selectArticleCount();

    public List<Article> selectAllArticleByCreated();

    public int changeArticle(Article article);

    public int addArticle(Article article);

    public int deleteArticleByArticleId(Integer id);

    //当用户点击文章时，文章点击数加1
    public void updateHits(Integer article_id);

    public List<Article> selectCategoriesAndTags();

    public List<Article> selectTagsByArticle();

    public String selectTagsByArticleId(int id);

    public void updateTagsByArticleId(String tags,int id);

    public void updateCategory(String oldName,String newName);

}
