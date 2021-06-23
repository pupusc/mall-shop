package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: chenli
 * @Date: Created In 上午10:06 2018/11/19
 * @Description: 检测升级更新配置 请求参数
 */
@ApiModel
@Data
public class AppUpgradeModifyRequest extends SettingBaseRequest{

    private static final long serialVersionUID = -949480343817773410L;

    /**
     * APP强制更新开关 0 关，不强制更新 1 开，强制更新
     */
    @ApiModelProperty(value = "APP强制更新开关-0 关，不强制更新 1 开，强制更新",dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    @NotNull
    private int forceUpdateFlag;

    /**
     * 最新版本号
     */
    @ApiModelProperty(value = "最新版本号")
    @NotNull
    private String latestVersion;

    /**
     * Android下载地址
     */
    @ApiModelProperty(value = "Android下载地址")
    @NotNull
    private String androidAddress;

    /**
     * App下载地址
     */
    @ApiModelProperty(value = "App下载地址")
    @NotNull
    private String appAddress;

    /**
     * 更新描述
     */
    @ApiModelProperty(value = "更新描述")
    @NotNull
    private String upgradeDesc;
}
