package com.wanmi.sbc.customer.api.request.address;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

/**
 * 会员收货地址-根据收货地址ID查询Request
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDeliveryAddressByIdRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 5217964293878642890L;

    /**
     * 收货地址ID
     */
    @ApiModelProperty(value = "收货地址ID")
    @NotBlank
    private String deliveryAddressId;
}
