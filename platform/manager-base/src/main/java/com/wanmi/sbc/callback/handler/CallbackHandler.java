package com.wanmi.sbc.callback.handler;

import java.util.Map;

public interface CallbackHandler {

    boolean support(String eventType);

    void handle(Map<String, Object> paramMap);
}
