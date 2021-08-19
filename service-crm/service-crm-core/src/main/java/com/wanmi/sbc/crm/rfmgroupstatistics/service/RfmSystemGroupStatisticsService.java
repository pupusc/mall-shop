package com.wanmi.sbc.crm.rfmgroupstatistics.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.RfmGroupStatisticsListRequest;
import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.RfmGroupStatisticsPageRequest;
import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.RfmGroupStatisticsRequest;
import com.wanmi.sbc.crm.api.response.rfmgroupstatistics.RfmGroupStatisticsPageResponse;
import com.wanmi.sbc.crm.bean.vo.RfmGroupDataVo;
import com.wanmi.sbc.crm.bean.vo.RfmgroupstatisticsDataVo;
import com.wanmi.sbc.crm.rfmgroupstatistics.mapper.RfmSystemGroupMapper;
import com.wanmi.sbc.crm.rfmgroupstatistics.mapper.RfmSystemGroupStatisticsMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @ClassName RfmSystemGroupStatisticsService
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/10/15 10:19
 **/
@Service
public class RfmSystemGroupStatisticsService {

    @Autowired
    private RfmSystemGroupStatisticsMapper rfmSystemGroupStatisticsMapper;

    @Autowired
    private RfmSystemGroupMapper rfmSystemGroupMapper;

    public void generate(LocalDateTime localDateTime){
        RfmGroupStatisticsRequest request = new RfmGroupStatisticsRequest();
        request.setStatDate(localDateTime.toLocalDate());
        request.setBeginTime(localDateTime.with(LocalTime.MIN));
        request.setEndTime(localDateTime.with(LocalTime.MAX));
        deleteSystemGroupStatistics(request);
        saveSystemGroupStatistics(request);
    }

    /**
     * @Author lvzhenwei
     * @Description 保存rfm模型系统分群统计数据
     * @Date 14:03 2019/10/15
     * @Param [request]
     * @return void
     **/
    public void saveSystemGroupStatistics(RfmGroupStatisticsRequest request){
        rfmSystemGroupStatisticsMapper.saveSystemGroupStatistics(request);
    }

    public void deleteSystemGroupStatistics(RfmGroupStatisticsRequest request){
        this.rfmSystemGroupStatisticsMapper.deleteSystemGroupStatistics(request);
    }
    /**
     * @Author lvzhenwei
     * @Description rfm系统人群分页查询接口
     * @Date 18:19 2019/10/15
     * @Param [request]
     * @return java.util.List<com.wanmi.sbc.crm.api.response.rfmgroupstatistics.RfmGroupStatisticsPageResponse>
     **/
    public PageInfo<RfmgroupstatisticsDataVo>  queryRfmGroupStatisticsDataPage(RfmGroupStatisticsPageRequest request){

        PageHelper.startPage(request.getPageNum()+1,request.getPageSize());
        List<RfmgroupstatisticsDataVo> rfmGroupStatisticsPageResponseList = rfmSystemGroupStatisticsMapper.queryRfmGroupStatisticsDataPage(request);
        PageInfo<RfmgroupstatisticsDataVo> rfmGroupStatisticsPage = new PageInfo<>(rfmGroupStatisticsPageResponseList,(int)((Page) rfmGroupStatisticsPageResponseList).getTotal());
        return rfmGroupStatisticsPage;
    }

    /**
     * @Author lvzhenwei
     * @Description rfm系统人群分页查询接口
     * @Date 18:19 2019/10/15
     * @Param [request]
     * @return java.util.List<com.wanmi.sbc.crm.api.response.rfmgroupstatistics.RfmGroupStatisticsPageResponse>
     **/
    public List<RfmgroupstatisticsDataVo> queryRfmGroupStatisticsDataList(RfmGroupStatisticsListRequest request){
        List<RfmgroupstatisticsDataVo> rfmGroupStatisticsPageResponseList = rfmSystemGroupStatisticsMapper.queryRfmGroupStatisticsDataList(request);
        return rfmGroupStatisticsPageResponseList;
    }


    public List<RfmGroupDataVo> queryAll(){
        return rfmSystemGroupMapper.queryRfmGroupList();
    }
}
