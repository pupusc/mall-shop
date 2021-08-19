package com.wanmi.sbc.order.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@ApiModel
@Data
public class SuitBuyRequest {

    /**
     * 活动主键
     */
    @ApiModelProperty("活动主键")
    @NotNull
    private Long marketingId;



}
