package com.wanmi.ares.report.flow.service;

import com.wanmi.ares.report.flow.dao.FlowMonthMapper;
import com.wanmi.ares.report.flow.model.root.FlowMonth;
import com.wanmi.ares.request.flow.FlowMonthRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName FlowMonthService
 * @Description 流量统计最近半年按月统计数据
 * @Author lvzhenwei
 * @Date 2019/8/27 17:20
 **/
@Service
public class FlowMonthService {

    @Resource
    private FlowMonthMapper flowMonthMapper;

    /**
     * @Author lvzhenwei
     * @Description 根据查询条件查询最近半年按月统计数据
     * @Date 17:25 2019/8/27
     * @Param [flowMonthRequest]
     * @return com.wanmi.ares.report.flow.model.root.FlowMonth
     **/
    public FlowMonth queryFlowMonthInfo(FlowMonthRequest flowMonthRequest){
        return flowMonthMapper.queryFlowMonthInfo(flowMonthRequest);
    }

}
