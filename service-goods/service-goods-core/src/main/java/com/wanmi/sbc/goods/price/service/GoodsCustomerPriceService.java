package com.wanmi.sbc.goods.price.service;

import com.wanmi.sbc.goods.price.model.root.GoodsCustomerPrice;
import com.wanmi.sbc.goods.price.repository.GoodsCustomerPriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品客户价格接口
 * Created by dyt on 2017/4/17.
 */
@Service
@Slf4j
public class GoodsCustomerPriceService {

    @Autowired
    private GoodsCustomerPriceRepository goodsCustomerPriceRepository;

    /**
     * 根据商品ID和客户ID批量查询
     * @param skuIds 商品SkuID
     * @param customerId 客户ID
     * @return
     */
    public List<GoodsCustomerPrice> getSkuCustomerPriceByGoodsInfoIdAndCustomerId(List<String> skuIds, String customerId){
        return goodsCustomerPriceRepository.findSkuByGoodsInfoIdAndCustomerId(skuIds, customerId);
    }

    /**
     * 根据商品SkuID查询SKU客户价
     * @param goodsInfoIds 商品SkuID
     * @return
     */
    public List<GoodsCustomerPrice> findSkuByGoodsInfoIds(List<String> goodsInfoIds){
        return goodsCustomerPriceRepository.findSkuByGoodsInfoIds(goodsInfoIds);
    }
}
