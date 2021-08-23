package com.wanmi.ares.report.flow.service;

import com.wanmi.ares.enums.StatisticsDataType;
import com.wanmi.ares.report.flow.model.reponse.FlowDataInfoResponse;
import com.wanmi.ares.report.flow.model.request.FlowDataRequest;
import com.wanmi.ares.request.flow.FlowDataListRequest;
import com.wanmi.ares.view.flow.FlowReportView;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName SaveFlowDataForMonthStrategy
 * @Description 保存流量统计数据采用策略模式--抽象业务处理器
 * @Author lvzhenwei
 * @Date 2019/8/26 10:19
 **/
@Component
public abstract class FlowDataStrategy {

    /**
     * @return com.wanmi.ares.enums.FlowDataType[]
     * @Author lvzhenwei
     * @Description 对应的策略值参数
     * @Date 15:35 2019/8/27
     * @Param []
     **/
    public abstract StatisticsDataType[] supports();

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 保存新增流量统计数据方法
     * @Date 14:19 2019/8/27
     * @Param [flowDataResponseList, flowDataRequest]
     **/
    public abstract void saveFlowData(List<FlowDataInfoResponse> flowDataResponseList, FlowDataRequest flowDataRequest);

    /**
     * @return com.wanmi.ares.view.flow.FlowReportView
     * @Author lvzhenwei
     * @Description 根据查询条件获取流量统计数据信息
     * @Date 14:19 2019/8/27
     * @Param [flowDataListRequest]
     **/
    public abstract FlowReportView getFlowData(FlowDataListRequest flowDataListRequest);
}
