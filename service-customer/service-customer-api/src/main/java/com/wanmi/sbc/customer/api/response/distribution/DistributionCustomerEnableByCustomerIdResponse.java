package com.wanmi.sbc.customer.api.response.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>依据会员id验证分销员状态response</p>
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerEnableByCustomerIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分销状态正常
     */
    @ApiModelProperty(value = "分销状态是否正常")
    private Boolean distributionEnable;

    /**
     * 分销员禁用原因
     */
    @ApiModelProperty(value = "禁用原因")
    private String forbiddenReason;
}
