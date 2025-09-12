## 项目配置

### 配置 Tomcat 线程池和连接数：

```yaml
server:
  port: 8080
  tomcat:
    max-connections: 10000   # 最大 TCP 连接数
    max-threads: 500         # 最大工作线程数
    accept-count: 1000       # 等待队列
```

### 压测接口
压测接口目前写在 order-server微服务中

```java
@RestController
public class TestController {

    // 快速接口，用于测试最大吞吐量
    @GetMapping("/fast")
    public String fast() {
        return "ok";
    }

    // 慢接口，用于测试并发上限
    @GetMapping("/slow")
    public String slow() throws InterruptedException {
        Thread.sleep(1000); // 模拟业务耗时 1 秒
        return "ok";
    }
}
```

## 使用 wrk 进行压测

### 安装 wrk
```
# macOS
brew install wrk
brew install watch
# Ubuntu
sudo apt-get install wrk
```

### 压测快速接口（吞吐量）
```
wrk -t8 -c1000 -d30s http://localhost:8080/fast
```
-t8 → 8 个 wrk 线程
-c1000 → 1000 个并发连接
-d30s → 持续 30 秒

### 观察结果：
QPS：输出 Requests/sec
延迟：输出 Latency

### 压测慢接口（并发上限）
```
wrk -t8 -c600 -d30s http://localhost:8080/slow  
```

当并发数超过 max-threads（500），会发现请求被阻塞在等待队列，延迟显著增加。

## 测试最大连接数
增加 wrk 并发连接数，直到出现 connection refused 或超时。

```
wrk -t8 -c12000 -d30s http://localhost:8080/fast
```

同时用系统命令监控连接数

```
netstat -an | grep 8080 | wc -l
 # 定时输出
 watch -n 5 'netstat -an | grep 8080 | wc -l'

```
对比 max-connections 参数，确认 Tomcat 最大可保持连接数。


## 监控方法
### JVM 指标
线程数：

```
jcmd <pid> Thread.print | wc -l

# 定时输出

 watch -n 5 'jcmd <pid> Thread.print | wc -l'

```
pid 查找方式

```
jps -l
```


堆内存、GC：使用 VisualVM 或 Prometheus + Micrometer

系统指标
CPU、内存：
```
top
htop
```

TCP 连接数：

```
netstat -an | grep 8080 | wc -l
```

队列长度（等待队列）：
```
ss -s
```
##  实验分析
慢接口 + 高并发 → 观察线程池瓶颈，并发数 ≈ max-threads
快速接口 + 高并发 → 观察吞吐量 QPS 与平均延迟
持续增加连接数 → 找到最大连接数上限，受 max-connections 和系统 FD 限制


##  总结
最大连接数 = max-connections + 系统 FD 上限
并发数 ≈ max-threads（阻塞模式）
QPS ≈ 并发数 ÷ 平均响应时间
提升策略：
非阻塞 WebFlux/Netty → 高并发连接，少线程
线程池调大 + 系统 FD 调大 → 支撑更多并发
多机部署 + 负载均衡 → 进一步扩展 QPS




