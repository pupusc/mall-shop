package com.wanmi.sbc.crm.rfmstatistic.mapper;

import com.wanmi.sbc.crm.api.request.rfmstatistic.RfmScoreStatisticRequest;
import com.wanmi.sbc.crm.rfmstatistic.model.RfmScoreStatistic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RfmScoreStatisticMapper继承基类
 */
@Mapper
public interface RfmScoreStatisticMapper  {

    void saveRStatistic(@Param("statDate")String statDate);

    void saveFStatistic(@Param("statDate")String statDate);

    void saveMStatistic(@Param("statDate")String statDate);

    void saveAvgStatistic(@Param("statDate")String statDate);

    void deleteByDate(@Param("statDate")String statDate);

    List<RfmScoreStatistic> queryList(RfmScoreStatisticRequest request);
}