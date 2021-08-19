package com.wanmi.sbc.job;

import com.wanmi.sbc.message.api.provider.pushsendnode.PushSendNodeProvider;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: sbc-micro-service
 * @description: 通知节任务
 * @create: 2020-01-14 17:45
 **/
@JobHandler(value = "PushSendNodeJobHandler")
@Component
@Slf4j
public class PushSendNodeJobHandler extends IJobHandler {

    @Autowired
    private PushSendNodeProvider pushSendNodeProvider;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        pushSendNodeProvider.nodeTask();
        return SUCCESS;
    }
}