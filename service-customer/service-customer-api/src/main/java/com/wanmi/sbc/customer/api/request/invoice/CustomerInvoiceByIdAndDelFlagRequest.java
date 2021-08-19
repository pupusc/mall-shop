package com.wanmi.sbc.customer.api.request.invoice;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 会员增票资质-根据增票资质ID查询Request
 */
@ApiModel
@Data
public class CustomerInvoiceByIdAndDelFlagRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 4047547995430811745L;
    /**
     * 增票ID
     */
    @ApiModelProperty(value = "增票ID")
    @NotNull
    private Long customerInvoiceId;

}
