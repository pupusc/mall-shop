package com.wanmi.sbc.setting.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.request.OperationLogAddRequest;
import com.wanmi.sbc.setting.log.OperationLog;
import com.wanmi.sbc.setting.log.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

//@Component
//@EnableBinding(LogSink.class)
public class OperationLogMqService {

    @Autowired
    private OperationLogService operationLogService;


    /**
     * mq接收操作日志
     *
     * @param msg
     */
    /*@StreamListener(MQConstant.OPERATE_LOG_ADD)
    public void receiveOperationLogMq(String msg) {
        OperationLogAddRequest addRequest = JSONObject.parseObject(msg, OperationLogAddRequest.class);
        OperationLog log = new OperationLog();
        KsBeanUtil.copyPropertiesThird(addRequest, log);
        if(Objects.isNull(log.getCompanyInfoId())) {
            log.setCompanyInfoId(0L);
        }
        operationLogService.add(log);
    }*/
}
