package com.boot;

import com.boot.pojo.Article;
import com.boot.pojo.archive;
import com.boot.pojo.tag;
import com.boot.service.archiveService;
import com.boot.service.articleService;
import com.boot.service.categoryService;
import com.boot.service.tagService;
import com.boot.utils.SpringSecurityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.swing.*;
import java.util.*;

@SpringBootTest
public class test {


    @Autowired
    private articleService articleService;

    @Autowired
    private RedisTemplate redisTemplate;

//    @Test
//    public void test(){
//
//        Random random = new Random();
//        for (int i = 0; i < 10; i++) {
//            int x = random.nextInt(5);
//            System.out.println(x);
//        }
//
//    }

    //删除缓存数据，初始化
    @Test
    public void deleteRedis(){
        Set keys = redisTemplate.keys("*");
        for (Object key : keys) {
            redisTemplate.delete(key);
        }
    }



    /**
     *导入tag数据
     */
    private Map<String,Integer> map=new LinkedHashMap<>();

    @Autowired
    private tagService tagService;

    @Test
    public void test1(){
        List<Article> articles = articleService.selectTagsByArticle();
        for (Article article : articles) {
            String tags = article.getTags();
            String[] split = tags.split(",");
            for (String s : split) {

                if (map.containsKey(s)) {
                    int i = map.get(s);
                    i++;
                    map.put(s,i);

                }else {
                    map.put(s,1);
                }
            }

        }
        Set<String> strings = map.keySet();
        for (String string : strings) {
            tag tag = new tag();
            tag.setTagName(string);
            tag.setTagCount(map.get(string));
            tagService.addTag(tag);

        }

    }

    @Autowired
    private categoryService categoryService;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private archiveService archiveService;
    @Test
    public void test2(){

//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        String encode = bCryptPasswordEncoder.encode("123456");
//
//        boolean matches = bCryptPasswordEncoder.matches("123456", "$2a$10$5ooQI8dir8jv0/gCa1Six.GpzAdIPf6pMqdminZ/3ijYzivCyPlfK");
//        System.out.println(matches);
//

        List<archive> archives = archiveService.selectAllArchiveGroup();
        System.out.println(archives);

    }



}
