package com.wanmi.sbc.account.api.response.customerdrawcash;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 会员提现管理 状态统计对象
 * @author chenyufei
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDrawCashStatusResponse implements Serializable {

    @ApiModelProperty(value = "待审核总数")
    private Integer waitAuditTotal;

    @ApiModelProperty(value = "已完成总数")
    private Integer finishTotal;

    @ApiModelProperty(value = "提现失败总数")
    private Integer drawCashFailedTotal;

    @ApiModelProperty(value = "审核未通过总数")
    private Integer auditNotPassTotal;

    @ApiModelProperty(value = "已取消总数")
    private Integer canceledTotal;
}
