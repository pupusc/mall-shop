package com.wanmi.sbc.message.task;

import com.wanmi.sbc.message.smssend.service.SmsSendService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: zgl
 * @Description:用于xxl-job调度定时发送短信
 * @Date: 2019-11-12
 */
@JobHandler(value = "smsSendTaskGenerate")
@Component
public class SmsSendTaskGenerate extends IJobHandler {

    @Resource
    private SmsSendService smsSendService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        smsSendService.scanSendTask();
        return SUCCESS;
    }
}
