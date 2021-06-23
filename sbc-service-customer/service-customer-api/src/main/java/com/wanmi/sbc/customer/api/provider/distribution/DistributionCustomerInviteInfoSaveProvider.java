package com.wanmi.sbc.customer.api.provider.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerInviteInfoAddRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributorCustomerInviteInfoReviseRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributorCustomerInviteInfoUpdateRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerInviteInfoAddResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>分销员邀新信息保存服务Provider</p>
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@FeignClient(value = "${application.customer.name}", contextId = "DistributionCustomerInviteInfoSaveProvider")
public interface DistributionCustomerInviteInfoSaveProvider {

    /**
     * 新增分销员邀新信息API（运营后台）
     * @param request 分销员邀新信息新增参数结构 {@link DistributionCustomerInviteInfoAddRequest}
     * @return 新增的分销员邀新信息信息 {@link DistributionCustomerInviteInfoAddResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer-invite-info-add")
    BaseResponse<DistributionCustomerInviteInfoAddResponse> add(@RequestBody @Valid DistributionCustomerInviteInfoAddRequest request);


    /**
     * 邀新信息数量修改
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer-invite-info-updat-count")
    BaseResponse updatCount(@RequestBody @Valid DistributorCustomerInviteInfoUpdateRequest request);

    /**
     * 补发邀新记录后更新分销员邀新信息数量
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer-invite-info-supply-again")
    BaseResponse afterSupplyAgainUpdate(@RequestBody @Valid DistributorCustomerInviteInfoUpdateRequest request);


    /**
     * 校正分销员邀新信息相关信息
     * @param request
     */
    @PostMapping("/customer/${application.customer.version}/ddistribution-customer-invite-info-revise")
    BaseResponse revise(@RequestBody @Valid DistributorCustomerInviteInfoReviseRequest request);
}

