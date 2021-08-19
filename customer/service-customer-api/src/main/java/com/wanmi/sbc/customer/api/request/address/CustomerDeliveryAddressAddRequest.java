package com.wanmi.sbc.customer.api.request.address;

import com.wanmi.sbc.customer.bean.dto.CustomerDeliveryAddressDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会员收货地址-根据employeeId查询Request
 */
@ApiModel
@Data
public class CustomerDeliveryAddressAddRequest extends CustomerDeliveryAddressDTO {


    private static final long serialVersionUID = 1483284220871887310L;

    @ApiModelProperty(value = "操作人员id")
    private String employeeId;
}
