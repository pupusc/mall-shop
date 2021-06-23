package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.RoleMenuQueryProvider;
import com.wanmi.sbc.setting.api.request.*;
import com.wanmi.sbc.setting.api.response.*;
import com.wanmi.sbc.setting.authority.service.RoleMenuService;
import com.wanmi.sbc.setting.bean.vo.RoleInfoAndMenuInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RoleMenuQueryController implements RoleMenuQueryProvider {
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public BaseResponse<RoleMenuFuncIdsQueryResponse> queryRoleMenuFuncIds(@RequestBody @Valid RoleMenuFuncIdsQueryRequest request) {

        return BaseResponse.success(roleMenuService.queryRoleMenuFuncIds(request.getRoleInfoId()));
    }

    @Override
    public BaseResponse<RoleMenuInfoListResponse> listRoleMenuInfo(@RequestBody @Valid RoleMenuInfoListRequest request) {
        RoleMenuInfoListResponse response = new RoleMenuInfoListResponse();
        response.setMenuInfoVOList(roleMenuService.queryRoleMenuInfoList(request.getRoleInfoId()));

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<AllRoleMenuInfoListResponse> listAllRoleMenuInfo(@RequestBody AllRoleMenuInfoListRequest request) {
        AllRoleMenuInfoListResponse response = new AllRoleMenuInfoListResponse();
        response.setMenuInfoVOList(roleMenuService.queryAllRoleMenuInfoList(request.getSystemTypeCd()));
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<AuthorityListResponse> listAuthority(@RequestBody @Valid AuthorityListRequest request) {
        AuthorityListResponse response = new AuthorityListResponse();
        response.setAuthorityVOList(roleMenuService.hasAuthorityList(request.getRoleInfoId()));
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<FunctionListResponse> listFunction(@RequestBody FunctionListRequest request) {
        FunctionListResponse response = new FunctionListResponse();
        response.setFunctionList(roleMenuService.hasFunctionList(request.getRoleInfoId(), request.getAuthorityNames()));

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<FunctionsByRoleIdListResponse> listFunctionsByRoleId(@RequestBody FunctionListByRoleIdRequest request) {
        FunctionsByRoleIdListResponse response = new FunctionsByRoleIdListResponse();
        response.setFunctionList(roleMenuService.queryFunctionsByRoleId(request.getRoleId(), request.getSystemTypeCd()));

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<RoleInfoAndMenuInfoListResponse> listRoleWithMenuNames(@RequestBody @Valid RoleInfoAndMenuInfoListRequest request){
        List<RoleInfoAndMenuInfoVO> list = roleMenuService.listRoleWithMenuNames(request.getRoleInfoIdList());
        return BaseResponse.success(new RoleInfoAndMenuInfoListResponse(list));
    }
}
