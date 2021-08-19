package com.wanmi.sbc.setting.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 成长值开关
 */
@Data
public class SystemLiveOpenResponse implements Serializable {
    private static final long serialVersionUID = 5308059885778234169L;
    /**
     * 1 true:开启 0 false:关闭
     */
    @ApiModelProperty(value = "成长值开关-true:开启 false:关闭")
    private boolean open;


}