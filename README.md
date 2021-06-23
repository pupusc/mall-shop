      
第一步：删除项目中的万米私服
第二步：手动增加依赖
      common  --无需改动
      setting:  --增加依赖包：sbc-osd,micro-service,org/patchca
      ares:
      vas
      pay:---Pingplusplus,com.wanmi.wxpay:wxpay-sdk
      erp
      customer:----无需改动
      linkedmall:---无需改动
      marketing:---无需改动
      message:---无需改动
      goods:---无需改动
      account:---无需改动
      elastic::---添加依赖包：com/wanmi/parent/seata-sharding-discovery-2.2.4-RELEASE/parent-seata-sharding-2.2.4-RELEASE.pom;
                            com/wanmi/micro-servic 引入seata-sharding-4.0.2-RELEASE
      order：--无需改动
      plaform:---添加依赖包： --com.alipay.fc.csplatform 
                            --of-security
                            --com.wanmi.sbc/service-common
                            --com.wanmi.parent/seata-2.2.0-RELEASE      