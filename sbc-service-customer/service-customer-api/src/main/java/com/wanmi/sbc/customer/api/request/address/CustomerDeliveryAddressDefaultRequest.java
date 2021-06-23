package com.wanmi.sbc.customer.api.request.address;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 会员收货地址-根据用户ID查询Request
 */
@ApiModel
@Data
public class CustomerDeliveryAddressDefaultRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -9192002071367277361L;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "会员标识UUID")
    @NotBlank
    private String customerId;
}
