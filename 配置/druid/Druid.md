

访问地址：

`127.0.0.1:8004/druid/login.html` 


### Druid 连接到集群，实现读写分离

MySQL Router → 已经实现了集群读写分离能力（一般是 3306 读写分离，或单独暴露 6446 写、6447 读端口）
Spring Boot + Druid → 应用侧连接池，需要通过 Router 来使用读写分离
Druid 不需要自己去做读写分离，只需要配置 MySQL Router 提供的读写端口。Router 会自动把写请求转发到 Primary，把读请求转发到 Secondary。