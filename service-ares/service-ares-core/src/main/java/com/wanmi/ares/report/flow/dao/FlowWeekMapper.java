package com.wanmi.ares.report.flow.dao;

import com.wanmi.ares.report.flow.model.root.FlowWeek;
import com.wanmi.ares.request.flow.FlowWeekRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FlowWeekMapper {

    int deleteByPrimaryKey(Long id);

    int deleteByPrimary(FlowWeek record);

    int insert(FlowWeek record);

    int insertFlowWeekList(List<FlowWeek> flowDataList);

    int insertSelective(FlowWeek record);

    FlowWeek selectByPrimaryKey(Long id);

    FlowWeek queryFlowWeekInfo(FlowWeekRequest flowWeekRequest);

    List<FlowWeek> getFlowWeekDataList(FlowWeek flowWeek);

    int updateByPrimaryKeySelective(FlowWeek record);

    int updateByPrimaryKey(FlowWeek record);
}