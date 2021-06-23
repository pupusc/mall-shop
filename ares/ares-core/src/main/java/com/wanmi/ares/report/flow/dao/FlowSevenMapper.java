package com.wanmi.ares.report.flow.dao;

import com.wanmi.ares.report.flow.model.root.FlowReport;
import com.wanmi.ares.report.flow.model.root.FlowSeven;
import com.wanmi.ares.request.flow.FlowSevenRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FlowSevenMapper {

    int deleteByPrimaryKey(Long id);

    int deleteByPrimary(FlowSeven record);

    int insert(FlowSeven record);

    int insertFlowSevenList(List<FlowSeven> flowDataList);

    int insertSelective(FlowSeven record);

    FlowSeven selectByPrimaryKey(Long id);

    FlowSeven queryFlowSevenInfo(FlowSevenRequest flowSevenRequest);

    List<FlowReport> getFlowSevenList(FlowSeven flowSeven);

    int updateByPrimaryKeySelective(FlowSeven record);

    int updateByPrimaryKey(FlowSeven record);
}