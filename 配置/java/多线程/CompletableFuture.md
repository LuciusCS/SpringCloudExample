

## CompletableFuture


| 场景                   | 建议                           |
| -------------------- | ---------------------------- |
| 计算密集型任务              | 使用小线程池 + `CompletableFuture` |
| IO 密集型任务（DB、HTTP 调用） | 使用更大的线程池                     |
| 高并发 Web 场景           | ❌ 禁止使用默认 commonPool          |
| 有依赖关系的任务             | 用 `thenCompose()` 串行         |
| 无依赖的任务               | 用 `allOf()` 并行聚合             |



CompletableFuture<BigDecimal> demandFuture = CompletableFuture.supplyAsync(() -> {

});

默认使用 ForkJoinPool.commonPool()