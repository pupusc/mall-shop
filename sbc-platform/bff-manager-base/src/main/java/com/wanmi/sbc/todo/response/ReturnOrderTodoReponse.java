package com.wanmi.sbc.todo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sunkun on 2017/9/18.
 */
@ApiModel
@Data
public class ReturnOrderTodoReponse {

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
