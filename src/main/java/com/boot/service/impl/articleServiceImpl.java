package com.boot.service.impl;

import com.boot.dao.articleMapper;
import com.boot.pojo.Article;
import com.boot.service.articleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class articleServiceImpl implements articleService {

    @Autowired
    private articleMapper articleMapper;

    @Override
    public List<Article> selectAllArticle() {
        return articleMapper.selectAllArticle();
    }


    @Override
    public Article selectArticleByArticleIdNoComment(Integer id) {
        return articleMapper.selectArticleByArticleIdNoComment(id);
    }

    @Override
    public List<Article> selectAllArticleOrderByDesc() {
        return articleMapper.selectAllArticleOrderByDesc();
    }

    @Override
    public List<Article> selectArticleAndComment() {
        return articleMapper.selectArticleAndComment();
    }

    @Override
    public List<Article> selectArticleOrderCreateDate() {
        return articleMapper.selectArticleOrderCreateDate();
    }

    @Override
    public int selectArticleCount() {
        return articleMapper.selectArticleCount();
    }

    @Override
    public List<Article> selectAllArticleByCreated() {
        return articleMapper.selectAllArticleByCreated();
    }

    @Override
    public int changeArticle(Article article) {
        return articleMapper.changeArticle(article);
    }

    @Override
    public int addArticle(Article article) {
        return articleMapper.addArticle(article);
    }

    @Override
    public int deleteArticleByArticleId(Integer id) {
        return articleMapper.deleteArticleByArticleId(id);
    }

    @Override
    public void updateHits(Integer article_id) {
        articleMapper.updateHits(article_id);
    }

    @Override
    public List<Article> selectCategoriesAndTags() {
        return articleMapper.selectCategoriesAndTags();
    }

    @Override
    public List<Article> selectTagsByArticle() {
        return articleMapper.selectTagsByArticle();
    }

    @Override
    public String selectTagsByArticleId(int id) {
        return articleMapper.selectTagsByArticleId(id);
    }

    @Override
    public void updateTagsByArticleId(String tags, int id) {
        articleMapper.updateTagsByArticleId(tags, id);
    }

    @Override
    public void updateCategory(String oldName, String newName) {
        articleMapper.updateCategory(oldName, newName);
    }

}
