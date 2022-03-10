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
import com.wanmi.sbc.goods.bean.wx.vo.assistant.WxLiveAssistantDetailVo;
import com.wanmi.sbc.goods.bean.wx.vo.assistant.WxLiveAssistantVo;
import com.wanmi.sbc.mini.mq.WxMiniMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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

    /**
     * @description 添加直播计划
     * @param wxLiveAssistantCreateRequest
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/add")
    public BaseResponse addAssistant(@RequestBody WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest){
        BaseResponse<Long> response = wxLiveAssistantProvider.addAssistant(wxLiveAssistantCreateRequest);
        if(response.getCode().equals(CommonErrorCode.SUCCESSFUL)){
            sendDelayMessage(response.getContext(), wxLiveAssistantCreateRequest.getEndTime());
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

    /**
     * @description 删除直播计划
     * @param id
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/delete")
    public BaseResponse deleteAssistant(@RequestParam Long id){
        BaseResponse<Map<String, Map<String, Integer>>> response = wxLiveAssistantProvider.deleteAssistant(id);
        if(response.getCode().equals(CommonErrorCode.SUCCESSFUL)){
            resetEsStock(response.getContext());
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

    /**
     * @description 修改直播计划
     * @param wxLiveAssistantCreateRequest
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/update")
    public BaseResponse updateAssistant(@RequestBody WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest){
        BaseResponse<Map<String, String>> response = wxLiveAssistantProvider.updateAssistant(wxLiveAssistantCreateRequest);
        if(response.getCode().equals(CommonErrorCode.SUCCESSFUL)){
            Map<String, String> context = response.getContext();
            if(context.get("endTime") != null){
                sendDelayMessage(Long.parseLong(context.get("id")), wxLiveAssistantCreateRequest.getEndTime());
            }
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

    /**
     * @description 查询直播计划
     * @param wxLiveAssistantSearchRequest
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/list")
    public BaseResponse<MicroServicePage<WxLiveAssistantVo>> listAssistant(@RequestBody WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest){
        return wxLiveAssistantProvider.listAssistant(wxLiveAssistantSearchRequest);
    }

    /**
     * @description 添加直播计划商品
     * @param wxLiveAssistantGoodsCreateRequest
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/addGoods")
    public BaseResponse addGoods(@RequestBody WxLiveAssistantGoodsCreateRequest wxLiveAssistantGoodsCreateRequest){
        return wxLiveAssistantProvider.addGoods(wxLiveAssistantGoodsCreateRequest);
    }

    /**
     * @description 删除直播计划商品
     * @param id 直播计划商品id
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/deleteGoods")
    public BaseResponse deleteGoods(@RequestParam Long id){
        BaseResponse<Map<String, Map<String, Integer>>> response = wxLiveAssistantProvider.deleteGoods(id);
        if(response.getCode().equals(CommonErrorCode.SUCCESSFUL)){
            resetEsStock(response.getContext());
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

    /**
     * @description 更新直播计划商品
     * @param wxLiveAssistantGoodsUpdateRequest
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/updateGoods")
    public BaseResponse updateGoodsInfos(@RequestBody WxLiveAssistantGoodsUpdateRequest wxLiveAssistantGoodsUpdateRequest){
        return wxLiveAssistantProvider.updateGoodsInfos(wxLiveAssistantGoodsUpdateRequest);
    }

    /**
     * @description 查询直播计划商品
     * @param wxLiveAssistantSearchRequest
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/listGoods")
    public BaseResponse<WxLiveAssistantDetailVo> listGoods(@RequestBody WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest){
        return wxLiveAssistantProvider.listGoods(wxLiveAssistantSearchRequest);
    }

    /**
     * @description 查询直播计划商品是否在直播计划中
     * @param goodsId
     * @menu 小程序
     * @status done
     */
    @PostMapping("/assistant/ifGoodsInLive")
    public BaseResponse<Boolean> ifGoodsInLive(@RequestParam String goodsId){
        return wxLiveAssistantProvider.ifGoodsInLive(goodsId);
    }

    private void sendDelayMessage(Long assistantId, String endTimeStr){
        log.info("发送直播助手延时消息: {},{}", assistantId, endTimeStr);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, df);
        LocalDateTime now2 = LocalDateTime.now();
        Duration duration2 = Duration.between(now2, endTime);

        Map<String, Object> map = new HashMap<>();
        map.put("assistantId", assistantId);
        map.put("time", endTime.format(df));
        map.put("event_type", 1);
        wxMiniMessageProducer.sendDelay(map, duration2.toMillis());
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
