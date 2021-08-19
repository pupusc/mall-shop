package com.wanmi.sbc.customer.api.response.address;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: wanggang
 * @createDate: 2018/12/5 11:30
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDeliveryAddressByCustomerIdResponse implements Serializable {
    private static final long serialVersionUID = 1408081364315056219L;

    @ApiModelProperty(value = "总数")
    private Integer result;
}
