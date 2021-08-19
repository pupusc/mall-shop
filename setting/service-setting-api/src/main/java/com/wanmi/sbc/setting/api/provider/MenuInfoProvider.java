package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.setting.name}", contextId = "MenuInfoProvider")
public interface MenuInfoProvider {
    /**
     * 插入菜单
     *
     * @param request {@link MenuAddRequest}
     * @return BaseResponse
     */
    @PostMapping("/setting/${application.setting.version}/authority/add-menu-info")
    BaseResponse addMenuInfo(@RequestBody MenuAddRequest request);

    /**
     * 更新菜单
     *
     * @param request {@link MenuModifyRequest}
     * @return BaseResponse
     */
    @PostMapping("/setting/${application.setting.version}/authority/modify-menu-info")
    BaseResponse modifyMenuInfo(@RequestBody MenuModifyRequest request);

    /**
     * 删除菜单
     *
     * @param request {@link MenuDeleteRequest}
     * @return BaseResponse
     */
    @PostMapping("/setting/${application.setting.version}/authority/delete-menu-info")
    BaseResponse deleteMenuInfo(@RequestBody MenuDeleteRequest request);

    /**
     * 插入功能
     *
     * @param request {@link FunctionInfoAddRequest}
     * @return BaseResponse
     */
    @PostMapping("/setting/${application.setting.version}/authority/add-function")
    BaseResponse addFunction(@RequestBody FunctionInfoAddRequest request);

    /**
     * 更新功能
     *
     * @param request {@link FunctionModifyRequest}
     * @return BaseResponse
     */
    @PostMapping("/setting/${application.setting.version}/authority/modify-function")
    BaseResponse modifyFunction(@RequestBody FunctionModifyRequest request);

    /**
     * 删除功能
     *
     * @param request {@link FunctionDeleteRequest}
     * @return BaseResponse
     */
    @PostMapping("/setting/${application.setting.version}/authority/delete-function")
    BaseResponse deleteFunction(@RequestBody FunctionDeleteRequest request);

    /**
     * 插入权限
     *
     * @param request {@link AuthorityAddRequest}
     * @return BaseResponse
     */
    @PostMapping("/setting/${application.setting.version}/authority/add-authority")
    BaseResponse addAuthority(@RequestBody AuthorityAddRequest request);

    /**
     * 更新权限
     *
     * @param request {@link AuthorityModifyRequest}
     * @return BaseResponse
     */
    @PostMapping("/setting/${application.setting.version}/authority/modify-authority")
    BaseResponse modifyAuthority(@RequestBody AuthorityModifyRequest request);

    /**
     * 删除权限
     *
     * @param request {@link AuthorityDeleteRequest}
     * @return BaseResponse
     */
    @PostMapping("/setting/${application.setting.version}/authority/delete-authority")
    BaseResponse deleteAuthority(@RequestBody AuthorityDeleteRequest request);
}
