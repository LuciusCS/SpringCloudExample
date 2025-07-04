

```
# elasticsearch 使用 spring-boot-starter-data-elasticsearch 和使用  elasticsearch-java 配置不同
elasticsearch:
uris: http://192.168.22.180:9200  # HTTP 地址（可多个） Elasticsearch 8.x 必须使用 uris 方式
#      cluster-name: elasticsearch      适用于 Elasticsearch ≤ 7.x 版本
#      cluster-nodes: 192.168.22.180:9200

```



# Spring Boot Actuator 访问地址

```
http://127.0.0.1:8005/actuator

```