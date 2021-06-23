package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.AboutUsModifyRequest;
import com.wanmi.sbc.setting.api.request.AppShareSettingModifyRequest;
import com.wanmi.sbc.setting.api.request.AppUpgradeModifyRequest;
import com.wanmi.sbc.setting.api.response.AppShareSettingGetResponse;
import com.wanmi.sbc.setting.api.response.AppUpgradeGetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午10:39 2018/11/19
 * @Description: 移动端设置provider
 */
@FeignClient(value = "${application.setting.name}", contextId = "MobileSettingProvider")
public interface MobileSettingProvider {

    /**
     * 修改app分享配置
     *
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/mobile/modify-app-share-setting")
    BaseResponse modifyAppShareSetting(@RequestBody AppShareSettingModifyRequest request);

    /**
     * 查询app分享配置
     *
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/mobile/get-app-share-setting")
    BaseResponse<AppShareSettingGetResponse> getAppShareSetting();

    /**
     * 修改APP检测升级配置
     *
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/mobile/modify-app-upgrade-setting")
    BaseResponse modifyAppUpgrade(@RequestBody AppUpgradeModifyRequest request);

    /**
     * 查询APP升级版本配置信息
     *
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/mobile/get-app-upgrade-setting")
    BaseResponse<AppUpgradeGetResponse> getAppUpgrade();

    /**
     * 修改关于我们
     *
     * @param aboutUsModifyRequest
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/mobile/modify-about-us")
    BaseResponse modifyAboutUs(@RequestBody AboutUsModifyRequest aboutUsModifyRequest);

    /**
     * 查询关于我们
     *
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/mobile/get-about-us")
    BaseResponse<String> getAboutUs();
}
