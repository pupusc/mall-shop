package com.wanmi.sbc.crm.provider.impl.rfmgroupstatistics;

import com.github.pagehelper.PageInfo;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.crm.api.provider.rfmgroupstatistics.RfmGroupStatisticsQueryProvide;
import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.GroupInfoListRequest;
import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.RfmCustomerListRequest;
import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.RfmGroupStatisticsListRequest;
import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.RfmGroupStatisticsPageRequest;
import com.wanmi.sbc.crm.api.response.rfmgroupstatistics.*;
import com.wanmi.sbc.crm.bean.vo.RfmgroupstatisticsDataVo;
import com.wanmi.sbc.crm.rfmgroupstatistics.service.RfmSystemGroupService;
import com.wanmi.sbc.crm.rfmgroupstatistics.service.RfmSystemGroupStatisticsService;
import com.wanmi.sbc.crm.rfmstatistic.service.RfmCustomerDetailService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * @ClassName RfmGroupStatisticsQueryController
 * @Description rfm系统人群查询接口实现类
 * @Author lvzhenwei
 * @Date 2019/10/15 17:17
 **/
@RestController
public class RfmGroupStatisticsQueryController implements RfmGroupStatisticsQueryProvide {

    @Autowired
    private RfmSystemGroupStatisticsService rfmSystemGroupStatisticsService;

    @Autowired
    private RfmSystemGroupService rfmSystemGroupService;

    @Autowired
    private RfmCustomerDetailService rfmCustomerDetailService;

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse<com.wanmi.sbc.crm.api.response.rfmgroupstatistics.RfmGroupStatisticsPageResponse>
     * @Author lvzhenwei
     * @Description rfm系统人群分页查询接口
     * @Date 18:19 2019/10/15
     * @Param [request]
     **/
    @Override
    public BaseResponse<RfmGroupStatisticsPageResponse> queryRfmGroupStatisticsDataPage(@RequestBody RfmGroupStatisticsPageRequest request) {
        if(request.getStatDate()==null){
            request.setStatDate(LocalDate.now().minusDays(1));
        }
        PageInfo<RfmgroupstatisticsDataVo> rfmGroupStatisticsPage = rfmSystemGroupStatisticsService.queryRfmGroupStatisticsDataPage(request);
        return BaseResponse.success(RfmGroupStatisticsPageResponse.builder().rfmGroupStatisticsPageResponse(
                new MicroServicePage<RfmgroupstatisticsDataVo>(rfmGroupStatisticsPage.getList(), request.getPageable(), rfmGroupStatisticsPage.getTotal()))
                .build());
    }

    /**
     * @Author lvzhenwei
     * @Description rfm系统人群列表查询接口
     * @Date 9:36 2019/10/16
     * @Param [request]
     * @return com.wanmi.sbc.common.base.BaseResponse<com.wanmi.sbc.crm.api.response.rfmgroupstatistics.RfmGroupStatisticsListResponse>
     **/
    @Override
    public BaseResponse<RfmGroupStatisticsListResponse> queryRfmGroupStatisticsDataList(@RequestBody RfmGroupStatisticsListRequest request) {
        if(request.getStatDate()==null) {
            //为了测试进行测试，上线前打开，并注释后面一个
            request.setStatDate(LocalDate.now().minusDays(1));
          //  request.setStatDate(LocalDate.now());
        }
        List<RfmgroupstatisticsDataVo> rfmGroupStatisticsPageResponseList = rfmSystemGroupStatisticsService.queryRfmGroupStatisticsDataList(request);
        return BaseResponse.success(RfmGroupStatisticsListResponse.builder().rfmGroupStatisticsListResponse(rfmGroupStatisticsPageResponseList).build());
    }

    @Override
    public BaseResponse<RfmGroupListResponse> queryRfmGroupList(){
        return BaseResponse.success(RfmGroupListResponse.builder().groupDataList(rfmSystemGroupService.queryRfmGroupList()).build());
    }

    @Override
    public BaseResponse<GroupInfoListResponse> queryGroupInfoList(@RequestBody GroupInfoListRequest request) {
        return BaseResponse.success(GroupInfoListResponse.builder().groupInfoVoList(rfmSystemGroupService.queryAllGroupList(request)).build());
    }

}
