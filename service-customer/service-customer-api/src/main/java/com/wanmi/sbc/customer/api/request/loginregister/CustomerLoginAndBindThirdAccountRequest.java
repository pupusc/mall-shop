package com.wanmi.sbc.customer.api.request.loginregister;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.dto.ThirdLoginRelationDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 会员登录注册-绑定第三方账号Request
 */
@ApiModel
@Data
public class CustomerLoginAndBindThirdAccountRequest extends CustomerBaseRequest implements Serializable {

    private static final long serialVersionUID = 4369815856432441487L;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    @NotBlank
    private String  phone;

    @ApiModelProperty(value = "第三方登录-共用DTO")
    @NotNull
    private ThirdLoginRelationDTO thirdLoginRelationDTO;

    @ApiModelProperty(value = "分享人用户id")
    private String shareUserId;
}
