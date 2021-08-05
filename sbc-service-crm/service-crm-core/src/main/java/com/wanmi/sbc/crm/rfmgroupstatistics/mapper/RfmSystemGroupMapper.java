package com.wanmi.sbc.crm.rfmgroupstatistics.mapper;

import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.GroupInfoListRequest;
import com.wanmi.sbc.crm.bean.vo.GroupInfoVo;
import com.wanmi.sbc.crm.bean.vo.RfmGroupDataVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RfmSystemGroupStatisticsMapper继承基类
 */
@Repository
public interface RfmSystemGroupMapper {

    /**
     * @Author lvzhenwei
     * @Description rfm系统人群列表查询接口
     * @Date 18:20 2019/10/15
     * @Param [request]
     * @return java.util.List<com.wanmi.sbc.crm.api.response.rfmgroupstatistics.RfmGroupStatisticsPageResponse>
     **/
    List<RfmGroupDataVo> queryRfmGroupList();

    List<GroupInfoVo> queryAllGroupList(GroupInfoListRequest request);

}