package com.wanmi.sbc.third.share.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class TicketResponse {

    @ApiModelProperty(value = "字符串")
    private String nonceStr;

    @ApiModelProperty(value = "时间戳")
    private String timestamp;

    @ApiModelProperty(value = "加密信息")
    private String signature;

    @ApiModelProperty(value = "url")
    private String url;

    @ApiModelProperty(value = "appId")
    private String appId;
}
