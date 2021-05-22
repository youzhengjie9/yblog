package com.boot;

import com.alibaba.fastjson.JSON;
import com.boot.pojo.Article;
import com.boot.pojo.Statistic;
import com.boot.service.articleService;
import com.boot.service.elasticSearchService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class elasticSearchTest {


    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    private final String INDEX="blog_article"; //索引名字必须全部是小写

    @Autowired
    private articleService articleService;

    @Autowired
    private elasticSearchService elasticSearchService;

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
    @Test
    void putDocument() throws IOException {

        List<Article> articles = articleService.selectAllArticle();

        BulkRequest bulkRequest = new BulkRequest();

        for (int i = 0; i < articles.size(); i++) {
            IndexRequest indexRequest = new IndexRequest(); //文档
            indexRequest.index(INDEX);//指定索引
            indexRequest.id(i+""); //文档id
            indexRequest.source(JSON.toJSONString(articles.get(i)),XContentType.JSON);
            bulkRequest.add(indexRequest);
        }



        client.bulk(bulkRequest,RequestOptions.DEFAULT);

    }

















}
