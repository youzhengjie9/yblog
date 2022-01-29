package com.boot;

import com.boot.service.ArticleService;
import com.boot.service.ElasticSearchService;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ElasticSearchTest {


    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    private final String INDEX="blog_article"; //索引名字必须全部是小写

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ElasticSearchService elasticSearchService;

    //创建索引
//    PUT blog_article
//    {
//        "mappings":{
//        "properties":{
//            "id":{
//                "type":"long"
//            },
//            "title":{
//                "type":"text",
//                        "analyzer":"standard"
//            },
//            "content":{
//                "type":"text",
//                        "analyzer":"standard"
//            },
//            "created":{
//                "type":"date"
//            },
//            "modified":{
//                "type":"date"
//            },
//            "categories":{
//                "type":"keyword"
//            },
//            "tags":{
//                "type":"keyword"
//            },
//            "allowComment":{
//                "type":"boolean"
//            },
//            "thumbnail":{
//                "type":"keyword"
//            }
//        }
//
//    }
//
//    }



//    @Test
//    void createIndex() throws IOException {
//
//        CreateIndexRequest createIndexRequest = new CreateIndexRequest(INDEX);
//        Article article = new Article();
//        Statistic statistic = new Statistic();
//        createIndexRequest.mapping(JSON.toJSONString(article), XContentType.JSON);
//        createIndexRequest.mapping(JSON.toJSONString(statistic),XContentType.JSON);
//        client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
//    }



    //将数据库的数据添加到es
//    @Test
//    void putDocument() throws IOException {
//
//        List<Article> articles = articleService.selectAllArticle();
//
//        BulkRequest bulkRequest = new BulkRequest();
//
//        for (int i = 0; i < articles.size(); i++) {
//            IndexRequest indexRequest = new IndexRequest(); //文档
//            indexRequest.index(INDEX);//指定索引
//            indexRequest.id(i+""); //文档id
//            indexRequest.source(JSON.toJSONString(articles.get(i)),XContentType.JSON);
//            bulkRequest.add(indexRequest);
//        }
//
//
//
//        client.bulk(bulkRequest,RequestOptions.DEFAULT);
//
//    }

    /**
     * [statistic, created, allowComment, categories, id, title, content, tags]
     * [statistic, created, modified, allowComment, categories, id, title, content, tags]
     * [statistic, created, allowComment, categories, id, title, content, tags]
     * [statistic, created, allowComment, categories, id, title, content, tags]
     * [statistic, created, allowComment, categories, id, title, content, tags]
     * @throws IOException
     */

    //BCryptPasswordEncoder随机盐值加密
//    $2a$10$ODZmd9S1sL9OcnUcufDpv.TwXbZyWr2V6EDOQ6vrz/ycD.mecwoMC
    //$2a$10$3KqGzxIzoCVzg2/YLlTUyOXO7z9.AKO3dx3rHfGWEQPScRA3Pbkl6
//    @Test
//    void test(){
//        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
//        String password="123456";
//        String encode = bCryptPasswordEncoder.encode(password);
//        System.out.println(encode);
//        System.out.println(bCryptPasswordEncoder.matches(password, encode));
//
//    }














}
