package com.wanmi.sbc.goods.api.provider.mini.goods;

import com.soybean.mall.wx.mini.goods.bean.request.WxDeleteProductRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.request.notice.NoticeProviderRequest;
import com.wanmi.sbc.goods.bean.wx.request.WxGoodsCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.WxGoodsSearchRequest;
import com.wanmi.sbc.goods.bean.wx.vo.WxGoodsVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "${application.goods.name}", contextId = "WxMiniGoodsProvider")
public interface WxMiniGoodsProvider {

    @PostMapping("/goods/${application.goods.version}/wx/add")
    BaseResponse add(@RequestBody WxGoodsCreateRequest wxGoodsCreateRequest);

    @PostMapping("/goods/${application.goods.version}/wx/list")
    BaseResponse<MicroServicePage<WxGoodsVo>> list(WxGoodsSearchRequest wxGoodsSearchRequest);

    @PostMapping("/goods/${application.goods.version}/wx/delete")
    BaseResponse delete(@RequestBody WxDeleteProductRequest wxDeleteProductRequest);

    @PostMapping("/${application.goods.version}/wx/audit/callback")
    BaseResponse auditCallback(@RequestBody Map<String, Object> paramMap);
}
