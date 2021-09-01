package com.wanmi.sbc.goods.api.provider.chooserule;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.chooserule.ChooseRuleProviderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.goods.name}", contextId = "ChooseRuleProvider")
public interface ChooseRuleProvider {


    @PostMapping("/goods/${application.goods.version}/chooserule/add")
    BaseResponse add(@RequestBody ChooseRuleProviderRequest chooseRuleProviderRequest);

    @PostMapping("/goods/${application.goods.version}/chooserule/update")
    BaseResponse update(@RequestBody ChooseRuleProviderRequest chooseRuleProviderRequest);

    @PostMapping("/goods/${application.goods.version}/chooserule/findByCondition")
    BaseResponse findByCondition(@RequestBody ChooseRuleProviderRequest chooseRuleProviderRequest);
}
