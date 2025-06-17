



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