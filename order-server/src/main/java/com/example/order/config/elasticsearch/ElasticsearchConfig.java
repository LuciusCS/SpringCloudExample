package com.example.order.config.elasticsearch;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;    // 旧版的 RestClient，但这里只是用来创建 Transport
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Autowired
    private RestClient restClient; // 注入Spring Boot配置好的RestClient

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        // 使用注入的RestClient创建Transport和Client
        ElasticsearchTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper() // 使用Jackson做JSON映射
        );
        return new ElasticsearchClient(transport);
    }
}