package com.wanmi.sbc.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhangjin on 2017/5/3.
 */
@ApiModel
@Data
public class RefuseReasonRequest implements Serializable{

    /**
     * 退款单主键
     */
    @ApiModelProperty(value = "退款单主键")
    private String refundId;

    /**
     * 拒绝原因
     */
    @ApiModelProperty(value = "拒绝原因")
    private String refuseReason;
}
