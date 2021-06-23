package com.wanmi.ares.report.flow.service;

import com.wanmi.ares.enums.StatisticsDataType;
import com.wanmi.ares.report.flow.dao.FlowReportMapper;
import com.wanmi.ares.report.flow.model.reponse.FlowDataInfoResponse;
import com.wanmi.ares.report.flow.model.request.FlowDataRequest;
import com.wanmi.ares.report.flow.model.request.FlowReportRequest;
import com.wanmi.ares.report.flow.model.root.FlowReport;
import com.wanmi.ares.request.flow.FlowDataListRequest;
import com.wanmi.ares.view.flow.FlowReportView;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @ClassName FlowDataForYesterdayStrategy
 * @Description 定义昨天统计数据的处理器
 * @Author lvzhenwei
 * @Date 2019/8/27 14:51
 **/
@Component
public class FlowDataForYesterdayStrategy extends FlowDataStrategy {

    @Resource
    private FlowReportMapper flowReportMapper;

    @Resource
    private FlowDataService flowDataService;

    @Override
    public StatisticsDataType[] supports() {
        return new StatisticsDataType[]{StatisticsDataType.YESTERDAY};
    }

    @Override
    public void saveFlowData(List<FlowDataInfoResponse> flowDataResponseList, FlowDataRequest flowDataRequest) {

    }

    @Override
    public FlowReportView getFlowData(FlowDataListRequest flowDataListRequest) {
        FlowReportRequest request = new FlowReportRequest();
        request.setCompanyId(flowDataListRequest.getCompanyId());
        request.setBeginDate(LocalDate.now().minusDays(1));
        request.setEndDate(LocalDate.now().minusDays(1));
        List<FlowReport> flowReportList = flowReportMapper.queryFlow(request);
        flowDataListRequest.setBeginDate(request.getBeginDate());
        flowDataListRequest.setEndDate(request.getEndDate());
        return flowDataService.getFlowDataInfoList(flowReportList, flowDataListRequest);
    }
}
