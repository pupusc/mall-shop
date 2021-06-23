package com.wanmi.sbc.customer.api.request.invoice;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 会员增票资质-根据用户ID查询Request
 */
@ApiModel
@Data
public class CustomerInvoiceByCustomerIdRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 4047547995430811745L;
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    @NotBlank
    private String customerId;

}
