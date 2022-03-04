package com.wanmi.sbc.callback.handler;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsStockProvider;
import com.wanmi.sbc.goods.api.provider.mini.goods.WxMiniGoodsProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ProductAuditCallbackHandler implements CallbackHandler {

    @Autowired
    private WxMiniGoodsProvider wxMiniGoodsProvider;
    @Autowired
    private EsGoodsStockProvider esGoodsStockProvider;

    @Override
    public boolean support(String eventType) {
        return "open_product_spu_audit".equals(eventType);
    }

    @Override
    public void handle(Map<String, Object> paramMap) {
        BaseResponse<Boolean> response = wxMiniGoodsProvider.auditCallback(paramMap);
        if(response.getContext()){
            Map<String, String> auditResult = (Map<String, String>) paramMap.get("OpenProductSpuAudit");
            String goodsId = auditResult.get("out_product_id");
            esGoodsStockProvider.updateWxAuditStatus(goodsId);
        }
    }
}
