package com.wanmi.sbc.setting.api.provider.popupadministration;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.popupadministration.*;
import com.wanmi.sbc.setting.api.response.popupadministration.PopupAdministrationDeleteResponse;
import com.wanmi.sbc.setting.api.response.popupadministration.PopupAdministrationPauseResponse;
import com.wanmi.sbc.setting.api.response.popupadministration.PopupAdministrationResponse;
import com.wanmi.sbc.setting.api.response.popupadministration.PopupAdministrationStartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>弹窗管理Provider</p>
 * @author weiwenhao
 * @date 2020-04-21
 */
@FeignClient(value = "${application.setting.name}",contextId = "PopupAdministrationProvider")
public interface PopupAdministrationProvider {


    /**
     * 新增弹窗
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/popup_administration/add")
    BaseResponse<PopupAdministrationResponse> add(@RequestBody @Valid PopupAdministrationRequest request);

    /**
     * 编辑弹窗管理
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/popup_administration/modify")
    BaseResponse<PopupAdministrationResponse> modify(@RequestBody @Valid PopupAdministrationModifyRequest request);

    /**
     * 删除弹窗
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/popup_administration/delete")
    BaseResponse<PopupAdministrationDeleteResponse> delete(@RequestBody @Valid PopupAdministrationDeleteRequest request);

    /**
     * 暂停弹窗
     * @param popupAdministrationId
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/popup_administration/pause_popup_administration")
    BaseResponse<PopupAdministrationPauseResponse> pausePopupAdministration(@RequestBody @Valid PopupAdministrationPauseRequest popupAdministrationId);


    /**
     * 启动弹窗
     * @param popupAdministrationId
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/popup_administration/start_popup_administration")
    BaseResponse<PopupAdministrationStartResponse> startPopupAdministration(@RequestBody @Valid PopupAdministrationStartRequest popupAdministrationId);

    /**
     * 弹窗在应用页面的排序
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/popup_administration/sort_popup_administration")
    BaseResponse sortPopupAdministration(@RequestBody @Valid List<PopupAdministrationSortRequest> request);


}
