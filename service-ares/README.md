# Ares - 数据分析统计中心

> ## 项目目录结构说明

>> ### 分为三个模块：ares-core、ares-api、ares-app。

>>> * ares-core包含了消息处理，数据整理，报表视图生成，读写等核心操作，对外(ares-app)暴露service接口。ares-core不依赖于api和app。
>>> * ares-api定义了MQ消息数据结构和Java RPC调用提供的一些数据操作对象和接口。
>>> * ares-app对应于DDD中的防腐层、开放服务Context。主要提供RPC和RestFUL方式的服务调用，以及用于MQ消息的接收。采用SpringBoot配置和启动应用。ares-app也是数据统计微服务部署的项目。

> ## 约束与规范

>> ### Elasticsearch
>>> * 数据池结构：索引:ares 类型:ares_data
```json 
            {
                  _id
                 "type":"create|pay|returnOrder"
                 "time":"createTime|payTime|returnAmtTime|returnOrderTime"(类型相应的时间)
                 "orderNo":"订单号"
                 "returnNo":"退单号"
                 "orderAmt":"总金额(类型相应的金额)"
                 "realAmt":"实际金额:(createTime|payTime相应的支付金额，returnOrderTime相应的退款金额):
                 skus[]:{
                        id,no.,name，brand,spec, num,price(取商品单价，特价商品取商品件数平摊特价金额，特价商品取退货商品件数平摊特价金额)，catId[],
                        type:"create|pay|returnAmt|returnOrder"
                      }
                 "employeeId":员工Id
                 "customerId":会员Id
                 "companyId":商家Id
                 "buyer":"买家{id,name,accountId,levelId}"
                 "company":"商家{id,name}"
            }
```

>>> * 全平台商品SKU日报表 索引:ares_sku_yyyyMM 类型:ares_sku_yyyyMMdd ，商家商品SKU日报表 ID为X 索引:ares_sku_yyyyMM_X 类型:ares_sku_yyyyMMdd_X
```json 
            {
                skuId:"商品Id"
                orderCount:"下单数"
                orderAmt:"下单金额"
                orderNum:"下单件数"
                payNum:"付款件数"
                returnOrderCount:"退单数"
                returnOrderAmt:"退单金额"
                returnOrderNum:"退单件数" 
                uvOrderCount:"独立访客下单数"
                viewNum:"浏览量"
            }
```

>>> * 商品分类日报表 索引:ares_sku_cate_yyyyMM 类型:ares_sku_cate_yyyyMMdd 
```json 
            {
                cateId:"分类Id"
                orderCount:"下单数"
                orderAmt:"下单金额"
                orderNum:"下单件数"
                payNum:"付款件数"
                returnOrderCount:"退单数"
                returnOrderAmt:"退单金额"
                returnOrderNum:"退单件数" 
            }
```

>>> * 商品品牌日报表 索引:ares_sku_brand_yyyyMM 类型:ares_sku_brand_yyyyMMdd 
```json 
            {
                brandId:"品牌Id"
                orderCount:"下单数"
                orderAmt:"下单金额"
                orderNum:"下单件数"
                payNum:"付款件数"
                returnOrderCount:"退单数"
                returnOrderAmt:"退单金额"
                returnOrderNum:"退单件数" 
            }
```
>> ### 全局参数校验配置
>>> * http://wiki.dev.wanmi.com/pages/viewpage.action?pageId=1900850

>> ### MQ 消息目的地命名规范
>>> 在使用MQ中间件来进行业务交互的过程中，消息存放的位置称为消息目的地，消息目的地有Queue与Topic两种类型；

>>> 命名规范：
>>>>>  a.格式：消息类型.持有消息的服务简称.业务命名
>>>>>  b.消息类型的取值：q:表示queue；t:表示topic
>>>>>  c.系统简称采用英文全小写,此处统一为ares
>>>>>  d. 模块简称采用英文全小写
>>>>>  e. 业务命名：业务自己根据消息用途来进行命名，使用意义完整的英文描述，采用驼峰命名法

>>> 命名示例
>>>>>  q.ares.customer.delete
>>>>>  q.ares.pvuv.push
>>>>>  q.ares.sku.create    
              
>>> 特别说明：
>>>>>  DataPool类的Elasticsearch之Mapping结构采用文件定制，文件是ares-core/resources/dataPool来设置。 由于采用murmur3-plugin方式来实现去重统计，避免应用阀值性造成ES缓存溢出导出返回结果不正确。

#### 2019-06-04 17:02:23
```
 1、为了实现集群模式 但是thrift 方式进行负载均衡不好实现(一种nginx代理，一种zookeeper)，另外为了技术统一遂改成spring的微服务实现方式  
 2、定时任务修改为xxl-job分布式调度任务，需要另外部署xxl-job服务 参考http://www.xuxueli.com/xxl-job/
```
| IJobHandler | cron | desc |
| ----------- | ---- | ---- |
| reportCleanExpirationTaskHandler | 0 0 2 1 * ? | 清理DB,OSS过期报表数据 |
| reportTaskHandler | 0 0 1 * * ? | 报表生成 |
| reportTodayTaskHandler | 0 0/10 * * * ? | 生成今日报表 |