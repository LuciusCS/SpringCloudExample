

## Feign 是同步阻塞式客户端

| 模式                                      | 描述                      | 线程占用    | 推荐场景         |
| --------------------------------------- | ----------------------- | ------- | ------------ |
| `Feign（同步）`                             | 调用远程服务时等待返回             | ✅ 占用线程  | 少量并发、简单业务    |
| `WebClient（异步）`                         | 调用后立即返回 `Mono`，I/O 异步通知 | ❌ 不占用线程 | 高并发、网关、聚合查询等 |
| `CompletableFuture.supplyAsync` + Feign | 只是把阻塞封装到后台线程            | ✅ 占用线程  | 适中并发、并行聚合场景  |


```java
CompletableFuture<List<BatteryDTO>> batteryFuture = CompletableFuture.supplyAsync(() ->
    new ArrayList<>(batteryService.getList(new BatteryCondition().setParkId(parkDTO.getId())))
);
```

在 supplyAsync 内部，实际是：
* 提交一个任务到 线程池（默认 ForkJoinPool.commonPool）；
* 线程池中取一个线程执行这段代码；
* 执行过程中调用 batteryService.getList(...)；
* Feign 发起 HTTP 请求；
* 在收到响应前，这个线程会同步阻塞等待网络结果；
* 直到请求返回，线程才继续执行接下来的逻辑。

如果同时有多个Feign请求，会导致ForkJoinPool.commonPool 中的的线程被耗尽，影响性能，因为 Feign接口请求，即使在等待过程中，也在占用线程


## 在等待远程响应时线程被释放，可以提高性能

### 方式 1：使用 异步 Feign（推荐）  重要
Feign 官方支持异步客户端，底层可用：
* Apache HttpAsyncClient
* 或 OkHttp（异步模式）
* 或 WebClient（Spring 5 reactive）




调用方使用异步的形式，被调用方使用正常的Spring MVC形式

下面的这种方式

```java

@FeignClient(name = "product-service")
public interface ProductServiceClient {

    
    /// Spring 的 @Async 会使用配置的线程池（默认是 SimpleAsyncTaskExecutor）
    ///ForkJoinPool 不会被使用
    ///执行 Feign 调用的线程会阻塞等待 HTTP 响应
    @Async // 声明该方法为异步执行
    @GetMapping("/products/{id}")
    CompletableFuture<Product> getProductAsync(@PathVariable("id") Long id);

    @GetMapping("/products/detail/{id}")
    ProductDetail  getProductDetailSync(@PathVariable("id") Long id);
}


/// 客户端异步调用
@Service
public class OrderService {

    @Autowired
    private ProductServiceClient productServiceClient;

    public void processOrder(Long productId) {
        
        /// 下面这种调用不会持有ForkJoin的线程，不对ForkJoin的线程进行阻塞
        /// 调用后立即返回，主线程不被阻塞
        CompletableFuture<Product> future = productServiceClient.getProductAsync(productId);
        
        /// 下面这种调用会持有FrokJoin的线程，会对ForkJoin的线程阻塞，不推荐，下面这种
        /// ForkJoinPool 线程会阻塞等待 HTTP 响应这实际上浪费了 ForkJoinPool 的线程资源
//        CompletableFuture<Product> future = CompletableFuture.supplyAsync(() -> {
//            productServiceClient.getProductSync(productId);
//        });


        CompletableFuture<ProductDetail> detailFuture = CompletableFuture.supplyAsync(() ->
                productServiceClient.getProductDetailSync(productId)
        );

        /// 同步线程阻塞
        ProductDetail productDetail = productServiceClient.getProductDetailSync(productId);

        // 使用 allOf 等待所有任务完成
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futureProduct, futureDetail
        );
        // 异步处理响应
//        future.whenComplete((product, throwable) -> {
//            if (throwable != null) {
//                // 处理异常
//                System.err.println("Error fetching product: " + throwable.getMessage());
//            } else {
//                // 处理成功响应
//                System.out.println("Received product: " + product.getName());
//            }
//        });

        // 主线程可以继续执行其他任务...
    }
}

/// 被调用方完全不需要知道调用方使用的是异步Feign，按照标准的Spring MVC Controller编写即可。异步调用是在Feign客户端这一侧实现的封装，对服务端透明。
@RestController
public class ProductController {

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable Long id) {
        // 模拟从数据库或其他服务获取产品信息
        return productService.findProductById(id);
    }
}

```

| 对比点                      | 第一种：`getProductAsync()`                            | 第二种：`supplyAsync(() -> getProductDetailSync())` |
| ------------------------ | -------------------------------------------------- | ----------------------------------------------- |
| **Feign 实现方式**           | `@Async` 异步方法（基于 Spring 线程池）或 `AsyncFeign`         | 普通同步 Feign 调用                                   |
| **CompletableFuture 来源** | 由 Spring 的 `@Async` 返回（或 Feign 内部封装）               | 由 `CompletableFuture` 创建                        |
| **调用线程（主线程）**            | ✅ 立即返回，不阻塞                                         | ✅ 立即返回，不阻塞                                      |
| **执行线程（谁去发请求）**          | Spring `@Async` 的线程池（默认 `SimpleAsyncTaskExecutor`） | 默认 `ForkJoinPool.commonPool` 线程                 |
| **Feign 内部调用是异步还是同步**    | 如果 Feign 使用异步 HTTP 客户端（如 AsyncFeign），则是真异步         | 一定是同步（阻塞）                                       |
| **ForkJoinPool 线程是否被占用** | ❌ 不占用                                              | ✅ 会被阻塞直到响应返回                                    |
| **能否释放线程等待网络 I/O**       | ✅ 能释放（取决于是否异步 Feign）                               | ❌ 不能释放（阻塞调用）                                    |
| **可伸缩性（高并发表现）**          | ⭐ 极好（非阻塞）                                          | ⚠️ 一般（阻塞，线程数受限）                                 |


| 写法                                                                                       | 特征             | 是否真正异步            | 线程使用               | 实际开发常见度 |
| ---------------------------------------------------------------------------------------- | -------------- | ----------------- | ------------------ | ------- |
| ✅ `CompletableFuture<Product> future = productServiceClient.getProductAsync(...)`        | 异步Feign + 异步返回 | 真异步（取决于底层Feign实现） | 线程立即释放             | ❌ 较少    |
| ⚙️ `CompletableFuture.supplyAsync(() -> productServiceClient.getProductDetailSync(...))` | 用线程池包同步调用      | 伪异步（线程仍阻塞）        | ForkJoin或自定义线程池被占用 | ⚠️ 偶尔使用 |
| 💡 `ProductDetail productDetail = productServiceClient.getProductDetailSync(...)`        | 直接同步调用         | 同步                | 当前线程阻塞             | ✅ 最常见   |



线程池配置：建议为@Async配置自定义线程池

```java

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsyncFeign-");
        executor.initialize();
        return executor;
    }
}
```




### 方式 2：改用 WebClient 或 Reactor（非阻塞 IO）
如果你不方便改 Feign，可以引入 WebClient 直接异步调用目标服务：


```java

WebClient webClient = WebClient.create("http://battery-service");

Mono<List<BatteryDTO>> batteryMono = webClient.get()
    .uri("/battery/list?parkId=" + parkDTO.getId())
    .retrieve()
    .bodyToMono(new ParameterizedTypeReference<List<BatteryDTO>>() {});

Mono<StrategyDTO> strategyMono = WebClient.create("http://strategy-service")
    .get()
    .uri("/strategy/get?parkId=" + parkDTO.getId() + "&date=" + date)
    .retrieve()
    .bodyToMono(StrategyDTO.class);

Mono.zip(batteryMono, strategyMono)
    .map(tuple -> {
        List<BatteryDTO> batteries = tuple.getT1();
        StrategyDTO strategy = tuple.getT2();
        // TODO: 业务逻辑
        return new ReportDTO(batteries, strategy);
    })
    .block(); // 或异步继续处理
```
