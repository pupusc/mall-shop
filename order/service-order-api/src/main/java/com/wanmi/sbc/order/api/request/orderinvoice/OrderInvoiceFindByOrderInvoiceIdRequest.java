package com.wanmi.sbc.order.api.request.orderinvoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel
public class OrderInvoiceFindByOrderInvoiceIdRequest {

    @ApiModelProperty(value = "id")
    String id;

}
