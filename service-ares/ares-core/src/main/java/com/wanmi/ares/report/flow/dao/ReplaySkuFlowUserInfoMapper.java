package com.wanmi.ares.report.flow.dao;


import com.wanmi.ares.report.flow.model.root.ReplaySkuFlowUserInfo;
import com.wanmi.ares.report.goods.model.criteria.GoodsQueryCriteria;
import com.wanmi.ares.report.goods.model.root.SkuReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReplaySkuFlowUserInfoMapper {

    int deleteByPrimaryKey(Long id);

    int deleteByPrimary(ReplaySkuFlowUserInfo record);

    int insert(ReplaySkuFlowUserInfo record);

    int insertByList(List<ReplaySkuFlowUserInfo> replaySkuFlowUserInfoList);

    int insertSelective(ReplaySkuFlowUserInfo record);

    ReplaySkuFlowUserInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReplaySkuFlowUserInfo record);

    int updateByPrimaryKey(ReplaySkuFlowUserInfo record);

    List<SkuReport> queryUvBySku(GoodsQueryCriteria criteria);
}