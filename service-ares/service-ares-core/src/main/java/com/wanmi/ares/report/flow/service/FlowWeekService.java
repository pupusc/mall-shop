package com.wanmi.ares.report.flow.service;

import com.wanmi.ares.report.flow.dao.FlowWeekMapper;
import com.wanmi.ares.report.flow.model.root.FlowWeek;
import com.wanmi.ares.request.flow.FlowWeekRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName FlowWeekService
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/8/27 17:26
 **/
@Service
public class FlowWeekService {

    @Resource
    private FlowWeekMapper flowWeekMapper;

    /**
     * @Author lvzhenwei
     * @Description
     * @Date 17:36 2019/8/27
     * @Param [flowWeekRequest]
     * @return com.wanmi.ares.report.flow.model.root.FlowWeek
     **/
    public FlowWeek queryFlowWeekInfo(FlowWeekRequest flowWeekRequest){
        return flowWeekMapper.queryFlowWeekInfo(flowWeekRequest);
    }
}
