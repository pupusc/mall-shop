package com.wanmi.sbc.customer.api.provider.growthvalue;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueAddRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>客户成长值明细表保存服务Provider</p>
 *
 * @author yang
 * @since 2019/2/22
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerGrowthValueProvider")
public interface CustomerGrowthValueProvider {

    /**
     * 新增客户成长值明细
     *
     * @param customerGrowthValueAddRequest
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/customergrowthvalue/add")
    BaseResponse increaseGrowthValue(@RequestBody @Valid CustomerGrowthValueAddRequest customerGrowthValueAddRequest);

}
