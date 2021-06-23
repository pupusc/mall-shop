package com.wanmi.sbc.returnorder.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yang
 * @since 2019/4/22
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnPointsRequest {

    /**
     * 申请金额
     */
    @ApiModelProperty(value = "申请金额")
    private Long applyPoints;
}
