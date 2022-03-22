package com.wanmi.sbc.mini.mq;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.goods.api.provider.mini.assistant.WxLiveAssistantProvider;
import com.wanmi.sbc.mini.mq.config.WxLiveMessageSink;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@EnableBinding(WxLiveMessageSink.class)
public class WxMiniMessageConsumer {

    @Autowired
    private WxLiveAssistantProvider wxLiveAssistantProvider;
    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @StreamListener(WxLiveMessageSink.MSG_DELAY_LIVE_ASSISTANT_CONSUMER)
    public void wxLiveDelayMessageConsume(String message) {
        log.info("直播助手事件 receive:{}", message);
        BaseResponse<List<String>> response = wxLiveAssistantProvider.afterWxLiveEnd(message);
        if(response.getCode().equals(CommonErrorCode.SUCCESSFUL)){
            List<String> goodsIds = response.getContext();
            log.info("商品es更新: {}", Arrays.toString(goodsIds.toArray()));
            if(CollectionUtils.isNotEmpty(goodsIds)){
                esGoodsInfoElasticProvider.deleteByGoods(EsGoodsDeleteByIdsRequest.builder().deleteIds(goodsIds).build());
                esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsIds(goodsIds).build());
            }
            log.info("直播助手事件 执行完成");
        }else {
            log.info("直播助手事件 执行失败");
        }
    }
}
