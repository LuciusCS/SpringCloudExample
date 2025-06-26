

## 启动容器

在elk目录下执行

```
docker compose up -d
```

## 对于某些系统，只能将日志输出至log文件

可以使用Filebeat 采集日志文件，并将其发送至logstash,

Filebeat 也能采集Nginx的运行日志


## 可以设置模块信息(索引)，发送给ElasticSeach



## Filebeat 也有内置模版建立索引



ELK常见架构

在实际使用ELK有几种常见的架构方式：

Elasticsearch + Logstash + Kibana：这种架构，通过Logstash收集日志，Elasticsearch分析日志，然后在Kibana中展示数据。这种架构虽然是官网介绍里的方式，但是在生产中却很少使用。
Elasticsearch + Logstash + filebeat + Kibana：与上一种架构相比，增加了一个filebeat模块。filebeat是一个轻量的日志收集代理，用来部署在客户端，优势是消耗非常少的资源(较logstash)就能够收集到日志。
            所以在生产中，往往会采取这种架构方式，但是这种架构有一个缺点，当logstash出现故障， 会造成日志的丢失。
Elasticsearch + Logstash + filebeat + redis + Kibana：此种架构是上面架构的完善版，通过增加中间件，来避免数据的丢失。当Logstash出现故障，日志还是存在中间件中，当Logstash再次启动，则会读取中间件中积压的日志。



## ELK 进阶参考

https://www.cnblogs.com/abiu/p/16050505.html