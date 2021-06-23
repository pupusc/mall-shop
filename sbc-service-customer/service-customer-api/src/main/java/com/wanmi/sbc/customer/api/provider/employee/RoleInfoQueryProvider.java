package com.wanmi.sbc.customer.api.provider.employee;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.employee.RoleInfoListRequest;
import com.wanmi.sbc.customer.api.request.employee.RoleInfoQueryRequest;
import com.wanmi.sbc.customer.api.response.employee.RoleInfoListResponse;
import com.wanmi.sbc.customer.api.response.employee.RoleInfoQueryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 角色信息查询API
 *
 * @Author: wanggang
 * @CreateDate: 2018/9/11 9:28
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "RoleInfoQueryProvider")
public interface RoleInfoQueryProvider {

    /**
     * 根据公司编号查询角色信息列表
     *
     * @param roleInfoListRequest {@link RoleInfoListRequest}
     * @return 角色列表信息 {@link RoleInfoListResponse}
     */
    @PostMapping("/customer/${application.customer.version}/role/list-by-company-info-id")
    BaseResponse<RoleInfoListResponse> listByCompanyInfoId(@RequestBody @Valid RoleInfoListRequest
                                                                   roleInfoListRequest);

    /**
     * 根据角色ID查询角色信息
     *
     * @param request {@link RoleInfoQueryRequest}
     * @return 角色信息 {@link RoleInfoQueryResponse}
     */
    @PostMapping("/customer/${application.customer.version}/role/get-by-role-info-id")
    BaseResponse<RoleInfoQueryResponse> getRoleInfoById(@RequestBody @Valid RoleInfoQueryRequest
                                                                request);
}
