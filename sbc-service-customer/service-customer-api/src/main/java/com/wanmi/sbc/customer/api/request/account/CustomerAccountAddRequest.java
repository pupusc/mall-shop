package com.wanmi.sbc.customer.api.request.account;

import com.wanmi.sbc.customer.bean.dto.CustomerAccountAddOrModifyDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会员银行账户添加Request
 */
@ApiModel
@Data
public class CustomerAccountAddRequest extends CustomerAccountAddOrModifyDTO{

    private static final long serialVersionUID = 6081525156323375317L;

    @ApiModelProperty(value = "操作人员id")
    private String employeeId;
}
