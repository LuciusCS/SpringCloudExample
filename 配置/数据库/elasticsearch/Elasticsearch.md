

```
# elasticsearch 使用 spring-boot-starter-data-elasticsearch 和使用  elasticsearch-java 配置不同
elasticsearch:
uris: http://192.168.22.180:9200  # HTTP 地址（可多个） Elasticsearch 8.x 必须使用 uris 方式
#      cluster-name: elasticsearch      适用于 Elasticsearch ≤ 7.x 版本
#      cluster-nodes: 192.168.22.180:9200

```


```
        <!--        在高版本 spring-boot-starter-data-elasticsearch 中其底层实际上依赖并封装了
               ‌elasticsearch-java（当时称为 Java API Client）‌ 或兼容的客户端 ，所以不需要下面的配置-->
        <!--        <dependency>-->
        <!--&lt;!&ndash;            早期版本使用org.elasticsearch.client ，后来改成co.elastic.clients &ndash;&gt;-->
        <!--&lt;!&ndash;            <groupId>org.elasticsearch.client</groupId>&ndash;&gt;-->
        <!--&lt;!&ndash;            elasticsearch-java 客户端是相对新的，官方尚未为其发布专门的 Spring Boot starter。&ndash;&gt;-->
        <!--            <groupId>co.elastic.clients</groupId>-->
        <!--            <artifactId>elasticsearch-java</artifactId>-->
        <!--            <version>8.17.6</version> &lt;!&ndash; 使用与 Elasticsearch 集群相匹配的版本 &ndash;&gt;-->
        <!--        </dependency>-->
```