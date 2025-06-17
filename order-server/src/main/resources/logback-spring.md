
创建 logback-spring.xml 后需要添加 

```xml

        <!--            用于集成elk-->
        <dependency>
            <groupId>net.logstash.logback</groupId>
            <artifactId>logstash-logback-encoder</artifactId>
<!--            <version>7.3</version>-->
        </dependency>

```

