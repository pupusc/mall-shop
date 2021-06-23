package com.wanmi.sbc.customer.provider.impl.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerInviteInfoSaveProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerInviteInfoAddRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributorCustomerInviteInfoReviseRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributorCustomerInviteInfoUpdateRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerAddForBossResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerAddResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerInviteInfoAddResponse;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerInviteInfoVO;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerInviteInfo;
import com.wanmi.sbc.customer.distribution.service.DistributionCustomerInviteInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>分销员保存服务接口实现</p>
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@RestController
@Validated
public class DistributionCustomerInviteInfoSaveController implements DistributionCustomerInviteInfoSaveProvider {

    @Autowired
    private DistributionCustomerInviteInfoService distributionCustomerInviteInfoService;


    /**
     * 新增分销员API（运营后台）
     * @param distributionCustomerAddRequest 分销员新增参数结构 {@link DistributionCustomerAddForBossResponse}
     * @return 新增的分销员信息 {@link DistributionCustomerAddResponse}
     * @author lq
     */
    @Override
    public BaseResponse<DistributionCustomerInviteInfoAddResponse> add(@RequestBody @Valid DistributionCustomerInviteInfoAddRequest distributionCustomerAddRequest) {
        DistributionCustomerInviteInfo info = new DistributionCustomerInviteInfo();
        KsBeanUtil.copyPropertiesThird(distributionCustomerAddRequest, info);
        DistributionCustomerInviteInfo vo = distributionCustomerInviteInfoService.add(info);
        return BaseResponse.success(new DistributionCustomerInviteInfoAddResponse(KsBeanUtil.convert(vo, DistributionCustomerInviteInfoVO.class)));
    }

    /**
     * 更新邀新信息
     * @param request
     */
    @Override
    public BaseResponse updatCount(@RequestBody @Valid DistributorCustomerInviteInfoUpdateRequest request) {
        distributionCustomerInviteInfoService.updatCount(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 补发邀新记录后更新分销员统计相关信息
     * @param request
     */
    @Override
    public BaseResponse afterSupplyAgainUpdate(@RequestBody @Valid DistributorCustomerInviteInfoUpdateRequest request) {
        distributionCustomerInviteInfoService.afterSupplyAgainUpdate(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 校正分销员邀新信息相关信息
     * @param request
     */
    @Override
    public BaseResponse revise(@RequestBody @Valid DistributorCustomerInviteInfoReviseRequest request) {
        distributionCustomerInviteInfoService.revise(request);
        return BaseResponse.SUCCESSFUL();
    }
}

