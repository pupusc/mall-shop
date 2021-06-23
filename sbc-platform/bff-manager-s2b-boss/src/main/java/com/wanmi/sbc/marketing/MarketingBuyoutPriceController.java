package com.wanmi.sbc.marketing;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelListRequest;
import com.wanmi.sbc.customer.api.response.store.StoreByIdResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.customer.bean.vo.StoreLevelVO;
import com.wanmi.sbc.marketing.api.provider.buyoutprice.MarketingBuyoutPriceQueryProvider;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.request.buyoutprice.MarketingBuyoutPriceSearchRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingGetByIdRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingPageRequest;
import com.wanmi.sbc.marketing.api.response.market.MarketingPageResponse;
import com.wanmi.sbc.marketing.bean.constant.MarketingErrorCode;
import com.wanmi.sbc.marketing.bean.dto.MarketingPageDTO;
import com.wanmi.sbc.marketing.bean.vo.MarketingForEndVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingPageVO;
import com.wanmi.sbc.marketing.request.MarketingPageListRequest;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Api(tags = "MarketingBuyoutPriceController", description = "一口价营销服务API")
@RestController
@RequestMapping("/marketing/buyout_price")
public class MarketingBuyoutPriceController {

    @Autowired
    private MarketingQueryProvider marketingQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private MarketingBuyoutPriceQueryProvider marketingBuyoutPriceQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private StoreLevelQueryProvider storeLevelQueryProvider;


    /**
     * 获取营销活动列表
     *
     * @param marketingPageListRequest {@link MarketingPageRequest}
     * @return
     */
    @ApiOperation(value = "获取营销活动列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<MarketingPageVO>> getMarketingList(@RequestBody MarketingPageListRequest marketingPageListRequest) {

        MarketingPageRequest marketingPageRequest = new MarketingPageRequest();
        marketingPageRequest.setStoreId(commonUtil.getStoreId());
        marketingPageRequest.setMarketingPageDTO(KsBeanUtil.convert(marketingPageListRequest, MarketingPageDTO.class));
        //判断是否需要展示营销信息和店铺名称
        if (marketingPageListRequest.getRules() != null) {
            marketingPageRequest.setRules(marketingPageListRequest.getRules());
            marketingPageRequest.getMarketingPageDTO().setShowStoreNameFlag(Boolean.TRUE);

        }
        BaseResponse<MarketingPageResponse> pageResponse = marketingQueryProvider.page(marketingPageRequest);
        return BaseResponse.success(pageResponse.getContext().getMarketingVOS());
    }

    /**
     * 搜索营销活动
     *
     * @param marketingPageListRequest {@link MarketingPageRequest}
     * @return
     */
    @ApiOperation(value = "搜索营销活动")
    @RequestMapping(value = "/searchMarketing", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<MarketingPageVO>> searchMarketingList(@RequestBody MarketingBuyoutPriceSearchRequest marketingPageListRequest) {
        marketingPageListRequest.setStoreId(commonUtil.getStoreId());
        marketingPageListRequest.setPlatform(Platform.BOSS);
        BaseResponse<MarketingPageResponse> pageResponse =
                marketingBuyoutPriceQueryProvider.search(marketingPageListRequest);
        return BaseResponse.success(pageResponse.getContext().getMarketingVOS());
    }

    /**
     * 根据营销Id获取营销详细信息
     *
     * @param marketingId
     * @return
     */
    @ApiOperation(value = "根据营销Id获取营销详细信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "marketingId", value = "营销Id", required = true)
    @RequestMapping(value = "/{marketingId}", method = RequestMethod.GET)
    public BaseResponse<MarketingForEndVO> getMarketingById(@PathVariable("marketingId") Long marketingId) {
        MarketingGetByIdRequest marketingGetByIdRequest = new MarketingGetByIdRequest();
        marketingGetByIdRequest.setMarketingId(marketingId);
        MarketingForEndVO marketingResponse = marketingQueryProvider.getByIdForSupplier(marketingGetByIdRequest)
                .getContext().getMarketingForEndVO();

        if (marketingResponse.getStoreId() != null && commonUtil.getStoreId() != null && commonUtil.getStoreId().longValue() != marketingResponse.getStoreId()) {
            throw new SbcRuntimeException(MarketingErrorCode.MARKETING_NO_AUTH_TO_VIEW);
        }

        // 根据营销活动的店铺id查询店铺信息
        StoreByIdResponse storeByIdResponse =
                storeQueryProvider.getById(StoreByIdRequest.builder().storeId(marketingResponse.getStoreId()).build()).getContext();
        if (Objects.nonNull(storeByIdResponse) && Objects.nonNull(storeByIdResponse.getStoreVO())) {

            BoolFlag companyType = storeByIdResponse.getStoreVO().getCompanyType();
            List<Long> levels = marketingResponse.getJoinLevelList();
            String levelName = "";
            if (BoolFlag.NO.equals(companyType)) {
                //平台客户等级
                List<CustomerLevelVO> customerLevelVOList =
                        customerLevelQueryProvider.listAllCustomerLevel().getContext().getCustomerLevelVOList();
                //平台
                if (CollectionUtils.isNotEmpty(customerLevelVOList) && CollectionUtils.isNotEmpty(levels)) {
                    levelName = levels.stream().flatMap(level -> customerLevelVOList.stream()
                            .filter(customerLevelVO -> level.equals(customerLevelVO.getCustomerLevelId()))
                            .map(v -> v.getCustomerLevelName())).collect(Collectors.joining(","));
                }
            } else {
                //商家
                StoreLevelListRequest storeLevelListRequest =
                        StoreLevelListRequest.builder().storeId(marketingResponse.getStoreId()).build();
                List<StoreLevelVO> storeLevelVOList = storeLevelQueryProvider
                        .listAllStoreLevelByStoreId(storeLevelListRequest)
                        .getContext().getStoreLevelVOList();
                if (CollectionUtils.isNotEmpty(storeLevelVOList) && CollectionUtils.isNotEmpty(levels)) {
                    levelName = levels.stream().flatMap(level -> storeLevelVOList.stream()
                            .filter(storeLevelVO -> level.equals(storeLevelVO.getStoreLevelId()))
                            .map(v -> v.getLevelName())).collect(Collectors.joining(","));
                }
            }
            marketingResponse.setJoinLevelName(levelName);
        }

        return BaseResponse.success(marketingResponse);
    }
}
