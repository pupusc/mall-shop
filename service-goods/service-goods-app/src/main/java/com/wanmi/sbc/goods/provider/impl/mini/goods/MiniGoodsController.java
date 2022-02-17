package com.wanmi.sbc.goods.provider.impl.mini.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.mini.goods.WxMiniGoodsProvider;
import com.wanmi.sbc.goods.bean.request.wx.goods.WxGoodsCreateRequest;
import com.wanmi.sbc.goods.mini.service.goods.WxGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MiniGoodsController implements WxMiniGoodsProvider {

    @Autowired
    private WxGoodsService wxGoodsService;

    @Override
    public BaseResponse add(WxGoodsCreateRequest wxGoodsCreateRequest) {
        wxGoodsService.addGoods(wxGoodsCreateRequest);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse auditCallback(Map<String, Object> paramMap) {
        wxGoodsService.auditCallback(paramMap);
        return BaseResponse.SUCCESSFUL();
    }


}
