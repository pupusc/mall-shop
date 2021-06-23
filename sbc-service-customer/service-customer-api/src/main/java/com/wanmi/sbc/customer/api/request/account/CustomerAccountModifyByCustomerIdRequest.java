package com.wanmi.sbc.customer.api.request.account;

import com.wanmi.sbc.customer.bean.dto.CustomerAccountAddOrModifyDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会员银行账户-根据用户ID修改Request
 */
@ApiModel
@Data
public class CustomerAccountModifyByCustomerIdRequest extends CustomerAccountAddOrModifyDTO{

    private static final long serialVersionUID = 4077911873391050426L;

    @ApiModelProperty(value = "会员标识UUID")
    private String customerId;
}
