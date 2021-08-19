package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询用户是否包含此接口
 * Author: bail
 * Time: 2018/1/9.14:02
 */
@ApiModel
@Data
public class AuthorityRequest extends SettingBaseRequest {

    private static final long serialVersionUID = -2398353595136590782L;
    /**
     * 接口路径
     */
    @ApiModelProperty(value = "接口路径")
    private String urlPath;

    /**
     * 请求类型
     */
    @ApiModelProperty(value = "请求类型-GET,POST,PUT,DELETE")
    private String requestType;
}