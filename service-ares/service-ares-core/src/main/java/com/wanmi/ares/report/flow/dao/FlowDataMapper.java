package com.wanmi.ares.report.flow.dao;

import com.wanmi.ares.report.flow.model.root.FlowData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FlowDataMapper {
    int deleteByPrimaryKey(Long id);

    int deleteByPrimary(FlowData record);

    int insert(FlowData record);

    int insertSelective(FlowData record);

    int insertFlowDataList(List<FlowData> flowDataList);

    FlowData selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FlowData record);

    int updateByPrimaryKey(FlowData record);
}