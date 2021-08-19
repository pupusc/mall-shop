package com.wanmi.sbc.customer.api.provider.growthvalue;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValuePageRequest;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueQueryRequest;
import com.wanmi.sbc.customer.api.response.CustomerQueryResponse;
import com.wanmi.sbc.customer.api.response.growthvalue.CustomerGrowthValuePageResponse;
import com.wanmi.sbc.customer.api.response.growthvalue.CustomerGrowthValueResponse;
import com.wanmi.sbc.customer.api.response.growthvalue.CustomerGrowthValueTodayResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>客户成长值表查询服务</p>
 *
 * @author yang
 * @since 2019/2/23
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerGrowthValueQueryProvider")
public interface CustomerGrowthValueQueryProvider {

    @PostMapping("/customer/${application.customer.version}/customergrowthvalue/page")
    BaseResponse<CustomerGrowthValuePageResponse> page(@RequestBody @Valid CustomerGrowthValuePageRequest customerGrowthValuePageRequest);

    @PostMapping("/customer/${application.customer.version}/customergrowthvalue/get-growth-value-today")
    BaseResponse<CustomerGrowthValueTodayResponse> getGrowthValueToday(@RequestBody @Valid CustomerGrowthValueQueryRequest customerGrowthValueQueryRequest);
}
