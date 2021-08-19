package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by feitingting on 2019/1/4.
 */
@ApiModel
@Data
public class MiniProgramSetContextRequest {
    /**
     * APPID
     */
    @ApiModelProperty(value = "APPID")
    private String appId;
    /**
     * APPSecret
     */
    @ApiModelProperty(value = "APPSecret")
    private String appSecret;

}
