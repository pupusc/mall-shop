package com.wanmi.sbc.order.api.response.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 根据结束时间统计退单响应结构
 * @Author: daiyitian
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ReturnOrderCountByEndDateResponse implements Serializable {

    private static final long serialVersionUID = -1076979847505660373L;

    /**
     * 统计个数
     */
    @ApiModelProperty(value = "统计个数")
    private int count;

}
