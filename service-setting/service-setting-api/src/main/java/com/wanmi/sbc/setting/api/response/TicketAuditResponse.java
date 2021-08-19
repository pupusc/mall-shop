package com.wanmi.sbc.setting.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class TicketAuditResponse implements Serializable {
    private static final long serialVersionUID = 7575697352886419636L;
    /**
     * true:审核 false:不审核
     */
    @ApiModelProperty(value = "审核开关-true:审核 false:不审核")
    private boolean aduit;
}
