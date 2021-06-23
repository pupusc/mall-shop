package com.wanmi.sbc.customer;

import com.wanmi.sbc.customer.validGroups.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by dyt on 2017/7/11.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCheckRequest {

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    @NotBlank(groups = {NotCustomerAccount.class})
    private String customerAccount;


    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    @NotBlank(groups = {NotVerify.class})
    private String verifyCode;

    /**
     * 注册来源,0:注册页面，1：注册弹窗
     */
    @ApiModelProperty(value = "验证码")
    @NotNull
    private Integer fromPage;
}
