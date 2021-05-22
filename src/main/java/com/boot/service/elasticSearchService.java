package com.boot.service;


import com.boot.pojo.Article;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.List;

public interface elasticSearchService {

    /**
     * 返回命中的结果
     * @param text
     * @return
     * @throws IOException
     */
    public SearchHit[] searchArticleGetHits(String text) throws IOException;


    public List<Article> getArticleListByHits(SearchHit[] hits);

}
