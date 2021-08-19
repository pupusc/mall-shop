package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.*;
import com.wanmi.sbc.setting.api.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.setting.name}", contextId = "RoleMenuQueryProvider")
public interface RoleMenuQueryProvider {
    /**
     * 查询角色拥有的菜单id和功能id信息
     *
     * @param request {@link RoleMenuFuncIdsQueryRequest}
     * @return 拥有的菜单id和功能id {@link RoleMenuFuncIdsQueryResponse}
     */
    @PostMapping("/setting/${application.setting.version}/authority/query-role-menu-func-ids")
    BaseResponse<RoleMenuFuncIdsQueryResponse> queryRoleMenuFuncIds(@RequestBody RoleMenuFuncIdsQueryRequest request);

    /**
     * 查询角色拥有的菜单信息
     *
     * @param request {@link RoleMenuInfoListRequest}
     * @return 角色拥有的菜单列表 {@link RoleMenuInfoListResponse}
     */
    @PostMapping("/setting/${application.setting.version}/authority/list-role-menu-info")
    BaseResponse<RoleMenuInfoListResponse> listRoleMenuInfo(@RequestBody RoleMenuInfoListRequest request);

    /**
     * 查询所有的菜单信息
     *
     * @param request {@link AllRoleMenuInfoListRequest}
     * @return 不同平台拥有的所有的菜单信息 {@link AllRoleMenuInfoListResponse}
     */
    @PostMapping("/setting/${application.setting.version}/authority/list-all-role-menu-info")
    BaseResponse<AllRoleMenuInfoListResponse> listAllRoleMenuInfo(@RequestBody AllRoleMenuInfoListRequest request);

    /**
     * 查询某角色的所有权限
     *
     * @param request {@link AuthorityListRequest}
     * @return 角色的所有权限 {@link AuthorityListResponse}
     */
    @PostMapping("/setting/${application.setting.version}/authority/list-authority")
    BaseResponse<AuthorityListResponse> listAuthority(@RequestBody AuthorityListRequest request);

    /**
     * 根据角色/功能名称list查询拥有的功能名称list
     *
     * @param request {@link FunctionListRequest}
     * @return 功能列表 {@link FunctionListResponse}
     */
    @PostMapping("/setting/${application.setting.version}/authority/list-function")
    BaseResponse<FunctionListResponse> listFunction(@RequestBody FunctionListRequest request);

    /**
     * 根据roleId查询对应角色下所有的功能
     *
     * @param request {@link FunctionListByRoleIdRequest}
     * @return 对应角色下所有的功能 {@link FunctionsByRoleIdListResponse}
     */
    @PostMapping("/setting/${application.setting.version}/authority/list-functions-by-role-id")
    BaseResponse<FunctionsByRoleIdListResponse> listFunctionsByRoleId(@RequestBody FunctionListByRoleIdRequest request);

    /**
     * 根据角色ID集合查询一级菜单权限集合
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/authority/list-role-with-menu-names")
    BaseResponse<RoleInfoAndMenuInfoListResponse> listRoleWithMenuNames(@RequestBody @Valid RoleInfoAndMenuInfoListRequest request);

}
