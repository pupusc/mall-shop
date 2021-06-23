package com.wanmi.sbc.popupadministration;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.popupadministration.PopupAdministrationQueryProvider;
import com.wanmi.sbc.setting.api.request.popupadministration.PageManagementRequest;
import com.wanmi.sbc.setting.api.response.popupadministration.PageManagementResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>弹窗管理服务API</p>
 *
 * @author weiwenhao
 * @date 2020-04-23
 */
@RestController
@Api(description = "弹窗管理查询服务API", tags = "PopupAdministrationQueryController")
@RequestMapping("/popup_administration")
public class PopupAdministrationQueryController {


    @Autowired
    private PopupAdministrationQueryProvider popupAdministrationQueryProvider;


    /**
     * 弹窗管理&页面管理列表查询
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "弹窗管理&页面管理列表查询")
    @PostMapping("/page_management_popup_administration")
    BaseResponse<PageManagementResponse> pageManagementAndPopupAdministrationList(@RequestBody @Valid PageManagementRequest request) {
        return popupAdministrationQueryProvider.pageManagementAndPopupAdministrationList(request);
    }

}
