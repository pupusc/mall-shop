package com.wanmi.sbc.goods.api.provider.nacos;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.response.nacos.GoodsNacosConfigResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(value = "${application.goods.name}", contextId = "GoodsNacosConfigProvider")
public interface GoodsNacosConfigProvider {


    /**
     * 获取nacos配置信息
     * @return
     */
    @GetMapping("/goods/${application.goods.version}/nacos/getNacosConfig")
    BaseResponse<GoodsNacosConfigResp> getNacosConfig();

}
