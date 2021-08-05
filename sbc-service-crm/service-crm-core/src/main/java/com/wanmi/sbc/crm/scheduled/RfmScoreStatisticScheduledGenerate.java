package com.wanmi.sbc.crm.scheduled;

import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.crm.rfmstatistic.service.RfmScoreStatisticService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JobHandler(value = "rfmScoreStatisticScheduledGenerate")
@Component
public class RfmScoreStatisticScheduledGenerate extends IJobHandler {

    @Autowired
    private RfmScoreStatisticService rfmScoreStatisticService;


    @Override
    public ReturnT<String> execute(String param) throws Exception {
        LocalDateTime localDatetime = LocalDateTime.now();
        if(StringUtils.isNotEmpty(param)){
            localDatetime = DateUtil.parseDayCanEmpty(param).minusDays(-1);
        }
        rfmScoreStatisticService.generate(DateUtil.format(localDatetime.minusDays(1),DateUtil.FMT_DATE_1));
        return ReturnT.SUCCESS;
    }
}
