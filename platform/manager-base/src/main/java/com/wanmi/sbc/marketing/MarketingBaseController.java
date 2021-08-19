package com.wanmi.sbc.marketing;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.ListStoreByNameRequest;
import com.wanmi.sbc.customer.bean.vo.StoreSimpleInfo;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.request.market.MarketingPageRequest;
import com.wanmi.sbc.marketing.api.response.market.MarketingPageResponse;
import com.wanmi.sbc.marketing.bean.dto.MarketingPageDTO;
import com.wanmi.sbc.marketing.bean.vo.MarketingPageVO;
import com.wanmi.sbc.marketing.request.MarketingPageListRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Api(tags = "MarketingBaseController", description = "营销基础服务API")
@RestController
@RequestMapping("/marketing")
public class MarketingBaseController {

    @Autowired
    private MarketingQueryProvider marketingQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private CommonUtil commonUtil;


    /**
     * 获取营销活动列表
     * @param marketingPageListRequest {@link MarketingPageRequest}
     * @return
     */
    @ApiOperation(value = "获取营销活动列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<MarketingPageVO>> getMarketingList(@RequestBody MarketingPageListRequest marketingPageListRequest) {
        Long storeId = commonUtil.getStoreId();
        MarketingPageRequest marketingPageRequest = new MarketingPageRequest();
        if(Objects.nonNull(storeId)) {
            marketingPageRequest.setStoreId(storeId);
        } else if(Objects.nonNull(marketingPageListRequest.getStoreId())){
            marketingPageRequest.setStoreId(marketingPageListRequest.getStoreId());
        } else {
            //模糊查询店铺名称
            if(StringUtils.isNotBlank(marketingPageListRequest.getStoreName())){
                List<StoreSimpleInfo> storeList = storeQueryProvider.listByStoreName(ListStoreByNameRequest.builder()
                        .storeName(marketingPageListRequest.getStoreName()).build()).getContext().getStoreSimpleInfos();
                if(CollectionUtils.isEmpty(storeList)){
                    return BaseResponse.success(new MicroServicePage<>(Collections.emptyList(), marketingPageListRequest.getPageable(), 0));
                }
                marketingPageListRequest.setStoreIds(storeList.stream().map(StoreSimpleInfo::getStoreId).collect(Collectors.toList()));
            }
            marketingPageListRequest.setShowStoreNameFlag(Boolean.TRUE);
        }
        marketingPageRequest.setMarketingPageDTO(KsBeanUtil.convert(marketingPageListRequest, MarketingPageDTO.class));
        marketingPageRequest.setRules(marketingPageListRequest.getRules());

        BaseResponse<MarketingPageResponse> pageResponse = marketingQueryProvider.page(marketingPageRequest);
        return BaseResponse.success(pageResponse.getContext().getMarketingVOS());
    }
}
