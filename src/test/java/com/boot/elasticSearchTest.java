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
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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

    /**
     * [statistic, created, allowComment, categories, id, title, content, tags]
     * [statistic, created, modified, allowComment, categories, id, title, content, tags]
     * [statistic, created, allowComment, categories, id, title, content, tags]
     * [statistic, created, allowComment, categories, id, title, content, tags]
     * [statistic, created, allowComment, categories, id, title, content, tags]
     * @throws IOException
     */


    @Test
    void test() throws IOException {
        List<Article> articles=new CopyOnWriteArrayList<>(); //线程安全的集合
        SearchHit[] javas = elasticSearchService.searchArticleGetHits("java");
//        for (SearchHit java : javas) {
//            Map<String, Object> map = java.getSourceAsMap();
//            Integer id = (Integer) map.get("id");
//            String title = (java.lang.String) map.get("title");
//            String content = (java.lang.String) map.get("content");
//            Long created = (Long) map.get("created");
//            String categories = (java.lang.String) map.get("categories");
//            String tags = (java.lang.String) map.get("tags");
//            Boolean allowComment = (Boolean) map.get("allowComment");
//            String thumbnail = (java.lang.String) map.get("thumbnail");
//            Article article=new Article();
//            article.setId(id);
//            article.setTitle(title);
//            article.setContent(content);
//            article.setCreated(new Date(created));
//            article.setCategories(categories);
//            article.setTags(tags);
//            article.setAllowComment(allowComment);
//            article.setThumbnail(thumbnail);
//            articles.add(article);
//
//        }


        for (SearchHit java : javas) {
            Map<String, HighlightField> highlightFields = java.getHighlightFields();
            HighlightField title = highlightFields.get("title");

            String s = Arrays.toString(title.getFragments());
            String substring = s.substring(1, s.length()-1);
            System.out.println(substring);
        }


    }














}
