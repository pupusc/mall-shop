package com.wanmi.sbc.goods.price.service;

import com.wanmi.sbc.goods.bean.enums.PriceType;
import com.wanmi.sbc.goods.price.model.root.GoodsLevelPrice;
import com.wanmi.sbc.goods.price.repository.GoodsLevelPriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品级别价格接口
 * Created by dyt on 2017/4/17.
 */
@Service
@Slf4j
public class GoodsLevelPriceService {

    @Autowired
    private GoodsLevelPriceRepository goodsLevelPriceRepository;

    /**
     * 根据批量商品ID和批量等级查询SKU的级别价
     * @param skuIds 商品ID
     * @param levelIds 会员等级ID
     * @return
     */
    public  List<GoodsLevelPrice> getSkuLevelPriceByGoodsInfoIdAndLevelIds(List<String> skuIds, List<Long> levelIds){
        return  goodsLevelPriceRepository.findSkuByGoodsInfoIdAndLevelIds(skuIds, levelIds);
    }


    /**
     * 根据商品SkuID查询SKU的级别价
     * @param goodsInfoIds 多商品SkuID
     * @return
     */
    public List<GoodsLevelPrice> findSkuByGoodsInfoIds(List<String> goodsInfoIds,  PriceType type){
        return  goodsLevelPriceRepository.findSkuByGoodsInfoIds(goodsInfoIds, type);
    }
}
