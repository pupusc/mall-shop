package com.wanmi.sbc.goods.api.provider.chooserule;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.chooserule.ChooseRuleProviderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.goods.name}", contextId = "ChooseRuleProvider")
public interface ChooseRuleProvider {


    @PostMapping("/goods/${application.goods.version}/chooserule/add")
    BaseResponse add(@Validated @RequestBody  ChooseRuleProviderRequest chooseRuleProviderRequest);

    @PostMapping("/goods/${application.goods.version}/chooserule/update")
    BaseResponse update(@Validated @RequestBody  ChooseRuleProviderRequest chooseRuleProviderRequest);

    @PostMapping("/goods/${application.goods.version}/chooserule/findByCondition")
    BaseResponse findByCondition(@Validated @RequestBody  ChooseRuleProviderRequest chooseRuleProviderRequest);
}
