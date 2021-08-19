package com.wanmi.sbc.order.api.request.orderinvoice;

import com.wanmi.sbc.common.enums.DeleteFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class OrderInvoiceFindByOrderInvoiceIdAndDelFlagRequest  implements Serializable {

    @ApiModelProperty(value = "id")
    String id;

    @ApiModelProperty(value = "删除状态")
    DeleteFlag flag;

}
