package com.wanmi.ares.report.flow.dao;


import com.wanmi.ares.report.flow.model.request.ReplayFlowDayUserInfoRequest;
import com.wanmi.ares.report.flow.model.root.ReplayFlowDayUserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReplayFlowDayUserInfoMapper {

    int deleteByPrimaryKey(Long id);

    int deleteByPrimary(ReplayFlowDayUserInfo record);

    int insert(ReplayFlowDayUserInfo record);

    int insertByList(List<ReplayFlowDayUserInfo> list);

    int insertSelective(ReplayFlowDayUserInfo record);

    ReplayFlowDayUserInfo selectByPrimaryKey(Long id);

    List<String> queryUserIdList(ReplayFlowDayUserInfoRequest record);

    int queryCountUserIds(ReplayFlowDayUserInfoRequest record);

    List<ReplayFlowDayUserInfo> selectByPrimary(ReplayFlowDayUserInfo record);

    int updateByPrimaryKeySelective(ReplayFlowDayUserInfo record);

    int updateByPrimaryKey(ReplayFlowDayUserInfo record);
}