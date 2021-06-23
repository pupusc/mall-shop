package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.RoleMenuProvider;
import com.wanmi.sbc.setting.api.request.RoleMenuAuthSaveRequest;
import com.wanmi.sbc.setting.authority.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleMenuController implements RoleMenuProvider {
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public BaseResponse saveRoleMenuAuth(@RequestBody RoleMenuAuthSaveRequest request) {
        roleMenuService.saveRoleMenuAuth(request);

        return BaseResponse.SUCCESSFUL();
    }
}
