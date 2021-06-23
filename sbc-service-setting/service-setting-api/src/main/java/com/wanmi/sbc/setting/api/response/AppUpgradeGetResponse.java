package com.wanmi.sbc.setting.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: chenli
 * @Date: Created In 上午10:06 2018/11/19
 * @Description: 检测升级更新配置 查询返回对象
 */
@ApiModel
@Data
public class AppUpgradeGetResponse implements Serializable {
    private static final long serialVersionUID = 476460339937433900L;

    /**
     * APP强制更新开关 0 关，不强制更新 1 开，强制更新
     */
    @ApiModelProperty(value = "APP强制更新开关-0 关，不强制更新 1 开，强制更新", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private int forceUpdateFlag;

    /**
     * 最新版本号
     */
    @ApiModelProperty(value = "最新版本号")
    private String latestVersion;

    /**
     * Android下载地址
     */
    @ApiModelProperty(value = "Android下载地址")
    private String androidAddress;

    /**
     * App下载地址
     */
    @ApiModelProperty(value = "App下载地址")
    private String appAddress;

    /**
     * 更新描述
     */
    @ApiModelProperty(value = "更新描述")
    private String upgradeDesc;

}
