package com.wanmi.sbc.customer.api.request.address;

import com.wanmi.sbc.customer.bean.dto.CustomerDeliveryAddressDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会员收货地址-根据employeeID查询Request
 */
@ApiModel
@Data
public class CustomerDeliveryAddressModifyRequest extends CustomerDeliveryAddressDTO {


    private static final long serialVersionUID = -2776351659171503621L;

    @ApiModelProperty(value = "操作人员id")
    private String employeeId;
}
