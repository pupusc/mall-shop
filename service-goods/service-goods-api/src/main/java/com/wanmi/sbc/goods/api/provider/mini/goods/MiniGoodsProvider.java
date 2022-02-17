package com.wanmi.sbc.goods.api.provider.mini.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.notice.NoticeProviderRequest;
import com.wanmi.sbc.goods.bean.request.wx.goods.WxGoodsCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.goods.name}", contextId = "MiniGoodsProvider")
public interface MiniGoodsProvider {

    @PostMapping("/goods/${application.goods.version}/wx/goods/add")
    BaseResponse add(@Validated(NoticeProviderRequest.Add.class) @RequestBody WxGoodsCreateRequest wxGoodsCreateRequest);
}
