package com.wanmi.sbc.mini.assistant;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsStockProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSkuStockSubRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSpuStockSubRequest;
import com.wanmi.sbc.goods.api.provider.mini.assistant.WxLiveAssistantProvider;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantGoodsCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantGoodsUpdateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantSearchRequest;
import com.wanmi.sbc.goods.bean.wx.vo.assistant.WxLiveAssistantGoodsVo;
import com.wanmi.sbc.mini.mq.WxMiniMessageProducer;
import com.wanmi.sbc.mini.mq.bean.WxLiveAssistantMessageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/wx/goods")
public class GoodsLiveAssistantController {

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private WxLiveAssistantProvider wxLiveAssistantProvider;
    @Autowired
    private WxMiniMessageProducer wxMiniMessageProducer;
    @Autowired
    private EsGoodsStockProvider esGoodsStockProvider;

    @PostMapping("/assistant/add")
    public BaseResponse addAssistant(@RequestBody WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest){
        BaseResponse<Long> response = wxLiveAssistantProvider.addAssistant(wxLiveAssistantCreateRequest);
        if(response.getCode().equals(CommonErrorCode.SUCCESSFUL)){
            sendDelayMessage(response.getContext(), wxLiveAssistantCreateRequest.getEndTime());
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

    @PostMapping("/assistant/delete")
    public BaseResponse addAssistant(@RequestParam Long id){
        BaseResponse<Map<String, Map<String, Integer>>> response = wxLiveAssistantProvider.deleteAssistant(id);
        if(response.getCode().equals(CommonErrorCode.SUCCESSFUL)){
            resetEsStock(response.getContext());
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

    @PostMapping("/assistant/update")
    public BaseResponse updateAssistant(@RequestBody WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest){
        BaseResponse<Long> response = wxLiveAssistantProvider.updateAssistant(wxLiveAssistantCreateRequest);
        if(response.getCode().equals(CommonErrorCode.SUCCESSFUL)){
            sendDelayMessage(response.getContext(), wxLiveAssistantCreateRequest.getEndTime());
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

    @PostMapping("/assistant/list")
    public BaseResponse listAssistant(@RequestBody WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest){
        return wxLiveAssistantProvider.listAssistant(wxLiveAssistantSearchRequest);
    }

    @PostMapping("/assistant/addGoods")
    public BaseResponse addGoods(@RequestBody WxLiveAssistantGoodsCreateRequest wxLiveAssistantGoodsCreateRequest){
        return wxLiveAssistantProvider.addGoods(wxLiveAssistantGoodsCreateRequest);
    }

    @PostMapping("/assistant/deleteGoods")
    public BaseResponse deleteGoods(@RequestParam Long id){
        BaseResponse<Map<String, Map<String, Integer>>> response = wxLiveAssistantProvider.deleteGoods(id);
        if(response.getCode().equals(CommonErrorCode.SUCCESSFUL)){
            resetEsStock(response.getContext());
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

    @PostMapping("/assistant/updateGoods")
    public BaseResponse updateGoodsInfos(@RequestBody WxLiveAssistantGoodsUpdateRequest wxLiveAssistantGoodsUpdateRequest){
        return wxLiveAssistantProvider.updateGoodsInfos(wxLiveAssistantGoodsUpdateRequest);
    }

    @PostMapping("/assistant/listGoods")
    public BaseResponse<MicroServicePage<WxLiveAssistantGoodsVo>> listGoods(@RequestBody WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest){
        return wxLiveAssistantProvider.listGoods(wxLiveAssistantSearchRequest);
    }

    private void sendDelayMessage(Long assistantId, String endTimeStr){
//        LocalDateTime startTime = wxLiveAssistantModel.getStartTime();
//        LocalDateTime now1 = LocalDateTime.now();
//        Duration duration1 = Duration.between(now1, startTime);
//        wxMiniMessageProducer.sendDelay(WxLiveAssistantMessageData.builder().assistantId(assistantId).type(0).time(startTime.format(df)).build(), duration1.toMillis());
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, df);
        LocalDateTime now2 = LocalDateTime.now();
        Duration duration2 = Duration.between(now2, endTime);
        wxMiniMessageProducer.sendDelay(WxLiveAssistantMessageData.builder().assistantId(assistantId).type(1).time(endTime.format(df)).build(), duration2.toMillis());
    }

    public void resetEsStock(Map<String, Map<String, Integer>> map){

        if(map != null && !map.isEmpty()){
            Map<String, Integer> skusMap = map.get("skus");
            if(!skusMap.isEmpty()){
                EsGoodsSkuStockSubRequest esGoodsSkuStockSubRequest = EsGoodsSkuStockSubRequest.builder().skusMap(skusMap).build();
                esGoodsStockProvider.batchResetStockBySkuId(esGoodsSkuStockSubRequest);
            }
            Map<String, Integer> spusMap = map.get("spus");
            if(!spusMap.isEmpty()){
                EsGoodsSpuStockSubRequest esGoodsSpuStockSubRequest = EsGoodsSpuStockSubRequest.builder().spusMap(spusMap).build();
                esGoodsStockProvider.batchResetStockBySpuId(esGoodsSpuStockSubRequest);
            }
        }
    }
}
