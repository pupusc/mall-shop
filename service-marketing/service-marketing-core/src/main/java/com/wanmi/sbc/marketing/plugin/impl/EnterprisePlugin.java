package com.wanmi.sbc.marketing.plugin.impl;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.api.provider.enterprise.EnterpriseGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterprisePriceGetRequest;
import com.wanmi.sbc.goods.api.response.enterprise.EnterprisePriceResponse;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import com.wanmi.sbc.marketing.plugin.IGoodsListPlugin;
import com.wanmi.sbc.marketing.request.MarketingPluginRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 企业购插件
 * Created by dyt on 2016/12/8.
 */
@Repository("enterPrisePlugin")
public class EnterprisePlugin implements IGoodsListPlugin {

    @Autowired
    private EnterpriseGoodsInfoQueryProvider enterpriseGoodsInfoQueryProvider;

    /**
     * 商品列表处理
     *
     * @param goodsInfos 商品数据
     * @param request    参数
     */
    @Override
    public void goodsListFilter(List<GoodsInfoVO> goodsInfos, MarketingPluginRequest request) {
        // 企业购商品状态确认 社交分销优先级>企业购
        goodsInfos.forEach(goodsInfoVO -> {
                    if (Objects.equals(goodsInfoVO.getDistributionGoodsAudit(), DistributionGoodsAudit.CHECKED)) {
                        // 分销商品去除企业购标记
                        goodsInfoVO.setEnterPriseAuditState(EnterpriseAuditState.INIT);
                        goodsInfoVO.setEnterPriseGoodsAuditReason(null);
                        goodsInfoVO.setEnterPrisePrice(null);
                    }
                }
        );

        List<String> goodsInfoIds = new ArrayList<>();
        Map<String, Long> buyCountMap = new HashMap<>();
        goodsInfos.forEach(e -> {
            if (e.getEnterPriseAuditState() != EnterpriseAuditState.CHECKED) {
                return;
            }
            goodsInfoIds.add(e.getGoodsInfoId());
            buyCountMap.put(e.getGoodsInfoId(), 1L);
        });
        if (goodsInfoIds.isEmpty()){
            return;
        }
        String customerId = null;
        CustomerVO customer = request.getCustomer();
        if (customer != null && StringUtils.isNotBlank(customer.getCustomerId())) {
            customerId = customer.getCustomerId();
        }
        //企业价
        EnterprisePriceGetRequest enterprisePriceGetRequest = new EnterprisePriceGetRequest();
        enterprisePriceGetRequest.setGoodsInfoIds(goodsInfoIds);
        enterprisePriceGetRequest.setCustomerId(customerId);
        enterprisePriceGetRequest.setBuyCountMap(buyCountMap);
        enterprisePriceGetRequest.setListFlag(true);
        enterprisePriceGetRequest.setOrderFlag(false);

        EnterprisePriceResponse enterprisePriceResponse = enterpriseGoodsInfoQueryProvider.userPrice(enterprisePriceGetRequest).getContext();

        Map<String, BigDecimal> priceMap = enterprisePriceResponse.getPriceMap();
        Map<String, BigDecimal> minMap = enterprisePriceResponse.getMinMap();
        Map<String, BigDecimal> maxMap = enterprisePriceResponse.getMaxMap();
        goodsInfos.forEach(e -> {
            if (e.getEnterPriseAuditState() != EnterpriseAuditState.CHECKED) {
                return;
            }
            e.setEnterpriseMinPrice(e.getEnterPrisePrice());
            e.setEnterpriseMaxPrice(e.getEnterPrisePrice());
            BigDecimal price = priceMap.get(e.getGoodsInfoId());
            if (price != null) {
                e.setEnterPrisePrice(price);
            }
            BigDecimal minPrice = minMap.get(e.getGoodsInfoId());
            if (minPrice != null) {
                e.setEnterpriseMinPrice(minPrice);
            }
            BigDecimal maxPrice = maxMap.get(e.getGoodsInfoId());
            if (maxPrice != null) {
                e.setEnterpriseMaxPrice(maxPrice);
            }
            List<GoodsIntervalPriceVO> intervalPriceList = e.getIntervalPriceList();
            if (intervalPriceList == null) {
                intervalPriceList = new ArrayList<>();
            }
            List<GoodsIntervalPriceVO> list = enterprisePriceResponse.getIntervalMap().get(e.getGoodsInfoId());
            if (list != null && !list.isEmpty()) {
                intervalPriceList.addAll(list);
                List<Long> collect = intervalPriceList.stream().map(GoodsIntervalPriceVO::getIntervalPriceId).collect(Collectors.toList());
                e.setIntervalPriceIds(collect);
                e.setIntervalPriceList(intervalPriceList);
            }
        });
    }

}