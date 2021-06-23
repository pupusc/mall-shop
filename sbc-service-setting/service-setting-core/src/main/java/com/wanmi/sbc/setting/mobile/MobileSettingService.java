package com.wanmi.sbc.setting.mobile;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.api.request.AboutUsModifyRequest;
import com.wanmi.sbc.setting.api.request.AppShareSettingModifyRequest;
import com.wanmi.sbc.setting.api.request.AppUpgradeModifyRequest;
import com.wanmi.sbc.setting.api.response.AppShareSettingGetResponse;
import com.wanmi.sbc.setting.api.response.AppUpgradeGetResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.config.Config;
import com.wanmi.sbc.setting.config.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午11:02 2018/11/16
 * @Description: 移动端设置service
 */
@Service
@Transactional(timeout = 10)
public class MobileSettingService {

    @Autowired
    private ConfigRepository configRepository;

    /**
     * 修改关于我们
     */
    @Transactional
    public void modifyAboutUs(AboutUsModifyRequest aboutUsModifyRequest) {
        configRepository.updateStatusByTypeAndConfigKey(ConfigType.ABOUT_US.toValue(), ConfigKey.MOBILE_SETTING
                .toValue(), 1, aboutUsModifyRequest.getContext());
    }

    /**
     * 查询关于我们
     */
    @Transactional
    public String getAboutUs() {
        Config config = configRepository.findByConfigTypeAndDelFlag(ConfigType.ABOUT_US.toValue(), DeleteFlag.NO);
        if (Objects.isNull(config)) {
            config = new Config();
            config.setConfigKey(ConfigKey.MOBILE_SETTING.toValue());
            config.setConfigType(ConfigType.ABOUT_US.toValue());
            config.setConfigName("关于我们");
            config.setContext("");
            config.setDelFlag(DeleteFlag.NO);
            configRepository.save(config);
        }
        return config.getContext();
    }

    /**
     * 修改APP检测升级配置
     *
     * @param request 配置内容
     */
    @Transactional
    public void modifyAppUpgrade(AppUpgradeModifyRequest request) {
        configRepository.updateStatusByTypeAndConfigKey(ConfigType.APP_UPDATE.toValue(),
                ConfigKey.MOBILE_SETTING.toValue(), 1, JSONObject.toJSONString(request));
    }

    /**
     * 查询APP升级版本配置信息
     *
     * @return
     */
    @Transactional
    public AppUpgradeGetResponse getAppUpgrade() {
        Config config = configRepository.findByConfigTypeAndDelFlag(ConfigType.APP_UPDATE.toValue(), DeleteFlag.NO);
        if (Objects.isNull(config)) {
            config = new Config();
            config.setConfigKey(ConfigKey.MOBILE_SETTING.toValue());
            config.setConfigType(ConfigType.APP_UPDATE.toValue());
            config.setConfigName("APP更新设置");
            config.setContext("");
            config.setDelFlag(DeleteFlag.NO);
            configRepository.save(config);
        }
        return JSONObject.parseObject(config.getContext(), AppUpgradeGetResponse.class);
    }

    /**
     * 修改app分享配置
     */
    @Transactional
    public void modifyAppShareSetting(AppShareSettingModifyRequest request) {
        configRepository.updateStatusByTypeAndConfigKey(ConfigType.APP_SHARE.toValue(), ConfigKey.MOBILE_SETTING
                .toValue(), 1, JSONObject.toJSONString(request));
    }

    /**
     * 查询app分享配置
     */
    @Transactional
    public AppShareSettingGetResponse getAppShareSetting() {
        Config config = configRepository.findByConfigTypeAndDelFlag(ConfigType.APP_SHARE.toValue(), DeleteFlag.NO);
        if (Objects.isNull(config)) {
            config = new Config();
            config.setConfigKey(ConfigKey.MOBILE_SETTING.toValue());
            config.setConfigType(ConfigType.APP_SHARE.toValue());
            config.setConfigName("APP分享配置");
            config.setContext("");
            config.setDelFlag(DeleteFlag.NO);
            configRepository.save(config);
        }
        return JSONObject.parseObject(config.getContext(), AppShareSettingGetResponse.class);
    }

}
