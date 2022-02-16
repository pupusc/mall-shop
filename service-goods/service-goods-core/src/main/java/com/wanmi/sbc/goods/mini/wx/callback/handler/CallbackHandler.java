package com.wanmi.sbc.goods.mini.wx.callback.handler;

import java.util.Map;

public interface CallbackHandler {

    boolean support(String eventType);

    void handle(Map<String, Object> paramMap);
}
