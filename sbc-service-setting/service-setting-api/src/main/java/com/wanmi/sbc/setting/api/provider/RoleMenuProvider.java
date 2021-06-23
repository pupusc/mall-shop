package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.RoleMenuAuthSaveRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.setting.name}", contextId = "RoleMenuProvider")
public interface RoleMenuProvider {
    /**
     * 保存角色拥有的菜单,功能,权限
     *
     * @param request {@link RoleMenuAuthSaveRequest}
     * @return BaseResponse
     */
    @PostMapping("/setting/${application.setting.version}/authority/save-role-menu-auth")
    BaseResponse saveRoleMenuAuth(@RequestBody RoleMenuAuthSaveRequest request);
}
