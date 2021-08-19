package com.wanmi.sbc.customer.api.request.loginregister;

import com.wanmi.sbc.customer.bean.enums.SmsTemplate;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 会员登录注册-发送手机验证码Request
 */
@ApiModel
@Data
public class CustomerSendMobileCodeRequest extends CustomerBaseRequest implements Serializable {


    private static final long serialVersionUID = 4644856082311634161L;
    @ApiModelProperty(value = "存入redis的验证码key")
    @NotBlank
    private String redisKey;

    @ApiModelProperty(value = "要发送短信的手机号码")
    @NotBlank
    private String mobile;

    @ApiModelProperty(value = "短信内容模版")
    @NotNull
    private SmsTemplate smsTemplate;
}
