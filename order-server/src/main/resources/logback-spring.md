
## 创建 logback-spring.xml 后需要添加 

```xml

        <!--            用于集成elk-->
        <dependency>
            <groupId>net.logstash.logback</groupId>
            <artifactId>logstash-logback-encoder</artifactId>
<!--            <version>7.3</version>-->
        </dependency>

```


用于日志输出添加clientIP 和 host


## 需要将日志文件输出到 ELK 中，配置的是 LogStash 的地址

```
      <appender name="LOGSTASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
            <destination>192.168.22.180:5044</destination>
            <encoder charset="UTF-8" class="net.logstash.logback.encoder.LogstashEncoder"/>
        </appender>
```


