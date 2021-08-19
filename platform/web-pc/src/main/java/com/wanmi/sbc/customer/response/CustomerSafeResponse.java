package com.wanmi.sbc.customer.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员绑定手机号返回
 * Created by CHENLI on 2017/7/22.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerSafeResponse {

    /**
     * 会员账号，绑定手机号
     */
    @ApiModelProperty(value = "会员账号，绑定手机号")
    private String customerAccount;

    /**
     * 密码安全级别
     */
    @ApiModelProperty(value = "密码安全级别")
    private Integer safeLevel;
}
