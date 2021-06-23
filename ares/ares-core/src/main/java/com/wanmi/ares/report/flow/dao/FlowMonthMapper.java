package com.wanmi.ares.report.flow.dao;

import com.wanmi.ares.report.flow.model.root.FlowMonth;
import com.wanmi.ares.report.flow.model.root.FlowReport;
import com.wanmi.ares.request.flow.FlowMonthRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FlowMonthMapper {

    int deleteByPrimaryKey(Long id);

    int deleteByPrimary(FlowMonth record);

    int insert(FlowMonth record);

    int insertFlowMonthList(List<FlowMonth> flowDataList);

    int insertSelective(FlowMonth record);

    FlowMonth selectByPrimaryKey(Long id);

    FlowMonth queryFlowMonthInfo(FlowMonthRequest flowMonthRequest);

    List<FlowReport> getFlowMonthDataList(FlowMonth record);

    int updateByPrimaryKeySelective(FlowMonth record);

    int updateByPrimaryKey(FlowMonth record);
}