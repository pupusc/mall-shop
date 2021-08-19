package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.MenuInfoProvider;
import com.wanmi.sbc.setting.api.request.*;
import com.wanmi.sbc.setting.authority.model.root.Authority;
import com.wanmi.sbc.setting.authority.model.root.FunctionInfo;
import com.wanmi.sbc.setting.authority.model.root.MenuInfo;
import com.wanmi.sbc.setting.authority.service.MenuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MenuInfoController implements MenuInfoProvider {
    @Autowired
    private MenuInfoService menuInfoService;


    @Override
    public BaseResponse addMenuInfo(@RequestBody MenuAddRequest request) {
        MenuInfo menuInfo = new MenuInfo();

        KsBeanUtil.copyPropertiesThird(request, menuInfo);
        menuInfoService.insertMenu(menuInfo);

        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifyMenuInfo(@RequestBody @Valid MenuModifyRequest request) {
        MenuInfo menuInfo = new MenuInfo();

        KsBeanUtil.copyPropertiesThird(request, menuInfo);
        menuInfoService.updateMenu(menuInfo);

        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteMenuInfo(@RequestBody @Valid MenuDeleteRequest request) {
        menuInfoService.deleteMenu(request.getMenuId());

        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse addFunction(@RequestBody FunctionInfoAddRequest request) {
        FunctionInfo info = new FunctionInfo();

        KsBeanUtil.copyPropertiesThird(request, info);

        menuInfoService.insertFunction(info);

        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifyFunction(@RequestBody @Valid FunctionModifyRequest request) {
        FunctionInfo info = new FunctionInfo();

        KsBeanUtil.copyPropertiesThird(request, info);

        menuInfoService.updateFunction(info);

        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteFunction(@RequestBody @Valid FunctionDeleteRequest request) {
        menuInfoService.deleteFunction(request.getFunctionId());

        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse addAuthority(@RequestBody AuthorityAddRequest request) {
        Authority authority = new Authority();

        KsBeanUtil.copyPropertiesThird(request, authority);

        menuInfoService.insertAuthority(authority);

        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifyAuthority(@RequestBody @Valid AuthorityModifyRequest request) {
        Authority authority = new Authority();

        KsBeanUtil.copyPropertiesThird(request, authority);

        menuInfoService.updateAuthority(authority);

        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteAuthority(@RequestBody @Valid AuthorityDeleteRequest request) {
        menuInfoService.deleteAuthority(request.getAuthorityId());

        return BaseResponse.SUCCESSFUL();
    }
}
