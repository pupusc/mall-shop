package com.wanmi.sbc.order.api.response.orderinvoice;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.order.bean.vo.OrderInvoiceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class OrderInvoiceFindAllResponse implements Serializable{

    @ApiModelProperty(value = "订单开票分页信息")
    MicroServicePage<OrderInvoiceVO> value;
}
