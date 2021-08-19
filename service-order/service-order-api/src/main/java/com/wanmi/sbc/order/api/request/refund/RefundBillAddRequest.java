package com.wanmi.sbc.order.api.request.refund;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.Operator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 退款单
 * Created by zhangjin on 2017/4/21.
 */
@Data
@ApiModel
public class RefundBillAddRequest extends BaseQueryRequest {

    /**
     * 退款单外键
     */
    @ApiModelProperty(value = "退款单外键")
    private String refundId;

    /**
     * 线下账户
     */
    @ApiModelProperty(value = "线下账户")
    private Long offlineAccountId;

    /**
     * 退款评论
     */
    @ApiModelProperty(value = "退款评论")
    private String refundComment;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    /**
     * 实付金额
     */
    @ApiModelProperty(value = "实付金额")
    private BigDecimal actualReturnPrice;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Operator operator;
}
