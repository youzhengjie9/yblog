package com.boot.service.impl;

import com.boot.dao.LikeMapper;
import com.boot.pojo.Like;
import com.boot.service.ArticleService;
import com.boot.service.LikeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 游政杰
 */
@Service
@Transactional
public class LikeServiceImpl implements LikeService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private LikeMapper likeMapper;

    private static final Object lock=new Object(); //悲观锁
    /**
     * 点赞文章
     * @param like
     */
    @Override
    public boolean likeArticle(Like like) {

        String flag = likeMapper.selectLikeExsit(like);
        if(!StringUtils.isEmpty(flag)){
            System.out.println("点赞失败");
            return false;
        }
        else {
            synchronized (lock){

                try {
                    int article_id = like.getArticle_id();
                    articleService.likeCountAddOne(article_id); //文章总点赞数+1
                    likeMapper.addLike(like);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }

        }

    }

    @Override
    public String selectLikeExsit(String username,int articleid) {
        Like like = new Like();
        like.setUsername(username);
        like.setArticle_id(articleid);
        return likeMapper.selectLikeExsit(like);
    }
}
