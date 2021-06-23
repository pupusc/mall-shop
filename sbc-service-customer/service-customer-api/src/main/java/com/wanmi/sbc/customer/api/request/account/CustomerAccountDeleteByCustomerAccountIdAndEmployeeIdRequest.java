package com.wanmi.sbc.customer.api.request.account;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 会员银行账户-根据银行账号id和employeeId删除Request
 * @Author: wanggang
 * @CreateDate: 2018/9/11 11:05
 * @Version: 1.0
 */
@ApiModel
@Data
public class CustomerAccountDeleteByCustomerAccountIdAndEmployeeIdRequest extends CustomerBaseRequest{

    private static final long serialVersionUID = 2833920091421796352L;

    @ApiModelProperty(value = "客户银行账号ID")
    @NotBlank
    private String customerAccountId;

    @ApiModelProperty(value = "操作人员id")
    @NotBlank
    private String employeeId;
}
