package com.wanmi.sbc.customer.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改绑定手机号请求参数
 * Created by CHENLI on 2017/7/24.
 */
@ApiModel
@Data
public class CustomerMobileRequest {

    /**
     * 用户编号
     */
    @ApiModelProperty(value = "客户详情id")
    private String customerId;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String customerAccount;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String verifyCode;

    /**
     * 旧验证码
     */
    @ApiModelProperty(value = "旧验证码")
    private String oldVerifyCode;
}
