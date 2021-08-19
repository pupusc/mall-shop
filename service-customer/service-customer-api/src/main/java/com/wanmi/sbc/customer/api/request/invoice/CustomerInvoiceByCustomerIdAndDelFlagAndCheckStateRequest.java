package com.wanmi.sbc.customer.api.request.invoice;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

/**
 * 会员增票资质-根据用户ID查询Request
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 4047547995430811745L;
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    @NotBlank
    private String customerId;

}
