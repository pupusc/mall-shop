# 千米微服务组件包

我们的目标：帮助开发者快速搭建自己的微服务！


## 版本说明

| micro-service             | spring boot    |
|:--------------------------|:---------------|
| <= 1.0.x                  | 1.3.x          |
| >= 1.1.x                  | 1.4.x          |
| >= 1.1.8                  | 1.4.x, 1.5.x   |



## Features

* [dubbo框架整合，同时支持两种rpc协议](./docs/dubbo-guide.md)
* [接口日志整合，提供dubbo、http接口日志的组件](./docs/intflog.md)
* [开发者日志，提供切面方式记录(com.wanmi..*Repository||com.wanmi..*Service)类中所有方法的in与out](./docs/applog.md)
* [restful接口ApiDoc自动配置(jwt鉴权支持需要springfox 2.5.0版本以上)](./docs/apidoc.md)
* [HikariCP数据源配置](./docs/hikari.md)
* [ehCache配置](./docs/ehcache.md)
* [memCached配置](./docs/memcached.md)
* [异步执行器](./docs/async-task.md)
* [httpClient配置](./docs/http-client.md)
* [LogstashKafkaAppender与LogstashRedisAppender组件](./docs/logstash.md) (since: 1.0.4-RELEASE)
* [多数据源配置、JPA整合、Mybatis整合](./docs/MultiDataSource.md)
* [在线日志级别调整](./docs/logger-level.md) (since: 1.1.9-RELEASE 调整至management endpoint)
* [kafka集成](./docs/kafka.md) (since 1.1.0-RELEASE)
* [elasticsearch 5.x rest client集成](./docs/es-rest-client.md) (since 1.1.0-RELEASE)
* [HTTP请求流控](./docs/traffic.md)
* [注册微服务到 Kong 网关](./docs/kong.md) (since 1.2.6-RELEASE)

## 使用方法

```xml
<dependency>
    <groupId>com.wanmi</groupId>
    <artifactId>micro-service</artifactId>
    <version>1.2.11-RELEASE</version>
</dependency>
```

## 使用Demo
* 后端服务: http://git.dev.qianmi.com/commons/micro-service-backend-sample.git
* 前端Restful: http://git.dev.qianmi.com/commons/micro-service-frontend-sample.git
* BFF Kong注册: http://git.dev.qianmi.com/commons/micro-service-kong-sample.git
