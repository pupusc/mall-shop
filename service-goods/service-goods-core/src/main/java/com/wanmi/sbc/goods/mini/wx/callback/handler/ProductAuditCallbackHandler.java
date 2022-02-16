package com.wanmi.sbc.goods.mini.wx.callback.handler;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductAuditCallbackHandler implements CallbackHandler {


    @Override
    public boolean support(String eventType) {
        return false;
    }

    @Override
    public void handle(Map<String, Object> paramMap) {

    }
}
