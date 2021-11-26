package com.wanmi.sbc.crm.scheduled;

import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.RfmGroupStatisticsRequest;
import com.wanmi.sbc.crm.rfmgroupstatistics.service.RfmSystemGroupStatisticsService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @ClassName RfmSystemGroupStatisticsScheduled
 * @Description rfm模型系统分群统计
 * @Author lvzhenwei
 * @Date 2019/10/15 13:57
 **/
@JobHandler(value = "rfmSystemGroupStatisticsJobHandler")
@Component
public class RfmSystemGroupStatisticsJobHandler  extends IJobHandler {

    @Autowired
    private RfmSystemGroupStatisticsService rfmSystemGroupStatisticsService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        LocalDateTime localDatetime = LocalDateTime.now();
        RfmGroupStatisticsRequest request = new RfmGroupStatisticsRequest();
        if(StringUtils.isNotEmpty(param)){
            localDatetime = DateUtil.parseDayCanEmpty(param);
            request.setStatDate(localDatetime.toLocalDate());
            request.setBeginTime(localDatetime.with(LocalTime.MIN));
            request.setEndTime(localDatetime.with(LocalTime.MAX));
        }else {

            request.setStatDate(localDatetime.minusDays(1).toLocalDate());
            request.setBeginTime(localDatetime.minusDays(1).with(LocalTime.MIN));
            request.setEndTime(localDatetime.minusDays(1).with(LocalTime.MAX));
        }
        rfmSystemGroupStatisticsService.deleteSystemGroupStatistics(request);
        rfmSystemGroupStatisticsService.saveSystemGroupStatistics(request);
        return SUCCESS;
    }
}
