package com.wanmi.sbc.order.common;

import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.goods.bean.dto.GoodsMinusStockDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsPlusStockDTO;
import com.wanmi.sbc.order.redis.RedisHIncrBean;
import com.wanmi.sbc.order.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品缓存服务
 * Created by daiyitian on 2017/4/11.
 */
@Slf4j
@Service
public class GoodsStockService {


    @Autowired
    private RedisService redisService;

    /**
     * 批量加库存
     *
     * @param dtoList 增量库存参数
     */
    public void batchAddStock(List<GoodsPlusStockDTO> dtoList) {
        if(CollectionUtils.isEmpty(dtoList)){
            return;
        }
        //缓存是扣库存性缓存，加库存则扣除
        List<RedisHIncrBean> beans = dtoList.stream().map(g -> new RedisHIncrBean(g.getGoodsId(), -g.getStock()))
                .collect(Collectors.toList());
        redisService.hincrPipeline(CacheKeyConstant.GOODS_STOCK_SUB_CACHE, beans);
    }

    /**
     * 批量减库存
     *
     * @param dtoList       减量库存参数
     */
    public void batchSubStock(List<GoodsMinusStockDTO> dtoList) {
        if(CollectionUtils.isEmpty(dtoList)){
            return;
        }
        List<RedisHIncrBean> beans = dtoList.stream().map(g -> new RedisHIncrBean(g.getGoodsId(), g.getStock()))
                .collect(Collectors.toList());
        redisService.hincrPipeline(CacheKeyConstant.GOODS_STOCK_SUB_CACHE, beans);
    }
}
