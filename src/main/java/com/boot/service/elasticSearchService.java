package com.boot.service;


import org.elasticsearch.search.SearchHit;

import java.io.IOException;

public interface elasticSearchService {

    /**
     * 返回命中的结果
     * @param text
     * @return
     * @throws IOException
     */
    public SearchHit[] searchArticle(String text) throws IOException;



}
