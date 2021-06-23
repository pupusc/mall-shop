package com.wanmi.sbc.marketing.provider.impl.market;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoResponseVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.request.market.*;
import com.wanmi.sbc.marketing.api.response.market.*;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.bean.vo.MarketingForEndVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingPageVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingViewVO;
import com.wanmi.sbc.marketing.buyoutprice.service.MarketingBuyoutPriceFullBaleLevelService;
import com.wanmi.sbc.marketing.buyoutprice.service.MarketingBuyoutPriceService;
import com.wanmi.sbc.marketing.common.request.MarketingQueryListRequest;
import com.wanmi.sbc.marketing.common.request.MarketingRequest;
import com.wanmi.sbc.marketing.common.request.SkuExistsRequest;
import com.wanmi.sbc.marketing.common.response.MarketingResponse;
import com.wanmi.sbc.marketing.common.service.MarkeingGoodsLevelService;
import com.wanmi.sbc.marketing.common.service.MarketingService;
import com.wanmi.sbc.marketing.halfpricesecondpiece.service.MarketingHalfPriceSecondPieceLevelService;
import com.wanmi.sbc.marketing.util.error.MarketingErrorCode;
import com.wanmi.sbc.setting.api.provider.SystemPointsConfigQueryProvider;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-21 14:27
 */
@Validated
@RestController
public class MarketingQueryController implements MarketingQueryProvider {

    @Autowired
    private MarketingService marketingService;

    @Autowired
    MarketingBuyoutPriceFullBaleLevelService buyoutPriceFullBaleLevelService;

    @Autowired
    MarketingBuyoutPriceService marketingBuyoutPriceService;

    @Autowired
    MarketingHalfPriceSecondPieceLevelService marketingHalfPriceSecondPieceLevelService;

    @Autowired
    GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    SystemPointsConfigQueryProvider systemPointsConfigQueryProvider;

    @Autowired
    private MarkeingGoodsLevelService markeingGoodsLevelService;

    /**
     * @param request 查询参数 {@link ExistsSkuByMarketingTypeRequest}
     * @return
     */
    @Override
    public BaseResponse<List<String>> queryExistsSkuByMarketingType(@RequestBody @Valid ExistsSkuByMarketingTypeRequest request) {
        List<String> skuList = marketingService.getExistsSkuByMarketingType(request.getStoreId(),
                KsBeanUtil.convert(request.getSkuExistsDTO(), SkuExistsRequest.class));
        if(request.getSkuExistsDTO().getMarketingType()== MarketingType.BUYOUT_PRICE && CollectionUtils.isNotEmpty(skuList)){
            GoodsInfoListByIdsRequest goodsSku=GoodsInfoListByIdsRequest.builder().goodsInfoIds(skuList).build();
            skuList= goodsInfoQueryProvider.listByIds(goodsSku).getContext().getGoodsInfos().stream().map(GoodsInfoVO::getGoodsInfoName).collect(Collectors.toList());
            return BaseResponse.success(skuList);
        }
        if(request.getSkuExistsDTO().getMarketingType() == MarketingType.MARKUP ){
            List<String> existsMarkupSkuBy = marketingService.getExistsMarkupSkuBy(request.getStoreId(),
                    KsBeanUtil.convert(request.getSkuExistsDTO(), SkuExistsRequest.class));
            if(CollectionUtils.isNotEmpty(skuList)){
                GoodsInfoListByIdsRequest goodsSku=GoodsInfoListByIdsRequest.builder().goodsInfoIds(skuList).build();
                List<String> goodsName = goodsInfoQueryProvider.listByIds(goodsSku).getContext().getGoodsInfos().stream().map(GoodsInfoVO::getGoodsInfoName).collect(Collectors.toList());
                throw new SbcRuntimeException(MarketingErrorCode.MARKUP_MARKETING_GOODS_TIME_CONFLICT,
                        new Object[]{ goodsName.get(0)});
            }
            if(CollectionUtils.isNotEmpty(existsMarkupSkuBy)){
                GoodsInfoListByIdsRequest goodsSku=GoodsInfoListByIdsRequest.builder().goodsInfoIds(existsMarkupSkuBy).build();
                List<String> goodsName = goodsInfoQueryProvider.listByIds(goodsSku).getContext().getGoodsInfos().stream().map(GoodsInfoVO::getGoodsInfoName).collect(Collectors.toList());
                throw new SbcRuntimeException(MarketingErrorCode.MARKUP_GOODS_TIME_CONFLICT,
                        new Object[]{ goodsName.get(0)});
            }
        }
        return BaseResponse.success(skuList);
    }

    /**
     * @param marketingPageRequest 分页查询参数 {@link MarketingPageRequest}
     * @return
     */
    @Override
    public BaseResponse<MarketingPageResponse> page(@RequestBody @Valid MarketingPageRequest marketingPageRequest) {
        MicroServicePage<MarketingPageVO> marketingPage =
                marketingService.getMarketingPage(KsBeanUtil.convert(marketingPageRequest.getMarketingPageDTO(),
                        MarketingQueryListRequest.class), marketingPageRequest.getStoreId());
        //判断是否需要展示营销规和店铺名称
        if (marketingPageRequest.getRules() != null && marketingPageRequest.getRules()) {
            buyoutPriceFullBaleLevelService.listFullBaleLevel(marketingPage.getContent());
        }
        //判断是否需展示店铺
        if(Boolean.TRUE.equals(marketingPageRequest.getMarketingPageDTO().getShowStoreNameFlag())){
            //获取店铺名称信息
            marketingBuyoutPriceService.getStoreName(marketingPage.getContent());
        }
        //判断是否需要展示第二件半价营销规
        if (marketingPageRequest.getMarketingPageDTO().getMarketingSubType()== MarketingSubType.HALF_PRICE_SECOND_PIECE) {
            marketingHalfPriceSecondPieceLevelService.halfPriceSecondPieceLevel(marketingPage.getContent());
        }

        return BaseResponse.success(MarketingPageResponse.builder().marketingVOS(marketingPage).build());
    }

    /**
     * @param getByIdRequest 唯一编号参数 {@link MarketingGetByIdRequest}
     * @return
     */
    @Override
    public BaseResponse<MarketingGetByIdResponse> getById(@RequestBody @Valid MarketingGetByIdRequest getByIdRequest) {
        MarketingVO marketingVO = KsBeanUtil.convert(marketingService.queryById(getByIdRequest.getMarketingId()),
                MarketingVO.class);
        return BaseResponse.success(MarketingGetByIdResponse.builder().marketingVO(marketingVO).build());
    }

    /**
     * @param getByIdRequest 唯一编号参数 {@link MarketingGetByIdRequest}
     * @return
     */
    @Override
    public BaseResponse<MarketingGetByIdForSupplierResponse> getByIdForSupplier(@RequestBody @Valid MarketingGetByIdRequest getByIdRequest) {
        MarketingResponse marketingResponse =
                marketingService.getMarketingByIdForSupplier(getByIdRequest.getMarketingId());
        MarketingForEndVO marketingForEndVO = KsBeanUtil.convert(marketingResponse, MarketingForEndVO.class);
        marketingForEndVO.setGoodsList(
                GoodsInfoResponseVO.builder()
                        .brands(marketingResponse.getGoodsList().getBrands())
                        .cates(marketingResponse.getGoodsList().getCates())
                        .goodses(marketingResponse.getGoodsList().getGoodses())
                        .goodsInfoPage(marketingResponse.getGoodsList().getGoodsInfoPage()).build());
        return BaseResponse.success(MarketingGetByIdForSupplierResponse.builder().marketingForEndVO(marketingForEndVO).build());

    }

    /**
     * @param getByIdRequest 唯一编号参数 {@link MarketingGetByIdRequest}
     * @return
     */
    public BaseResponse<MarketingByIdAndSubtypeResponse> getByMarketingId(@RequestBody @Valid MarketingGetByIdRequest getByIdRequest){
        MarketingResponse marketingResponse = marketingService.getMarketingByIdForSupplier(getByIdRequest.getMarketingId());
        if(!marketingResponse.getSubType().equals(MarketingSubType.SUITS_GOODS)){
            throw new SbcRuntimeException(MarketingErrorCode.NOT_EXIST);
        }
        MarketingVO marketingVO = KsBeanUtil.convert(marketingResponse,MarketingVO.class);
        return BaseResponse.success(MarketingByIdAndSubtypeResponse.builder().marketingVO(marketingVO).build());
    }

    /**
     * @param getByIdRequest 唯一编号参数 {@link MarketingGetByIdRequest}
     * @return
     */
    @Override
    public BaseResponse<MarketingGetByIdForCustomerResponse> getByIdForCustomer(@RequestBody @Valid MarketingGetByIdRequest getByIdRequest) {
        MarketingForEndVO marketingForEndVO =
                KsBeanUtil.convert(marketingService.getMarketingByIdForCustomer(getByIdRequest.getMarketingId()),
                        MarketingForEndVO.class);
        return BaseResponse.success(MarketingGetByIdForCustomerResponse.builder().marketingForEndVO(marketingForEndVO).build());
    }

    /**
     * @param queryByIdsRequest 唯一编号参数列表 {@link MarketingGetByIdRequest}
     * @return
     */
    @Override
    public BaseResponse<MarketingQueryByIdsResponse> queryByIds(@RequestBody @Valid MarketingQueryByIdsRequest queryByIdsRequest) {
        List<MarketingVO> marketingVOList =
                KsBeanUtil.convert(marketingService.queryByIds(queryByIdsRequest.getMarketingIds()), MarketingVO.class);
        return BaseResponse.success(MarketingQueryByIdsResponse.builder().marketingVOList(marketingVOList).build());
    }

    /**
     * 根据多个id获取多个营销View实体
     * @param queryByIdsRequest 唯一编号参数列表 {@link MarketingViewQueryByIdsRequest}
     * @return
     */
    @Override
    public BaseResponse<MarketingViewQueryByIdsResponse> queryViewByIds(@RequestBody @Valid MarketingViewQueryByIdsRequest queryByIdsRequest) {
        List<MarketingViewVO> marketingVOList = KsBeanUtil.convert(marketingService.queryByIds(queryByIdsRequest.getMarketingIds()), MarketingViewVO.class);
        //增加规则查询
        if (Boolean.TRUE.equals(queryByIdsRequest.getLevelFlag())) {
            marketingService.joinMarketingLevels(marketingVOList);
        }
        return BaseResponse.success(MarketingViewQueryByIdsResponse.builder().marketingViewList(marketingVOList).build());
    }

    /**
     * @param getByIdRequest 唯一编号参数 {@link MarketingGetByIdRequest}
     * @return
     */
    @Override
    public BaseResponse<MarketingGetGoodsByIdResponse> getGoodsById(@RequestBody @Valid MarketingGetByIdRequest getByIdRequest) {
        GoodsInfoResponse goodsByMarketingId = marketingService.getGoodsByMarketingId(getByIdRequest.getMarketingId());
        return BaseResponse.success(MarketingGetGoodsByIdResponse.builder()
                .goodsInfoResponseVO(KsBeanUtil.convert(goodsByMarketingId, GoodsInfoResponseVO.class)).build());
    }

    /**
     * @param queryByIdsRequest 唯一编号列表参数 {@link MarketingQueryByIdsRequest}
     * @return
     */
    @Override
    public BaseResponse<MarketingQueryStartingByIdsResponse> queryStartingByIds(@RequestBody @Valid MarketingQueryByIdsRequest queryByIdsRequest) {
        List<String> ids = marketingService.queryStartingMarketing(queryByIdsRequest.getMarketingIds());
        return BaseResponse.success(MarketingQueryStartingByIdsResponse.builder().marketingIdList(ids).build());
    }

    /**
     * @param request 查询参数 {@link MarketingMapGetByGoodsIdRequest}
     * @return
     */
    @Override
    public BaseResponse<MarketingMapGetByGoodsIdResponse> getMarketingMapByGoodsId(@RequestBody @Valid MarketingMapGetByGoodsIdRequest request) {
        MarketingRequest marketingRequest = KsBeanUtil.convert(request, MarketingRequest.class);
        Map<String, List<MarketingResponse>> marketingMapByGoodsId = marketingService.getMarketingMapByGoodsId
                (marketingRequest);
        return BaseResponse.success(MarketingMapGetByGoodsIdResponse.builder()
                .listMap(MarketingConvert.marketingRes2MarketingForEnd(marketingMapByGoodsId)).build());
    }

    @Override
    public BaseResponse<MarketingGoodsForXsiteResponse> queryForXsite(@RequestBody @Valid MarketingGoodsForXsiteRequest request){
        BaseResponse<MarketingGoodsForXsiteResponse> response = markeingGoodsLevelService.dealByMarketingLevel(request.getGoodsInfoIds(), request.getMarketingLevelType());
        return response;
    }
}