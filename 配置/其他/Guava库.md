


## Guava库的使用
新项目：能用 JDK 原生功能就用 JDK；需要缓存用 Caffeine；其余按需引入小型库。
老项目：如果已经深度依赖 Guava，没必要迁移，Guava 依然安全可靠。
中间态：如果你只用到集合工具类和 Optional，完全可以替换掉，减少依赖。


Guava 没有被淘汰，但它的“工具箱”角色已经逐渐被 JDK + Caffeine + 其他专用库 取代。
现在引入 Guava 更多是为了它的少数特性（比如 Multimap、BloomFilter），而不是全量依赖。


## 替代Guava库的实践

| Guava 功能                                      | 推荐替代方案                                                                                 |
| --------------------------------------------- | -------------------------------------------------------------------------------------- |
| 集合工具（`Lists.newArrayList`, `Sets.newHashSet`） | Java Stream / `Collectors.toList()`                                                    |
| Optional                                      | `java.util.Optional`                                                                   |
| Function / Predicate                          | `java.util.function.*`                                                                 |
| Cache                                         | [Caffeine](https://github.com/ben-manes/caffeine) （更快，支持异步刷新、统计）                       |
| BloomFilter                                   | Redis Bloom / Redisson Bloom / [Caffeine + Sketches](https://datasketches.apache.org/) |
| EventBus                                      | Spring ApplicationEvent / Reactor / Kafka / Disruptor                                  |
| Range                                         | 手写类 or 使用 Apache Commons Lang/Math                                                     |
| Ordering（排序器）                                 | Java 8 Comparator + Lambda                                                             |

