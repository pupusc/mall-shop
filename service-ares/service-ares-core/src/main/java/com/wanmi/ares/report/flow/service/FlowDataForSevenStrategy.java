package com.wanmi.ares.report.flow.service;

import com.wanmi.ares.enums.StatisticsDataType;
import com.wanmi.ares.report.flow.dao.FlowSevenMapper;
import com.wanmi.ares.report.flow.model.reponse.FlowDataInfoResponse;
import com.wanmi.ares.report.flow.model.request.FlowDataRequest;
import com.wanmi.ares.report.flow.model.root.FlowReport;
import com.wanmi.ares.report.flow.model.root.FlowSeven;
import com.wanmi.ares.request.flow.FlowDataListRequest;
import com.wanmi.ares.view.flow.FlowReportView;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SaveFlowDataForSevenStrategy
 * @Description 定义最近7天统计数据的处理器
 * @Author lvzhenwei
 * @Date 2019/8/26 10:25
 **/
@Component
public class FlowDataForSevenStrategy extends FlowDataStrategy {

    @Resource
    private FlowSevenMapper flowSevenMapper;

    @Resource
    private FlowDataService flowDataService;

    @Override
    public StatisticsDataType[] supports() {
        return new StatisticsDataType[]{StatisticsDataType.SEVEN};
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 保存最近7天统计流量统计数据
     * @Date 14:20 2019/8/27
     * @Param [flowDataResponseList, flowDataRequest]
     **/
    @Override
    public void saveFlowData(List<FlowDataInfoResponse> flowDataResponseList, FlowDataRequest flowDataRequest) {
        List<FlowSeven> flowSevenList = new ArrayList<>();
        flowDataResponseList.forEach(info -> {
            FlowSeven flowSeven = new FlowSeven();
            BeanUtils.copyProperties(info, flowSeven);
            flowSeven.setDate(LocalDate.now());
            flowSeven.setCreatTime(LocalDateTime.now());
            flowSevenList.add(flowSeven);
        });
        flowSevenMapper.insertFlowSevenList(flowSevenList);
    }

    /**
     * @return com.wanmi.ares.view.flow.FlowReportView
     * @Author lvzhenwei
     * @Description 获取最近7天统计流量统计数据
     * @Date 14:20 2019/8/27
     * @Param [flowDataListRequest]
     **/
    @Override
    public FlowReportView getFlowData(FlowDataListRequest flowDataListRequest) {
        FlowSeven record = new FlowSeven();
        record.setCompanyId(flowDataListRequest.getCompanyId());
        flowDataListRequest.setBeginDate(LocalDate.now().minusDays(7));
        flowDataListRequest.setEndDate(LocalDate.now().minusDays(1));
        List<FlowReport> flowReportList = flowSevenMapper.getFlowSevenList(record);
        return flowDataService.getFlowDataInfoList(flowReportList, flowDataListRequest);
    }
}
