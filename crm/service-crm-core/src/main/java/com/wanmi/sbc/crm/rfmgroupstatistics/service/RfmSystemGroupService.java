package com.wanmi.sbc.crm.rfmgroupstatistics.service;

import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.GroupInfoListRequest;
import com.wanmi.sbc.crm.bean.vo.GroupInfoVo;
import com.wanmi.sbc.crm.bean.vo.RfmGroupDataVo;
import com.wanmi.sbc.crm.rfmgroupstatistics.mapper.RfmSystemGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName RfmSystemGroupStatisticsService
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/10/15 10:19
 **/
@Service
public class RfmSystemGroupService {

    @Autowired
    private RfmSystemGroupMapper rfmSystemGroupMapper;



    /**
     * @Author lvzhenwei
     * @Description rfm系统人群分页查询接口
     * @Date 18:19 2019/10/15
     * @Param [request]
     * @return java.util.List<com.wanmi.sbc.crm.bean.vo.RfmGroupDataVo>
     **/
    public List<RfmGroupDataVo> queryRfmGroupList(){
        return rfmSystemGroupMapper.queryRfmGroupList();
    }

    /**
     * @Author lvzhenwei
     * @Description 根据查询条件获取分群信息
     * @Date 13:47 2020/1/17
     * @Param [request]
     * @return java.util.List<com.wanmi.sbc.crm.bean.vo.GroupInfoVo>
     **/
    public List<GroupInfoVo> queryAllGroupList(GroupInfoListRequest request){
        return rfmSystemGroupMapper.queryAllGroupList(request);
    }

}
