package com.boot.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {


    @Bean
    public RestHighLevelClient restHighLevelClient() {

        HttpHost httpHost = new HttpHost("127.0.0.1", 9200, "http");
        RestClientBuilder restClientBuilder = RestClient.builder(httpHost);
        return new RestHighLevelClient(restClientBuilder);
    }


}
