package com.wanmi.sbc.customer.api.request.quicklogin;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.enums.ThirdLoginType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


/**
 * 微信小程序授权信息解密
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdLoginDecodeWxAppRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -1224496566453107979L;

    /**
     * 用户Id
     */
    @ApiModelProperty(value = "密文")
    @NotNull
    private String encrypted;


    @ApiModelProperty(value = "iv")
    @NotNull
    private String iv;

    @ApiModelProperty(value = "code")
    @NotNull
    private String code;
}
