package com.wanmi.sbc.customer.provider.impl.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerRankingQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerIdsListRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerRankingPageRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerRankingQueryRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerIdsListResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerRankingPageResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerRankingResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailWithImgVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerRankingVO;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerRanking;
import com.wanmi.sbc.customer.distribution.service.DistributionCustomerRankingService;
import com.wanmi.sbc.customer.quicklogin.service.ThirdLoginRelationService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>用户分销排行榜查询服务接口实现</p>
 * @author lq
 * @date 2019-04-19 10:05:05
 */
@RestController
@Validated
public class DistributionCustomerRankingQueryController implements DistributionCustomerRankingQueryProvider {
    @Autowired
    private DistributionCustomerRankingService distributionCustomerRankingService;


    @Autowired
    private ThirdLoginRelationService thirdLoginRelationService;

    @Override
    public BaseResponse<DistributionCustomerRankingPageResponse> page(@RequestBody @Valid DistributionCustomerRankingPageRequest distributionCustomerRankingPageReq) {
        DistributionCustomerRankingQueryRequest queryReq = new DistributionCustomerRankingQueryRequest();
        KsBeanUtil.copyPropertiesThird(distributionCustomerRankingPageReq, queryReq);
        Page<DistributionCustomerRanking> distributionCustomerRankingPage = distributionCustomerRankingService.page(queryReq);
        Page<DistributionCustomerRankingVO> newPage = distributionCustomerRankingPage.map(entity -> distributionCustomerRankingService.wrapperVo(entity));
        MicroServicePage<DistributionCustomerRankingVO> microPage = new MicroServicePage<>(newPage, distributionCustomerRankingPageReq.getPageable());
        DistributionCustomerRankingPageResponse finalRes = new DistributionCustomerRankingPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    /**
     * 查询排名信息
     * @param request
     * @return
     */
    @Override
    public BaseResponse<DistributionCustomerRankingResponse> ranking(@RequestBody @Valid DistributionCustomerRankingPageRequest request) {
        DistributionCustomerRankingQueryRequest queryReq = new DistributionCustomerRankingQueryRequest();
        KsBeanUtil.copyPropertiesThird(request, queryReq);
        Page<DistributionCustomerRanking> distributionCustomerRankingPage = distributionCustomerRankingService.page(queryReq);
        Page<DistributionCustomerRankingVO> newPage = distributionCustomerRankingPage.map(entity -> distributionCustomerRankingService.wrapperVo(entity));
        DistributionCustomerRankingResponse rankingResponse = new DistributionCustomerRankingResponse();
        List<DistributionCustomerRankingVO> rankingVOList =new ArrayList<>(newPage.getContent()) ;

        DistributionCustomerRankingVO distributionCustomerRankingVO = new DistributionCustomerRankingVO();
        distributionCustomerRankingVO.setCustomerId(request.getMyCustomerId());
        if (CollectionUtils.isEmpty(rankingVOList)) {
            rankingVOList = new ArrayList<>();
        }
        //获取用户信息-头像、昵称
        CustomerIdsListRequest customerIdsListRequest = new CustomerIdsListRequest();
        List<String> customerIds = rankingVOList.stream().map(DistributionCustomerRankingVO::getCustomerId).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(customerIds)) {
            customerIds = new ArrayList<>();
        }
        // 我的排名
        if (!customerIds.contains(distributionCustomerRankingVO.getCustomerId())&&customerIds.size()<request.getPageSize()) {
            customerIds.add(distributionCustomerRankingVO.getCustomerId());
            //初始化一条当前用户的排名信息
            rankingVOList.add(init(distributionCustomerRankingVO.getCustomerId()));

        }
        customerIdsListRequest.setCustomerIds(customerIds);
        BaseResponse<CustomerIdsListResponse> listByCustomerIds = thirdLoginRelationService.listWithImgByCustomerIds(customerIdsListRequest);
        List<CustomerDetailWithImgVO> customerVOList = listByCustomerIds.getContext().getCustomerVOList();
        for (int i = 0; i < rankingVOList.size(); i++) {
            DistributionCustomerRankingVO vo = rankingVOList.get(i);
            CustomerDetailWithImgVO customerDetailWithImgVO = customerVOList.stream().filter(ivo -> ivo.getCustomerId().equals(vo.getCustomerId())).findFirst().orElse(new CustomerDetailWithImgVO());
            vo.setRanking(String.valueOf(i + 1));
            //头像
            vo.setImg(customerDetailWithImgVO.getHeadimgurl());
            vo.setName(customerDetailWithImgVO.getCustomerName());
            // 排行
            if (vo.getCustomerId().equals(distributionCustomerRankingVO.getCustomerId())) {
                distributionCustomerRankingVO.setRanking(String.valueOf(i + 1));
                //头像
                distributionCustomerRankingVO.setImg(customerDetailWithImgVO.getHeadimgurl());
                distributionCustomerRankingVO.setName(customerDetailWithImgVO.getCustomerName());
            }
        }

        rankingResponse.setRankingVOList(rankingVOList);
        rankingResponse.setDistributionCustomerRankingVO(distributionCustomerRankingVO);
        return BaseResponse.success(rankingResponse);
    }

    /**
     * 初始化一条排名信息
     * @return
     */
    private DistributionCustomerRankingVO init(String customerId) {
        DistributionCustomerRankingVO distributionCustomerRankingVO = new DistributionCustomerRankingVO();
        distributionCustomerRankingVO.setCustomerId(customerId);
        distributionCustomerRankingVO.setInviteCount(0);
        distributionCustomerRankingVO.setInviteAvailableCount(0);
        distributionCustomerRankingVO.setSaleAmount(BigDecimal.ZERO);
        distributionCustomerRankingVO.setCommission(BigDecimal.ZERO);
        return distributionCustomerRankingVO;

    }
}

