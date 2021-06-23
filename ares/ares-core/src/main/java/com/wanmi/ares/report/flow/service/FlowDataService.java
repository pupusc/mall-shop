package com.wanmi.ares.report.flow.service;

import com.wanmi.ares.enums.SortOrder;
import com.wanmi.ares.enums.StatisticsDataType;
import com.wanmi.ares.enums.StatisticsWeekType;
import com.wanmi.ares.report.flow.dao.FlowReportMapper;
import com.wanmi.ares.report.flow.dao.FlowWeekMapper;
import com.wanmi.ares.report.flow.model.request.FlowReportRequest;
import com.wanmi.ares.report.flow.model.root.FlowReport;
import com.wanmi.ares.report.flow.model.root.FlowWeek;
import com.wanmi.ares.request.flow.FlowDataListRequest;
import com.wanmi.ares.request.flow.FlowRequest;
import com.wanmi.ares.utils.DateUtil;
import com.wanmi.ares.view.flow.FlowReportView;
import com.wanmi.ares.view.flow.FlowView;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName FlowDataService
 * @Description 流量统计service
 * @Author lvzhenwei
 * @Date 2019/8/26 15:42
 **/
@Service
public class FlowDataService {

    @Resource
    private FlowDataStrategyChooser flowDataStrategyChooser;

    @Resource
    private FlowReportMapper flowReportMapper;

    @Resource
    private FlowWeekMapper flowWeekMapper;

    /**
     * @return com.wanmi.ares.view.flow.FlowReportView
     * @Author lvzhenwei
     * @Description 获取流量数据
     * @Date 14:59 2019/8/27
     * @Param [request]
     **/
    public FlowReportView getFlowDataList(FlowRequest request) {
        FlowDataListRequest flowDataListRequest = getFlowDataListRequest(request);
        FlowDataStrategy flowDataStrategy = flowDataStrategyChooser.choose(flowDataListRequest.getStatisticsDataType());
        return flowDataStrategy.getFlowData(flowDataListRequest);
    }

    /**
     * @return com.wanmi.ares.view.flow.FlowReportView
     * @Author lvzhenwei
     * @Description 获取流量数据汇总
     * @Date 14:59 2019/8/27
     * @Param [flowReportList, flowDataListRequest]
     **/
    public FlowReportView getFlowDataInfoList(List<FlowReport> flowReportList, FlowDataListRequest flowDataListRequest) {
        FlowReportView flowReportView = new FlowReportView();
        List<FlowView> flowViewList = new ArrayList<>();
        if (flowReportList.size() > 0) {
            FlowReport flowReport = flowReportList.get(0);
            flowReportView.setId(flowReport.getId());
            flowReportView.setSkuTotalPv(flowReport.getSkuTotalPv());
            flowReportView.setTotalPv(flowReport.getTotalPv());
            flowReportView.setTotalUv(flowReport.getTotalUv());
            flowReportView.setSkuTotalUv(flowReport.getSkuTotalUv());
            List<FlowReport> reportList;
            if (Objects.isNull(flowDataListRequest.getStatisticsWeekType())) {
                FlowReportRequest request = new FlowReportRequest();
                request.setCompanyId(flowDataListRequest.getCompanyId());
                request.setBeginDate(flowDataListRequest.getBeginDate());
                request.setEndDate(flowDataListRequest.getEndDate());
                if(flowDataListRequest.getSortOrder().getValue()==0){
                    request.setSortOrder(SortOrder.ASC);
                } else if(flowDataListRequest.getSortOrder().getValue()==1){
                    request.setSortOrder(SortOrder.DESC);
                }
                request.setSortName(flowDataListRequest.getSortName());
                reportList = flowReportMapper.queryFlow(request);
                reportList.forEach(info -> {
                    FlowView flowView = new FlowView();
                    BeanUtils.copyProperties(info, flowView);
                    flowView.setTitle(info.getDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + DateUtil.getWeekStr(info.getDate()));
                    flowViewList.add(flowView);
                });
            } else {
                FlowWeek flowWeek = new FlowWeek();
                flowWeek.setCompanyId(flowDataListRequest.getCompanyId());
                flowWeek.setType(flowDataListRequest.getStatisticsWeekType().toValue());
                flowWeek.setMonth(flowDataListRequest.getMonth());
                List<FlowWeek> flowWeekList = flowWeekMapper.getFlowWeekDataList(flowWeek);
                flowWeekList.forEach(flowWeekInfo -> {
                    FlowView flowView = new FlowView();
                    flowView.setDate(flowWeekInfo.getDate().format(DateTimeFormatter.ofPattern(DateUtil.FMT_DATE_1)));
                    flowView.setSkuTotalPv(flowWeekInfo.getGoodsPv());
                    flowView.setTotalPv(flowWeekInfo.getPv());
                    flowView.setSkuTotalUv(flowWeekInfo.getGoodsUv());
                    flowView.setTotalUv(flowWeekInfo.getUv());
                    flowView.setTitle(flowWeekInfo.getWeekStartDate().format(DateTimeFormatter.ofPattern(DateUtil.FMT_DATE_3)) + "-" +
                            flowWeekInfo.getWeekEndDate().format(DateTimeFormatter.ofPattern(DateUtil.FMT_DATE_3)));
                    flowViewList.add(flowView);
                });
            }
        }
        flowReportView.setFlowList(flowViewList);
        return flowReportView;
    }

    private FlowDataListRequest getFlowDataListRequest(FlowRequest request) {
        FlowDataListRequest flowDataListRequest = new FlowDataListRequest();
        BeanUtils.copyProperties(request, flowDataListRequest);
        StatisticsDataType flowDataType;
        if (request.getSelectType().equals("0")) {
            flowDataType = StatisticsDataType.TODAY;
        } else if (request.getSelectType().equals("1")) {
            flowDataType = StatisticsDataType.YESTERDAY;
        } else if (request.getSelectType().equals("2")) {
            flowDataType = StatisticsDataType.SEVEN;
        } else if (request.getSelectType().equals("3")) {
            flowDataType = StatisticsDataType.THIRTY;
            if (request.isWeek()) {
                flowDataListRequest.setStatisticsWeekType(StatisticsWeekType.THIRTY_WEEk);
            }
        } else {
            flowDataType = StatisticsDataType.MONTH;
            flowDataListRequest.setMonth(request.getSelectType());
            if (request.isWeek()) {
                flowDataListRequest.setStatisticsWeekType(StatisticsWeekType.MONTH_WEEk);
            }
        }
        flowDataListRequest.setStatisticsDataType(flowDataType);
        return flowDataListRequest;
    }
}
