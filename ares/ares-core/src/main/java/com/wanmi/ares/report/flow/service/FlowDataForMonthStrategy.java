package com.wanmi.ares.report.flow.service;

import com.wanmi.ares.enums.StatisticsDataType;
import com.wanmi.ares.report.flow.dao.FlowMonthMapper;
import com.wanmi.ares.report.flow.model.reponse.FlowDataInfoResponse;
import com.wanmi.ares.report.flow.model.request.FlowDataRequest;
import com.wanmi.ares.report.flow.model.root.FlowMonth;
import com.wanmi.ares.report.flow.model.root.FlowReport;
import com.wanmi.ares.request.flow.FlowDataListRequest;
import com.wanmi.ares.utils.DateUtil;
import com.wanmi.ares.view.flow.FlowReportView;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SaveFlowDataForMonthStrategy
 * @Description 定义按月统计数据的处理器
 * @Author lvzhenwei
 * @Date 2019/8/26 10:22
 **/
@Component
public class FlowDataForMonthStrategy extends FlowDataStrategy {

    @Resource
    private FlowMonthMapper flowMonthMapper;

    @Resource
    private FlowDataService flowDataService;

    @Override
    public StatisticsDataType[] supports() {
        return new StatisticsDataType[]{StatisticsDataType.MONTH};
    }

    @Override
    public void saveFlowData(List<FlowDataInfoResponse> flowDataResponseList, FlowDataRequest flowDataRequest) {
        List<FlowMonth> flowMonthList = new ArrayList<>();
        flowDataResponseList.forEach(info -> {
            FlowMonth flowMonth = new FlowMonth();
            BeanUtils.copyProperties(info, flowMonth);
            flowMonth.setDate(LocalDate.now());
            flowMonth.setMonth(flowDataRequest.getMonth());
            flowMonth.setCreatTime(LocalDateTime.now());
            flowMonthList.add(flowMonth);
        });
        flowMonthMapper.insertFlowMonthList(flowMonthList);
    }

    @Override
    public FlowReportView getFlowData(FlowDataListRequest flowDataListRequest) {
        FlowMonth record = new FlowMonth();
        record.setCompanyId(flowDataListRequest.getCompanyId());
        record.setMonth(flowDataListRequest.getMonth());
        List<FlowReport> flowReportList = flowMonthMapper.getFlowMonthDataList(record);
        LocalDate beginDate = DateUtil.parse2Date(flowDataListRequest.getMonth() + "-01", DateUtil.FMT_DATE_1);
        LocalDate endDate = beginDate.plusDays(beginDate.lengthOfMonth() - 1);
        flowDataListRequest.setBeginDate(beginDate);
        flowDataListRequest.setEndDate(endDate);
        return flowDataService.getFlowDataInfoList(flowReportList, flowDataListRequest);
    }
}
