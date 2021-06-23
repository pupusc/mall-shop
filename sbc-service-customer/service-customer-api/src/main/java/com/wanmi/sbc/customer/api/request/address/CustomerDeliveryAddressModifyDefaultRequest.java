package com.wanmi.sbc.customer.api.request.address;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 会员收货地址-根据收货地址ID和用户ID查询Request
 */
@ApiModel
@Data
public class CustomerDeliveryAddressModifyDefaultRequest extends CustomerBaseRequest implements Serializable {

    private static final long serialVersionUID = 2759090810825466838L;

    @ApiModelProperty(value = "收货地址ID")
    @NotBlank
    private String deliveryAddressId;

    @ApiModelProperty(value = "会员标识UUID")
    @NotBlank
    private String customerId;
}
