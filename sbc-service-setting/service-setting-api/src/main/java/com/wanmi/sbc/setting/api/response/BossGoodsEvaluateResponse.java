package com.wanmi.sbc.setting.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class BossGoodsEvaluateResponse implements Serializable {
    private static final long serialVersionUID = 8401519653458177637L;
    /**
     * true:开启 false:不开启
     */
    @ApiModelProperty(value = "是否开启商品评价")
    private boolean evaluate;
}
