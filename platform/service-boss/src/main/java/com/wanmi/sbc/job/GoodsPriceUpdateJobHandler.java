package com.wanmi.sbc.job;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoAdjustPriceRequest;
import com.wanmi.sbc.feishu.FeiShuNoticeEnum;
import com.wanmi.sbc.feishu.constant.FeiShuMessageConstant;
import com.wanmi.sbc.feishu.service.FeiShuSendMessageService;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsPriceSyncRequest;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoPriceChangeDTO;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import com.wanmi.sbc.redis.RedisService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 查询goodsPriceSync表 更新商品价格和es
 **/
@JobHandler(value = "goodsPriceUpdateJobHandler")
@Component
@Slf4j
public class GoodsPriceUpdateJobHandler extends IJobHandler {

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private FeiShuSendMessageService feiShuSendMessageService;


    //分布式锁名称
    private static final String BATCH_GET_GOODS_PRICE_AND_SYNC_LOCKS = "BATCH_GET_GOODS_PRICE_AND_SYNC_LOCKS";

    @Override
    public ReturnT<String> execute(String params) throws Exception {
        RLock lock = redissonClient.getLock(BATCH_GET_GOODS_PRICE_AND_SYNC_LOCKS);
        if (lock.isLocked()) {
            log.error("定时任务在执行中,下次执行.");
            return null;
        }
        lock.lock();
        log.info("同步商品价格任务执行开始");
        try {
            syncBookuuPrice();
            return SUCCESS;
        } catch (RuntimeException e) {
            log.error("同步库存定时任务,参数错误", e);
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        } finally {
            //释放锁
            lock.unlock();
        }
    }

    /**
     * 同步博库成本价
     */
    private void syncBookuuPrice() {
        GoodsPriceSyncRequest goodsInfoListByIdRequest = new GoodsPriceSyncRequest();
        goodsInfoListByIdRequest.setPageNum(0);
        goodsInfoListByIdRequest.setPageSize(20);

        BaseResponse<MicroServicePage<GoodsInfoPriceChangeDTO>> baseResponse = goodsProvider.syncGoodsPrice(goodsInfoListByIdRequest);
        MicroServicePage<GoodsInfoPriceChangeDTO> result = baseResponse.getContext();
        if (result.getTotal() == 0) {
            log.info("同步博库成本价数量为空");
            return;
        }
        syncEsPrice(result.getContent());
        sendMessage(baseResponse.getContext().getContent());

        for (int pageNum = 1; pageNum < result.getTotalPages(); ++pageNum) {
            log.info("同步博库成本价,共{}条数据,当前第{}页", result.getTotal(), pageNum);
            goodsInfoListByIdRequest.setPageNum(0);
            baseResponse = goodsProvider.syncGoodsPrice(goodsInfoListByIdRequest);
            syncEsPrice(baseResponse.getContext().getContent());
            sendMessage(baseResponse.getContext().getContent());
        }

    }


    private void syncEsPrice(List<GoodsInfoPriceChangeDTO> list) {
        log.info("============Es更新的价格:{}==================", list);
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        EsGoodsInfoAdjustPriceRequest esGoodsInfoAdjustPriceRequest = EsGoodsInfoAdjustPriceRequest.builder().goodsInfoIds(list.stream().map(GoodsInfoPriceChangeDTO::getGoodsInfoId).collect(Collectors.toList())).type(PriceAdjustmentType.MARKET).build();
        esGoodsInfoElasticProvider.adjustPrice(esGoodsInfoAdjustPriceRequest);
        //更新redis商品基本数据
        list.forEach(price -> {
            String goodsDetailInfo = redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + price.getGoodsId());
            if (StringUtils.isNotBlank(goodsDetailInfo)) {
                redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + price.getGoodsId());
            }
        });
    }

    private void sendMessage(List<GoodsInfoPriceChangeDTO> list) {
        log.info("============成本价变动消息发送:{}==================", list);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(change -> {
            //毛利率计算
            BigDecimal oldRate = new BigDecimal(0);
            BigDecimal newRate = new BigDecimal(0);
            if(change.getMarketPrice() != null && change.getMarketPrice().compareTo(new BigDecimal(0)) != 0){
                oldRate = (change.getMarketPrice().subtract(change.getOldPrice())).multiply(new BigDecimal(100)).divide(change.getMarketPrice(),2,RoundingMode.HALF_UP);
                newRate = (change.getMarketPrice().subtract(change.getNewPrice())).multiply(new BigDecimal(100)).divide(change.getMarketPrice(),2,RoundingMode.HALF_UP);
            }
            if (newRate.compareTo(new BigDecimal(FeiShuMessageConstant.FEI_SHU_COST_PRICE_LIMIT)) <0) {
                String content = MessageFormat.format(FeiShuMessageConstant.FEI_SHU_STOCK_NOTIFY, change.getSkuNo(), change.getName(),
                        change.getMarketPrice(), change.getTime() ,change.getOldPrice(), change.getNewPrice(), oldRate, newRate);
                feiShuSendMessageService.sendMessage(content, FeiShuNoticeEnum.COST_PRICE);
            }
        });
    }

}
