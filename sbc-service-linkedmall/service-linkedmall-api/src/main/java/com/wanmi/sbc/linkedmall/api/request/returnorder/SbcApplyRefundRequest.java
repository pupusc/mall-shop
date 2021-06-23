package com.wanmi.sbc.linkedmall.api.request.returnorder;

import com.aliyuncs.linkedmall.model.v20180116.ApplyRefundRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class SbcApplyRefundRequest {


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


    @ApiModelProperty(value = "申请退款金额，单位为分")
    @NotNull
    private Long applyRefundFee;


    @ApiModelProperty(value = "退款数量，可不传")
    private Integer applyRefundCount;

    @ApiModelProperty(value = "退款原因Id")
    @NotNull
    private Long applyReasonTextId;

    @ApiModelProperty(value = "留言，卖家说明，某些原因必须留言")
    private String leaveMessage;

    @ApiModelProperty(value = "凭证图片，某些原因必须有凭证")
    private List<ApplyRefundRequest.LeavePictureList> leavePictureList;


    /**
     * 1.未收到货 2.已收到货 3.已寄回 4.未发货 5.卖家确认收货  6.已发货
     */
    @ApiModelProperty(value = "货物状态")
    @NotNull
    private Integer goodsStatus;
}
