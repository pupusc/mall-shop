package com.wanmi.sbc.crm.customgroup.mapper;

import com.wanmi.sbc.crm.customgroup.model.CustomGroupStatistics;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomGroupStatisticsMapper {

    int deleteByStatDate(@Param("statDate")String statDate);

    List<CustomGroupStatistics> queryListByGroupId(@Param("groupId") String groupId);

    int insertBySelect(@Param("statDate")String statDate);
}