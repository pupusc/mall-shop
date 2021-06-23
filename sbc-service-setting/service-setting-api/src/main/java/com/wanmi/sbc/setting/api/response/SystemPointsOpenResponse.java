package com.wanmi.sbc.setting.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 积分开关
 */
@Data
public class SystemPointsOpenResponse implements Serializable {
    private static final long serialVersionUID = -7087214621356886419L;

    /**
     * 1 true:开启 0 false:关闭
     */
    @ApiModelProperty(value = "积分开关-true:开启 false:关闭")
    private boolean open;
}