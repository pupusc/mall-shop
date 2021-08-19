package com.wanmi.sbc.customer.api.request.account;

import com.wanmi.sbc.customer.bean.dto.CustomerAccountAddOrModifyDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会员银行账户-根据employeeId修改Request
 */
@ApiModel
@Data
public class CustomerAccountModifyRequest extends CustomerAccountAddOrModifyDTO{

    private static final long serialVersionUID = -1469274484762938357L;

    @ApiModelProperty(value = "操作人员id")
    private String employeeId;
}
