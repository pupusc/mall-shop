package com.wanmi.ares.report.flow.dao;

import com.wanmi.ares.report.flow.model.reponse.FlowDataInfoResponse;
import com.wanmi.ares.report.flow.model.reponse.FlowStoreReportResponse;
import com.wanmi.ares.report.flow.model.request.FlowDataRequest;
import com.wanmi.ares.report.flow.model.request.FlowReportRequest;
import com.wanmi.ares.report.flow.model.request.FlowStoreReportRequest;
import com.wanmi.ares.report.flow.model.root.FlowReport;
import com.wanmi.ares.source.model.root.Store;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by sunkun on 2017/10/14.
 */
@Mapper
public interface FlowReportMapper {

    void saveFlow(FlowReport flowReport);

    FlowReport queryFlowReportInfo(FlowReportRequest flowReportRequest);

    List<FlowReport> queryFlow(FlowReportRequest flowReportRequest);

    List<FlowReport> queryFlowByIds(FlowReportRequest flowReportRequest);

    List<FlowDataInfoResponse> queryFlowDataInfo(FlowDataRequest flowDataRequest);

    List<FlowStoreReportResponse> queryFlowStoreReportList(FlowStoreReportRequest request);

    int queryFlowDataNum(FlowDataRequest flowDataRequest);

    void deleteById(String id);

    void deleteByIds(List<String> ids);

    int queryFlowCount(FlowReportRequest flowReportRequest);

    List<FlowReport> queryFlowPage(FlowReportRequest flowReportRequest);

    int clearFlowReport(String date);

    List<Store> queryStoreFlowPage(FlowReportRequest flowReportRequest);

    List<FlowReport> queryFlowPageByStore(FlowReportRequest flowReportRequest);

    int countFlowPageByStore(FlowReportRequest flowReportRequest);

}
