package com.wanmi.sbc.customer.api.request.account;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 会员银行账户-根据用户ID查询Request
 * @Author: wanggang
 * @CreateDate: 2018/9/11 11:05
 * @Version: 1.0
 */
@ApiModel
@Data
public class CustomerAccountListRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 1063301624839083590L;

    @ApiModelProperty(value = "会员标识UUID")
    @NotBlank
    private String customerId;
}
