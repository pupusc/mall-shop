package com.wanmi.sbc.crm.api.provider.rfmgroupstatistics;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.GroupInfoListRequest;
import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.RfmCustomerListRequest;
import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.RfmGroupStatisticsListRequest;
import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.RfmGroupStatisticsPageRequest;
import com.wanmi.sbc.crm.api.response.rfmgroupstatistics.*;
import org.springframework.cloud.openfeign.FeignClient;;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author lvzhenwei
 * @Description rfm系统人群查询接口
 * @Date 17:13 2019/10/15
 * @Param
 * @return
 **/
@FeignClient(value = "${application.crm.name}",contextId = "RfmGroupStatisticsQueryProvide")
public interface RfmGroupStatisticsQueryProvide {

    /**
     * @Author lvzhenwei
     * @Description rfm系统人群分页查询接口
     * @Date 17:15 2019/10/15
     * @Param [request]
     * @return com.wanmi.sbc.common.base.BaseResponse<com.wanmi.sbc.crm.api.response.rfmgroupstatistics.RfmGroupStatisticsPageResponse>
     **/
    @PostMapping("/crm/${application.crm.version}/rfmGroupStatistics/queryRfmGroupStatisticsDataPage")
    BaseResponse<RfmGroupStatisticsPageResponse> queryRfmGroupStatisticsDataPage(@RequestBody RfmGroupStatisticsPageRequest request);

    /**
     * @Author lvzhenwei
     * @Description rfm系统人群列表查询接口
     * @Date 17:16 2019/10/15
     * @Param [request]
     * @return com.wanmi.sbc.common.base.BaseResponse<com.wanmi.sbc.crm.api.response.rfmgroupstatistics.RfmGroupStatisticsListResponse>
     **/
    @PostMapping("/crm/${application.crm.version}/rfmGroupStatistics/queryRfmGroupStatisticsDataList")
    BaseResponse<RfmGroupStatisticsListResponse> queryRfmGroupStatisticsDataList(@RequestBody RfmGroupStatisticsListRequest request);

    /**
     * @Author lvzhenwei
     * @Description rfm系统人群列表查询接口
     * @Date 17:16 2019/10/15
     * @Param [request]
     * @return com.wanmi.sbc.common.base.BaseResponse<com.wanmi.sbc.crm.api.response.rfmgroupstatistics.RfmGroupStatisticsListResponse>
     **/
    @PostMapping("/crm/${application.crm.version}/rfmGroupStatistics/queryRfmGroupList")
    BaseResponse<RfmGroupListResponse> queryRfmGroupList();

    /**
     * @Author lvzhenwei
     * @Description //TODO
     * @Date 13:49 2020/1/17
     * @Param []
     * @return com.wanmi.sbc.common.base.BaseResponse<com.wanmi.sbc.crm.api.response.rfmgroupstatistics.RfmGroupListResponse>
     **/
    @PostMapping("/crm/${application.crm.version}/rfmGroupStatistics/queryGroupInfoList")
    BaseResponse<GroupInfoListResponse> queryGroupInfoList(@RequestBody GroupInfoListRequest request);

}
