package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.bean.enums.AuditState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-05 15:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeAuditBatchRequest implements Serializable {

    /**
     * 交易id
     */
    @ApiModelProperty(value = "交易id")
    private String[] ids;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态")
    private AuditState auditState;

    /**
     * 原因
     */
    @ApiModelProperty(value = "原因")
    private String reason;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Operator operator;

}
