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
    BaseResponse<List<String>> deleteAssistant(@RequestParam("id") Long id);

    @PostMapping("/wx/assistant/${application.goods.version}/update")
    BaseResponse<Map<String, String>> updateAssistant(@RequestBody WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest);

    @PostMapping("/wx/assistant/${application.goods.version}/list")
    BaseResponse<MicroServicePage<WxLiveAssistantVo>> listAssistant(@RequestBody WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest);

    @PostMapping("/wx/assistant/goods/${application.goods.version}/add")
    BaseResponse addGoods(@RequestBody WxLiveAssistantGoodsCreateRequest wxLiveAssistantGoodsCreateRequest);

    @PostMapping("/wx/assistant/goods/${application.goods.version}/delete")
    BaseResponse<List<String>> deleteGoods(@RequestParam("id") Long id);

    @PostMapping("/wx/assistant/goods/${application.goods.version}/update")
    BaseResponse<String> updateGoodsInfos(@RequestBody WxLiveAssistantGoodsUpdateRequest wxLiveAssistantGoodsUpdateRequest);

    @PostMapping("/wx/assistant/goods/${application.goods.version}/list")
    BaseResponse<WxLiveAssistantDetailVo> listGoods(@RequestBody WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest);

    @PostMapping("/wx/assistant/${application.goods.version}/live-end")
    BaseResponse<List<String>> afterWxLiveEnd(@RequestParam("message") String message);

    @PostMapping("/wx/assistant/${application.goods.version}/if-goods-in-live")
    BaseResponse<List<String>> ifGoodsInLive(@RequestBody List<String> goodsIds);
}
