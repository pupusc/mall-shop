package com.wanmi.sbc.callback.handler;

import java.util.Map;

//回调处理
public interface CallbackHandler {

    boolean support(String eventType);

    String handle(Map<String, Object> paramMap);
}
