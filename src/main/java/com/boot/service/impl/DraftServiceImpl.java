package com.boot.service.impl;

import com.boot.dao.DraftMapper;
import com.boot.pojo.Article;
import com.boot.pojo.Draft;
import com.boot.pojo.Statistic;
import com.boot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import com.boot.service.categoryService;

@Service
public class DraftServiceImpl implements DraftService {

    @Autowired
    private DraftMapper draftMapper;

    @Autowired
    private categoryService categoryService;

    @Autowired
    private articleService articleService;

    @Autowired
    private statisticService statisticService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private tagService tagService;

    @Override
    public List<Draft> selectAllDraft(String username) {
        return draftMapper.selectAllDraft(username);
    }

    @Override
    public int selectDraftCount(String username) {
        return draftMapper.selectDraftCount(username);
    }

    @Override
    public Draft selectDraftByID(int id) {
        return draftMapper.selectDraftByID(id);
    }

    @Override
    public void deleteDraftByID(int id) {
        draftMapper.deleteDraftByID(id);
    }

    @Override
    public void publishDraft(Article article,int draftid) {

        //发布操作代码
        try {
            article.setCategories("默认分类");
            categoryService.updateCategoryCount(article.getCategories());
            article.setAllowComment(true);
            java.util.Date date1 = new java.util.Date();
            Date date = new Date(date1.getTime());
            article.setCreated(date);
            articleService.addArticle(article); //开启了 keyProperty="id" useGeneratedKeys="true"，自动生成的主键id会保存在article.getId()里。
            statisticService.addStatistic(new Statistic(article.getId(), 0, 0));

            //发布完就删除草稿
            draftMapper.deleteDraftByID(draftid);

            String tags = article.getTags();
            String[] post_split = tags.split(",");
            //如果没有这个某个标签，我们就把这个标签添加进去
            for (String s : post_split) {
                //通过redis判断有没有这个标签
                Integer o = (Integer) redisTemplate.opsForValue().get("tag_" + s);
                if (o == null) {
                    //如果缓存中没有这个标签就添加
                    tagService.insertTag(s);
                    //添加完数据库之后，我们还要把数据添加到redis缓存中
                    redisTemplate.opsForValue().set("tag_" + s, 1);
                } else {
                    //如果缓存中有这个标签就+1
                    tagService.changeTagByTagNameIncr(s);
                    o++;
                    redisTemplate.opsForValue().set("tag_" + s, o);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    @Override
    public void modifyDraft(Draft draft) {
        draftMapper.modifyDraft(draft);
    }

    @Override
    public void addDraft(Draft draft) {
        draftMapper.addDraft(draft);
    }

    @Override
    public List<Draft> selectDraftByTitle(String title) {
        return draftMapper.selectDraftByTitle(title);
    }


}
