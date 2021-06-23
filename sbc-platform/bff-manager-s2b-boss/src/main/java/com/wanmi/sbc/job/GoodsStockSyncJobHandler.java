package com.wanmi.sbc.job;

import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsStockProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSkuStockSubRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSpuStockSubRequest;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoMinusStockDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoPlusStockDTO;
import com.wanmi.sbc.redis.RedisHIncrBean;
import com.wanmi.sbc.redis.RedisService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 定时任务Handler
 * 商品GOODS表将redis库存扣除stock
 *
 * @author dyt
 */
@JobHandler(value = "goodsStockSyncJobHandler")
@Component
@Slf4j
public class GoodsStockSyncJobHandler extends IJobHandler {

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private EsGoodsStockProvider esGoodsStockProvider;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        // 每10分钟执行 0 0/10 * * * ?
        //更新ES库存
        Map<String, String> res = redisService.hgetAll(CacheKeyConstant.GOODS_STOCK_SUB_CACHE);
        log.info("更新Spu的Es库存");
        if (MapUtils.isNotEmpty(res)) {
            res.forEach((goodsId, stock) -> {
                log.info("更新spuEs库存，扣除{} -> spuId:{}", stock, goodsId);
                Long tmpStock = NumberUtils.toLong(stock, 0);
                if (tmpStock != 0) {
                    try {
                        //持久至es
                        esGoodsStockProvider.subStockBySpuId(EsGoodsSpuStockSubRequest.builder().
                                spuId(goodsId).stock(tmpStock).build());
                    } catch (Exception e) {
                        log.error("更新Spu的Es库存异常", e);
                    }
                }
            });
            goodsProvider.syncStock();
        }

        Map<String, String> skuRes = redisService.hgetAll(CacheKeyConstant.GOODS_STOCK_SUB_CACHE_SKU);
        log.info("更新Sku的Es库存");
        if (MapUtils.isNotEmpty(skuRes)) {
            List<GoodsInfoMinusStockDTO> errStock = new ArrayList<>();//保存更新异常的数据
            List<GoodsInfoPlusStockDTO> resetStock = new ArrayList<>();//重置
            skuRes.forEach((skuId, stock) -> {
                log.info("新Sku的Es库存，扣除{} -> skuId:{}", stock, skuId);
                Long tmpStock = NumberUtils.toLong(stock, 0);
                if (tmpStock != 0) {
                    try {
                        //持久至es
                        esGoodsStockProvider.subStockBySkuId(EsGoodsSkuStockSubRequest.builder().
                                skuId(skuId).stock(tmpStock).build());
                        resetStock.add(new GoodsInfoPlusStockDTO(tmpStock, skuId));
                    } catch (Exception e) {
                        log.error("更新Sku的Es库存异常", e);
                        errStock.add(new GoodsInfoMinusStockDTO(tmpStock, skuId));
                    }
                }
            });
            LocalTime nowTime = LocalTime.now();
            if (nowTime.getHour() > 0 && nowTime.getHour() < 8) {
                //如果在夜间，全清
                redisService.delete(CacheKeyConstant.GOODS_STOCK_SUB_CACHE_SKU);
                //保留错误数据，以便下次同步继续
                batchSubStock(errStock);
            } else {//重置-扣除redis
                batchAddStock(resetStock);
            }
        }
        return SUCCESS;
    }

    /**
     * 批量加sku库存
     *
     * @param dtoList 增量库存参数
     */
    public void batchAddStock(List<GoodsInfoPlusStockDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        //缓存是扣库存性缓存，加库存则扣除
        List<RedisHIncrBean> beans = dtoList.stream().map(g -> new RedisHIncrBean(g.getGoodsInfoId(), -g.getStock()))
                .collect(Collectors.toList());
        redisService.hincrPipeline(CacheKeyConstant.GOODS_STOCK_SUB_CACHE_SKU, beans);
    }

    /**
     * 批量减sku库存
     *
     * @param dtoList 减量库存参数
     */
    public void batchSubStock(List<GoodsInfoMinusStockDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        List<RedisHIncrBean> beans = dtoList.stream().map(g -> new RedisHIncrBean(g.getGoodsInfoId(), g.getStock()))
                .collect(Collectors.toList());
        redisService.hincrPipeline(CacheKeyConstant.GOODS_STOCK_SUB_CACHE_SKU, beans);
    }
}
