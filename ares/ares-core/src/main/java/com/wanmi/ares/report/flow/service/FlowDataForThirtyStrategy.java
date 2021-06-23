package com.wanmi.ares.report.flow.service;

import com.wanmi.ares.enums.SortOrder;
import com.wanmi.ares.enums.StatisticsDataType;
import com.wanmi.ares.report.flow.dao.FlowThirtyMapper;
import com.wanmi.ares.report.flow.model.reponse.FlowDataInfoResponse;
import com.wanmi.ares.report.flow.model.request.FlowDataRequest;
import com.wanmi.ares.report.flow.model.root.FlowReport;
import com.wanmi.ares.report.flow.model.root.FlowThirty;
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
 * @ClassName SaveFlowDataForThirtyStrategy
 * @Description 定义最近30天统计数据的处理器
 * @Author lvzhenwei
 * @Date 2019/8/26 10:26
 **/
@Component
public class FlowDataForThirtyStrategy extends FlowDataStrategy {

    @Resource
    private FlowThirtyMapper flowThirtyMapper;

    @Resource
    private FlowDataService flowDataService;

    @Override
    public StatisticsDataType[] supports() {
        return new StatisticsDataType[]{StatisticsDataType.THIRTY};
    }

    @Override
    public void saveFlowData(List<FlowDataInfoResponse> flowDataResponseList, FlowDataRequest flowDataRequest) {
        List<FlowThirty> flowThirtyList = new ArrayList<>();
        flowDataResponseList.forEach(info -> {
            FlowThirty flowThirty = new FlowThirty();
            BeanUtils.copyProperties(info, flowThirty);
            flowThirty.setDate(LocalDate.now());
            flowThirty.setCreatTime(LocalDateTime.now());
            flowThirtyList.add(flowThirty);
        });
        flowThirtyMapper.insertFlowThirtyList(flowThirtyList);
    }

    @Override
    public FlowReportView getFlowData(FlowDataListRequest flowDataListRequest) {
        FlowThirty flowThirty = new FlowThirty();
        flowThirty.setCompanyId(flowDataListRequest.getCompanyId());
        flowDataListRequest.setBeginDate(LocalDate.now().minusDays(30));
        flowDataListRequest.setEndDate(LocalDate.now());
        flowDataListRequest.setSortOrder(SortOrder.ASC);
        flowDataListRequest.setSortName("date");
        List<FlowReport> flowReportList = flowThirtyMapper.getFlowThirtyDataList(flowThirty);
        return flowDataService.getFlowDataInfoList(flowReportList, flowDataListRequest);
    }
}
