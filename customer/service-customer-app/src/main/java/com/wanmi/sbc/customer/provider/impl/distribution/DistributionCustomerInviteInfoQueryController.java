package com.wanmi.sbc.customer.provider.impl.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerInviteInfoQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerInviteInfoPageRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerInviteInfoQueryRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerInviteInfoPageResponse;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerInviteInfoVO;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerInviteInfo;
import com.wanmi.sbc.customer.distribution.service.DistributionCustomerInviteInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>分销员查询服务接口实现</p>
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@RestController
@Validated
public class DistributionCustomerInviteInfoQueryController implements DistributionCustomerInviteInfoQueryProvider {


    @Autowired
    private DistributionCustomerInviteInfoService distributionCustomerInviteInfoService;

    /**
     * 分页查询分销员API
     * @param distributionCustomerInviteInfoPageReq 分页请求参数和筛选对象 {@link DistributionCustomerInviteInfoPageRequest}
     * @return 分销员分页列表信息 {@link DistributionCustomerInviteInfoPageResponse}
     * @author lq
     */
    @Override
    public BaseResponse<DistributionCustomerInviteInfoPageResponse> page(@RequestBody @Valid DistributionCustomerInviteInfoPageRequest distributionCustomerInviteInfoPageReq) {
        DistributionCustomerInviteInfoQueryRequest queryReq = new DistributionCustomerInviteInfoQueryRequest();
        KsBeanUtil.copyPropertiesThird(distributionCustomerInviteInfoPageReq, queryReq);
        Page<DistributionCustomerInviteInfo> distributionCustomerInviteInfoPage = distributionCustomerInviteInfoService.page(queryReq);
        Page<DistributionCustomerInviteInfoVO> newPage =
                distributionCustomerInviteInfoPage.map(entity -> distributionCustomerInviteInfoService.wrapperVo(entity));
        MicroServicePage<DistributionCustomerInviteInfoVO> microPage = new MicroServicePage<>(newPage,
                distributionCustomerInviteInfoPageReq.getPageable());
        DistributionCustomerInviteInfoPageResponse finalRes = new DistributionCustomerInviteInfoPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

}

