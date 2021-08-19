package com.wanmi.sbc.todo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商家待处理面板
 * Created by daiyitian on 2017/9/18.
 */
@ApiModel
@Data
public class BossTodoResponse {

    /**
     * 待审核商家
     */
    @ApiModelProperty(value = "待审核商家")
    private long waitSupplier;

    /**
     * 待审核商品
     */
     @ApiModelProperty(value = "待审核商品")
    private long waitGoods;

    /**
     * 待付款
     */
     @ApiModelProperty(value = "待付款")
    private long waitPay;

    /**
     * 待退款
     */
     @ApiModelProperty(value = "待退款")
    private long waitRefund;

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
     * 待结算账单
     */
     @ApiModelProperty(value = "待结算账单")
    private long waitSettle;

    /**
     * 商品审核开关 true:开启 false:关闭
     */
     @ApiModelProperty(value = "商品审核开关")
    private boolean checkGoodsFlag = false;

    /**
     * 客户审核开关 true:开启 false:关闭
     */
     @ApiModelProperty(value = "客户审核开关")
    private boolean checkCustomerFlag = false;

    /**
     * 增票资质审核开关 true:开启 false:关闭
     */
     @ApiModelProperty(value = "增票资质审核开关")
    private boolean checkCustomerInvoiceFlag = false;
}
