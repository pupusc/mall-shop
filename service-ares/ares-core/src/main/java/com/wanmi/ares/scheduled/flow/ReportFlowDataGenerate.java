package com.wanmi.ares.scheduled.flow;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName ReportFlowDataGenerate
 * @Description 流量统计最近七天、最近30天以及自然月的pv、uv数据
 * @Author lvzhenwei
 * @Date 2019/8/22 10:58
 **/
@JobHandler(value = "reportFlowDataGenerate")
@Component
public class ReportFlowDataGenerate extends IJobHandler {

    @Resource
    private FlowDataStatisticsService flowDataStatisticsService;

    @Override
    public ReturnT<String> execute(String type) throws Exception {
        flowDataStatisticsService.generateFlowData(type);
        return SUCCESS;
    }

}
