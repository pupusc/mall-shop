      
第一步：删除项目中的万米私服

第二步：手动增加依赖

      > common  --无需改动<br/>
      > setting:  --增加依赖包：sbc-osd,micro-service,org/patchca<br/>
      > ares:<br/>
      > vas<br/>
      > pay:---Pingplusplus,com.wanmi.wxpay:wxpay-sdk<br/>
      > erp<br/>
      > customer:----无需改动<br/>
      > linkedmall:---无需改动<br/>
      > marketing:---无需改动<br/>
      > message:---无需改动<br/>
      > goods:---无需改动<br/>
      > account:---无需改动<br/>
      > elastic::---添加依赖包：com/wanmi/parent/seata-sharding-discovery-2.2.4-RELEASE/parent-seata-sharding-2.2.4-RELEASE.pom;<br/>
                            com/wanmi/micro-servic 引入seata-sharding-4.0.2-RELEASE<br/>
      > order：--无需改动<br/>
      > plaform:---添加依赖包： --com.alipay.fc.csplatform <br/>
                            --of-security<br/>
                            --com.wanmi.sbc/service-common<br/>
                            --com.wanmi.parent/seata-2.2.0-RELEASE      <br/>