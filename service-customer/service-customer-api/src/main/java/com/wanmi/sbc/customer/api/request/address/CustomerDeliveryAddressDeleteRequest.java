package com.wanmi.sbc.customer.api.request.address;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 会员收货地址-根据收货地址ID删除Request
 */
@ApiModel
@Data
public class CustomerDeliveryAddressDeleteRequest extends CustomerBaseRequest implements Serializable {


    private static final long serialVersionUID = -5678141403090382444L;

    @ApiModelProperty(value = "收货地址ID")
    @NotBlank
    private String addressId;
}
