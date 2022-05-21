package com.wanmi.sbc.marketing.plugin.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreCustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.MaxDiscountPaidCardRequest;
import com.wanmi.sbc.customer.bean.vo.*;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListCategoryEnum;
import com.wanmi.sbc.goods.api.provider.blacklist.GoodsBlackListProvider;
import com.wanmi.sbc.goods.api.provider.info.VideoChannelSetFilterControllerProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceQueryProvider;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListPageProviderRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsIntervalPriceListBySkuIdsRequest;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoDetailByGoodsInfoResponse;
import com.wanmi.sbc.goods.bean.enums.GoodsPriceType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import com.wanmi.sbc.marketing.plugin.IGoodsDetailPlugin;
import com.wanmi.sbc.marketing.plugin.IGoodsListPlugin;
import com.wanmi.sbc.marketing.request.MarketingPluginRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 付费会员插件
 * Created by dyt on 2016/12/8.
 */
@Slf4j
@Repository("paidCardPlugin")
@RefreshScope
public class PaidCardPlugin implements IGoodsListPlugin, IGoodsDetailPlugin {

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;


    @Autowired
    private StoreCustomerQueryProvider storeCustomerQueryProvider;

    @Autowired
    private GoodsIntervalPriceQueryProvider goodsIntervalPriceQueryProvider;

    @Autowired
    private GoodsBlackListProvider goodsBlackListProvider;

    @Autowired
    private VideoChannelSetFilterControllerProvider videoChannelSetFilterControllerProvider;

//    @Value("${exclude-product:000}")
//    private String excludeProduct;

    /**
     * 获取黑名单
     * @return
     */
    private List<String> listUnVipPriceBlackList () {
        List<String> unVipPriceBlackList = new ArrayList<>();
        //获取黑名单
        GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest = new GoodsBlackListPageProviderRequest();
        goodsBlackListPageProviderRequest.setBusinessCategoryColl(
                Collections.singletonList(GoodsBlackListCategoryEnum.UN_SHOW_VIP_PRICE.getCode()));
        BaseResponse<GoodsBlackListPageProviderResponse> goodsBlackListPageProviderResponseBaseResponse = goodsBlackListProvider.listNoPage(goodsBlackListPageProviderRequest);
        GoodsBlackListPageProviderResponse context = goodsBlackListPageProviderResponseBaseResponse.getContext();
        if (context.getUnVipPriceBlackListModel() != null && !CollectionUtils.isEmpty(context.getUnVipPriceBlackListModel().getGoodsIdList())) {
            unVipPriceBlackList.addAll(context.getUnVipPriceBlackListModel().getGoodsIdList());
        }
        return unVipPriceBlackList;
    }

    /**
     * 商品列表处理
     *
     * @param goodsInfos 商品数据
     * @param request    参数
     */
    @Override
    public void goodsListFilter(List<GoodsInfoVO> goodsInfos, MarketingPluginRequest request) {
        List<String> unVipPriceBlackList = this.listUnVipPriceBlackList();
        log.info("PaidCardPlugin  goodsListFilter param : {}, config : {}", JSONArray.toJSONString(goodsInfos), JSONArray.toJSONString(unVipPriceBlackList));
        if (Objects.isNull(request.getCustomer())) {
            return;
        }
        PaidCardVO paidCardVO = this.obtainPaidCard(request);
        if (Objects.isNull(paidCardVO)) {
            return;
        }

        //是否设置独立字段
        Boolean isIndependent = request.getIsIndependent();

        //按市场设价处理逻辑
        dealMarketType(goodsInfos, paidCardVO, isIndependent, unVipPriceBlackList);
        //按客户等级设价
        dealCustomerType(goodsInfos, paidCardVO, isIndependent, unVipPriceBlackList);
        //按订货量设价
        //  dealBuyNumType(goodsInfos, request, paidCardDiscount,storeId);
    }

    private void dealBuyNumType(List<GoodsInfoVO> goodsInfos, MarketingPluginRequest request, BigDecimal paidCardDiscount, Long storeId) {
        List<GoodsInfoVO> goodsInfoList = goodsInfos.stream().filter(goodsInfo -> Integer.valueOf(GoodsPriceType
                .STOCK.toValue()).equals(goodsInfo.getPriceType())
                && (goodsInfo.getCompanyType().equals(BoolFlag.NO) || (goodsInfo.getCompanyType().equals(BoolFlag.YES) && Objects.nonNull(storeId) && storeId.equals(goodsInfo.getStoreId())))
        ).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(goodsInfoList)) {
            return;
        }

        List<String> goodsIdList = goodsInfoList.stream().map(GoodsInfoVO::getGoodsInfoId).distinct().collect(Collectors.toList());
        GoodsIntervalPriceListBySkuIdsRequest goodsIntervalPriceListBySkuIdsRequest = new GoodsIntervalPriceListBySkuIdsRequest();
        goodsIntervalPriceListBySkuIdsRequest.setSkuIds(goodsIdList);
        List<GoodsIntervalPriceVO> goodsIntervalPriceVOList = goodsIntervalPriceQueryProvider.listByGoodsIds(goodsIntervalPriceListBySkuIdsRequest).getContext().getGoodsIntervalPriceVOList();
        Map<String, List<GoodsIntervalPriceVO>> intervalPriceMap = goodsIntervalPriceVOList.stream().collect(Collectors.groupingBy(GoodsIntervalPriceVO::getGoodsInfoId));
        goodsInfoList.forEach(goodsInfo -> {
                    String goodsInfoId = goodsInfo.getGoodsInfoId();
                    List<GoodsIntervalPriceVO> goodsIntervalPriceVOS = intervalPriceMap.get(goodsInfoId);
                    goodsIntervalPriceVOS.sort(Comparator.comparing(GoodsIntervalPriceVO::getPrice));
                    goodsInfo.setIntervalMinPrice(goodsIntervalPriceVOS.get(0).getPrice().multiply(paidCardDiscount).setScale(2, BigDecimal.ROUND_HALF_UP));
                    goodsInfo.setIntervalMaxPrice(goodsIntervalPriceVOS.get(goodsIntervalPriceVOS.size() - 1).getPrice().multiply(paidCardDiscount).setScale(2, BigDecimal.ROUND_HALF_UP));
                    goodsInfo.setIntervalPriceList(goodsIntervalPriceVOS);
                    goodsInfo.setIntervalPriceIds(goodsIntervalPriceVOS.stream().map(GoodsIntervalPriceVO::getIntervalPriceId).collect(Collectors.toList()));
                });
    }

    private void dealCustomerType(List<GoodsInfoVO> goodsInfos, PaidCardVO paidCardVO, Boolean isIndependent, List<String> excludeIds) {
        //设价方式为客户等级、自营商品、非企业购商品
        List<GoodsInfoVO> goodsInfoList = goodsInfos.stream().filter(goodsInfo -> Integer.valueOf(GoodsPriceType
                .CUSTOMER.toValue()).equals(goodsInfo.getPriceType())
                && (goodsInfo.getCompanyType().equals(BoolFlag.NO))).collect(Collectors.toList());

        List<String> skuIdList = goodsInfos.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
        Map<String, Boolean> goodsId2VideoChannelMap = videoChannelSetFilterControllerProvider.filterGoodsIdHasVideoChannelMap(skuIdList).getContext();

        goodsInfoList.forEach(goodsInfo -> {
            if(excludeIds.contains(goodsInfo.getGoodsId())){
                log.info("PaidCardPlugin goodsListFilter，{}", goodsInfo.getGoodsId());
                return;
            }

            if (goodsId2VideoChannelMap.get(goodsInfo.getGoodsId()) != null && goodsId2VideoChannelMap.get(goodsInfo.getGoodsId())) {
                log.info("PaidCardPlugin goodsListFilter2，{}", goodsInfo.getGoodsId());
                return;
            }

            BigDecimal discountPrice = goodsInfo.getMarketPrice().multiply(paidCardVO.getDiscountRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
            //是否设置单独价格
            if(isIndependent) {
                goodsInfo.setPaidCardPrice(discountPrice);
            } else {
                goodsInfo.setSalePrice(discountPrice);
            }
            goodsInfo.setPaidCardIcon(paidCardVO.getIcon());
        });
    }

    /**
     * 按市场设价处理逻辑
     *
     * @param goodsInfos
     * @param paidCardVO
     */
    private void dealMarketType(List<GoodsInfoVO> goodsInfos, PaidCardVO paidCardVO, Boolean isIndependent, List<String> excludeIds) {
        //设价方式为市场价、自营商品、非企业购商品
        List<GoodsInfoVO> goodsInfoList = goodsInfos.stream().filter(goodsInfo -> Integer.valueOf(GoodsPriceType
                .MARKET.toValue()).equals(goodsInfo.getPriceType())
                && (goodsInfo.getCompanyType().equals(BoolFlag.NO))).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(goodsInfoList)) {
            return;
        }

        /**
         * 视频号价格黑名单
         */
        List<String> skuIdList = goodsInfos.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
        Map<String, Boolean> goodsId2VideoChannelMap = videoChannelSetFilterControllerProvider.filterGoodsIdHasVideoChannelMap(skuIdList).getContext();

        goodsInfoList.forEach(goodsInfo -> {
            if(excludeIds.contains(goodsInfo.getGoodsId())){
                log.info("PaidCardPlugin goodsListFilter，{}", goodsInfo.getGoodsId());
                return;
            }
            if (goodsId2VideoChannelMap.get(goodsInfo.getGoodsId()) != null && goodsId2VideoChannelMap.get(goodsInfo.getGoodsId())) {
                log.info("PaidCardPlugin goodsListFilter2，{}", goodsInfo.getGoodsId());
                return;
            }
            BigDecimal discountPrice = goodsInfo.getMarketPrice().multiply(paidCardVO.getDiscountRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
            if(isIndependent) {
                goodsInfo.setPaidCardPrice(discountPrice);
            } else {
                goodsInfo.setSalePrice(discountPrice);
            }
            goodsInfo.setPaidCardIcon(paidCardVO.getIcon());
        });
    }

    /**
     * 商品详情处理
     *
     * @param detailResponse 商品详情数据
     * @param request        参数
     */
    @Override
    public void goodsDetailFilter(GoodsInfoDetailByGoodsInfoResponse detailResponse, MarketingPluginRequest request) {
        List<String> unVipPriceBlackList = this.listUnVipPriceBlackList();
        log.info("PaidCardPlugin  goodsListFilter param : {}, config : {}", JSONObject.toJSONString(detailResponse), JSONArray.toJSONString(unVipPriceBlackList));
        if (Objects.isNull(request.getCustomer())
                || (!Integer.valueOf(GoodsPriceType.MARKET.toValue()).equals(detailResponse.getGoodsInfo().getPriceType()))) {
            return;
        }
        List<GoodsInfoVO> goodsInfoVOList = new ArrayList<>(Arrays.asList(detailResponse.getGoodsInfo()));
        PaidCardVO paidCardVO = this.obtainPaidCard(request);
        if (Objects.isNull(paidCardVO)) {
            return;
        }

        //按市场设价处理逻辑
        dealMarketType(goodsInfoVOList, paidCardVO, Boolean.TRUE, unVipPriceBlackList);
        //按客户等级设价
        dealCustomerType(goodsInfoVOList, paidCardVO, Boolean.TRUE, unVipPriceBlackList);
        //按订货量设价
       // dealBuyNumType(goodsInfoVOList, request, paidCardDiscount, storeId);
    }


    /**
     * 公共私有方法-处理等级价/客户价
     *
     * @param goodsInfoList 商品列表
     * @param request       插件参数
     */
    private void setPrice(List<GoodsInfoVO> goodsInfoList, MarketingPluginRequest request, BigDecimal paidCardDiscount) {

        //设置默认会员折扣
        goodsInfoList.stream()
                .forEach(goodsInfo -> {
                    goodsInfo.setSalePrice(goodsInfo.getMarketPrice().multiply(paidCardDiscount).setScale(2, BigDecimal.ROUND_HALF_UP));
                });
    }

    private PaidCardVO obtainPaidCard(MarketingPluginRequest request) {
        String customerId = request.getCustomer().getCustomerId();
        List<PaidCardVO> paidCardVOList = paidCardCustomerRelQueryProvider.getMaxDiscountPaidCard(MaxDiscountPaidCardRequest.builder()
                .customerId(customerId)
                .build()).getContext();
        if (CollectionUtils.isNotEmpty(paidCardVOList)) {
            return paidCardVOList.get(0);
        }

        return null;
    }

}