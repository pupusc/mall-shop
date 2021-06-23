package com.wanmi.sbc.order.api.response.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 创建退单响应结构
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ReturnOrderAddResponse implements Serializable {

    private static final long serialVersionUID = 2297225812533215222L;

    /**
     * 退单id
     */
    @ApiModelProperty(value = "退单id")
    String returnOrderId;
}
