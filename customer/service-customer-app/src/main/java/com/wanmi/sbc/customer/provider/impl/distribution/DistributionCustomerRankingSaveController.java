package com.wanmi.sbc.customer.provider.impl.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerRankingSaveProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerRankingAddRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerRankingInitRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerRankingModifyRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerRankingAddResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerRankingInitResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerRankingModifyResponse;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerRankingVO;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerRanking;
import com.wanmi.sbc.customer.distribution.service.DistributionCustomerRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>用户分销排行榜保存服务接口实现</p>
 * @author lq
 * @date 2019-04-19 10:05:05
 */
@RestController
@Validated
public class DistributionCustomerRankingSaveController implements DistributionCustomerRankingSaveProvider {
    @Autowired
    private DistributionCustomerRankingService distributionCustomerRankingService;

    @Override
    public BaseResponse<DistributionCustomerRankingAddResponse> add(@RequestBody @Valid DistributionCustomerRankingAddRequest distributionCustomerRankingAddRequest) {
        DistributionCustomerRanking distributionCustomerRanking = new DistributionCustomerRanking();
        KsBeanUtil.copyPropertiesThird(distributionCustomerRankingAddRequest, distributionCustomerRanking);
        return BaseResponse.success(new DistributionCustomerRankingAddResponse(
                distributionCustomerRankingService.wrapperVo(distributionCustomerRankingService.add(distributionCustomerRanking))));
    }

    @Override
    public BaseResponse<DistributionCustomerRankingModifyResponse> modify(@RequestBody @Valid DistributionCustomerRankingModifyRequest distributionCustomerRankingModifyRequest) {
        DistributionCustomerRanking distributionCustomerRanking = new DistributionCustomerRanking();
        KsBeanUtil.copyPropertiesThird(distributionCustomerRankingModifyRequest, distributionCustomerRanking);
        return BaseResponse.success(new DistributionCustomerRankingModifyResponse(
                distributionCustomerRankingService.wrapperVo(distributionCustomerRankingService.modify(distributionCustomerRanking))));
    }


    @Override
    public BaseResponse<DistributionCustomerRankingInitResponse> initRankingData(@RequestBody @Valid  DistributionCustomerRankingInitRequest request) {
        List<DistributionCustomerRanking> distributionCustomerRankingList = distributionCustomerRankingService.initRankingData(request);
        List<DistributionCustomerRankingVO> distributionCustomerRankingVOList = distributionCustomerRankingList.stream().map(entity -> distributionCustomerRankingService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new DistributionCustomerRankingInitResponse(distributionCustomerRankingVOList));
    }

}

