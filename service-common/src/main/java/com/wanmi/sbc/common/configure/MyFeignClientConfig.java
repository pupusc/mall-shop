//package com.wanmi.sbc.common.configure;
//
//import feign.Client;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
//import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
//import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Description:
// * Company    : 上海黄豆网络科技有限公司
// * Author     : duanlongshan@dushu365.com
// * Date       : 2021/9/25 2:08 上午
// * Modify     : 修改日期          修改人员        修改说明          JIRA编号
// ********************************************************************/
//@Configuration
//public class MyFeignClientConfig {
//
//    @Bean
//    public Client feignClient(@Qualifier("clientFactory") SpringClientFactory clientFactory) {
//        CachingSpringLoadBalancerFactory bean = new CachingSpringLoadBalancerFactory(clientFactory);
//        return new LoadBalancerFeignClient(new MyFeignClient(null, null), bean, clientFactory);
//    }
//}
