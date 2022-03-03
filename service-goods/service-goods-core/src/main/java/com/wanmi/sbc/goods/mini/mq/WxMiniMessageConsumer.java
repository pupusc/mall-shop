package com.wanmi.sbc.goods.mini.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.mini.model.goods.WxLiveAssistantModel;
import com.wanmi.sbc.goods.mini.mq.bean.WxLiveAssistantMessageData;
import com.wanmi.sbc.goods.mini.mq.config.WxLiveMessageSink;
import com.wanmi.sbc.goods.mini.repository.goods.WxLiveAssistantRepository;
import com.wanmi.sbc.goods.mini.service.assistant.WxLiveAssistantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Component
@EnableBinding(WxLiveMessageSink.class)
public class WxMiniMessageConsumer {

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private WxLiveAssistantService wxLiveAssistantService;
    @Autowired
    private WxLiveAssistantRepository wxLiveAssistantRepository;

    @StreamListener(WxLiveMessageSink.MSG_DELAY_LIVE_ASSISTANT_CONSUMER)
    public void wxLiveDelayMessageConsume(String message) {
        log.info("WxMiniMessageConsumer receive:{}", message);
        WxLiveAssistantMessageData wxLiveAssistantMessageData = JSONObject.parseObject(message, WxLiveAssistantMessageData.class);
        if(wxLiveAssistantMessageData.getType() == 0){
            //开始直播
        }else if(wxLiveAssistantMessageData.getType() == 1){
            Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(wxLiveAssistantMessageData.getAssistantId());
            if(opt.isPresent()){
                WxLiveAssistantModel wxLiveAssistantModel = opt.get();
                if(wxLiveAssistantModel.getDelFlag().equals(DeleteFlag.NO) && wxLiveAssistantModel.getEndTime().format(df).equals(wxLiveAssistantMessageData.getTime())){
                    log.info("直播助手结束直播:{}, 所有直播商品将恢复原价和库存", wxLiveAssistantMessageData.getAssistantId());
                    wxLiveAssistantService.resetStockAndProce(wxLiveAssistantMessageData.getAssistantId());
                }
            }
        }
    }
}
