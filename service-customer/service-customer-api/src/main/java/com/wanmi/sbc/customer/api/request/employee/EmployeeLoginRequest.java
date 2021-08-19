package com.wanmi.sbc.customer.api.request.employee;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

/**
 * 登录、注册请求参数
 * Created by chenli on 2017/11/3.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeLoginRequest extends CustomerBaseRequest {


    private static final long serialVersionUID = 7963313012591035153L;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    @NotBlank
    private String account;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @NotBlank
    private String password;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String verifyCode;

    /**
     * 账号类型(0 b2b账号 1 s2b平台端账号 2 s2b端商家账号 3 s2b供应商账号)
     */
    @ApiModelProperty(value = "账号类型")
    private Integer accountType;

    /**
     * 商家/店铺类型（0供应商，1商家）
     */
    @ApiModelProperty(value = "商家/店铺类型")
    private Integer storeType;
}
