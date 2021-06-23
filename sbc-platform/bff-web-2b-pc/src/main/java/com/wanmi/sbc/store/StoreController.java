package com.wanmi.sbc.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StorePageRequest;
import com.wanmi.sbc.customer.api.response.store.StoreBaseInfoResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
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
import com.wanmi.sbc.setting.api.provider.platformaddress.PlatformAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressListRequest;
import com.wanmi.sbc.setting.bean.vo.PlatformAddressVO;
import com.wanmi.sbc.store.response.StoreResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 店铺相关接口
 * Created by daiyitian on 2017/4/21.
 */
@Api(tags = "StoreController", description = "店铺API")
@RestController
public class StoreController {

    @Autowired
//    private StoreService storeService;
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsIntervalPriceService goodsIntervalPriceService;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Autowired
    private PlatformAddressQueryProvider platformAddressQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 店铺列表
     *
     * @param queryRequest 搜索条件
     * @return 返回分页结果
     */
    @ApiOperation(value = "店铺列表")
    @RequestMapping(value = "/stores", method = RequestMethod.POST)
    public BaseResponse<StoreResponse> list(@RequestBody StorePageRequest queryRequest) {
        CustomerVO customer = commonUtil.getCustomer();
        StoreResponse response = new StoreResponse();
        queryRequest.setAuditState(CheckState.CHECKED);
        queryRequest.setStoreState(StoreState.OPENING);
        queryRequest.setGteContractStartDate(LocalDateTime.now());
        queryRequest.setLteContractEndDate(LocalDateTime.now());
        queryRequest.setStoreType(StoreType.SUPPLIER);
//        Page<Store> page = storeService.page(queryRequest);
        MicroServicePage<StoreVO> page = storeQueryProvider.page(queryRequest).getContext().getStoreVOPage();
        if(CollectionUtils.isEmpty(page.getContent())){
            return BaseResponse.success(response);
        }

        GoodsInfoViewPageRequest infoQueryRequest = new GoodsInfoViewPageRequest();
        infoQueryRequest.setPageSize(5);
        infoQueryRequest.setAuditStatus(CheckStatus.CHECKED);
        infoQueryRequest.setAddedFlag(AddedFlag.YES.toValue());//上架
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());//可用
        infoQueryRequest.putSort("addedTime", SortType.DESC.toValue());
        infoQueryRequest.putSort("goodsInfoId", SortType.ASC.toValue());

        List<StoreBaseInfoResponse> storeList = new ArrayList<>();
        getStores(response, page, infoQueryRequest, storeList);

        response.setStorePage(new PageImpl<>(storeList, queryRequest.getPageable(), page.getTotalElements()));
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



    /**
     * 店铺列表(未登录)
     *
     * @param queryRequest 搜索条件
     * @return 返回分页结果
     */
    @ApiOperation(value = "店铺列表(未登录)")
    @RequestMapping(value = "/nonLoginStores", method = RequestMethod.POST)
    public BaseResponse<StoreResponse> nonLoginList(@RequestBody StorePageRequest queryRequest) {
        CustomerVO customer = null;
        StoreResponse response = new StoreResponse();
        queryRequest.setAuditState(CheckState.CHECKED);
        queryRequest.setStoreState(StoreState.OPENING);
        queryRequest.setGteContractStartDate(LocalDateTime.now());
        queryRequest.setLteContractEndDate(LocalDateTime.now());
        MicroServicePage<StoreVO> page = storeQueryProvider.page(queryRequest).getContext().getStoreVOPage();
        if(CollectionUtils.isEmpty(page.getContent())){
            return BaseResponse.success(response);
        }

        GoodsInfoViewPageRequest infoQueryRequest = new GoodsInfoViewPageRequest();
        infoQueryRequest.setPageSize(5);
        infoQueryRequest.setAuditStatus(CheckStatus.CHECKED);
        infoQueryRequest.setAddedFlag(AddedFlag.YES.toValue());//上架
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());//可用
        infoQueryRequest.putSort("addedTime", SortType.DESC.toValue());
        infoQueryRequest.putSort("goodsInfoId", SortType.ASC.toValue());

        List<StoreBaseInfoResponse> storeList = new ArrayList<>();
        getStores(response, page, infoQueryRequest, storeList);

        response.setStorePage(new PageImpl<>(storeList, queryRequest.getPageable(), page.getTotalElements()));
        //计算区间价
        GoodsIntervalPriceByCustomerIdResponse priceResponse =
                goodsIntervalPriceService.getGoodsIntervalPriceVOList(response.getGoodsInfoList(), null);
        response.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
        response.setGoodsInfoList(priceResponse.getGoodsInfoVOList());
        //计算营销价格
        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(response.getGoodsInfoList(), GoodsInfoDTO.class));
        filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
        response.setGoodsInfoList(marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList());

        return BaseResponse.success(response);
    }


    private void getStores(StoreResponse response, Page<StoreVO> page, GoodsInfoViewPageRequest infoQueryRequest, List<StoreBaseInfoResponse> storeList) {
        for (StoreVO store : page.getContent()) {
            StoreBaseInfoResponse baseInfoResponse = new StoreBaseInfoResponse().convertFromEntity(store);
            infoQueryRequest.setStoreId(baseInfoResponse.getStoreId());
//            List<GoodsInfo> goodsInfoList = goodsInfoService.page(infoQueryRequest).getGoodsInfoPage().getContent();
            List<GoodsInfoVO> goodsInfoList = goodsInfoQueryProvider.pageView(infoQueryRequest).getContext().getGoodsInfoPage
                    ().getContent();
            if(CollectionUtils.isNotEmpty(goodsInfoList)){
                baseInfoResponse.setSkuIds(goodsInfoList.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList()));
                response.getGoodsInfoList().addAll(goodsInfoList);
            }
            storeList.add(baseInfoResponse);
        }
        //填充省市区名称
        this.fillArea(storeList);
    }

    private void fillArea(List<StoreBaseInfoResponse> storeList){
        if(CollectionUtils.isNotEmpty(storeList)){
            List<String> addrIds = new ArrayList<>();
            storeList.forEach(detail -> {
                addrIds.add(Objects.toString(detail.getProvinceId()));
                addrIds.add(Objects.toString(detail.getCityId()));
                addrIds.add(Objects.toString(detail.getAreaId()));
            });
            List<PlatformAddressVO> voList = platformAddressQueryProvider.list(PlatformAddressListRequest.builder().addrIdList(addrIds).build()).getContext().getPlatformAddressVOList();
            if(CollectionUtils.isNotEmpty(voList)){
                Map<String, String> addrMap = voList.stream().collect(Collectors.toMap(PlatformAddressVO::getAddrId, PlatformAddressVO::getAddrName));
                storeList.forEach(detail -> {
                    detail.setProvinceName(addrMap.get(Objects.toString(detail.getProvinceId())));
                    detail.setCityName(addrMap.get(Objects.toString(detail.getCityId())));
                    detail.setAreaName(addrMap.get(Objects.toString(detail.getAreaId())));
                });
            }
        }
    }
}
