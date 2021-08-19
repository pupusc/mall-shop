package com.wanmi.sbc.setting.api.provider.popupadministration;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.popupadministration.PageManagementGetIdRequest;
import com.wanmi.sbc.setting.api.request.popupadministration.PageManagementRequest;
import com.wanmi.sbc.setting.api.request.popupadministration.PopupAdministrationPageRequest;
import com.wanmi.sbc.setting.api.response.popupadministration.PageManagementResponse;
import com.wanmi.sbc.setting.api.response.popupadministration.PopupAdministrationPageResponse;
import com.wanmi.sbc.setting.api.response.popupadministration.PopupAdministrationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>弹窗管理Provider</p>
 * @author weiwenhao
 * @date 2020-04-21
 */
@FeignClient(value = "${application.setting.name}",contextId = "PopupAdministrationQueryProvider")
public interface PopupAdministrationQueryProvider {


    /**
     * 弹窗列表查询
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/popup_administration/page")
    BaseResponse<PopupAdministrationPageResponse> page(@RequestBody @Valid PopupAdministrationPageRequest  request);

    /**
     * 弹窗列表详情查询
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/popup_administration/popupAdministration_id")
    BaseResponse<PopupAdministrationResponse> getPopupAdministrationById(@RequestBody @Valid PageManagementGetIdRequest request);

    /**
     * 弹窗管理&页面管理列表查询
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/popup_administration/page_management_popup_administration")
    BaseResponse<PageManagementResponse>  pageManagementAndPopupAdministrationList(@RequestBody @Valid PageManagementRequest request);

}
