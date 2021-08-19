package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by feitingting on 2019/1/4.
 */
@ApiModel
@Data
public class MiniProgramSetRequest  {
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
    /**
     * 小程序配置的启用状态
     */
    @ApiModelProperty(value = "小程序配置的启用状态", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private  Integer status;
}
