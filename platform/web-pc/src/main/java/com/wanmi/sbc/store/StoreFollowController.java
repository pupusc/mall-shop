package com.wanmi.sbc.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.follow.StoreCustomerFollowQueryProvider;
import com.wanmi.sbc.customer.api.request.follow.StoreCustomerFollowPageRequest;
import com.wanmi.sbc.customer.api.request.follow.StoreCustomerFollowRequest;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.StoreCustomerFollowVO;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewPageRequest;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.intervalprice.GoodsIntervalPriceService;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.store.response.StoreCustomerFollowResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 店铺关注Controller
 * Created by daiyitian on 17/4/12.
 */
@Api(tags = "StoreFollowController", description = "店铺关注Api")
@RestController
@RequestMapping("/store")
public class StoreFollowController {

    @Autowired
    private StoreCustomerFollowQueryProvider storeCustomerFollowQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Autowired
    private GoodsIntervalPriceService goodsIntervalPriceService;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 获取店铺关注列表
     * @param request 查询条件
     * @return 店铺关注分页
     */
    @ApiOperation(value = "获取店铺关注列表")
    @RequestMapping(value = "/storeFollows", method = RequestMethod.POST)
    public BaseResponse<StoreCustomerFollowResponse> info(@RequestBody StoreCustomerFollowRequest request) {
        CustomerVO customer = commonUtil.getCustomer();
        request.setCustomerId(customer.getCustomerId());
        StoreCustomerFollowResponse response = new StoreCustomerFollowResponse();
        StoreCustomerFollowPageRequest pageRequest = new StoreCustomerFollowPageRequest();
        KsBeanUtil.copyPropertiesThird(request, pageRequest);
        response.setStorePage(storeCustomerFollowQueryProvider.pageStoreCustomerFollow(pageRequest).getContext().getCustomerFollowVOPage());
        List<StoreCustomerFollowVO> storeList = response.getStorePage().getContent();
        if(CollectionUtils.isEmpty(storeList)){
            return BaseResponse.success(response);
        }
        GoodsInfoViewPageRequest infoQueryRequest = new GoodsInfoViewPageRequest();
        infoQueryRequest.setPageSize(5);
        infoQueryRequest.setAuditStatus(CheckStatus.CHECKED);
        infoQueryRequest.setAddedFlag(AddedFlag.YES.toValue());//上架
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());//可用
        infoQueryRequest.putSort("addedTime", SortType.DESC.toValue());
        infoQueryRequest.putSort("goodsInfoId", SortType.ASC.toValue());
        infoQueryRequest.setVendibility(DefaultFlag.YES.toValue());
        storeList.forEach(store -> {

            //TODO 去除敏感信息
            store.setContactEmail(null);
            store.setContactMobile(null);
            store.setContactPerson(null);

            infoQueryRequest.setStoreId(store.getStoreId());
//            List<GoodsInfo> goodsInfoList = goodsInfoService.page(infoQueryRequest).getGoodsInfoPage().getContent();
            List<GoodsInfoVO> goodsInfoList = goodsInfoQueryProvider.pageView(infoQueryRequest).getContext().getGoodsInfoPage().getContent();
            if(CollectionUtils.isNotEmpty(goodsInfoList)){
                store.setSkuIds(goodsInfoList.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList()));
                response.getGoodsInfoList().addAll(goodsInfoList);
            }
        });
        if(CollectionUtils.isEmpty(response.getGoodsInfoList())){
            return BaseResponse.success(response);
        }

        //计算区间价
        GoodsIntervalPriceByCustomerIdResponse priceResponse =
                goodsIntervalPriceService.getGoodsIntervalPriceVOList(response.getGoodsInfoList(), customer.getCustomerId());
        response.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
        response.setGoodsInfoList(priceResponse.getGoodsInfoVOList());
        //计算营销价格
        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(response.getGoodsInfoList(), GoodsInfoDTO.class));
        filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
        response.setGoodsInfoList(marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList());
        return BaseResponse.success(response);
    }
}
