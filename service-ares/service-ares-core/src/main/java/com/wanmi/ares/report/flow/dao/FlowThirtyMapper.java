package com.wanmi.ares.report.flow.dao;

import com.wanmi.ares.report.flow.model.root.FlowReport;
import com.wanmi.ares.report.flow.model.root.FlowThirty;
import com.wanmi.ares.request.flow.FlowThirtyRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FlowThirtyMapper {

    int deleteByPrimaryKey(Long id);

    int deleteByPrimary(FlowThirty record);

    int insert(FlowThirty record);

    int insertFlowThirtyList(List<FlowThirty> flowDataList);

    int insertSelective(FlowThirty record);

    FlowThirty selectByPrimaryKey(Long id);

    FlowThirty queryFlowThirtyInfo(FlowThirtyRequest flowThirtyRequest);

    List<FlowReport> getFlowThirtyDataList(FlowThirty flowThirty);

    int updateByPrimaryKeySelective(FlowThirty record);

    int updateByPrimaryKey(FlowThirty record);
}