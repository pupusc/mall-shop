package com.wanmi.sbc.customer.api.request.distribution;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>单个查询分销员请求参数</p>
 *
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerByIdRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 分销员标识UUID
     */
    @NotNull
    private String distributionId;
}