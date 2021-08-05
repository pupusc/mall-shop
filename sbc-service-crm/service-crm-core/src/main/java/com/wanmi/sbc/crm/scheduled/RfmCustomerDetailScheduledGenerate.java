package com.wanmi.sbc.crm.scheduled;

import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.crm.rfmstatistic.service.RfmCustomerDetailService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@JobHandler(value = "rfmCustomerDetailTaskHandler")
@Component
public class RfmCustomerDetailScheduledGenerate extends IJobHandler {

    @Autowired
    private RfmCustomerDetailService rfmCustomerDetailService;

    /**
     * cron = "0 0 1 * * ?"
     * @param param
     * @return
     */
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("rfm客户明细统计");
        LocalDateTime localDatetime = LocalDateTime.now();
        if(StringUtils.isNotEmpty(param)){
            localDatetime = DateUtil.parseDayCanEmpty(param).minusDays(-1);
        }
        this.rfmCustomerDetailService.generate(localDatetime);
        return SUCCESS;

    }
}
