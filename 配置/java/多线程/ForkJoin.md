


## 以下情况默认使用公共 ForkJoinPool：
| 场景                                                             | 示例                                 | 是否使用 commonPool |
| -------------------------------------------------------------- | ---------------------------------- | --------------- |
| `CompletableFuture.supplyAsync()` / `runAsync()`（未指定 Executor） | `CompletableFuture.runAsync(task)` | ✅ 是             |
| `parallelStream()`                                             | `list.parallelStream().map(...)`   | ✅ 是             |
| `Stream.parallel()`                                            | `Stream.of(...).parallel()`        | ✅ 是             |
| `Arrays.parallelSort()`                                        | `Arrays.parallelSort(arr)`         | ✅ 是             |
| `CompletableFuture.thenApplyAsync()`（未指定 Executor）             | `future.thenApplyAsync(fn)`        | ✅ 是             |
| `CompletableFuture.allOf()` / `anyOf()`（当组合多个异步任务时）            | `CompletableFuture.allOf(...)`     | ✅ 是（取决于前面任务）    |
| `ForkJoinTask.invokeAll()` / `ForkJoinTask.fork()`             | 手动使用 ForkJoin 任务                   | ✅ 是（同一池）        |


```
Map<String, ?> funcResultMap = funcMap.entrySet()
    .parallelStream()
    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));

```

每个 parallelStream() 默认使用的是：

```
   ForkJoinPool.commonPool();
/// 即 Java 全局共享的 公共 ForkJoinPool。
```

这个池子默认的并行度（线程数）是：
```
Runtime.getRuntime().availableProcessors() - 1
///也就是 CPU 核数 - 1（通常留一个给主线程）。
```

| 项目   | `parallelStream()`             | `ForkJoinPool`              |
| ---- | ------------------------------ | --------------------------- |
| 调用方式 | 简单高层抽象（Stream API）             | 直接操作底层线程池                   |
| 执行模型 | 内部使用 ForkJoinPool.commonPool() | 你可以手动创建自己的 ForkJoinPool     |
| 控制粒度 | 自动拆分流中的任务（按元素划分）               | 你自己定义任务的拆分逻辑（RecursiveTask） |
| 适合场景 | 数据流处理、集合计算                     | 更复杂的分治任务（如递归算法、矩阵运算）        |



注意事项
不要在高并发 Web 服务中滥用 parallelStream()
因为所有并行流默认共享一个全局 ForkJoinPool.commonPool()。
一旦有大量请求并发使用 parallelStream()，线程池被占满，会阻塞其他任务。
想控制并发度时建议使用自定义 ForkJoinPool

例如：

```
ForkJoinPool customPool = new ForkJoinPool(8);
customPool.submit(() -> myList.parallelStream().forEach(...)).join();
```
parallelStream() 更适合计算密集型任务
例如：数值计算、大量数据转换。
不适合 IO 密集任务（如网络请求、文件读写），否则线程会空等。


### ForkJoinPool.commonPool() 是一个JVM 级别的全局公共线程池，
ForkJoinPool.commonPool() 是所有默认并行任务（Stream、CompletableFuture、parallelSort 等）的公共线程池。
在高并发 Web 环境中，这会导致任务相互竞争 CPU 线程，

| 使用场景                                                                    | 说明                |
| ----------------------------------------------------------------------- | ----------------- |
| `Stream.parallel()` / `parallelStream()`                                | 默认并行流使用的线程池       |
| `CompletableFuture.supplyAsync(...)` / `runAsync(...)`（无自定义 Executor 时） | 默认异步任务线程池         |
| `Arrays.parallelSort()`                                                 | 并行排序用的线程池         |
| 部分第三方库（例如 Jackson 的 `parallel processing`、某些 Reactor 操作符）               | 可能默认使用 commonPool |
| JDK 内部一些异步计算（如 ClassLoader、部分内部工具类）                                     | 极少数情况会使用          |

#### 最佳实践

| 场景          | 建议                                               |
| ----------- | ------------------------------------------------ |
| 后台批处理任务、脚本  | 可直接用 `parallelStream()`                          |
| Web 服务端或微服务 | 🚫 禁止使用默认 `parallelStream()` 或无参 `supplyAsync()` |
| 异步任务        | ✅ 明确传入自定义线程池                                     |
| CPU 密集型任务   | ✅ 限制线程数，不超过 CPU 核心数                              |
| IO 密集型任务    | ✅ 使用异步/反应式模型，或更大线程池                              |




### 注意

你项目中所有使用 CompletableFuture、parallelStream()、parallelSort() 等操作的地方都会争夺同一个线程池；
如果其中一个任务阻塞（比如 IO、网络请求），就可能导致别的异步任务被“卡住”；
监控中表现为：CPU 核心数用不满，但异步任务明显延迟严重。


| 操作类型                                     | 默认线程池                   | 建议             |
| ---------------------------------------- | ----------------------- | -------------- |
| `CompletableFuture.*Async()`（无 executor） | ForkJoinPool.commonPool | 显式传入 Executor  |
| `parallelStream()`                       | ForkJoinPool.commonPool | 慎用，尤其在 Web 应用中 |
| `parallelSort()`                         | ForkJoinPool.commonPool | OK，但注意大数组      |
| 自定义线程池                                   | 不占用 commonPool          | 推荐方式 ✅         |
