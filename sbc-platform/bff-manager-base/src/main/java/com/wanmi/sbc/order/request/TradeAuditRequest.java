package com.wanmi.sbc.order.request;


import com.wanmi.sbc.order.bean.enums.AuditState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/28.
 */
@ApiModel
@Data
public class TradeAuditRequest implements Serializable{

    private static final long serialVersionUID = -5514593295638480862L;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态")
    private AuditState auditState = AuditState.CHECKED;

    /**
     * 原因备注，用于审核驳回
     */
    @ApiModelProperty(value = "原因备注，用于审核驳回")
    private String reason;
}
