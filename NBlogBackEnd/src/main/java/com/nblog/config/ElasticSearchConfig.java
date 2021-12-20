package com.nblog.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liulin
 * elasticsearch 相关配置
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.elasticsearch")
public class ElasticSearchConfig {

    private String host;
    private Integer port;
    private String scheme;

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost(host,port,scheme)
        ));
    }
}
