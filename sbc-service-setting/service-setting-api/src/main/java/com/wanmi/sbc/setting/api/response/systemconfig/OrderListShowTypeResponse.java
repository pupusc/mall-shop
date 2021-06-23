package com.wanmi.sbc.setting.api.response.systemconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by dyt on 2019/11/7.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListShowTypeResponse {

    /**
     * 状态 0:未启用1:已启用
     */
    @ApiModelProperty(value = "状态 0:订单精简版 1:订单明细版")
    private Integer status;
}
