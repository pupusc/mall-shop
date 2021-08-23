package com.wanmi.ares.thrift;


import com.alibaba.fastjson.JSON;
import com.wanmi.ares.enums.QueryDateCycle;
import com.wanmi.ares.enums.SortOrder;
import com.wanmi.ares.interfaces.FlowService;
import com.wanmi.ares.report.flow.model.reponse.FlowReponse;
import com.wanmi.ares.report.flow.model.request.FlowReportRequest;
import com.wanmi.ares.report.flow.service.FlowReportService;
import com.wanmi.ares.request.flow.FlowRequest;
import com.wanmi.ares.utils.DateUtil;
import com.wanmi.ares.view.flow.FlowPageView;
import com.wanmi.ares.view.flow.FlowReportView;
import com.wanmi.ares.view.flow.FlowView;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunkun on 2017/10/13.
 */
@Service
public class FlowServiceImpl implements FlowService.Iface {

    @Resource
    private FlowReportService flowReportService;

    @Override
    public FlowReportView getFlowList(FlowRequest request) {
        FlowReportRequest flowReportRequest = new FlowReportRequest();
        BeanUtils.copyProperties(request, flowReportRequest);
        this.getDate(request.getSelectType(),flowReportRequest);
//        flowReportRequest.setBeginDate(DateUtil.parse2Date(request.getBeginDate(), DateUtil.FMT_DATE_1));
//        flowReportRequest.setEndDate(DateUtil.parse2Date(request.getEndDate(), DateUtil.FMT_DATE_1));
        com.wanmi.ares.report.flow.model.reponse.FlowReportReponse flowReportReponse = flowReportService.getList(flowReportRequest);
        FlowReportView flowReportView = new FlowReportView();
        BeanUtils.copyProperties(flowReportReponse, flowReportView);

        List<FlowView> flowViews = new ArrayList<>();
        flowReportReponse.getFlowList().forEach(info -> {
            FlowView flowView = new FlowView();
            BeanUtils.copyProperties(info, flowView);
            flowViews.add(flowView);
        });
        flowReportView.setFlowList(flowViews);

        System.err.println("flowReportReponse--->" + JSON.toJSONString(flowReportReponse));

        return flowReportView;
    }

    @Override
    public FlowPageView getFlowPage(FlowRequest request)  {
        FlowReportRequest flowReportRequest = new FlowReportRequest();
        BeanUtils.copyProperties(request, flowReportRequest);
        this.getDate(request.getSelectType(),flowReportRequest);
//        flowReportRequest.setBeginDate(DateUtil.parse2Date(request.getBeginDate(), DateUtil.FMT_DATE_1));
//        flowReportRequest.setEndDate(DateUtil.parse2Date(request.getEndDate(), DateUtil.FMT_DATE_1));
        flowReportRequest.setSortOrder(request.getSortOrder() == SortOrder.ASC ? SortOrder.ASC : SortOrder.DESC);
        Page<FlowReponse> page = flowReportService.getPage(flowReportRequest);
        FlowPageView flowPageView = new FlowPageView();
        BeanUtils.copyProperties(page, flowPageView);
        List<FlowView> flowViews = new ArrayList<>();
        page.getContent().forEach(info -> {
            FlowView flowView = new FlowView();
            BeanUtils.copyProperties(info, flowView);
            flowView.setDate(DateUtil.format(info.getDate(), DateUtil.FMT_DATE_1));
            flowViews.add(flowView);
        });
        flowPageView.setTotalElements((int) page.getTotalElements());
        flowPageView.setContent(flowViews);
        return flowPageView;
    }

    @Override
    public FlowPageView getStoreList(FlowRequest request)  {
        FlowReportRequest flowReportRequest = new FlowReportRequest();
        BeanUtils.copyProperties(request, flowReportRequest);
        if (request.getSortOrder() == SortOrder.DESC) {
            flowReportRequest.setSortOrder(SortOrder.DESC);
        }
        this.getDate(request.getSelectType(),flowReportRequest);
//        flowReportRequest.setBeginDate(DateUtil.parse2Date(request.getBeginDate(), DateUtil.FMT_DATE_1));
//        flowReportRequest.setEndDate(DateUtil.parse2Date(request.getEndDate(), DateUtil.FMT_DATE_1));
        Page<FlowReponse> page = flowReportService.getStoreList(flowReportRequest);
        FlowPageView flowPageView = new FlowPageView();
        BeanUtils.copyProperties(page, flowPageView);
        List<FlowView> flowViews = new ArrayList<>();
        page.getContent().forEach(info -> {
            FlowView flowView = new FlowView();
            BeanUtils.copyProperties(info, flowView);
            flowViews.add(flowView);
        });
        flowPageView.setTotalElements((int) page.getTotalElements());
        flowPageView.setContent(flowViews);
        return flowPageView;
    }

    private void getDate(String selectType, FlowReportRequest request) {
        if ("0".equals(selectType) || "1".equals(selectType) || "2".equals(selectType) || "3".equals(selectType)) {
            LocalDate beginDate = DateUtil.parseByDateCycle(QueryDateCycle.findByValue(Integer.parseInt(selectType)));
            request.setBeginDate(beginDate);
            if (DateUtil.format(LocalDate.now(), DateUtil.FMT_DATE_1).equals(DateUtil.format(beginDate, DateUtil.FMT_DATE_1))) {
                request.setEndDate(beginDate);
            } else {
                request.setEndDate(LocalDate.now());
            }
        } else {
            request.setBeginDate(DateUtil.pareeByMonthFirst(selectType));
            request.setEndDate(DateUtil.parseByMonth(selectType));
        }
    }
}
