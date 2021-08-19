package com.wanmi.sbc.customer.api.provider.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.customer.*;
import com.wanmi.sbc.customer.api.response.customer.DistributionInviteNewUpdateResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionInviteNewAddResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionInviteNewRecordBatchUpdateResponse;
import com.wanmi.sbc.customer.bean.vo.DistributionInviteNewVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Description: 邀新provider
 * @Autho qiaokang
 * @Date：2019-03-05 09:31:13
 */
@FeignClient(value = "${application.customer.name}", contextId = "DistributionInviteNewProvider")
public interface DistributionInviteNewProvider {

    /**
     * 新增邀新记录
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/add-invite-new")
    BaseResponse<DistributionInviteNewAddResponse> addDistributionInviteNewRecord(@RequestBody @Valid DistributionInviteNewAddRequest request);

    /**
     * 更新邀新记录
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/update-invite-new")
    BaseResponse<DistributionInviteNewVo> updateAndFlash(@RequestBody @Valid DistributionInviteNewUpdateRequest request);

    /**
     * 批量更新邀新记录
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/batch-update")
    BaseResponse<DistributionInviteNewRecordBatchUpdateResponse> batchUpdate(@RequestBody @Valid DistributionInviteNewRecordBatchUpdateRequest request);

    /**
     * 更新邀新记录-补发
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/update-invite-new-supply-again")
    BaseResponse<DistributionInviteNewVo> updateBySupplyAgain(@RequestBody @Valid DistributionInviteNewSupplyAgainRequest request);

    /**
     * 新增邀新记录
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/add-register")
    BaseResponse addRegister(@RequestBody @Valid DistributionInviteNewAddRegisterRequest request);

    /**
     * 更新邀新记录，发放奖励奖金、优惠券（定时任务触发）
     * @param modifyRegisterRequest
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/modify")
    BaseResponse<DistributionInviteNewUpdateResponse> modify(@RequestBody @Valid DistributionInviteNewModifyRegisterRequest modifyRegisterRequest);

}
