package com.wanmi.sbc.customer.api.response.distribution.performance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>查询分销业绩中去重后的分销员id Response</p>
 * Created by of628-wenzhi on 2019-04-23-15:16.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class DistinctDistributionIdsQueryResponse implements Serializable {
    private static final long serialVersionUID = -3724793143139497225L;

    /**
     * 去重后的分销员id集合
     */
    @ApiModelProperty("去重后的分销员id集合")
    private List<String> distributionIds;
}
