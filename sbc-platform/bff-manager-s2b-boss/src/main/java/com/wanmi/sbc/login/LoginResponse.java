package com.wanmi.sbc.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录返回
 * Created by aqlu on 15/11/28.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class LoginResponse implements Serializable{

    /**
     * jwt验证token
     */
    @ApiModelProperty(value = "jwt验证token")
    private String token;

    /**
     * 账号名称
     */
    @ApiModelProperty(value = "账号名称")
    private String accountName;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;

}
