package com.boot.service.impl;

import com.boot.pojo.Article;
import com.boot.service.elasticSearchService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class elasticSearchServiceImpl implements elasticSearchService {

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    //定义查询字段常量
    private final String SEARCH_1="content";
    private final String SEARCH_2="title";

    @Override
    public SearchHit[] searchArticle(String text) throws IOException {

        SearchRequest searchRequest = new SearchRequest();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchSourceBuilder query = searchSourceBuilder.query(QueryBuilders.multiMatchQuery(text, SEARCH_1, SEARCH_2));

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:'red''>");
        highlightBuilder.postTags("</span>");
        highlightBuilder.field("content");
        highlightBuilder.field("title");
        searchSourceBuilder.highlighter(highlightBuilder);


        searchRequest.source(searchSourceBuilder);

        //使用多字段查找
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
//        for (SearchHit hit : hits) {
//            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
//            System.out.println(highlightFields);
//
//
//        }

        return hits;
    }
}
