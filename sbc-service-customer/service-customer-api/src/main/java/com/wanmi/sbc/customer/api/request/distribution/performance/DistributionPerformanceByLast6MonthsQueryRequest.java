package com.wanmi.sbc.customer.api.request.distribution.performance;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;

/**
 * <p>分销业绩按月查询请求参数</p>
 * Created by of628-wenzhi on 2019-04-18-18:39.
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class DistributionPerformanceByLast6MonthsQueryRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = 5212314251735154775L;

    /**
     * 分销员id
     */
    @NotBlank
    @ApiModelProperty("分销员id")
    private String distributionId;
}
