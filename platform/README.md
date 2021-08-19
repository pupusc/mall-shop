#b2b-platform

# 原型地址
[点我过去咯](http://ued.dev.qianmi.com/o2o_ppd/B2B/BOSS/#g=1&p=b2b)

#代码规范
具体参考b2b-platform-goods
实体类：是否删除字段以com.wanmi.sbc.common.enums.DeleteFlag枚举类为准，字段命名与数据库字段，遇下划线“_”的右首第一字母必须大写。

#关于异常
在ResultCode.properties中定义异常代码（每个模块代码不一样）,所有异常统一返回类型为BaseResponse;

【设计数据库】
1.请先使用mysql Workbench，设计ER图，生成mwb文件和SQL文件都放至【migrate-sql】文件夹下
2.在版本迭代，需要增加或修改的DDL都一律放在update_ddl.sql文件中，并加以说明和修改日期

【新增/编辑功能】：
    1.相关功能先添加相应的SaveRequest类，继承BaseRequest
    2.利用@RequestBody方式，JSON自动注入填充实体属性
    3.Service的相关保存方法，必须加@Transactional，保证事务一致性

#JPA数据库代码规范
【查询功能】
    1.相关功能先添加相应的QueryRequest类，QueryRequest类需继承com.wanmi.sbc.common.base.BaseQueryRequest
    2.编写相应的Repository接口，需继承JpaSpecificationExecutor
    3.实现getWhereCriteria的动态条件封装方法
    4.查询分页时返回类型为BaseQueryResponse<T>（QueryResponse类）
 PS：目的是为了减少Repository接口的查询方法，力求多次复用findAll方法，如遇到需要复杂的SQL，如or形式、子表、关联表形式的查询，复杂条件的update等等，可以考虑在内增加。
【关于参数】
    前端要什么数据，就返回什么数据，前端有哪些参数，就接哪些参数
 
 #技术栈
  ### 微服务相关
  Spring Cloud
  FeignClient
  Eureka
  Spring Boot
  ### 持久层框架
  Spring-data-jpa
  ### 存储
  MySql
  Mongodb
  ### 全文检索
  ElasticSearch
  ### 缓存
  Redis
  ### 消息队列
  ActiveMQ
  ### 任务调度
  Quartz

#MongoDB数据库代码规范
待完善