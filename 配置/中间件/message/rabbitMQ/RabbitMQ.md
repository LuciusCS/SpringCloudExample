



## 对RabbitMQ进行封装

对RabbitMQ中的消息添加消息头





## 问题一

使用Docker 启动 RabbitMQ后，不能访问控制面板

控制面板访问路径 ： 127.0.0.1:15672

docker 镜像应该选用 rabbitmq:3-management 镜像 (内置管理插件)





@Configuration
public class RabbitMqConfig {
private static final Logger logger = LoggerFactory.getLogger(RabbitMqConfig.class);

    public static final String queue1 = "demo_queue_1";
    public static final String dead_queue = "dead_queue";

    public static final String exchange = "demo_exchange";
    public static final String dead_exchange = "dead_exchange";

    public static final String queue_key_1 = "demo_queue_key_1";
    public static final String dead_queue_key_1 = "dead_queue_key_1";


    //队列
    @Bean
    public Queue demoQueue1(){
//        return new Queue(queue1);

        return QueueBuilder.durable(queue1)
                .withArgument("x-message-ttl", 6000)  // 设置消息过期时间 6秒
                .withArgument("x-dead-letter-exchange", dead_exchange)  // 设置死信交换机
                .withArgument("x-dead-letter-routing-key", dead_queue_key_1)  // 设置死信路由键
                .build();
    }
    //队列
    @Bean
    public Queue demoQueue2(){
        return new Queue(queue2);
    }
    //直连交换机
    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(exchange, false, false);
    }
    //绑定 key，关联交换机和队列
    @Bean
    public Binding binding(Queue demoQueue1, DirectExchange exchange){
        return BindingBuilder.bind(demoQueue1).to(exchange).with(queue_key_1);
    }



    //定义死信队列
    @Bean
    public Queue deadQueue(){
        return new Queue(dead_queue);
    }
    //定义死信交换机
    @Bean
    public DirectExchange deadExchange(){
        return new DirectExchange(dead_exchange);
    }
    //绑定
    @Bean
    public Binding deadBinding(Queue deadQueue, DirectExchange deadExchange){
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(dead_queue_key_1);
    }

}
