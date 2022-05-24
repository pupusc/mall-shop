package com.wanmi.sbc.goods.api.provider.mini.assistant;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantGoodsCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantGoodsUpdateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantSearchRequest;
import com.wanmi.sbc.goods.bean.wx.vo.assistant.WxLiveAssistantDetailVo;
import com.wanmi.sbc.goods.bean.wx.vo.assistant.WxLiveAssistantVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "${application.goods.name}", contextId = "WxLiveAssistantProvider")
public interface WxLiveAssistantProvider {

    @PostMapping("/wx/assistant/${application.goods.version}/add")
    BaseResponse<Long> addAssistant(@RequestBody WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest);

    @PostMapping("/wx/assistant/${application.goods.version}/delete")
//    BaseResponse<List<String>> deleteAssistant(@RequestParam("id") Long id);
    BaseResponse deleteAssistant(@RequestParam("id") Long id);

    @PostMapping("/wx/assistant/${application.goods.version}/update")
    BaseResponse updateAssistant(@RequestBody WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest);

    @PostMapping("/wx/assistant/${application.goods.version}/list")
    BaseResponse<MicroServicePage<WxLiveAssistantVo>> listAssistant(@RequestBody WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest);

    @PostMapping("/wx/assistant/goods/${application.goods.version}/add")
    BaseResponse<List<String>> addGoods(@RequestBody WxLiveAssistantGoodsCreateRequest wxLiveAssistantGoodsCreateRequest);

    @PostMapping("/wx/assistant/goods/${application.goods.version}/delete")
    BaseResponse<List<String>> deleteGoods(@RequestParam("id") Long id);

    @PostMapping("/wx/assistant/goods/${application.goods.version}/update")
    BaseResponse<String> updateGoodsInfos(@RequestBody WxLiveAssistantGoodsUpdateRequest wxLiveAssistantGoodsUpdateRequest);

    @PostMapping("/wx/assistant/goods/${application.goods.version}/list")
    BaseResponse<WxLiveAssistantDetailVo> listGoods(@RequestBody WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest);

//    @PostMapping("/wx/assistant/${application.goods.version}/live-end")
//    BaseResponse<List<String>> afterWxLiveEnd(@RequestParam("message") String message);

    @PostMapping("/wx/assistant/${application.goods.version}/if-goods-in-live")
    BaseResponse<List<String>> ifGoodsInLive(@RequestBody List<String> goodsIds);

    /**
     * 直播计划价格、同步到商品
     * @param wxLiveAssistantId
     * @return
     */
    @GetMapping("/wx/assistant/goods/${application.goods.version}/open-assistant-goods-valid/{wxLiveAssistantId}")
    BaseResponse openAssistantGoodsValid(@PathVariable("wxLiveAssistantId") Long wxLiveAssistantId);


    /**
     * 直播计划价格、还原价格和库存
     * @param wxLiveAssistantId
     * @return
     */
    @GetMapping("/wx/assistant/goods/${application.goods.version}/close-assistant-goods-valid/{wxLiveAssistantId}")
    BaseResponse closeAssistantGoodsValid(@PathVariable("wxLiveAssistantId") Long wxLiveAssistantId);



}
