package com.soybean.mall.wx.mini.callback.handler;

import com.wanmi.sbc.goods.api.provider.mini.goods.WxMiniGoodsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class ProductAuditCallbackHandler implements CallbackHandler {

    @Autowired
    private WxMiniGoodsProvider wxMiniGoodsProvider;

    @Override
    public boolean support(String eventType) {
        return "open_product_spu_audit".equals(eventType);
    }

    @Override
    public void handle(Map<String, Object> paramMap) {
        wxMiniGoodsProvider.auditCallback(paramMap);
    }
}
