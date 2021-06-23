package com.wanmi.sbc.marketing;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StoreBaseInfoByIdRequest;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoSiteQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoDetailByGoodsInfoResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.provider.marketingsuits.MarketingSuitsQueryProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.market.MarketingGetByIdRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingSuitsBySkuIdRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsDetailFilterRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.response.info.GoodsInfoListByGoodsInfoResponse;
import com.wanmi.sbc.marketing.api.response.market.MarketingMoreSuitsGoodInfoIdResponse;
import com.wanmi.sbc.marketing.bean.dto.GoodsInfoDetailByGoodsInfoDTO;
import com.wanmi.sbc.marketing.bean.vo.MarketingForEndVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Api(tags = "MarketingController", description = "营销服务 API")
@RestController
@RequestMapping("/marketing")
@Validated
public class MarketingController {

    @Autowired
    private MarketingQueryProvider marketingQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private MarketingSuitsQueryProvider marketingSuitsQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;


    /**
     * 根据营销Id获取营销详细信息
     * @param marketingId
     * @return
     */
    @ApiOperation(value = "根据营销Id获取营销详细信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "marketingId", value = "营销Id", required = true)
    @RequestMapping(value = "/{marketingId}", method = RequestMethod.GET)
    public BaseResponse<MarketingForEndVO> getMarketingById(@PathVariable("marketingId")Long marketingId){
        MarketingGetByIdRequest marketingGetByIdRequest = new MarketingGetByIdRequest();
        marketingGetByIdRequest.setMarketingId(marketingId);
        MarketingForEndVO response = marketingQueryProvider.getByIdForCustomer(marketingGetByIdRequest).getContext().getMarketingForEndVO();
        // 校验店铺是否过期
        storeQueryProvider.getStoreBaseInfoById(new StoreBaseInfoByIdRequest(response.getStoreId()));
        return BaseResponse.success(response);
    }


    /**
     *  移动端-商品详情-组合商品-活动列表
     */
    @ApiOperation(value = "查看活动列表")
    @RequestMapping(value = "/getMoreSuitsInfo", method = RequestMethod.POST)
    public BaseResponse<MarketingMoreSuitsGoodInfoIdResponse> getMoreSuitsInfo(@RequestBody @Valid MarketingSuitsBySkuIdRequest request){
//        request.setBaseStoreId(commonUtil.getStoreIdWithDefault());
        GoodsInfoByIdResponse infoByIdResponse = goodsInfoQueryProvider.getById(GoodsInfoByIdRequest.builder().goodsInfoId(request.getGoodsInfoId()).build()).getContext();
        request.setStoreId(infoByIdResponse.getStoreId());
        MarketingMoreSuitsGoodInfoIdResponse response = marketingSuitsQueryProvider.getMarketingBySuitsSkuId(request).getContext();
        return BaseResponse.success(response);
    }

    /**
     *  移动端-商品详情-组合商品-活动列表(登录状态)
     */
    @ApiOperation(value = "查看活动列表")
    @RequestMapping(value = "/login/getMoreSuitsInfo", method = RequestMethod.POST)
    public BaseResponse<MarketingMoreSuitsGoodInfoIdResponse> getMoreSuitsInfoForLogin(@RequestBody @Valid MarketingSuitsBySkuIdRequest request){
        request.setUserId(commonUtil.getOperatorId());
        if(Objects.isNull(request.getStoreId())){
            GoodsInfoByIdResponse infoByIdResponse = goodsInfoQueryProvider.getById(GoodsInfoByIdRequest.builder().goodsInfoId(request.getGoodsInfoId()).build()).getContext();
            request.setStoreId(infoByIdResponse.getStoreId());
        }
        MarketingMoreSuitsGoodInfoIdResponse response = marketingSuitsQueryProvider.getMarketingBySuitsSkuId(request).getContext();
        return BaseResponse.success(response);
    }
}
