package com.wanmi.ares.report.flow.service;

import com.wanmi.ares.report.flow.dao.FlowThirtyMapper;
import com.wanmi.ares.report.flow.model.root.FlowThirty;
import com.wanmi.ares.request.flow.FlowThirtyRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName FlowThirtyService
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/8/27 17:00
 **/
@Service
public class FlowThirtyService {

    @Resource
    private FlowThirtyMapper flowThirtyMapper;

    /**
     * @Author lvzhenwei
     * @Description 根据查询条件获取流量统计近30天数统计数据内容
     * @Date 17:19 2019/8/27
     * @Param [flowThirtyRequest]
     * @return com.wanmi.ares.report.flow.model.root.FlowThirty
     **/
    public FlowThirty queryFlowThirtInfo(FlowThirtyRequest flowThirtyRequest){
        return flowThirtyMapper.queryFlowThirtyInfo(flowThirtyRequest);
    }
}
