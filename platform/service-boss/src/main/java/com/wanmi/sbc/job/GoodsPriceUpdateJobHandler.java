package com.wanmi.sbc.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoAdjustPriceRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSkuStockSubRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSpuStockSubRequest;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsPriceSyncRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdRequest;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoPriceChangeDTO;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import com.wanmi.sbc.job.model.entity.GoodsPriceSync;
import com.wanmi.sbc.redis.RedisService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private ObjectMapper objectMapper;

    @Value("${notice.send.message.url}")
    private String noticeSendMsgUrl;

    @Value("${notice.send.message.token}")
    private String noticeSendMsgToken;

    @Value("${notice.send.message.tenantId}")
    private String noticeSendMsgTenantId;

    @Value("${notice.send.message.noticeId}")
    private Integer noticeSendMsgNoticeId;

    private String NOTICE_SEND_MESSAGE ="{0} {1}当前售价{2}，于{3}成本价由{4}调整为{5}，原毛利率{6}%变为{7}%";

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
        log.info("同步商品成本价任务执行开始");
        try {
            GoodsPriceSync param = objectMapper.readValue(params, GoodsPriceSync.class);
            syncBookuuPrice(param);
            syncCostPrice(param);
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
    private void syncBookuuPrice(GoodsPriceSync param) {
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
            goodsInfoListByIdRequest.setPageNum(pageNum);
            baseResponse = goodsProvider.syncGoodsPrice(goodsInfoListByIdRequest);
            syncEsPrice(baseResponse.getContext().getContent());
            sendMessage(baseResponse.getContext().getContent());
        }

    }

    /**
     * 同步管易成本价
     */
    private void syncCostPrice(GoodsPriceSync param) {
        GoodsPriceSyncRequest goodsInfoListByIdRequest = new GoodsPriceSyncRequest();
        goodsInfoListByIdRequest.setPageNum(0);
        goodsInfoListByIdRequest.setPageSize(20);
        goodsInfoListByIdRequest.setGoodsInfoNos(param.getGoodsInfoNo());

        BaseResponse<MicroServicePage<GoodsInfoPriceChangeDTO>> baseResponse = goodsProvider.syncGoodsInfoCostPrice(goodsInfoListByIdRequest);
        MicroServicePage<GoodsInfoPriceChangeDTO> result = baseResponse.getContext();
        if (result.getTotal() == 0) {
            log.info("同步管易成本价数量为空");
            return;
        }
        syncEsPrice(result.getContent());
        sendMessage(baseResponse.getContext().getContent());
        for (int pageNum = 1; pageNum < result.getTotalPages(); ++pageNum) {
            log.info("同步管易成本价,共{}条数据,当前第{}页", result.getTotal(), pageNum);
            goodsInfoListByIdRequest.setPageNum(pageNum);
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
            doPost(change);
        });
    }

    private void doPost(GoodsInfoPriceChangeDTO change) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(noticeSendMsgUrl);
        JSONObject response = null;
        try {
            post.addHeader("Content-type", "application/json; charset=utf-8");
            post.setHeader("Accept", "application/json");
            post.setHeader("token",noticeSendMsgToken);
            post.setHeader("tenantId",noticeSendMsgTenantId);
            Map<String,Object> content = new HashMap<>();
            //毛利率计算
            BigDecimal oldRate = new BigDecimal(0);
            BigDecimal newRate = new BigDecimal(0);
            if(change.getMarketPrice() != null && change.getMarketPrice().compareTo(new BigDecimal(0)) != 0){
                oldRate = (change.getMarketPrice().subtract(change.getOldPrice())).divide(change.getMarketPrice()).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
                newRate = (change.getMarketPrice().subtract(change.getNewPrice())).divide(change.getMarketPrice()).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
            }
            content.put("content", MessageFormat.format(NOTICE_SEND_MESSAGE,change.getSkuNo(),change.getName(),change.getMarketPrice(),change.getTime(),change.getOldPrice(),change.getNewPrice(),oldRate,newRate));
            Map<String,Object> map = new HashMap<>();
            map.put("replaceParams",content);
            map.put("noticeId",noticeSendMsgNoticeId);
            StringEntity entity = new StringEntity(JSON.toJSONString(map),"UTF-8");
            post.setEntity(entity);
            HttpResponse res = httpClient.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(res.getEntity());
                response = JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
