package com.wanmi.ares.report.flow.service;

import com.wanmi.ares.report.flow.dao.FlowSevenMapper;
import com.wanmi.ares.report.flow.model.root.FlowSeven;
import com.wanmi.ares.request.flow.FlowSevenRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName FlowSevenService
 * @Description 流量统计最忌7天流量数据统计
 * @Author lvzhenwei
 * @Date 2019/8/27 16:53
 **/
@Service
public class FlowSevenService {

    @Resource
    private FlowSevenMapper flowSevenMapper;

    /**
     * @Author lvzhenwei
     * @Description 根据查询条件返回流量统计最近7天统计数据内容
     * @Date 16:58 2019/8/27
     * @Param [flowSevenRequest]
     * @return com.wanmi.ares.report.flow.model.root.FlowSeven
     **/
    public FlowSeven queryFlowSevenInfo(FlowSevenRequest flowSevenRequest){
        return flowSevenMapper.queryFlowSevenInfo(flowSevenRequest);
    }
}
