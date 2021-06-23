package com.wanmi.sbc.setting.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class BossGoodsAuditResponse implements Serializable {
    private static final long serialVersionUID = 8401519653458177637L;
    /**
     * true:审核 false:不审核
     */
    @ApiModelProperty(value = "是否需要审核-true:审核 false:不审核")
    private boolean audit;
}
