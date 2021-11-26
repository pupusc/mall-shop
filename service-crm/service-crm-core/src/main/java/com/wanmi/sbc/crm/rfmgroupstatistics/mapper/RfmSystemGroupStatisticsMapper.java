package com.wanmi.sbc.crm.rfmgroupstatistics.mapper;

import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.RfmGroupStatisticsListRequest;
import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.RfmGroupStatisticsPageRequest;
import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.RfmGroupStatisticsRequest;
import com.wanmi.sbc.crm.api.response.rfmgroupstatistics.RfmGroupStatisticsPageResponse;
import com.wanmi.sbc.crm.bean.vo.RfmgroupstatisticsDataVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RfmSystemGroupStatisticsMapper继承基类
 */
@Repository
public interface RfmSystemGroupStatisticsMapper {

    /**
     * @Author lvzhenwei
     * @Description 保存rfm模型系统分群统计数据
     * @Date 14:03 2019/10/15
     * @Param [request]
     * @return void
     **/
    void saveSystemGroupStatistics(RfmGroupStatisticsRequest request);

    /**
     * @Author lvzhenwei
     * @Description rfm系统人群分页查询接口
     * @Date 18:20 2019/10/15
     * @Param [request]
     * @return java.util.List<com.wanmi.sbc.crm.api.response.rfmgroupstatistics.RfmGroupStatisticsPageResponse>
     **/
    List<RfmgroupstatisticsDataVo> queryRfmGroupStatisticsDataPage(RfmGroupStatisticsPageRequest request);

    /**
     * @Author lvzhenwei
     * @Description rfm系统人群列表查询接口
     * @Date 18:20 2019/10/15
     * @Param [request]
     * @return java.util.List<com.wanmi.sbc.crm.api.response.rfmgroupstatistics.RfmGroupStatisticsPageResponse>
     **/
    List<RfmgroupstatisticsDataVo> queryRfmGroupStatisticsDataList(RfmGroupStatisticsListRequest request);

    /**
     * 按照日期删除数据
     * @param request
     */
    void deleteSystemGroupStatistics(RfmGroupStatisticsRequest request);

}