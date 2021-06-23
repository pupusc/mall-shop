package com.wanmi.sbc.crm.api.provider.customerplanconversion;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.customerplanconversion.CustomerPlanConversionByIdRequest;
import com.wanmi.sbc.crm.api.request.customerplanconversion.CustomerPlanConversionByPlanIdRequest;
import com.wanmi.sbc.crm.api.request.customerplanconversion.CustomerPlanConversionListRequest;
import com.wanmi.sbc.crm.api.request.customerplanconversion.CustomerPlanConversionPageRequest;
import com.wanmi.sbc.crm.api.response.customerplanconversion.CustomerPlanConversionByIdResponse;
import com.wanmi.sbc.crm.api.response.customerplanconversion.CustomerPlanConversionListResponse;
import com.wanmi.sbc.crm.api.response.customerplanconversion.CustomerPlanConversionPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>运营计划转化效果查询服务Provider</p>
 *
 * @author zhangwenchang
 * @date 2020-02-12 00:16:50
 */
@FeignClient(value = "${application.crm.name}", contextId = "CustomerPlanConversionQueryProvider")
public interface CustomerPlanConversionQueryProvider {
    /**
     * 单个查询运营计划转化效果API
     *
     * @param customerPlanConversionByPlanIdIdRequest 单个查询运营计划转化效果请求参数 {@link CustomerPlanConversionByPlanIdRequest}
     * @return 运营计划转化效果详情 {@link CustomerPlanConversionByIdResponse}
     * @author zhangwenchang
     */
    @PostMapping("/crm/${application.crm.version}/customerplanconversion/get-by-plan-id")
    BaseResponse<CustomerPlanConversionByIdResponse> getByPlanId(@RequestBody @Valid CustomerPlanConversionByPlanIdRequest customerPlanConversionByPlanIdIdRequest);

}

