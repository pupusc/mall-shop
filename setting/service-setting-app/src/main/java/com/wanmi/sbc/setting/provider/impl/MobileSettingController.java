package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.MobileSettingProvider;
import com.wanmi.sbc.setting.api.request.AboutUsModifyRequest;
import com.wanmi.sbc.setting.api.request.AppShareSettingModifyRequest;
import com.wanmi.sbc.setting.api.request.AppUpgradeModifyRequest;
import com.wanmi.sbc.setting.api.response.AppShareSettingGetResponse;
import com.wanmi.sbc.setting.api.response.AppUpgradeGetResponse;
import com.wanmi.sbc.setting.mobile.MobileSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午10:41 2018/11/19
 * @Description: 移动端设置controller
 */
@RestController
public class MobileSettingController implements MobileSettingProvider {

    @Autowired
    MobileSettingService mobileService;

    /**
     * 修改app分享配置
     */
    @Override
    public BaseResponse modifyAppShareSetting(@RequestBody @Valid AppShareSettingModifyRequest request) {
        mobileService.modifyAppShareSetting(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询app分享配置
     */
    @Override
    public BaseResponse<AppShareSettingGetResponse> getAppShareSetting() {
        AppShareSettingGetResponse response = mobileService.getAppShareSetting();
        return BaseResponse.success(response);
    }

    /**
     * 修改APP检测升级配置
     * @param request 配置内容
     */
    @Override
    public BaseResponse modifyAppUpgrade(@RequestBody AppUpgradeModifyRequest request) {
        mobileService.modifyAppUpgrade(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询APP升级版本配置信息
     * @return
     */
    @Override
    public BaseResponse<AppUpgradeGetResponse> getAppUpgrade() {
        AppUpgradeGetResponse response = mobileService.getAppUpgrade();
        return BaseResponse.success(response);
    }

    /**
     * 修改关于我们
     * @param aboutUsModifyRequest
     * @return
     */
    @Override
    public BaseResponse modifyAboutUs(@RequestBody AboutUsModifyRequest aboutUsModifyRequest) {
        mobileService.modifyAboutUs(aboutUsModifyRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询关于我们
     * @return
     */
    @Override
    public BaseResponse<String> getAboutUs() {
        return BaseResponse.success(mobileService.getAboutUs());
    }
}
