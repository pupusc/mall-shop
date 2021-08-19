package com.wanmi.sbc.order.api.request.logistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据会员id查询请求参数</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsLogSimpleListByCustomerIdRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会员id")
    private String customerId;
}