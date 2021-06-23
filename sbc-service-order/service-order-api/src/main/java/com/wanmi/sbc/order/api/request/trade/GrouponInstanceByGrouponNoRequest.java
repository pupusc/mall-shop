package com.wanmi.sbc.order.api.request.trade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class GrouponInstanceByGrouponNoRequest implements Serializable {


    private static final long serialVersionUID = 5454453055876550142L;
    /**
     * 团号
     */
    @ApiModelProperty(value = "团号")
    private String grouponNo;

}
