package com.wanmi.sbc.order.api.response.orderinvoice;

import com.wanmi.sbc.order.bean.vo.OrderInvoiceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class OrderInvoiceFindByOrderInvoiceIdAndDelFlagResponse {

     @ApiModelProperty(value = "订单开票信息")
     OrderInvoiceVO orderInvoiceVO;
}
