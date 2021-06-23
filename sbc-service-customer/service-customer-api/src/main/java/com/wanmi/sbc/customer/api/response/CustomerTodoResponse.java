package com.wanmi.sbc.customer.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sunkun on 2017/9/18.
 */
@ApiModel
@Data
public class CustomerTodoResponse {

    /**
     * 待审核客户
     */
    @ApiModelProperty(value = "待审核客户")
    private long waitAuditCustomer;

    /**
     * 待审核增票资质
     */
    @ApiModelProperty(value = "待审核增票资质")
    private long waitAuditCustomerInvoice;

    /**
     * 待审核开票订单
     */
    @ApiModelProperty(value = "待审核开票订单")
    private long waitAuditOrderInvoice;

}
