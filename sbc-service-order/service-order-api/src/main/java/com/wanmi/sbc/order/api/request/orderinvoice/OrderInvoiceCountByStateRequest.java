package com.wanmi.sbc.order.api.request.orderinvoice;

import com.wanmi.sbc.account.bean.enums.InvoiceState;
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
 * @Date: 2018-12-03 15:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class OrderInvoiceCountByStateRequest implements Serializable {

    /**
     * 商家信息id
     */
    @ApiModelProperty(value = "商家信息id")
    private Long companyInfoId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 开票状态
     */
    @ApiModelProperty(value = "开票状态")
    private InvoiceState invoiceState;

}
