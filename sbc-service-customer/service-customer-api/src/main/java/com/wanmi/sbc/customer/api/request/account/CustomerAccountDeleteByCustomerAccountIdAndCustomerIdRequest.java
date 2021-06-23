package com.wanmi.sbc.customer.api.request.account;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 会员银行账户-根据银行账号ID和用户ID删除Request
 * @Author: wanggang
 * @CreateDate: 2018/9/11 11:05
 * @Version: 1.0
 */
@ApiModel
@Data
public class CustomerAccountDeleteByCustomerAccountIdAndCustomerIdRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 4687248708578855438L;

    @ApiModelProperty(value = "客户银行账号ID")
    @NotBlank
    private String customerAccountId;

    @ApiModelProperty(value = "会员标识UUID")
    @NotBlank
    private String customerId;
}
