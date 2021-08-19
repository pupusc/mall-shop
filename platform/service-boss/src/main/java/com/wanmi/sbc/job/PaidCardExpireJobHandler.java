package com.wanmi.sbc.job;


import com.wanmi.sbc.paidcard.service.PaidCardJobService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@JobHandler(value = "paidCardExpireJobHandler")
@Component
@Slf4j
public class PaidCardExpireJobHandler extends IJobHandler {

    @Autowired
    private PaidCardJobService paidCardJobService;


    @Override
    public ReturnT<String> execute(String param) throws Exception {
        System.out.println("执行了");
        paidCardJobService.sendExpireMsg();
        return SUCCESS;
    }


}
