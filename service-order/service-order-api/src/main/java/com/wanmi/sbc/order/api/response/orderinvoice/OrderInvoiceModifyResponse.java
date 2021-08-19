package com.wanmi.sbc.order.api.response.orderinvoice;

import com.wanmi.sbc.order.bean.vo.OrderInvoiceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-30 16:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class OrderInvoiceModifyResponse implements Serializable {

    @ApiModelProperty(value = "订单开票信息")
    private OrderInvoiceVO orderInvoiceVO;

}
