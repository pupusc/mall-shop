## Spring Cloud Stream

### 什么是 [SCS(Spring Cloud Stream)](https://spring.io/projects/spring-cloud-stream)

> Spring Cloud Stream 是一个框架，用于构建与共享消息系统连接的高度可伸缩的事件驱动微服务。
>
> 该框架提供了基于已建立的和熟悉的 Spring 习惯用法和最佳实践的灵活编程模型，包括支持持久的发布/订阅语义、用户组和有状态分区。

### 为什么要用 SCS

目前市场上的消息中间件比较多, 优秀的消息中间件也很多. 项目交付客户之后, 客户可能想用的消息中间件跟我们标品使用的不一样. 有的可能想用 RabbitMq, 有的想用 Kafka etc. 因此, 我们需要找一个解决方案用最小的代价去替换消息中间件. SCS 就是用来做这个事情的一个框架.

### 为什么用 Rabbit MQ

首先我们系统使用的一直是 Active MQ, 但是我们因为要使用 SCS, 但是 SCS 目前还不支持 Active MQ. 支持列表如下:

1. [RabbitMQ](https://github.com/spring-cloud/spring-cloud-stream-binder-rabbit)
2. [Apache Kafka](https://github.com/spring-cloud/spring-cloud-stream-binder-kafka)
3. [Kafka Streams](https://github.com/spring-cloud/spring-cloud-stream-binder-kafka/tree/master/spring-cloud-stream-binder-kafka-streams)
4. [Amazon Kinesis](https://github.com/spring-cloud/spring-cloud-stream-binder-aws-kinesis)
5. [Google PubSub (partner maintained)](https://github.com/spring-cloud/spring-cloud-gcp/tree/master/spring-cloud-gcp-pubsub-stream-binder)
6. [Solace PubSub+ (partner maintained)](https://github.com/SolaceProducts/spring-cloud-stream-binder-solace)
7. [Azure Event Hubs (partner maintained)](https://github.com/Microsoft/spring-cloud-azure/tree/master/spring-cloud-azure-eventhub-stream-binder)

目前关于 [ActiveMQ](https://github.com/spring-cloud/spring-cloud-stream-binder-jms)还是 alpha 版本, 并且最新的更新在 2 年前.

至于为什么选择 Rabbit MQ, 我想可能是因为他排在第一位.

### 使用

1. 搭建 RabbitMQ 环境, 获取地址, 端口号, 账号, 密码

   目前默认的服务端的端口号为 5672, 账号密码均为 guest.

2. 启动 RabbitMQ 的插件 rabbitmq_management

   该插件会启动一个可视化界面, 用于观察消息的具体信息.
   该可视化界面的端口默认为 15672

3. 项目引入 SCS 相关依赖

   ```
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-stream-binder-rabbit</artifactId>
    </dependency>
   ```

   ares 等模块需要加上具体版本号

   ```
    <version>1.3.4.RELEASE</version>
   ```

4. 代码

   First: 首先创建消费者端的 Sink 接口

   ```
   import org.springframework.cloud.stream.annotation.Input;
   import org.springframework.messaging.SubscribableChannel;

   // 因为默认存在了Sink接口, 避免以后出现重名
   // 因此使用XXXSink来命名
   public interface XXXSink {

     @Input(MQConstants.xxxxxxx1)  // 此处的MQConstants是具体监听的MQConstants
     SubscribableChannel receiveMethodName1(); // 返回类型为SubscribableChannel, 方法名任意

     @Input(MQConstants.xxxxxxx2)
     SubscribableChannel receiveMethodName2();

     ......
     ......
     ......

   }
   ```

   Second: 监听消息, 完成具体实现

   ```
   import org.springframework.cloud.stream.annotation.EnableBinding;
   import org.springframework.cloud.stream.annotation.StreamListener;

   // 需要绑定XXXSink接口
   @EnableBinding(XXXSink.class)
   public class XXXConsumer {

     // 监听 XXXSink 的@Input
     @StreamListener(MQConstant.xxxxxxx1)
     public void method1(String json) {
       // 对json字符串进行解析

     }

     @StreamListener(MQConstant.xxxxxxx2)
     public void method2() {

     }

   }

   ```

   Third: 发送消息

   ```

   // 动态通道选择, 自动完成注册. 无需具体绑定哪个Sink
   @EnableBinding
   public class XXXProvider {

    @Autowired
    private BinderAwareChannelResolver resolver;

    public void send1() {

      ......
      ......
      ......

      // 因为之前发送消息都是用的JSON字符串, 所以这里默认还是使用之前的方式
      resolver.resolveDestination(MQConstant.xxxxxxx1).send(new GenericMessage<>(JSONObject.toJSONString(someObj1)));
    }

    public void send2() {

      ......
      ......
      ......

      resolver.resolveDestination(MQConstant.xxxxxxx2).send(new GenericMessage<>(JSONObject.toJSONString(someObj2)));
    }

   }
   ```

   Last:

   所有的 Consumer 模块和 Provider 模块需要在配置文件中加上关于 RabbitMQ 的配置

   目前加上该配置的模块有: Supplier, Boss, Order, Goods, Customer, Setting, Marketing, Ares, Perseus

   ```
    # ----------------------------------------
    # rabbitmq config
    # ----------------------------------------
    spring.rabbitmq.host = 118.31.238.229
    spring.rabbitmq.port = 5672
    spring.rabbitmq.username = guest
    spring.rabbitmq.password = guest
   ```

   因为可能出现集群部署或者开发者本地部署的情况, 因为消息使用的是订阅模式, 如果有多端, 可能会让消息消费多次. 集群部署情况下, 默认为同一个 group, 消费消息会使用轮询模式. 所以, 所有的 consumer 的配置文件中需要添加如下配置(group 名字任意, 但需要唯一)

   目前加上该配置的有 setting 模块, marketing 模块和 Ares 模块

   ```
    # 消费者端需要加上该配置, 做轮询
    spring.cloud.stream.default.group = group-1
   ```

   如果在本地开发默认下需要调试关于 group 模块, 有两种情况

   1. 自己启动的 consumer 模块, 但是 RabbitMQ 使用统一(测试环境或者开发环境)环境上的:

      此时需要更改本地的 group 名称, 多调用几次, 使本地启动的 consumer 等到派发到这边的消息

   2. 自己启动的 consumer 模块和 RabbitMQ

      此时无需更改本地的 group 名称, 保持和统一环境一致. 更改本地的 rabbit 配置为自己本地的 rabbit 服务
