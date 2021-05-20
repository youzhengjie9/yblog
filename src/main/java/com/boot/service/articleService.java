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

}
