package com.wanmi.sbc.customer.api.provider.employee;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.employee.RoleInfoAddRequest;
import com.wanmi.sbc.customer.api.request.employee.RoleInfoDeleteRequest;
import com.wanmi.sbc.customer.api.request.employee.RoleInfoModifyRequest;
import com.wanmi.sbc.customer.api.response.employee.RoleInfoAddResponse;
import com.wanmi.sbc.customer.api.response.employee.RoleInfoModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 角色信息-添加/修改/删除API
 * @Author: wanggang
 * @CreateDate: 2018/9/11 9:28
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "RoleInfoProvider")
public interface RoleInfoProvider {

    /**
     * 新增角色信息
     *
     * @param saveRequest {@link RoleInfoAddRequest}
     * @return 角色信息 {@link RoleInfoAddResponse}
     */
    @PostMapping("/customer/${application.customer.version}/role/add")
    BaseResponse<RoleInfoAddResponse> add(@RequestBody @Valid RoleInfoAddRequest saveRequest);

    /**
     * 修改角色信息
     *
     * @param editRequest {@link RoleInfoModifyRequest}
     * @return 角色信息 {@link RoleInfoModifyResponse}
     */
    @PostMapping("/customer/${application.customer.version}/role/modify")
    BaseResponse<RoleInfoModifyResponse> modify(@RequestBody @Valid RoleInfoModifyRequest editRequest);

    /**
     * 删除角色信息
     *
     * @param request {@link RoleInfoDeleteRequest}
     * @return 
     */
    @PostMapping("/customer/${application.customer.version}/role/delete")
    BaseResponse delete(@RequestBody @Valid RoleInfoDeleteRequest request);

}
