package com.wanmi.sbc.account.api.response.finance.record;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 结算单待结算统计响应请求
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettlementCountResponse implements Serializable {

    private static final long serialVersionUID = -4920262534476730225L;

    /**
     * 待结算数量
     */
    @ApiModelProperty(value = "待结算数量")
    private Long count;
}
