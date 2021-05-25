package com.boot;

import com.boot.pojo.Article;
import com.boot.service.articleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class test {


    @Autowired
    private articleService articleService;

    @Test
    public void test(){

        List<Article> articles = articleService.selectCategoriesAndTags();

        for (Article article : articles) {
            String categories = article.getCategories();
            String tags = article.getTags();



        }




    }







}
