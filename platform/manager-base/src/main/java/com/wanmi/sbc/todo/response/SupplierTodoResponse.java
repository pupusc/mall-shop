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
public class SupplierTodoResponse {

    /**
     * 待开票订单
     */
    @ApiModelProperty(value = "待开票订单")
    private long waitInvoice;

    /**
     * 待审核商品
     */
    @ApiModelProperty(value = "待审核商品")
    private long waitGoods;

    /**
     * 待结算账单
     */
    @ApiModelProperty(value = "待结算账单")
    private long waitSettle;

    /**
     * 商品审核开关 true:开启 false:关闭
     */
    @ApiModelProperty(value = "商品审核开关", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private boolean checkGoodsFlag = false;
}
