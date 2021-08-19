package com.wanmi.sbc.crm.api.provider.customerplanconversion;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.customerplanconversion.CustomerPlanConversionAddRequest;
import com.wanmi.sbc.crm.api.request.customerplanconversion.CustomerPlanConversionDelByIdListRequest;
import com.wanmi.sbc.crm.api.request.customerplanconversion.CustomerPlanConversionDelByIdRequest;
import com.wanmi.sbc.crm.api.request.customerplanconversion.CustomerPlanConversionModifyRequest;
import com.wanmi.sbc.crm.api.response.customerplanconversion.CustomerPlanConversionAddResponse;
import com.wanmi.sbc.crm.api.response.customerplanconversion.CustomerPlanConversionModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>运营计划转化效果保存服务Provider</p>
 *
 * @author zhangwenchang
 * @date 2020-02-12 00:16:50
 */
@FeignClient(value = "${application.crm.name}", contextId = "CustomerPlanConversionSaveProvider")
public interface CustomerPlanConversionSaveProvider {

    /**
     * 新增运营计划转化效果API
     *
     * @param customerPlanConversionAddRequest 运营计划转化效果新增参数结构 {@link CustomerPlanConversionAddRequest}
     * @return 新增的运营计划转化效果信息 {@link CustomerPlanConversionAddResponse}
     * @author zhangwenchang
     */
    @PostMapping("/crm/${application.crm.version}/customerplanconversion/add")
    BaseResponse<CustomerPlanConversionAddResponse> add(@RequestBody @Valid CustomerPlanConversionAddRequest customerPlanConversionAddRequest);

    /**
     * 修改运营计划转化效果API
     *
     * @param customerPlanConversionModifyRequest 运营计划转化效果修改参数结构 {@link CustomerPlanConversionModifyRequest}
     * @return 修改的运营计划转化效果信息 {@link CustomerPlanConversionModifyResponse}
     * @author zhangwenchang
     */
    @PostMapping("/crm/${application.crm.version}/customerplanconversion/modify")
    BaseResponse<CustomerPlanConversionModifyResponse> modify(@RequestBody @Valid CustomerPlanConversionModifyRequest customerPlanConversionModifyRequest);

    /**
     * 单个删除运营计划转化效果API
     *
     * @param customerPlanConversionDelByIdRequest 单个删除参数结构 {@link CustomerPlanConversionDelByIdRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author zhangwenchang
     */
    @PostMapping("/crm/${application.crm.version}/customerplanconversion/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid CustomerPlanConversionDelByIdRequest customerPlanConversionDelByIdRequest);

    /**
     * 批量删除运营计划转化效果API
     *
     * @param customerPlanConversionDelByIdListRequest 批量删除参数结构 {@link CustomerPlanConversionDelByIdListRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author zhangwenchang
     */
    @PostMapping("/crm/${application.crm.version}/customerplanconversion/delete-by-id-list")
    BaseResponse deleteByIdList(@RequestBody @Valid CustomerPlanConversionDelByIdListRequest customerPlanConversionDelByIdListRequest);

}

