package com.wanmi.sbc.crm.api.provider.customgroup;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.customgroup.CustomGroupCheckTagRequest;
import com.wanmi.sbc.crm.api.request.customgroup.CustomGroupListParamRequest;
import com.wanmi.sbc.crm.api.request.customgroup.CustomGroupListRequest;
import com.wanmi.sbc.crm.api.request.customgroup.CustomGroupRequest;
import com.wanmi.sbc.crm.api.response.customgroup.CustomGroupListForParamResponse;
import com.wanmi.sbc.crm.api.response.customgroup.CustomGroupQueryAllResponse;
import org.springframework.cloud.openfeign.FeignClient;;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-11
 * \* Time: 16:35
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@FeignClient(value = "${application.crm.name}",contextId = "CustomGroupProvider")
public interface CustomGroupProvider {

    /**
     * 新增自定义人群
     *
     */
    @PostMapping("/crm/${application.crm.version}/customgroup/add")
    BaseResponse add(@RequestBody @Valid CustomGroupRequest request);

    /**
     * 修改自定义人群
     * @param request
     * @return
     */
    @PostMapping("/crm/${application.crm.version}/customgroup/modify")
    BaseResponse modify(@RequestBody @Valid CustomGroupRequest request);

    /**
     * 根据id删除自定义人群
     * @param request
     * @return
     */
    @PostMapping("/crm/${application.crm.version}/customgroup/deleteById")
    BaseResponse deleteById(@RequestBody @Valid CustomGroupRequest request);

    /**
     * 根据id获取自定人群
     * @param request
     * @return
     */
    @PostMapping("/crm/${application.crm.version}/customgroup/queryById")
    BaseResponse queryById(@RequestBody @Valid CustomGroupRequest request);

    @PostMapping("/crm/${application.crm.version}/customgroup/list")
    BaseResponse list(@RequestBody @Valid CustomGroupListRequest request);

    @PostMapping("/crm/${application.crm.version}/customgroup/list-for-param")
    BaseResponse<CustomGroupListForParamResponse> listForParam(@RequestBody @Valid CustomGroupListParamRequest request);

    @PostMapping("/crm/${application.crm.version}/customgroup/queryAll")
    BaseResponse<CustomGroupQueryAllResponse> queryAll();

    @PostMapping("/crm/${application.crm.version}/customgroup/checkCustomerTag")
    BaseResponse checkCustomerTag(@RequestBody @Valid CustomGroupCheckTagRequest request);

}
