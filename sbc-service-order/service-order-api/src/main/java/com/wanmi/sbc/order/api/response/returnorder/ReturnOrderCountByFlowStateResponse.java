package com.wanmi.sbc.order.api.response.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 根据状态统计退单响应结构
 * Created by sunkun on 2017/9/18.
 */
@Data
@ApiModel
public class ReturnOrderCountByFlowStateResponse implements Serializable {

    private static final long serialVersionUID = -7985134715113803775L;

    /**
     * 待审核
     */
    @ApiModelProperty(value = "待审核")
    private long waitAudit;

    /**
     * 待填写物流
     */
    @ApiModelProperty(value = "待填写物流")
    private long waitFillLogistics;

    /**
     * 待收货
     */
    @ApiModelProperty(value = "待收货")
    private long waitReceiving;

    /**
     * 待退款
     */
    @ApiModelProperty(value = "待退款")
    private long waitRefund = 0;
}
