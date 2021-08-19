package com.wanmi.sbc.third.login.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel
@Data
@Builder
public class ThirdLoginSendCodeResponse {

    @ApiModelProperty(value = "是否注册")
    private Boolean isRegister;
}
