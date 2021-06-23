package com.wanmi.sbc.customer.api.request.employee;

import com.wanmi.sbc.customer.bean.enums.SmsTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeSmsRequest implements Serializable {

    private static final long serialVersionUID = -2277400040469369568L;
    /**
     * 存入redis的验证码key
     */
    @ApiModelProperty(value = "存入redis的验证码key")
    @NotBlank
    private String redisKey;

    /**
     * 要发送短信的手机号码
     */
    @ApiModelProperty(value = "要发送短信的手机号码")
    @NotBlank
    private String mobile;

    /**
     * 短信内容模版
     */
    @ApiModelProperty(value = "短信内容模版")
    @NotNull
    private SmsTemplate smsTemplate;

}
