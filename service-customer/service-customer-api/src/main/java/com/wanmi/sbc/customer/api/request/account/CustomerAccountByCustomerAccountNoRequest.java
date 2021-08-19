package com.wanmi.sbc.customer.api.request.account;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 会员银行账户-根据银行账号查询Request
 * @Author: wanggang
 * @CreateDate: 2018/9/11 11:05
 * @Version: 1.0
 */
@ApiModel
@Data
public class CustomerAccountByCustomerAccountNoRequest extends CustomerBaseRequest{

    private static final long serialVersionUID = -2696451164606307220L;

    @ApiModelProperty(value = "银行账号")
    @NotBlank
    private String customerAccountNo;
}
