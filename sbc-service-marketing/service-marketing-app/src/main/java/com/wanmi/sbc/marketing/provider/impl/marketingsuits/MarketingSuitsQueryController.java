package com.wanmi.sbc.marketing.provider.impl.marketingsuits;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.marketingsuits.MarketingSuitsQueryProvider;
import com.wanmi.sbc.marketing.api.request.market.MarketingSuitsBySkuIdRequest;
import com.wanmi.sbc.marketing.api.request.marketingsuits.*;
import com.wanmi.sbc.marketing.api.response.market.MarketingMoreGoodsInfoResponse;
import com.wanmi.sbc.marketing.api.response.market.MarketingMoreSuitsGoodInfoIdResponse;
import com.wanmi.sbc.marketing.api.response.marketingsuits.*;
import com.wanmi.sbc.marketing.bean.vo.MarketingSuitsVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingVO;
import com.wanmi.sbc.marketing.marketingsuits.model.root.MarketingSuits;
import com.wanmi.sbc.marketing.marketingsuits.service.MarketingSuitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>组合商品主表查询服务接口实现</p>
 * @author zhk
 * @date 2020-04-01 20:54:00
 */
@RestController
@Validated
public class MarketingSuitsQueryController implements MarketingSuitsQueryProvider {
    @Autowired
    private MarketingSuitsService marketingSuitsService;

    @Override
    public BaseResponse<MarketingSuitsPageResponse> page(@RequestBody @Valid MarketingSuitsPageRequest marketingSuitsPageReq) {
        MarketingSuitsQueryRequest queryReq = KsBeanUtil.convert(marketingSuitsPageReq, MarketingSuitsQueryRequest.class);
        Page<MarketingSuits> marketingSuitsPage = marketingSuitsService.page(queryReq);
        Page<MarketingSuitsVO> newPage = marketingSuitsPage.map(entity -> marketingSuitsService.wrapperVo(entity));
        MicroServicePage<MarketingSuitsVO> microPage = new MicroServicePage<>(newPage, marketingSuitsPageReq.getPageable());
        MarketingSuitsPageResponse finalRes = new MarketingSuitsPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<MarketingSuitsListResponse> list(@RequestBody @Valid MarketingSuitsListRequest marketingSuitsListReq) {
        MarketingSuitsQueryRequest queryReq = KsBeanUtil.convert(marketingSuitsListReq, MarketingSuitsQueryRequest.class);
        List<MarketingSuits> marketingSuitsList = marketingSuitsService.list(queryReq);
        List<MarketingSuitsVO> newList = marketingSuitsList.stream().map(entity -> marketingSuitsService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new MarketingSuitsListResponse(newList));
    }

    @Override
    public BaseResponse<MarketingSuitsByIdResponse> getById(@RequestBody @Valid MarketingSuitsByIdRequest marketingSuitsByIdRequest) {
        MarketingSuits marketingSuits =
                marketingSuitsService.getOne(marketingSuitsByIdRequest.getId());
        return BaseResponse.success(new MarketingSuitsByIdResponse(marketingSuitsService.wrapperVo(marketingSuits)));
    }


    /**
     * 根据营销活动id查询组合购活动信息
     * @param marketingSuitsByMarketingIdRequest 根据营销活动id查询组合购活动信息 {@link MarketingSuitsByMarketingIdRequest}
     * @return 组合商品主表详情 {@link MarketingSuitsByMarketingIdResponse}
     * @author zhk
     */
    @Override
    public BaseResponse<MarketingSuitsByMarketingIdResponse> getByMarketingId(@RequestBody @Valid MarketingSuitsByMarketingIdRequest marketingSuitsByMarketingIdRequest) {
        return BaseResponse.success(marketingSuitsService.getByMarketingId(marketingSuitsByMarketingIdRequest.getMarketingId()));
    }


    // 根据goodinfoid查询正在进行中的活动列表


    /**
     * 活动信息验证
     */
    @Override
    public BaseResponse<MarketingSuitsValidResponse> validSuitOrderBeforeCommit(@RequestBody @Valid MarketingSuitsValidRequest marketingSuitsValidRequest) {
        MarketingSuitsByMarketingIdResponse marketingSuits = marketingSuitsService.getByMarketingId(marketingSuitsValidRequest.getMarketingId());
        MarketingVO marketingVO = marketingSuits.getMarketingVO();

        // 活动时间
        //校验营销活动
        if (marketingVO.getIsPause() == BoolFlag.YES || marketingVO.getDelFlag() == DeleteFlag.YES || marketingVO.getBeginTime().isAfter(LocalDateTime.now())
                || marketingVO.getEndTime().isBefore(LocalDateTime.now())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, marketingVO.getMarketingName() + "无效！");
        }
        MarketingSuitsValidResponse result = KsBeanUtil.convert(marketingSuits, MarketingSuitsValidResponse.class);
        return BaseResponse.success(result);

    }

    @Override
    public BaseResponse<MarketingSuitsByMarketingIdResponse> getByMarketingIdForSupplier(@Valid @RequestBody MarketingSuitsByMarketingIdRequest marketingSuitsByMarketingIdRequest) {
        MarketingSuitsByMarketingIdResponse response = marketingSuitsService.getByMarketingIdForSupplier(marketingSuitsByMarketingIdRequest.getMarketingId());
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<MarketingMoreSuitsGoodInfoIdResponse> getMarketingBySuitsSkuId(@RequestBody @Valid MarketingSuitsBySkuIdRequest request) {
        List<MarketingMoreGoodsInfoResponse> marketingList = marketingSuitsService.getMarketingSuitsBySkuId(request.getGoodsInfoId(), request.getStoreId(), request.getUserId());
        return BaseResponse.success(MarketingMoreSuitsGoodInfoIdResponse.builder().marketingMoreGoodsInfoResponseList(marketingList).build());
    }


}

