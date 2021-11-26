package com.wanmi.sbc.linkedmall.api.request.order;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * \* User: yhy
 * \* Date: 2020-8-12
 * \* Time: 17:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class SbcOrderQueryRequest implements Serializable {

    @ApiModelProperty(value = "linkedmall 主订单id")
    @NotBlank
    private String lmOrderId;

    @ApiModelProperty(value = "商城内部用户id")
    @NotBlank
    private String bizUid;

}
