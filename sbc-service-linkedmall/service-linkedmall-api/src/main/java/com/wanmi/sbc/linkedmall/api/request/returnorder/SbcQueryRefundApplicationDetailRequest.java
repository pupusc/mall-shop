package com.wanmi.sbc.linkedmall.api.request.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class SbcQueryRefundApplicationDetailRequest {


    @ApiModelProperty(value = "子订单号")
    @NotBlank
    private String subLmOrderId;

    @ApiModelProperty(value = "用户id")
    @NotBlank
    private String bizUid;
}
