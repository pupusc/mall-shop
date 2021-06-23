package com.wanmi.sbc.third.login.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel
@Data
@Builder
public class WechatSetDetailResponse {

    /**
     * 微信appId
     */
    @ApiModelProperty(value = "微信appId")
    private String appId;
}
