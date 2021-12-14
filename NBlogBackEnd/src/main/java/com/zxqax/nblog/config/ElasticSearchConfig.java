package com.zxqax.nblog.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    private final String ES_HOST = "nblog.org.cn";
    private final Integer ES_PORT = 9200;
    private final String ES_SCHEME = "http";


    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost(ES_HOST,ES_PORT,ES_SCHEME)
        ));

        return client;
    }
}
