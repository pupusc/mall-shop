package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.MenuAndAuthorityListRequest;
import com.wanmi.sbc.setting.api.request.MenuAndFunctionListRequest;
import com.wanmi.sbc.setting.api.response.MenuAndAuthorityListResponse;
import com.wanmi.sbc.setting.api.response.MenuAndFunctionListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.setting.name}", contextId = "MenuInfoQueryProvider")
public interface MenuInfoQueryProvider {

    /**
     * 查询系统所有的菜单,功能信息
     *
     * @param request {@link MenuAndFunctionListRequest}
     * @return 菜单及功能列表 {@link MenuAndFunctionListResponse}
     */
    @PostMapping("/setting/${application.setting.version}/authority/list-menu-and-function")
    BaseResponse<MenuAndFunctionListResponse> listMenuAndFunction(@RequestBody MenuAndFunctionListRequest request);

    /**
     * 查询系统所有的菜单,功能,权限信息
     *
     * @param request {@link MenuAndAuthorityListRequest}
     * @return 统所有的菜单,功能,权限 {@link MenuAndAuthorityListResponse}
     */
    @PostMapping("/setting/${application.setting.version}/authority/list-menu-and-authority")
    BaseResponse<MenuAndAuthorityListResponse> listMenuAndAuthority(@RequestBody MenuAndAuthorityListRequest request);
}
