package com.wanmi.sbc.setting.provider.impl.popupadministration;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.popupadministration.PopupAdministrationProvider;
import com.wanmi.sbc.setting.api.request.popupadministration.*;
import com.wanmi.sbc.setting.api.response.popupadministration.PopupAdministrationDeleteResponse;
import com.wanmi.sbc.setting.api.response.popupadministration.PopupAdministrationPauseResponse;
import com.wanmi.sbc.setting.api.response.popupadministration.PopupAdministrationResponse;
import com.wanmi.sbc.setting.api.response.popupadministration.PopupAdministrationStartResponse;
import com.wanmi.sbc.setting.bean.vo.PopupAdministrationVO;
import com.wanmi.sbc.setting.popupadministration.model.PopupAdministration;
import com.wanmi.sbc.setting.popupadministration.service.PopupAdministrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>弹窗管理服务接口实现</p>
 *
 * @author weiwnehao
 * @date 2020-04-21
 */
@RestController
public class PopupAdministrationController implements PopupAdministrationProvider {

    @Autowired
    private PopupAdministrationService popupAdministrationService;

    /**
     * 新增弹窗管理
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<PopupAdministrationResponse> add(@Valid PopupAdministrationRequest request) {
        return popupAdministrationService.add(request);
    }

    /**
     * 编辑弹窗管理
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<PopupAdministrationResponse> modify(@Valid PopupAdministrationModifyRequest request) {
        PopupAdministration popupAdministratio =
                popupAdministrationService.modify(request);
        PopupAdministrationVO popupAdministrationVO = new PopupAdministrationVO();
        KsBeanUtil.copyPropertiesThird(popupAdministratio, popupAdministrationVO);
        return BaseResponse.success(new PopupAdministrationResponse(popupAdministrationVO));
    }

    /**
     * 删除弹窗
     * @param request
     * @return
     */
    @Override
    public BaseResponse<PopupAdministrationDeleteResponse> delete(@Valid PopupAdministrationDeleteRequest request) {
        return BaseResponse.success(new PopupAdministrationDeleteResponse(popupAdministrationService.deletePopupAdministration(request.getPopupId())));
    }

    /**
     * 暂停弹窗
     * @param popupAdministrationId
     * @return
     */
    @Override
    public BaseResponse<PopupAdministrationPauseResponse> pausePopupAdministration(PopupAdministrationPauseRequest popupAdministrationId) {
        return BaseResponse.success(new PopupAdministrationPauseResponse(popupAdministrationService.pausePopupAdministration(popupAdministrationId.getPopupAdministrationId())));
    }

    /**
     * 启动弹窗
     * @param popupAdministrationId
     * @return
     */
    @Override
    public BaseResponse<PopupAdministrationStartResponse> startPopupAdministration(PopupAdministrationStartRequest popupAdministrationId) {
        return BaseResponse.success(new PopupAdministrationStartResponse(popupAdministrationService.startPopupAdministration(popupAdministrationId.getPopupAdministrationId())));
    }

    /**
     * 应用页面排序
     * @param request
     * @return
     */
    @Override
    public BaseResponse sortPopupAdministration(@Valid List<PopupAdministrationSortRequest> request) {
        return popupAdministrationService.sortPopupAdministration(request);
    }
}
