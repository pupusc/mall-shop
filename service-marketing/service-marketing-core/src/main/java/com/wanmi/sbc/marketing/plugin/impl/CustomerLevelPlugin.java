package com.wanmi.sbc.marketing.plugin.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DistributionCommissionUtils;
import com.wanmi.sbc.customer.api.provider.distribution.DistributorLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributorLevelByCustomerIdRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributorLevelByCustomerIdResponse;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import com.wanmi.sbc.goods.api.provider.price.GoodsCustomerPriceQueryProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsLevelPriceQueryProvider;
import com.wanmi.sbc.goods.api.request.price.GoodsCustomerPriceBySkuIdsAndCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsLevelPriceBySkuIdsAndLevelIdsRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoDetailByGoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsCustomerPriceBySkuIdsAndCustomerIdResponse;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.GoodsPriceType;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsCustomerPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsLevelPriceVO;
import com.wanmi.sbc.marketing.plugin.IGoodsDetailPlugin;
import com.wanmi.sbc.marketing.plugin.IGoodsListPlugin;
import com.wanmi.sbc.marketing.request.MarketingPluginRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 会员等级插件
 * Created by dyt on 2016/12/8.
 */
@Repository("customerLevelPlugin")
public class CustomerLevelPlugin implements IGoodsListPlugin, IGoodsDetailPlugin{


    @Autowired
    private GoodsLevelPriceQueryProvider goodsLevelPriceQueryProvider;

    @Autowired
    private GoodsCustomerPriceQueryProvider goodsCustomerPriceQueryProvider;

    @Autowired
    private DistributorLevelQueryProvider distributorLevelQueryProvider;

    /**
     * 商品列表处理
     * @param goodsInfos 商品数据
     * @param request 参数
     */
    @Override
    public void goodsListFilter(List<GoodsInfoVO> goodsInfos, MarketingPluginRequest request) {
        if (Objects.isNull(request.getCustomer())) {
            return;
        }
        goodsInfos = calDistributionCommission(goodsInfos,request.getCustomer().getCustomerId());
        List<GoodsInfoVO> goodsInfoList = goodsInfos.stream().filter(goodsInfo -> Integer.valueOf(GoodsPriceType
                .CUSTOMER.toValue()).equals(goodsInfo.getPriceType())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(goodsInfoList)){
            return;
        }

        this.setLevelPrice(goodsInfoList, request);
    }

    /**
     * 商品详情处理
     * @param detailResponse 商品详情数据
     * @param request 参数
     */
    @Override
    public void goodsDetailFilter(GoodsInfoDetailByGoodsInfoResponse detailResponse, MarketingPluginRequest request) {
        if (Objects.isNull(request.getCustomer()) || (!Integer.valueOf(GoodsPriceType.CUSTOMER.toValue()).equals(detailResponse.getGoodsInfo().getPriceType()))) {
            return;
        }
        List<GoodsInfoVO> goodsInfoVOList = new ArrayList<>(Arrays.asList(detailResponse.getGoodsInfo()));
        goodsInfoVOList = calDistributionCommission(goodsInfoVOList,request.getCustomer().getCustomerId());
        this.setLevelPrice(goodsInfoVOList, request);
    }



    /**
     * 公共私有方法-处理等级价/客户价
     * @param goodsInfoList 商品列表
     * @param request 插件参数
     */
    private void setLevelPrice(List<GoodsInfoVO> goodsInfoList, MarketingPluginRequest request){

        Map<Long, CommonLevelVO> levelMap = request.getLevelMap();
        List<Long> levels = new ArrayList<>();
        //设置默认会员折扣
        goodsInfoList.stream()
                .forEach(goodsInfo -> {
                    CommonLevelVO customerLevel = levelMap.get(goodsInfo.getStoreId());
                    if (customerLevel != null && customerLevel.getLevelDiscount() != null) {
                        BigDecimal finalDiscount = customerLevel.getLevelDiscount();
                        goodsInfo.setSalePrice(goodsInfo.getSalePrice().multiply(finalDiscount).setScale(2, BigDecimal.ROUND_HALF_UP));
                        levels.add(customerLevel.getLevelId());
                    } else {
                        levels.add(Constants.GOODS_PLATFORM_LEVEL_ID);
                    }
                });

        //设置级别价
        List<String> skuIds = goodsInfoList.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(skuIds) && CollectionUtils.isNotEmpty(levels)) {
            List<GoodsLevelPriceVO> levelPrices = goodsLevelPriceQueryProvider.listBySkuIdsAndLevelIds(new GoodsLevelPriceBySkuIdsAndLevelIdsRequest(skuIds,levels)).getContext().getGoodsLevelPriceList();
            if (CollectionUtils.isNotEmpty(levelPrices)) {
                goodsInfoList.stream()
                        .forEach(goodsInfo ->{
                            CommonLevelVO customerLevel = levelMap.get(goodsInfo.getStoreId());
                            //提取符合等级取同一个SkuId的进行设定
                            levelPrices.stream()
                                    .filter(levelPrice -> {
                                        if(customerLevel != null){
                                            return levelPrice.getLevelId().equals(customerLevel.getLevelId()) && levelPrice.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId());
                                        }
                                        return levelPrice.getLevelId().equals(Constants.GOODS_PLATFORM_LEVEL_ID) && levelPrice.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId());
                                    })
                                    .findFirst().ifPresent(levelPrice -> {
                                        if(levelPrice.getPrice() != null) {
                                            goodsInfo.setSalePrice(levelPrice.getPrice());
                                        }
                                        //无货或库存低于起订量
                                        if (goodsInfo.getStock() < 1 || (goodsInfo.getCount() != null && goodsInfo.getCount() > goodsInfo.getStock())) {
                                            goodsInfo.setValidFlag(Constants.no);//无效
                                            if(GoodsStatus.OK.equals(goodsInfo.getGoodsStatus())) {
                                                goodsInfo.setGoodsStatus(GoodsStatus.OUT_STOCK);
                                            }
                                        }
                                    });
                        });
            }
        }

        //设置客户指定价
        skuIds = goodsInfoList.stream()
                .filter(goodsInfo -> Constants.yes.equals(goodsInfo.getCustomFlag()))
                .map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(skuIds)) {
            Map<String, GoodsCustomerPriceVO> customerPriceMap = null;
            GoodsCustomerPriceBySkuIdsAndCustomerIdResponse goodsCustomerPriceBySkuIdsAndCustomerIdResponse = goodsCustomerPriceQueryProvider
                    .listBySkuIdsAndCustomerId(new GoodsCustomerPriceBySkuIdsAndCustomerIdRequest(skuIds, request.getCustomer().getCustomerId())).getContext();
            if (Objects.nonNull(goodsCustomerPriceBySkuIdsAndCustomerIdResponse)){
                customerPriceMap = goodsCustomerPriceBySkuIdsAndCustomerIdResponse.getGoodsCustomerPriceVOList().stream()
                        .collect(Collectors.toMap(GoodsCustomerPriceVO::getGoodsInfoId, c -> c));
            }
            if (MapUtils.isNotEmpty(customerPriceMap)) {
                final Map<String, GoodsCustomerPriceVO> goodsCustomerPriceVOMap = customerPriceMap;
                goodsInfoList.stream()
                        .filter(goodsInfo -> goodsCustomerPriceVOMap.containsKey(goodsInfo.getGoodsInfoId()))
                        .forEach(goodsInfo -> {
                            GoodsCustomerPriceVO customerPrice = goodsCustomerPriceVOMap.get(goodsInfo.getGoodsInfoId());
                            if(customerPrice.getPrice() != null) {
                                goodsInfo.setSalePrice(customerPrice.getPrice());
                            }

                            //无货或库存低于起订量
                            if(goodsInfo.getStock() < 1 || (goodsInfo.getCount() != null && goodsInfo.getCount() > goodsInfo.getStock())){
                                goodsInfo.setValidFlag(Constants.no);//无效
                                if(GoodsStatus.OK.equals(goodsInfo.getGoodsStatus())) {
                                    goodsInfo.setGoodsStatus(GoodsStatus.OUT_STOCK);
                                }
                            }


                        });
            }
        }
    }


    /**
     * 计算分销佣金-根据分销员等级佣金比例、分销商品佣金
     * @param goodsInfoVOList
     * @param customerId
     * @return
     */
    private List<GoodsInfoVO> calDistributionCommission(List<GoodsInfoVO> goodsInfoVOList,String customerId){
        if (StringUtils.isBlank(customerId)){
            return goodsInfoVOList;
        }
        BaseResponse<DistributorLevelByCustomerIdResponse> baseResponse = distributorLevelQueryProvider.getByCustomerId(new DistributorLevelByCustomerIdRequest(customerId));
        goodsInfoVOList.stream().forEach(goodsInfoVO -> {
            DistributorLevelVO distributorLevelVO =  baseResponse.getContext().getDistributorLevelVO();
            if (Objects.nonNull(distributorLevelVO)&& Objects.nonNull(goodsInfoVO.getDistributionCommission())&& Objects.nonNull(distributorLevelVO.getCommissionRate()) && Objects.nonNull(distributorLevelVO.getCommissionRate()) && DistributionGoodsAudit.CHECKED == goodsInfoVO.getDistributionGoodsAudit()){
                BigDecimal commissionRate = distributorLevelVO.getCommissionRate();
                BigDecimal distributionCommission = goodsInfoVO.getDistributionCommission();
                distributionCommission = DistributionCommissionUtils.calDistributionCommission(distributionCommission,commissionRate);
                goodsInfoVO.setDistributionCommission(distributionCommission);
            }
        });
        return goodsInfoVOList;
    }

}