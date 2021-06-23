package com.wanmi.sbc.goods.price.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelMapByCustomerIdAndStoreIdsRequest;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelMapGetResponse;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.goods.bean.enums.GoodsPriceType;
import com.wanmi.sbc.goods.bean.enums.PriceType;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.price.model.root.GoodsIntervalPrice;
import com.wanmi.sbc.goods.price.repository.GoodsIntervalPriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品订货区间价格实体
 * Created by dyt on 2017/4/17.
 */
@Service
@Slf4j
public class GoodsIntervalPriceService {


    @Autowired
    private GoodsIntervalPriceRepository goodsIntervalPriceRepository;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    /**
     * 为每个SKU填充区间价范围值intervalMinPrice,intervalMaxPrice,intervalPriceIds，并返回相应区间价结果
     * @param goodsInfoList SKU
     * @param levelMap 等级
     * @return 区间价结果
     */
    public List<GoodsIntervalPrice> putIntervalPrice(List<GoodsInfo> goodsInfoList, Map<Long, CommonLevelVO> levelMap){
        if (CollectionUtils.isEmpty(goodsInfoList)) {
            return new ArrayList<>();
        }

        List<String> skuIds = goodsInfoList.stream()
                .filter(goodsInfo -> Integer.valueOf(GoodsPriceType.STOCK.toValue()).equals(goodsInfo.getPriceType()))
                .map(GoodsInfo::getGoodsInfoId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(skuIds) ) {
            goodsInfoList.forEach(goodsInfo -> goodsInfo.setIntervalPriceIds(new ArrayList<>()));
            return new ArrayList<>();
        }
        List<GoodsIntervalPrice> intervalPrices = goodsIntervalPriceRepository.findSkuByGoodsInfoIds(skuIds);
        if (CollectionUtils.isEmpty(intervalPrices)) {
            return new ArrayList<>();
        }
        List<GoodsIntervalPrice> goodsIntervalPrices = new ArrayList<>();
        KsBeanUtil.copyList(intervalPrices, goodsIntervalPrices);
        intervalPrices.clear();



        goodsInfoList.forEach(goodsInfo -> {
            //设置含会员折扣
            if (MapUtils.isNotEmpty(levelMap) && Constants.yes.equals(goodsInfo.getLevelDiscountFlag())) {
                goodsIntervalPrices.stream()
                        .filter(intervalPrice -> goodsInfo.getGoodsInfoId().equals(intervalPrice.getGoodsInfoId()) && intervalPrice.getPrice() != null)
                        .forEach(intervalPrice -> {
                            CommonLevelVO customerLevel = levelMap.get(goodsInfo.getStoreId());
                            if(customerLevel != null && customerLevel.getLevelDiscount() != null) {
                                intervalPrice.setPrice(intervalPrice.getPrice().multiply(customerLevel.getLevelDiscount()).setScale(2, BigDecimal.ROUND_HALF_UP));
                            }
                        });
            }

            //填充扁平化ids和范围值
            List<BigDecimal> intervalPriceList = goodsIntervalPrices.stream().filter(intervalPrice -> goodsInfo.getGoodsInfoId().equals(intervalPrice.getGoodsInfoId())).map(GoodsIntervalPrice::getPrice).collect(Collectors.toList());
            goodsInfo.setIntervalPriceIds(goodsIntervalPrices.stream().filter(intervalPrice -> goodsInfo.getGoodsInfoId().equals(intervalPrice.getGoodsInfoId())).map(GoodsIntervalPrice::getIntervalPriceId).collect(Collectors.toList()));
            goodsInfo.setIntervalMinPrice(intervalPriceList.stream().filter(Objects::nonNull).min(BigDecimal::compareTo).orElse(null));
            goodsInfo.setIntervalMaxPrice(intervalPriceList.stream().filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(null));
        });

        return goodsIntervalPrices;
    }

    /**
     * 为SPU获取区间价
     * @param goodsList Spu列表
     * @param levelMap 等级
     * @return
     */
    public List<GoodsIntervalPrice> putGoodsIntervalPrice(List<Goods> goodsList, Map<Long, CommonLevelVO> levelMap){
        if (CollectionUtils.isEmpty(goodsList) ) {
            return new ArrayList<>();
        }

        List<String> spuIds = goodsList.stream()
                .filter(spu -> Integer.valueOf(GoodsPriceType.STOCK.toValue()).equals(spu.getPriceType()))
                .map(Goods::getGoodsId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(spuIds) ) {
            return new ArrayList<>();
        }

        List<GoodsIntervalPrice> intervalPrices = goodsIntervalPriceRepository.findByGoodsIds(spuIds);
        if (CollectionUtils.isEmpty(intervalPrices)) {
            return new ArrayList<>();
        }

        List<GoodsIntervalPrice> goodsIntervalPrices = new ArrayList<>();
        KsBeanUtil.copyList(intervalPrices, goodsIntervalPrices);
        intervalPrices.clear();

        goodsList.forEach(spu -> {
            //设置含会员折扣
            if (MapUtils.isNotEmpty(levelMap) && Constants.yes.equals(spu.getLevelDiscountFlag())) {
                goodsIntervalPrices.stream()
                        .filter(intervalPrice -> Objects.nonNull(intervalPrice.getPrice()) && levelMap.containsKey(spu.getStoreId()))
                        .forEach(intervalPrice -> {
                            CommonLevelVO customerLevel = levelMap.get(spu.getStoreId());
                            if(Objects.nonNull(customerLevel.getLevelDiscount())) {
                                intervalPrice.setPrice(intervalPrice.getPrice().multiply(customerLevel.getLevelDiscount()).setScale(2, BigDecimal.ROUND_HALF_UP));
                            }
                        });
            }
        });
        return goodsIntervalPrices;
    }


    /**
     * 根据会员ID和店铺批量获取店铺等级
     * @param customerId 会员
     * @param storeIds 店铺
     * @return
     */
    public Map<Long, CommonLevelVO> getLevelMap(String customerId, List<Long> storeIds){
        Map<Long, CommonLevelVO> levelMap = new HashMap<>();
        if(Objects.nonNull(customerId) && CollectionUtils.isNotEmpty(storeIds)) {
            CustomerLevelMapByCustomerIdAndStoreIdsRequest customerLevelMapByCustomerIdAndStoreIdsRequest = new CustomerLevelMapByCustomerIdAndStoreIdsRequest();
            customerLevelMapByCustomerIdAndStoreIdsRequest.setCustomerId(customerId);
            customerLevelMapByCustomerIdAndStoreIdsRequest.setStoreIds(storeIds);
            BaseResponse<CustomerLevelMapGetResponse> customerLevelMapGetResponseBaseResponse =
                    customerLevelQueryProvider.listCustomerLevelMapByCustomerIdAndIds(customerLevelMapByCustomerIdAndStoreIdsRequest);
            Map<Long, CommonLevelVO> tmpMap = customerLevelMapGetResponseBaseResponse.getContext()
                    .getCommonLevelVOMap();
            levelMap.putAll(tmpMap);
        }
        return levelMap;
    }

    /**
     * 根据goodsInfoIds批量查询区间价列表
     * @param skuIds 商品skuId
     * @return 区间价列表
     */
    public List<GoodsIntervalPrice> findBySkuIds(List<String> skuIds){
        return goodsIntervalPriceRepository.findSkuByGoodsInfoIds(skuIds);
    }
}
