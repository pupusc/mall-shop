package com.wanmi.sbc.crm.customergroup;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.crmgroup.CrmGroupProvider;
import com.wanmi.sbc.crm.api.provider.customgroup.CustomGroupProvider;
import com.wanmi.sbc.crm.api.provider.customgrouprel.CustomGroupRelProvide;
import com.wanmi.sbc.crm.api.provider.rfmgroupstatistics.RfmGroupStatisticsQueryProvide;
import com.wanmi.sbc.crm.api.provider.rfmstatistic.RfmScoreStatisticQueryProvider;
import com.wanmi.sbc.crm.api.request.crmgroup.CrmGroupRequest;
import com.wanmi.sbc.crm.api.request.customgroup.CustomGroupListParamRequest;
import com.wanmi.sbc.crm.api.request.customgroup.CustomGroupListRequest;
import com.wanmi.sbc.crm.api.request.customgrouprel.CustomGroupRelRequest;
import com.wanmi.sbc.crm.api.request.rfmgroupstatistics.GroupInfoListRequest;
import com.wanmi.sbc.crm.api.request.rfmstatistic.RfmCustomerDetailByCustomerIdRequest;
import com.wanmi.sbc.crm.api.response.customgroup.CustomGroupQueryAllResponse;
import com.wanmi.sbc.crm.api.response.rfmgroupstatistics.GroupInfoListResponse;
import com.wanmi.sbc.crm.api.response.rfmgroupstatistics.RefmCustomerDetailByCustomerIdResponse;
import com.wanmi.sbc.crm.api.response.rfmgroupstatistics.RfmGroupListResponse;
import com.wanmi.sbc.crm.bean.vo.CustomGroupRelVo;
import com.wanmi.sbc.crm.bean.vo.CustomGroupVo;
import com.wanmi.sbc.crm.customergroup.response.CustomerGroupListResponse;
import com.wanmi.sbc.crm.customergroup.response.CustomerGroupNameResponse;
import com.wanmi.sbc.message.bean.constant.ReceiveGroupType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Api(description = "会员分群API", tags = "CustomerGroupController")
@RestController
@RequestMapping(value = "/customer/group")
public class CustomerGroupController {

    @Autowired
    private RfmScoreStatisticQueryProvider rfmScoreStatisticQueryProvider;

    @Autowired
    private CustomGroupRelProvide customGroupRelProvide;

    @Autowired
    private RfmGroupStatisticsQueryProvide rfmGroupStatisticsQueryProvide;
    @Autowired
    private CustomGroupProvider customGroupProvider;

    @Autowired
    private CrmGroupProvider crmGroupProvider;

    @ApiOperation(value = "根据会员id获取分群信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerId", value = "会员ID", required = true)
    @GetMapping("/{customerId}")
    public BaseResponse<CustomerGroupNameResponse> getList(@PathVariable String customerId) {
        List<String> groupNames = new ArrayList<>();
        RefmCustomerDetailByCustomerIdResponse response = rfmScoreStatisticQueryProvider.customerDetail(
                RfmCustomerDetailByCustomerIdRequest.builder().customerId(customerId).build()).getContext();
        if (Objects.nonNull(response)) {
            groupNames.add(response.getSystemGroupName());
        }
//        List<CustomGroupRelVo> voList = customGroupRelProvide.queryListByCustomerId(
//                CustomGroupRelRequest.builder().customerId(customerId).build()).getContext();
        List<CustomGroupVo> customGroupVos = customGroupProvider.listForParam(CustomGroupListParamRequest.builder().customerId(customerId).build()).getContext().getCustomGroupVos();
        List<CustomGroupRelVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(customGroupVos)) {
            groupNames.addAll(customGroupVos.stream().map(CustomGroupVo::getGroupName).collect(Collectors.toList()));
            voList = KsBeanUtil.convertList(customGroupVos,CustomGroupRelVo.class);
        }
        return BaseResponse.success(CustomerGroupNameResponse.builder().groupNames(groupNames).customGroupRelVoList(voList).build());
    }

    @ApiOperation(value = "获取所有分群信息")
    @GetMapping("/list")
    public BaseResponse<List<CustomerGroupListResponse>> getAllList() {
        List<CustomerGroupListResponse> retList = new ArrayList<>();
        RfmGroupListResponse rfmGroupList = rfmGroupStatisticsQueryProvide.queryRfmGroupList().getContext();
        CustomGroupQueryAllResponse customGroupList = customGroupProvider.queryAll().getContext();
        if (rfmGroupList != null) {

            rfmGroupList.getGroupDataList()
                    .stream()
                    .forEach(
                            rfmGroupDataVo ->
                                    retList.add(
                                            CustomerGroupListResponse
                                                    .builder()
                                                    .groupId(ReceiveGroupType.SYS + "_" + rfmGroupDataVo.getId())
                                                    .groupName(rfmGroupDataVo.getGroupName())
                                                    .build()
                                    )
                    );
        }
        if (customGroupList != null) {
            customGroupList.getCustomGroupVoList()
                    .stream()
                    .forEach(
                            customGroupVo -> retList.add(
                                    CustomerGroupListResponse
                                            .builder()
                                            .groupId(ReceiveGroupType.CUSTOM + "_" + customGroupVo.getId())
                                            .groupName(customGroupVo.getGroupName())
                                            .build()
                            )
                    );
        }

        return BaseResponse.success(retList);
    }

    @ApiOperation(value = "获取所有分群人数")
    @PostMapping("/customer-total")
    public BaseResponse<Long> getCustomerTotal(@RequestBody List<String> groupIdList) {
        Long totalCount = 0L;
        List<Long> sysGroupList = new ArrayList<>();
        List<Long> customGroupList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(groupIdList)) {
            for (String groupId : groupIdList) {
                String arr[] = groupId.split("_");
                if (arr[0].equals(ReceiveGroupType.SYS)) {
                    sysGroupList.add(Long.parseLong(arr[1]));
                }
                if (arr[0].equals(ReceiveGroupType.CUSTOM)) {
                    customGroupList.add(Long.parseLong(arr[1]));
                }
            }
            totalCount = crmGroupProvider.queryCustomerPhoneCount(CrmGroupRequest.builder()
                    .customGroupList(customGroupList)
                    .sysGroupList(sysGroupList)
                    .build()).getContext();
        }
        return BaseResponse.success(totalCount);
    }

    @ApiOperation(value = "根据条件获取分群信息列表")
    @PostMapping("/queryGroupInfoList")
    public BaseResponse<List<CustomerGroupListResponse>> queryGroupInfoList(@RequestBody GroupInfoListRequest request) {
        List<CustomerGroupListResponse> retList = new ArrayList<>();
        GroupInfoListResponse groupInfoListResponse = rfmGroupStatisticsQueryProvide.queryGroupInfoList(request).getContext();
        if (Objects.nonNull(groupInfoListResponse)) {
            if (CollectionUtils.isNotEmpty(groupInfoListResponse.getGroupInfoVoList())) {
                groupInfoListResponse.getGroupInfoVoList().stream().forEach(groupInfoVo -> {
                    retList.add(CustomerGroupListResponse.builder().groupId(groupInfoVo.getGroupId()).groupName(groupInfoVo.getGroupName()).build());
                });
            }
        }
        return BaseResponse.success(retList);
    }
}
