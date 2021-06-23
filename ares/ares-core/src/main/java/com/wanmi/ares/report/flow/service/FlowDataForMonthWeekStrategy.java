package com.wanmi.ares.report.flow.service;

import com.wanmi.ares.enums.StatisticsDataType;
import com.wanmi.ares.enums.StatisticsWeekType;
import com.wanmi.ares.report.flow.dao.FlowWeekMapper;
import com.wanmi.ares.report.flow.model.reponse.FlowDataInfoResponse;
import com.wanmi.ares.report.flow.model.request.FlowDataRequest;
import com.wanmi.ares.report.flow.model.root.FlowWeek;
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
 * @ClassName SaveFlowDataForWeekStrategy
 * @Description 定义按月统计的按周统计数据的处理器
 * @Author lvzhenwei
 * @Date 2019/8/26 10:28
 **/
@Component
public class FlowDataForMonthWeekStrategy extends FlowDataStrategy {

    @Resource
    private FlowWeekMapper flowWeekMapper;

    @Override
    public StatisticsDataType[] supports() {
        return new StatisticsDataType[]{StatisticsDataType.MONTH_WEEk};
    }

    @Override
    public void saveFlowData(List<FlowDataInfoResponse> flowDataResponseList, FlowDataRequest flowDataRequest) {
        List<FlowWeek> flowWeekList = new ArrayList<>();
        flowDataResponseList.forEach(info -> {
            FlowWeek flowWeek = new FlowWeek();
            BeanUtils.copyProperties(info, flowWeek);
            flowWeek.setDate(LocalDate.now());
            flowWeek.setType(StatisticsWeekType.MONTH_WEEk.toValue());
            flowWeek.setMonth(flowDataRequest.getMonth());
            flowWeek.setWeekStartDate(flowDataRequest.getWeekStartDate());
            flowWeek.setWeekEndDate(flowDataRequest.getWeekEndDate());
            flowWeek.setCreatTime(LocalDateTime.now());
            flowWeekList.add(flowWeek);
        });
        flowWeekMapper.insertFlowWeekList(flowWeekList);
    }

    @Override
    public FlowReportView getFlowData(FlowDataListRequest flowDataListRequest) {
        return null;
    }
}
