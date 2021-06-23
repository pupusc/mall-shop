package com.wanmi.sbc.elastic.mq.operationlog;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.elastic.operationlog.model.root.EsOperationLog;
import com.wanmi.sbc.elastic.operationlog.service.EsOperationLogService;
import com.wanmi.sbc.setting.api.provider.OperationLogProvider;
import com.wanmi.sbc.setting.api.request.OperationLogAddRequest;
import com.wanmi.sbc.setting.api.response.OperationLogAddResponse;
import com.wanmi.sbc.setting.bean.vo.OperationLogVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@EnableBinding(EsLogSink.class)
public class EsOperationLogMqService {

    @Autowired
    private OperationLogProvider operationLogProvider;

    @Autowired
    private EsOperationLogService esOperationLogService;

    /**
     * mq接收操作日志
     *
     * @param msg
     */
    @StreamListener(MQConstant.OPERATE_LOG_ADD)
    public void receiveOperationLogMq(String msg) {
        OperationLogAddRequest addRequest = JSONObject.parseObject(msg, OperationLogAddRequest.class);

        if (Objects.isNull(addRequest.getCompanyInfoId())) {
            addRequest.setCompanyInfoId(0L);
        }
        OperationLogAddResponse logAddResponse = operationLogProvider.add(addRequest).getContext();
        OperationLogVO operationLogVO = logAddResponse.getOperationLogVO();
        EsOperationLog esOperationLog = new EsOperationLog();
        BeanUtils.copyProperties(operationLogVO, esOperationLog);
        esOperationLogService.add(esOperationLog);
        log.info("操作日志添加成功！");

    }


}
