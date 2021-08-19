package com.wanmi.sbc.customer.api.response.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class EmployeeMobileSmsResponse implements Serializable {

    private static final long serialVersionUID = -6018656420135546535L;

    //true:可以发送，false:
    @ApiModelProperty(value = "是否可以发送验证码")
    protected boolean isSendSms;
}
