



## RabbitMQ 消息传递的Bean类

Bean类必须实现Serializable 接口


## 参考文章

https://www.cnblogs.com/cnff/p/18070920


## message 消息持久化

默认 RabbitTemplate 类下的 convertAndSend 就是持久化的消息。


## 模拟消费失败事件，只需要在 MqConsumer 消费消息中抛出异常即可

## 模拟死信队列事件，只需不让 消息被 MqConsumer 进行消费即可

## RabbitMQ 在取消超时订单中的应用

1、订单服务生成订单后，发送一条延时消息到消息队列
2、消息队列在消息到达支付过期时间时，将消息投递给消费者，执行取消订单的逻辑


## RabbitMQ 有延时插件可以使用，参考上面的文章

## Redis 延迟队列，在取消超时订单中的应用

生产者将任务信息发送到zset集合，带有任务执行的时间戳

守护线程检测zset 集合中到期的任务，若任务到期，将任务编号转移到list集合，消费者从list集合弹出任务，并执行任务逻辑


## RabbitMQ 使用的三种场景
### 普通消息传递： 用 direct 或 topic + queue。

#### 使用 direct exchange + queue
```java

@Bean
public DirectExchange directExchange() {
    return new DirectExchange("direct_logs");
}

@Bean
public Queue orderQueue() {
    return new Queue("order_queue");
}

@Bean
public Binding binding() {
    return BindingBuilder.bind(orderQueue()).to(directExchange()).with("order.created");
}


```
### 路由匹配模式 TopicExchange

```
@Bean
public TopicExchange topicExchange() {
    return new TopicExchange("topic_logs");
}

@Bean
public Queue payQueue() {
    return new Queue("pay_queue");
}

@Bean
public Binding payBinding() {
    return BindingBuilder.bind(payQueue()).to(topicExchange()).with("order.pay.#");
}

```

### 广播： 用 FanoutExchange
```java
@Bean
public FanoutExchange fanoutExchange() {
    return new FanoutExchange("fanout_logs");
}

@Bean
public Queue smsQueue() {
    return new Queue("sms_queue");
}

@Bean
public Binding smsBinding() {
    return BindingBuilder.bind(smsQueue()).to(fanoutExchange());
}
```


### 高吞吐量事件流： 用 stream
#### 需要使用 RabbitMQ 的 StreamPlugin 插件(在RabbitMQ中进行配置)

```
rabbitmq-plugins enable rabbitmq_stream
```

#### Java中需要引入 Stream 客户端

```
<dependency>
  <groupId>com.rabbitmq</groupId>
  <artifactId>rabbitmq-stream-client</artifactId>
  <version>0.14.0</version>
</dependency>
```

#### 创建Stream 并发送消息

```
Environment env = Environment.builder().build();

Producer producer = env.producerBuilder()
        .stream("event_stream")
        .build();

producer.send(Message.builder()
        .addData("High throughput event".getBytes())
        .build());
```


#### 消息消费

```
Consumer consumer = env.consumerBuilder()
        .stream("event_stream")
        .messageHandler(ctx -> {
            System.out.println("接收事件: " + new String(ctx.message().getBodyAsBinary()));
        })
        .build();

```

### RabbitMQ 本身不支持分区，但可以通过“路由键 + 多个队列 + 路由规则”模拟分区行为。
Kafka 原生支持分区（推荐）

实现原理：
1、创建多个队列，比如 queue-0, queue-1, queue-2, queue-3
2、使用一个 topic exchange，根据 partitionKey 决定 routingKey（如：partition.0）
3、每个消费者监听特定的 routingKey


### Kafka 原生支持分区（推荐）
Kafka 是天然的分区模型，可以不用 Spring Cloud Stream，直接使用 Kafka 客户端或 Spring for Kafka