package com.wanmi.sbc.setting.provider.impl.popupadministration;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.popupadministration.PopupAdministrationQueryProvider;
import com.wanmi.sbc.setting.api.request.popupadministration.PageManagementGetIdRequest;
import com.wanmi.sbc.setting.api.request.popupadministration.PageManagementRequest;
import com.wanmi.sbc.setting.api.request.popupadministration.PopupAdministrationPageRequest;
import com.wanmi.sbc.setting.api.response.popupadministration.PageManagementResponse;
import com.wanmi.sbc.setting.api.response.popupadministration.PopupAdministrationPageResponse;
import com.wanmi.sbc.setting.api.response.popupadministration.PopupAdministrationResponse;
import com.wanmi.sbc.setting.bean.vo.PopupAdministrationVO;
import com.wanmi.sbc.setting.popupadministration.model.PopupAdministration;
import com.wanmi.sbc.setting.popupadministration.service.PopupAdministrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>弹窗管理服务接口实现</p>
 * @author weiwnehao
 * @date 2020-04-21
 */
@RestController
public class PopupAdministrationQueryController implements PopupAdministrationQueryProvider {

    @Autowired
    private PopupAdministrationService popupAdministrationService;

    /**
     * 分页&搜索查询
     * @param request
     * @return
     */
    @Override
    public BaseResponse<PopupAdministrationPageResponse> page(@Valid PopupAdministrationPageRequest request) {
        MicroServicePage<PopupAdministrationVO> microServicePage= popupAdministrationService.page(request);
        return BaseResponse.success(PopupAdministrationPageResponse.builder().popupAdministrationVOS(microServicePage).build()) ;
    }

    /**
     * 根据id查询弹窗管理
     * @param request
     * @return
     */
    @Override
    public BaseResponse<PopupAdministrationResponse> getPopupAdministrationById(@Valid PageManagementGetIdRequest request) {
         PopupAdministration popupAdministratio=
                 popupAdministrationService.getPopupAdministrationById(request.getPopupId());
         PopupAdministrationVO popupAdministrationVO=new PopupAdministrationVO();
        KsBeanUtil.copyPropertiesThird(popupAdministratio,popupAdministrationVO);
        return BaseResponse.success(new PopupAdministrationResponse(popupAdministrationVO));
    }

    /**
     * 弹窗管理&页面管理查询
     * @param request
     * @return
     */

    @Override
    public BaseResponse<PageManagementResponse> pageManagementAndPopupAdministrationList(@Valid PageManagementRequest request) {
        return popupAdministrationService.pageManagementAndPopupAdministrationList(request);
    }
}
