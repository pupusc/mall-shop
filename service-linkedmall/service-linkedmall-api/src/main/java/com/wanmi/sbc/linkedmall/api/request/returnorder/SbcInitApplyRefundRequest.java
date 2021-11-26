package com.wanmi.sbc.linkedmall.api.request.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class SbcInitApplyRefundRequest {


    @ApiModelProperty(value = "子订单号")
    @NotBlank
    private String subLmOrderId;

    @ApiModelProperty(value = "用户id")
    @NotBlank
    private String bizUid;


    /**
     * 1.仅退款  3.退货退款
     */
    @ApiModelProperty(value = "退款类型")
    @NotNull
    private Integer bizClaimType;


    /**
     * 1.未收到货 2.已收到货 3.已寄回 4.未发货 5.卖家确认收货  6.已发货
     */
    @ApiModelProperty(value = "货物状态")
    @NotNull
    private Integer goodsStatus;
}
