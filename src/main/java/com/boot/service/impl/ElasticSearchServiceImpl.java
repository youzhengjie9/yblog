package com.boot.service.impl;

import com.boot.pojo.Article;
import com.boot.service.ElasticSearchService;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    //定义查询字段常量
    private final String SEARCH_1 = "content";
    private final String SEARCH_2 = "title";

    @Override
    public SearchHit[] searchArticleGetHits(String text) throws IOException {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("blog_article");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchSourceBuilder query = searchSourceBuilder.query(QueryBuilders.multiMatchQuery(text, SEARCH_1, SEARCH_2));

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        highlightBuilder.field("content");
        highlightBuilder.field("title");
        searchSourceBuilder.highlighter(highlightBuilder);


        searchRequest.source(searchSourceBuilder);


        //        searchSourceBuilder.fetchSource()

        //使用多字段查找
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
//

        return hits;
    }

    @Override
    public List<Article> getArticleListByHits(SearchHit[] hits) {
        List<Article> articles = new CopyOnWriteArrayList<>(); //线程安全的集合

        for (SearchHit hit : hits) {
            Map<String, Object> map = hit.getSourceAsMap();
            Integer id = (Integer) map.get("id");
            String title = (java.lang.String) map.get("title");
            String content = (java.lang.String) map.get("content");
            Long created = (Long) map.get("created");
//            Long modified = (Long) map.get("modified");
            String categories = (java.lang.String) map.get("categories");
            String tags = (java.lang.String) map.get("tags");
            Boolean allowComment = (Boolean) map.get("allowComment");
            String thumbnail = (java.lang.String) map.get("thumbnail");
            Article article = new Article();
            article.setId(id);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlight_title = highlightFields.get("title");
            if (highlight_title == null || highlight_title.equals("")) {
                article.setTitle(title);
            } else {
                String s = Arrays.toString(highlight_title.getFragments());
                String high_title = s.substring(1, s.length() - 1);
                article.setTitle(high_title);
            }


            article.setContent(content);

            article.setCreated(new Date(created));
            article.setCategories(categories);
            article.setTags(tags);
            article.setAllowComment(allowComment);
            article.setThumbnail(thumbnail);
            articles.add(article);
        }

        return articles;
    }
}
