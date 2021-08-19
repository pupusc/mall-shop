package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.MenuInfoQueryProvider;
import com.wanmi.sbc.setting.api.request.MenuAndAuthorityListRequest;
import com.wanmi.sbc.setting.api.request.MenuAndFunctionListRequest;
import com.wanmi.sbc.setting.api.response.MenuAndAuthorityListResponse;
import com.wanmi.sbc.setting.api.response.MenuAndFunctionListResponse;
import com.wanmi.sbc.setting.authority.service.MenuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuInfoQueryController implements MenuInfoQueryProvider {
    @Autowired
    private MenuInfoService menuInfoService;

    @Override
    public BaseResponse<MenuAndFunctionListResponse> listMenuAndFunction(@RequestBody MenuAndFunctionListRequest request) {
        MenuAndFunctionListResponse response = new MenuAndFunctionListResponse();
        response.setMenuInfoVOList(menuInfoService.queryMenuAndFunctionList(request.getSystemTypeCd()));
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<MenuAndAuthorityListResponse> listMenuAndAuthority(@RequestBody MenuAndAuthorityListRequest request) {
        MenuAndAuthorityListResponse response = new MenuAndAuthorityListResponse();
        response.setMenuInfoVOList(menuInfoService.queryMenuAndAuthorityList(request.getSystemTypeCd()));
        return BaseResponse.success(response);
    }
}
