package com.wanmi.sbc.account.api.request.customerdrawcash;

import com.wanmi.sbc.account.bean.enums.AuditStatus;
import com.wanmi.sbc.account.bean.enums.DrawCashStatus;
import com.wanmi.sbc.account.bean.enums.FinishStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * 修改处理单状态
 */
@ApiModel
@EqualsAndHashCode()
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDrawCashModifyAuditStatusRequest {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "提现单编号")
    private String drawCashId;


    @ApiModelProperty(value = "审核状态")
    private AuditStatus auditStatus;


    @ApiModelProperty(value = "审核失败的原因")
    private String rejectReason;

    /**
     * 提现状态(0:未提现,1:提现失败,2:提现成功)
     */
    @ApiModelProperty(value = "提现状态(0:未提现,1:提现失败,2:提现成功)")
    private DrawCashStatus drawCashStatus;

    /**
     * 提现单完成状态(0:未完成,1:已完成)
     */
    @ApiModelProperty(value = "提现单完成状态(0:未完成,1:已完成)")
    private FinishStatus finishStatus;

    /**
     * 提现失败原因
     */
    @ApiModelProperty(value = "提现失败原因")
    private String drawCashFailedReason;

    /**
     * 业务编号
     */
    @ApiModelProperty(value = "业务编号")
    private String businessId;

}
