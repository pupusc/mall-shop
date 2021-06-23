package com.wanmi.sbc.customer.api.response.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: wanggang
 * @createDate: 2018/12/5 11:25
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccountModifyByCustomerIdResponse implements Serializable {
    private static final long serialVersionUID = 1238885230641643967L;

    @ApiModelProperty(value = "银行账号总数")
    private Integer result;
}
